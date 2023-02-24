package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeBCD;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ChannelSendData;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import com.isandy.yizd.ChargeNetty.Pojo.ChargeMongo;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 请求充电桩实时状态，属主动请求，类别代码是0x12，主动请求
 * 对应回应充电桩状态，回应代码是0x13，周期上送、变位上送、召唤
 */
@Component
@Lazy
public class YiDaHuaChargeStatusRequest {
    @Resource
    YiChargeBCD chargeBCD;

    @Resource
    ChargeMongo chargeMongo;

    /**
     *
     * @param strBCD 需要实时请求的桩编码
     * @param muzzleNum 需要实时请求的 1 代表1号枪 2 代表2号枪 int
     * @param channel 需要对应netty的channel通道 channel
     */
    public void Start(String strBCD, int muzzleNum, Channel channel) {
        byte[] BCD = chargeBCD.getBytesBCD(strBCD);
        byte[] Status = ResData.responseData(DaHuaCmdEnum.读取实时监测数据, new byte[]{
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                //需要请求实时状态的枪号
                //0x01 1号枪
                //0x02 2号枪
                ByteUtils.toByte(muzzleNum, 1)[0],
        }, chargeMongo.findSeq(strBCD, muzzleNum));
        ChannelSendData.Send(Status, channel);
    }
}
