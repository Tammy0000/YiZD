package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.*;
import com.isandy.yizd.dao.Redis;
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
    Redis redis;

    /**
     * 对时
     * @param strBCD strBCD
     * @param channel channel
     * @throws ParseException times
     */
    public void Start(String strBCD, Channel channel) throws ParseException {
        byte[] Data = ByteUtils.toByte(strBCD);
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
        }, redis.getSeq(strBCD));
        ChannelSendData.Send(timer, channel);
    }
}
