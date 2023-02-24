package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeBCD;
import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.*;
import com.isandy.yizd.ChargeNetty.Pojo.ChargeMongo;
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
    @Resource
    YiChargeBCD chargeBCD;

    @Resource
    ChargeMongo mongo;
    /**
     *
     * @param strBCD context
     * @param channel channel
     * @param isWork true工作，反之
     * @param ChargePercent 充电桩最大允许输出功率 最大 100%，最小30%
     */
    public void Start(String strBCD, Channel channel, boolean isWork, int ChargePercent){
        byte iw = (byte) (isWork ? 0x00 : 0x01);
        byte max = (byte) ChargePercent;
        byte[] BCD = chargeBCD.getBytesBCD(strBCD);
        byte[] chargemax = ResData.responseData(DaHuaCmdEnum.充电桩工作参数设置, new byte[]{
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                //是否允许工作 0x00 表示允许正常工作 0x01 表示停止使用，锁定充电桩
                iw,
                //充电桩最大允许输出功率 1BIN 表示 1%，最大 100%，最小
                //30%
                max,
        }, mongo.findSeq(strBCD));
        ChannelSendData.Send(chargemax, channel);
    }

    public void Start(String strBCD, Channel channel, int ChargePercent) {
        byte max = (byte) ChargePercent;
        byte[] BCD = chargeBCD.getBytesBCD(strBCD);
        byte[] chargemax = ResData.responseData(DaHuaCmdEnum.充电桩工作参数设置, new byte[]{
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                //是否允许工作 0x00 表示允许正常工作 0x01 表示停止使用，锁定充电桩
                ByteUtils.toByte(0, 1)[0],
                //充电桩最大允许输出功率 1BIN 表示 1%，最大 100%，最小
                //30%
                max,
        }, mongo.findSeq(strBCD));
        ChannelSendData.Send(chargemax, channel);
    }
}
