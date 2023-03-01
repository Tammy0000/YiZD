package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeBCD;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ChannelSendData;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
@Lazy
public class YiDaHuaChargeStartChargeService {
    @Resource
    YiChargeBCD chargeBCD;

    /**
     * 余额
     * 按照协议暂时默认为10000（账户余额：100）
     */
    private static final byte[] currentMoney = ByteUtils.toByte(10000, 4, false);

    /**
     *
     * @param strBCD strBCD
     * @param muzzleNum 枪号
     * @param channel channel
     * 默认金额9999.99元，慎用
     * 2023年2月15日13:13:21
     */
    public void Start(String strBCD, int muzzleNum, Channel channel) {
        byte[] temp = new byte[2];
        temp[0] = 0x46;
        temp[1] = 0x22;
        int seq = ByteUtils.toInt(temp);
        byte[] BCD = chargeBCD.getBytesBCD(strBCD);
        byte[] charge = ResData.responseData(DaHuaCmdEnum.运营平台远程控制启机, new byte[]{
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
        }, seq);
        ChannelSendData.Send(charge, channel);
        log.info("桩号："+strBCD+","+muzzleNum+"号枪发送充电指令成功");
    }

    /**
     *
     * @param strBCD strBCD
     * @param muzzleNum 枪号
     * @param channel channel
     * @param Money 金额
     * 金额
     */
    public void Start(String strBCD, int muzzleNum, Channel channel, double Money) {
        byte[] temp = new byte[2];
        temp[0] = 0x46;
        temp[1] = 0x22;
        int seq = ByteUtils.toInt(temp);
        byte[] BCD = chargeBCD.getBytesBCD(strBCD);
        /*
          在数据库存入的是保留两位小数点的金额
          例如数据库实际金额是12.34元，则发送电桩的数值是1234
          所以要乘以100
         */
        int mon = (int) (Money * 100);
        byte[] bytes_mon = ByteUtils.toByte(mon, 4, false);
        byte[] charge = ResData.responseData(DaHuaCmdEnum.运营平台远程控制启机, new byte[]{
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
                bytes_mon[0],
                bytes_mon[1],
                bytes_mon[2],
                bytes_mon[3],
        }, seq);
        ChannelSendData.Send(charge, channel);
    }

    /**
     * @param strBCD strBCD
     * @param PayData 交易流水
     * @param LogicCard 逻辑卡号
     * @param PhyCard 物理卡号
     * @param muzzleNum 枪号
     * @param channel channel
     * @param Money 金额
     */
    public void Start(String strBCD, byte[] PayData, byte[] LogicCard, byte[] PhyCard, int muzzleNum, Channel channel, double Money){
        byte[] temp = new byte[2];
        temp[0] = 0x46;
        temp[1] = 0x22;
        int seq = ByteUtils.toInt(temp);
        byte[] BCD = chargeBCD.getBytesBCD(strBCD);
        /*
          在数据库存入的是保留两位小数点的金额
          例如数据库实际金额是12.34元，则发送电桩的数值是1234
          所以要乘以100
         */
        int mon = (int) (Money * 100);
        byte[] bytes_mon = ByteUtils.toByte(mon, 4, false);
        byte[] charge = ResData.responseData(DaHuaCmdEnum.运营平台远程控制启机, new byte[]{
                /*
                交易流水
                格式桩号（7bytes）+枪号（1byte）+年月日时分秒（6bytes）+自增序号（2bytes）
                 */
                //BCD编号7位
                PayData[0],
                PayData[1],
                PayData[2],
                PayData[3],
                PayData[4],
                PayData[5],
                PayData[6],
                //枪号1位
                PayData[7],
                //年月日时分秒6位
                PayData[8],
                PayData[9],
                PayData[10],
                PayData[11],
                PayData[12],
                PayData[13],
                //自增序号2位
                PayData[14],
                PayData[15],
                //以上是交易流水编号
    //-------------------------------------------//
    //___________________________________________//
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
                LogicCard[0],
                LogicCard[1],
                LogicCard[2],
                LogicCard[3],
                LogicCard[4],
                LogicCard[5],
                LogicCard[6],
                LogicCard[7],
                // 物理卡号
                PhyCard[0],
                PhyCard[1],
                PhyCard[2],
                PhyCard[3],
                PhyCard[4],
                PhyCard[5],
                PhyCard[6],
                PhyCard[7],
                //账户余额
                bytes_mon[0],
                bytes_mon[1],
                bytes_mon[2],
                bytes_mon[3],
        },seq);
        ChannelSendData.Send(charge, channel);
    }
}
