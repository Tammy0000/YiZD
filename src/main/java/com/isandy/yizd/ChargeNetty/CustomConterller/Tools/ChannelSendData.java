package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class ChannelSendData {

    public static boolean Send(byte[] bytes, Channel channel) {
        ByteBuf byteBuf = Unpooled.buffer(1024);
        try {
            byteBuf.writeBytes(bytes);
            byteBuf.resetReaderIndex();
            channel.writeAndFlush(byteBuf);
            return true;
        } catch (Exception e) {
            System.out.println("发送失败，检查通道或者客户端是否存在!");
            return false;
        }
    }
}
