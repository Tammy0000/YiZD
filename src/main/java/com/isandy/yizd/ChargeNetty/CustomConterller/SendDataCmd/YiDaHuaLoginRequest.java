package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.*;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * 登陆应答对应的判别类型是0x01
 * 登陆应答判别类型是0x02
 */
@Component
public class YiDaHuaLoginRequest {
    /**
     * 处理电桩初识登陆认证信息
     * @param code true 允许登陆，反之
     */
    public void Start(YiChargeContext context, Channel channel, boolean code) {

        byte[] data = context.getBCD();
        byte logs = code ? ByteUtils.toByte(0x00, 1)[0]:ByteUtils.toByte(0x01, 1)[0];
        byte[] resbyte = ResData.responseData(context, DaHuaCmdEnum.登录应答, new byte[]{
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                data[5],
                data[6],
                logs,
        }, context.getInt_sequence());
        ChannelSendData.Send(resbyte, channel);
    }

    /**
     * 自动默认登陆
     */
    public void Start(YiChargeContext context, Channel channel) {
        byte[] data = context.getBCD();
        byte[] resbyte = ResData.responseData(context, DaHuaCmdEnum.登录应答, new byte[]{
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                data[5],
                data[6],
                ByteUtils.toByte(0x00, 1)[0],
        }, context.getInt_sequence());
        ChannelSendData.Send(resbyte, channel);
    }
}
