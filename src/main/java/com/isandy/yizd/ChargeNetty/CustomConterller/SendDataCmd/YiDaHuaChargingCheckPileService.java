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
 * 这个是计费模型验证请求 对应判别类型代码是0x05
 * 回应的类型代码请求是0x06
 * 具体对接在哪一步后续再补上
 * 2023年1月11日16:17:56
 */
@Component
@Lazy
public class YiDaHuaChargingCheckPileService {
    @Resource
    YiChargeBCD chargeBCD;

    @Resource
    ChargeMongo chargeMongo;

    /**
     *
     * @param check true 与平台一致， 反之不一致
     */
    public void Start(String strBCD, Channel channel, boolean check) {
        byte[] BCD = chargeBCD.getBytesBCD(strBCD);
        byte flag = check ? ByteUtils.toByte(0x00, 1)[0]:ByteUtils.toByte(0x01, 1)[0];
        byte[] JF = ResData.responseData(DaHuaCmdEnum.计费模型验证请求应答, new byte[]{
                //BCD编码
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                //计费编号
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                //0x00验证一致
                //0x01验证不一致
                flag,
        }, chargeMongo.findSeq(strBCD));
        ChannelSendData.Send(JF, channel);
    }

    /**
     * 默认与平台一致。
     */
    public void Start(String strBCD, Channel channel) {
        byte[] BCD = chargeBCD.getBytesBCD(strBCD);
        byte[] JF = ResData.responseData(DaHuaCmdEnum.计费模型验证请求应答, new byte[]{
                //BCD编码
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                //计费编号
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                //0x00验证一致
                //0x01验证不一致
                ByteUtils.toByte(0x00, 1)[0],
        }, chargeMongo.findSeq(strBCD));
        ChannelSendData.Send(JF, channel);
    }
}
