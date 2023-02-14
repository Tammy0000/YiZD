package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 2023年1月16日14:06:27
 * 主动启动充电充电对应判别代码是0x34
 * 属主动请求
 * 2023年1月16日17:24:43
 * 最后两位不需要采用帧校验域
 * 将0x46, 0x22添加到byte中，再转换整形（ByteUtils）
 */
@Slf4j
@Component
public class YiChargeCustomerStartChargeService {

    /**
     * 余额
     * 按照协议暂时默认为9999 （账户余额：9999）
     */
    private static final byte[] currentMoney = ByteUtils.toByte(999999, 4, false);

    public void Start(YiChargeContext context, byte[] BCD, int muzzleNum, Channel channel, int Int_sequence) {
        ByteBuf byteBuf = Unpooled.buffer();
        byte[] charge = ResData.responseData(context, DaHuaCmdEnum.运营平台远程控制启机, new byte[]{
                // 交易流水号
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                ByteUtils.toByte(0, 1)[0],
                // 桩编号
                BCD[0],
                BCD[1],
                BCD[2],
                BCD[3],
                BCD[4],
                BCD[5],
                BCD[6],
                //启动枪号
                ByteUtils.toByte(muzzleNum, 1)[0],
                //逻辑卡号
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                // 物理卡号
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
                //账户余额
                currentMoney[0], currentMoney[1], currentMoney[2], currentMoney[3],
        }, Int_sequence);
        byteBuf.writeBytes(charge);
        channel.writeAndFlush(byteBuf);
        log.info("桩号："+context.getStrBCD()+","+muzzleNum+"号枪发送充电指令成功");
    }
}
