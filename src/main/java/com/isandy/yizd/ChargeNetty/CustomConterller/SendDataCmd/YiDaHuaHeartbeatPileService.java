package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.*;
import com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext.YiChargeContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 心跳请求的对应类别代码是0x03
 * 心跳回应的对应类别代码是0x04
 */
@Component
@Lazy
public class YiDaHuaHeartbeatPileService {
    @Resource
    SearchSeq seq;

    //Start代表的是回应发送
    public void Start(YiChargeContext context, Channel channel) {
        byte[] data = context.getMessage_body();
        byte[] PONG = ResData.responseData(context, DaHuaCmdEnum.心跳包Pong, new byte[]{
                //BCD编码
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                data[5],
                data[6],
                //0x01 枪号
                data[7],
                //0x00 心跳应答
                data[8],
                ByteUtils.toByte(0, 1)[0],
        }, seq.find(context.getStrBCD()));
        context.setMuzzleNum(ByteUtils.toInt(data[7]));
        context.setMuzzleStatus(ByteUtils.toInt(data[8]));
        ChannelSendData.Send(PONG, channel);
    }
}
