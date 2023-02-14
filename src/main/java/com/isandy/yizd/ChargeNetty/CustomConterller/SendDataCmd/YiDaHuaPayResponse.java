package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * 交易记录确认 对应的识别代码是0x40, 属应答回复
 * 当0x3b请求时需要做出应答发送
 */
@Component
public class YiDaHuaPayResponse {
    /**
     * 默认正常账单
     * @param context context
     * @param PayData 交易流水 (byte[])
     * @param channel channel
     * @param Int_sequence sequence
     */
    public void Start(YiChargeContext context, byte[] PayData, Channel channel, int Int_sequence){
        ByteBuf byteBuf = Unpooled.buffer();
        byte[] bytes = ResData.responseData(context, DaHuaCmdEnum.交易记录确认, new byte[]{
                /*
                  16位流水号，我个人理解就是从0x3b获取到的流水号。暂没测试
                  2023年1月16日21:14:35
                  麻痹，今天拉肚子，一边拉肚子，一边写代码
                */
                PayData[0],
                PayData[1],
                PayData[2],
                PayData[3],
                PayData[4],
                PayData[5],
                PayData[6],
                PayData[7],
                PayData[8],
                PayData[9],
                PayData[10],
                PayData[11],
                PayData[12],
                PayData[13],
                PayData[14],
                PayData[15],
                /*
                  确认结果 0x00 上传成功 0x01 非法账单
                */
                ByteUtils.toByte(0, 1)[0],
        }, Int_sequence);
        byteBuf.writeBytes(bytes);
        channel.writeAndFlush(byteBuf);
    }

    /**
     *
     * @param context context
     * @param PayData 交易流水 (byte[])
     * @param channel channel
     * @param bill true 正常账单，false非法账单
     * @param Int_sequence sequence
     */
    public void Start(YiChargeContext context, byte[] PayData, Channel channel, int Int_sequence, boolean bill){
        ByteBuf byteBuf = Unpooled.buffer();
        int i = bill ? 0:1;
        byte[] bytes = ResData.responseData(context, DaHuaCmdEnum.交易记录确认, new byte[]{
                PayData[0],
                PayData[1],
                PayData[2],
                PayData[3],
                PayData[4],
                PayData[5],
                PayData[6],
                PayData[7],
                PayData[8],
                PayData[9],
                PayData[10],
                PayData[11],
                PayData[12],
                PayData[13],
                PayData[14],
                PayData[15],
                /*
                  确认结果 0x00 上传成功 0x01 非法账单
                */
                ByteUtils.toByte(i, 1)[0],
        }, Int_sequence);
        byteBuf.writeBytes(bytes);
        channel.writeAndFlush(byteBuf);
    }
}
