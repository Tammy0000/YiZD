package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ChannelSendData;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 这个是计费模型验证请求 对应判别类型代码是0x05
 * 回应的类型代码请求是0x06
 * 具体对接在哪一步后续再补上
 * 2023年1月11日16:17:56
 */
@Component
@Lazy
public class YiDaHuaChargingCheckPileService {

    /**
     *
     * @param check true 与平台一致， 反之不一致
     */
    public void Start(YiChargeContext context, Channel channel, boolean check) {
        byte[] data = context.getMessage_body();
        byte flag = check ? ByteUtils.toByte(0x00, 1)[0]:ByteUtils.toByte(0x01, 1)[0];
        byte[] JF = ResData.responseData(context, DaHuaCmdEnum.计费模型验证请求应答, new byte[]{
                //BCD编码
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                data[5],
                data[6],
                //计费编号
                data[7],
                data[8],
                //0x00验证一致
                //0x01验证不一致
                flag,
        }, context.getInt_sequence());
        ChannelSendData.Send(JF, channel);
    }

    /**
     * 默认与平台一致。
     */
    public void Start(YiChargeContext context, Channel channel) {
        byte[] data = context.getMessage_body();
        byte[] JF = ResData.responseData(context, DaHuaCmdEnum.计费模型验证请求应答, new byte[]{
                //BCD编码
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                data[5],
                data[6],
                //计费编号
                data[7],
                data[8],
                //0x00验证一致
                //0x01验证不一致
                ByteUtils.toByte(0x00, 1)[0],
        }, context.getInt_sequence());
        ChannelSendData.Send(JF, channel);
    }
}
