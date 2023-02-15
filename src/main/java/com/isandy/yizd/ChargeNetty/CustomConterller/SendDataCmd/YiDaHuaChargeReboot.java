package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
    SearchSeq seq;
    /**
     * @param context context
     * @param channel channel 需要对应netty的channel通道 channel
     * @param run 执行时间 true立即执行，反之
     */
    public void Start(YiChargeContext context, Channel channel, boolean run) {
        byte time = (byte) (run ? 0x01:0x02);
        byte[] BCD = context.getBCD();
        byte[] reboot = ResData.responseData(context, DaHuaCmdEnum.远程重启, new byte[]{
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
        }, seq.find(context.getStrBCD()));
        ChannelSendData.Send(reboot, channel);
    }

    /**
     *
     * @param context context
     * @param channel channel 需要对应netty的channel通道 channel
     */
    public void Start(YiChargeContext context, Channel channel) {
        byte[] BCD = context.getBCD();
        byte[] reboot = ResData.responseData(context, DaHuaCmdEnum.远程重启, new byte[]{
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
        }, seq.find(context.getStrBCD()));
        ChannelSendData.Send(reboot, channel);
    }
}
