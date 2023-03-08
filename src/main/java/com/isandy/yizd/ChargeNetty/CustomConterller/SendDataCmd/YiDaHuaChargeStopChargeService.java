package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ChannelSendData;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import com.isandy.yizd.dao.Redis;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 2023年1月16日21:48:37
 * 运营平台远程停机 对应判别代码是0x36
 * 返回的是0x35 远程停机命令回复
 * 按需发送，主动请求
 */
@Component
@Lazy
public class YiDaHuaChargeStopChargeService {
    @Resource
    Redis redis;

    /**
     *
     * @param strBCD strBCD
     * @param muzzleNum 枪号
     * @param channel channel
     */
    public void Start(String strBCD, int muzzleNum, Channel channel) {
        byte[] BCD = ByteUtils.toByte(strBCD);
        byte[] bytes = ResData.responseData(DaHuaCmdEnum.运营平台远程停机, new byte[]{
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                /*
                  停止枪号
                */
                ByteUtils.toByte(muzzleNum, 1)[0],
        }, redis.getSeq(strBCD));
        ChannelSendData.Send(bytes, channel);
    }
}
