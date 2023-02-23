package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.*;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class YiDaHuaChargeRateModelRequest {

    /**
     * 充电桩计费模型请求
     * @param context context
     */
    public void Start(YiChargeContext context, Channel channel) {
        byte[] BCD = context.getBCD();
        byte[] a00 = ByteUtils.toByte(0, 1, false);
        byte[] a06 = ByteUtils.toByte(600000, 4, false);
        byte[] a02 = ByteUtils.toByte(200000, 4, false);
        byte[] bytes = ResData.responseData(context, DaHuaCmdEnum.计费模型请求应答, new byte[]{
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                //计费模型编号
                ByteUtils.toByte(2, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                //尖电费费率
                a06[0], a06[1], a06[2], a06[3],
                //尖服务费费率
                a02[0], a02[2], a02[3], a02[3],
                //峰电费费率
                a06[0], a06[1], a06[2], a06[3],
                //峰服务费费率
                a02[0], a02[2], a02[3], a02[3],
                //平电费费率
                a06[0], a06[1], a06[2], a06[3],
                //平服务费费率
                a02[0], a02[2], a02[3], a02[3],
                //谷电费费率
                a06[0], a06[1], a06[2], a06[3],
                //谷服务费费率
                a02[0], a02[2], a02[3], a02[3],
                //时段费率号
                a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0],
                a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0],
                a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0],
                a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0],
                a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0],
                a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0], a00[0],
        }, context.getInt_sequence());
        ChannelSendData.Send(bytes, channel);
    }
}
