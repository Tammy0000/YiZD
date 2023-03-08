package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ChannelSendData;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import com.isandy.yizd.dao.Redis;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登陆应答对应的判别类型是0x01
 * 登陆应答判别类型是0x02
 */
@Component
public class YiDaHuaLoginRequest {
    @Resource
    Redis redis;

    /**
     * 处理电桩初识登陆认证信息
     * @param code true 允许登陆，反之
     */
    public void Start(String strBCD, Channel channel, boolean code) {
        byte[] BCD = ByteUtils.toByte(strBCD);
        byte logs = code ? ByteUtils.toByte(0x00, 1)[0]:ByteUtils.toByte(0x01, 1)[0];
        byte[] resbyte = ResData.responseData(DaHuaCmdEnum.登录应答, new byte[]{
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                logs,
        }, redis.getSeq(strBCD));
        ChannelSendData.Send(resbyte, channel);
    }

    /**
     * 自动默认登陆
     */
    public void Start(String strBCD, Channel channel) {
        byte[] data = ByteUtils.toByte(strBCD);
        byte[] resbyte = ResData.responseData(DaHuaCmdEnum.登录应答, new byte[]{
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                data[5],
                data[6],
                ByteUtils.toByte(0x00, 1)[0],
        }, redis.getSeq(strBCD));
        ChannelSendData.Send(resbyte, channel);
    }
}
