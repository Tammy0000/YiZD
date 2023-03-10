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
 * 2023年1月29日19:17:08
 * 远程重启
 * 重启充电桩，应对部分问题，如卡死
 * 对应判别代码是 0x92 按需发送 主动请求
 */
@Component
@Lazy
public class YiDaHuaChargeReboot {
    @Resource
    Redis redis;

    /**
     * @param strBCD strBCD
     * @param channel channel 需要对应netty的channel通道 channel
     * @param run 执行时间 true立即执行，反之
     */
    public void Start(String strBCD, Channel channel, boolean run) {
        byte time = (byte) (run ? 0x01:0x02);
        byte[] BCD = ByteUtils.toByte(strBCD);
        byte[] reboot = ResData.responseData(DaHuaCmdEnum.远程重启, new byte[]{
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                /*
                执行时间
                0x01立即执行
                0x02空闲执行
                 */
                time,
        }, redis.getSeq(strBCD));
        ChannelSendData.Send(reboot, channel);
    }

    /**
     *
     * @param strBCD strBCD
     * @param channel channel 需要对应netty的channel通道 channel
     */
    public void Start(String strBCD, Channel channel) {
        byte[] BCD = ByteUtils.toByte(strBCD);
        byte[] reboot = ResData.responseData(DaHuaCmdEnum.远程重启, new byte[]{
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                /*
                执行时间
                默认空闲执行
                0x01立即执行
                0x02空闲执行
                 */
                ByteUtils.toByte(1, 1, false)[0],
        }, redis.getSeq(strBCD));
        ChannelSendData.Send(reboot, channel);
    }
}
