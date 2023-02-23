package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.*;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
@Lazy
public class YiDaHuaChargeOnTimer {
    public void Start(YiChargeContext context, Channel channel) throws ParseException {
        byte[] Data = context.getBCD();
        byte[] times = Cp56Time2a.toBytes(new Date());
        byte[] timer = ResData.responseData(context, DaHuaCmdEnum.对时设置应答, new byte[]{
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
        }, context.getInt_sequence());
        ChannelSendData.Send(timer, channel);
    }
}
