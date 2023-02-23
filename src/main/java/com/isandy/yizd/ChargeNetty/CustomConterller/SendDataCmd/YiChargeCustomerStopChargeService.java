package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.*;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * 2023年1月16日21:48:37
 * 运营平台远程停机 对应判别代码是0x36
 * 返回的是0x35 远程停机命令回复
 * 按需发送，主动请求
 */
@Component
public class YiChargeCustomerStopChargeService {

    /**
     *
     * @param context context
     * @param muzzleNum 枪号
     * @param channel channel
     */
    public void Start(YiChargeContext context, int muzzleNum, Channel channel) {
        byte[] BCD = context.getBCD();
        byte[] bytes = ResData.responseData(context, DaHuaCmdEnum.运营平台远程停机, new byte[]{
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
        }, context.getInt_sequence());
        ChannelSendData.Send(bytes, channel);
    }
}
