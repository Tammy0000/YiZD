package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeBCD;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ChannelSendData;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.Cp56Time2a;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import com.isandy.yizd.ChargeNetty.Pojo.ChargeMongo;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;

@Component
@Lazy
/*
  对时设置
 */
public class YiDaHuaChargeOnTimer {
    @Resource
    YiChargeBCD chargeBCD;

    @Resource
    ChargeMongo chargeMongo;

    /**
     * 对时
     * @param strBCD strBCD
     * @param channel channel
     * @throws ParseException times
     */
    public void Start(String strBCD, Channel channel) throws ParseException {
        byte[] Data = chargeBCD.getBytesBCD(strBCD);
        byte[] times = Cp56Time2a.toBytes(new Date());
        byte[] timer = ResData.responseData(DaHuaCmdEnum.对时设置, new byte[]{
                Data[0],
                Data[1],
                Data[2],
                Data[3],
                Data[4],
                Data[5],
                Data[6],
                //时间7位
                times[0],
                times[1],
                times[2],
                times[3],
                times[4],
                times[5],
                times[6],
        }, chargeMongo.findSeq(strBCD));
        ChannelSendData.Send(timer, channel);
    }
}
