package com.isandy.yizd.ChargeNetty.Customcoder;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class ChargeDecoder extends ByteToMessageDecoder {

    byte[] start = new byte[]{
            0x68,
    };

    Hashtable<String, byte[]> channels = new Hashtable<>();

    ArrayList<Integer> types = new ArrayList<>();


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) {
        types.add(38);
        types.add(17);
        types.add(68); //0x13
        types.add(166);
        types.add(52); //0x23
        types.add(34); //0x33
        //临时存储
        ByteBuf byteBuf = Unpooled.buffer();
        CoderContext coderContext = new CoderContext();
        coderContext.initCode(in);
        byte[] typeData = coderContext.getTypeData();
        int type = ByteUtils.toInt(typeData);
        /*
        记录一下坑爹的报文
        2023年1月28日16:01:38
        报文内容是： 0a 68 0d d9 11 00 03 32 01 06 00 19 25 22 01 00 b0
         */
        /*
        不知道是网络问题，还是MTU设置问题。电桩传过来的报文，很多奇怪的格式。希望尽快找到
        另外还没有找到更好的方法解决恶意客户端链接问题
        2023年1月28日21:56:06
         */
        in.resetReaderIndex();
        //0x13类型 出现拆包现象。正常情况下会发送68位byte,如果出现拆包情况会出现64+4
        //特别标记！！！！！！
        // 1.如果在本地调试，例如采用FRP映射在本地，本地地址不变后面端口号会改变，有可能代表不同电桩的报文
        // 2.但是在实际测试中端口号改变后，心跳包依然都可以正常发送
        // 3.所以在实际服务器测试中ctx.channel().toString()截取出来的IP建议不要添加端口，避免在HashTab中出现问题
        // 4.另外一个坑就是在下文中 ByteBuf 的in，不能直接list.add(in)形式传出去，否则在下一个通道中，出现0字节现象
        // 2023年1月13日15:09:33
        byteBuf.writeBytes(in);
        int num = byteBuf.readableBytes();
        if (num == 17) {
            if (byteBuf.readByte() == start[0]) {
                byteBuf.resetReaderIndex();
                list.add(byteBuf);
            }else {
                byteBuf.resetReaderIndex();
                byte[] bytes = new byte[num];
                bytes[num - 1] = byteBuf.readByte();
                for (int i = 0; i < num - 1; i++) {
                    bytes[i] = byteBuf.readByte();
                }
                byteBuf.clear();
                byteBuf.writeBytes(bytes);
                if (byteBuf.readByte() == start[0]) {
                    byteBuf.resetReaderIndex();
                    list.add(byteBuf);
                }
            }
        } else if (type == DaHuaCmdEnum.心跳包Ping.getCmd() && num <= 17) {
            filter(byteBuf, list, ctx.channel(), num);
        } else if (type == DaHuaCmdEnum.交易记录.getCmd() && num <= 166) {
            filter(byteBuf, list, ctx.channel(), num);
        } else if (type == DaHuaCmdEnum.上传实时监测数据.getCmd() && num <= 68) {
            filter(byteBuf, list, ctx.channel(), num);
        } else if (type == DaHuaCmdEnum.登陆.getCmd() && num <= 38) {
            filter(byteBuf, list, ctx.channel(), num);
        } else if (type == DaHuaCmdEnum.对时设置应答.getCmd() && num <= 22) {
            filter(byteBuf, list, ctx.channel(), num);
        } else {
            byte[] bytes = new byte[num];
            for (int i = 0; i < num; i++) {
                bytes[i] = byteBuf.readByte();
            }
            byteBuf.clear();
            Set<Map.Entry<String, byte[]>> entries = channels.entrySet();
            for (Map.Entry<String, byte[]> e: entries) {
                if (types.contains(num + e.getValue().length)) {
                    byteBuf.writeBytes(e.getValue());
                    byteBuf.writeBytes(bytes);
                    list.add(byteBuf);
                    channels.remove(e.getKey());
                }
            }
        }
    }

    private void filter(ByteBuf byteBuf, List<Object> list, Channel channel, int num){
        if (types.contains(num)) {
            byteBuf.resetReaderIndex();
            list.add(byteBuf);
        }else {
            byte[] bytes = new byte[num];
            for (int i = 0; i < num; i++) {
                bytes[i] = byteBuf.readByte();
            }
            if (!channels.containsKey(channel.toString()+num)) {
                channels.put(channel.toString()+num, bytes);
            } else {
                channels.replace(channel.toString()+num, bytes);
            }
        }
    }
}
