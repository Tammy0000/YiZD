package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ChannelSendData;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

/**
 * 2023年2月14日22:50:21
 * 充电桩工作参数设置
 * 对应判别代码是 0x52 按需发送 主动请求
 */
@Resource
@Lazy
public class YiDaHuaChargeWorkSetting {
    /**
     *
     * @param context context
     * @param channel channel
     * @param isWork true工作，反之
     * @param ChargePercent 充电桩最大允许输出功率 最大 100%，最小30%
     */
    public void Start(YiChargeContext context, Channel channel, boolean isWork, int ChargePercent){
        byte iw = (byte) (isWork ? 0x00 : 0x01);
        byte max = (byte) ChargePercent;
        byte[] bcd = context.getBCD();
        byte[] chargemax = ResData.responseData(context, DaHuaCmdEnum.充电桩工作参数设置, new byte[]{
                bcd[0],
                bcd[1],
                bcd[2],
                bcd[3],
                bcd[4],
                bcd[5],
                bcd[6],
                //是否允许工作 0x00 表示允许正常工作 0x01 表示停止使用，锁定充电桩
                iw,
                //充电桩最大允许输出功率 1BIN 表示 1%，最大 100%，最小
                //30%
                max,
        }, context.getInt_sequence());
        ChannelSendData.Send(chargemax, channel);
    }

    public void Start(YiChargeContext context, Channel channel, int ChargePercent) {
        byte max = (byte) ChargePercent;
        byte[] bcd = context.getBCD();
        byte[] chargemax = ResData.responseData(context, DaHuaCmdEnum.充电桩工作参数设置, new byte[]{
                bcd[0],
                bcd[1],
                bcd[2],
                bcd[3],
                bcd[4],
                bcd[5],
                bcd[6],
                //是否允许工作 0x00 表示允许正常工作 0x01 表示停止使用，锁定充电桩
                ByteUtils.toByte(0, 1)[0],
                //充电桩最大允许输出功率 1BIN 表示 1%，最大 100%，最小
                //30%
                max,
        }, context.getInt_sequence());
        ChannelSendData.Send(chargemax, channel);
    }
}
