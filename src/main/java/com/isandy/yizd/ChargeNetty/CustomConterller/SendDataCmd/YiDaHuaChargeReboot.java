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
 * 2023年1月29日19:17:08
 * 远程重启
 * 重启充电桩，应对部分问题，如卡死
 * 对应判别代码是 0x92 按需发送 主动请求
 */
@Component
public class YiDaHuaChargeReboot {
    /**
     * @param context context
     * @param BCD 电桩BCD编号
     * @param channel channel 需要对应netty的channel通道 channel
     * @param Int_sequence 序列号位
     * @param run 执行时间 0x01立即执行 0x02空闲执行 留空默认空闲执行
     */
    public void Start(YiChargeContext context, byte[] BCD, Channel channel, int Int_sequence, boolean run) {
        byte time = (byte) (run ? 0x01:0x02);
        ByteBuf byteBuf = Unpooled.buffer();
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
        },Int_sequence);
        byteBuf.writeBytes(reboot);
        channel.writeAndFlush(byteBuf);
    }

    /**
     *
     * @param context context
     * @param BCD 电桩BCD编号
     * @param channel channel 需要对应netty的channel通道 channel
     * @param Int_sequence 序列号位
     */
    public void Start(YiChargeContext context, byte[] BCD, Channel channel, int Int_sequence) {
        ByteBuf byteBuf = Unpooled.buffer();
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
                ByteUtils.toByte(1, 1)[0],
        },Int_sequence);
        byteBuf.writeBytes(reboot);
        channel.writeAndFlush(byteBuf);
    }
}
