package com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.dao.ChargeRealTimeStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

@Component
@Getter
@Setter
@Slf4j
public class YiChargeContext {
    @Resource
    ChargeRealTimeStatus crts;
    ByteBuf byteBuf;

    Channel channel;

    /**
     * 起始标志
     */
    byte[] start;

    /**
     * 数据长度
     */
    byte[] DataLengths;

    /**
     * 序列号域（byte）
     */
    byte[] byte_sequence;

    /**
     * 序列号域（int）
     */
    int int_sequence;

    /**
     * 加密标志
     */
    byte[] encryption;

    /**
     * 帧类型
     */
    byte[] TypeData;

    /**
     * 消息体
     */
    byte[] message_body;

    /**
     * 帧校验域
     */
    byte[] frameType;

    /**
     * 电桩BCD编码
     */
    byte[] BCD;

    /**
     * 2023.1.14
     * 新增字符串BCD编码；方便存入数据库或者Redis空格出现问题
     */
    String StrBCD;

    /**
     * 电桩发送过来没经处理过的报文
     */
    byte[] SourceData;

    /**
     * 电桩枪号
     */
    int muzzleNum;

    //
    /**
     * 枪状态
     * 0x00 离线
     * 0x01 故障
     * 0x02 空闲
     * 0x03 充电
     */
    int muzzleStatus;

    /**
     * 累计充电时间 单位min
     */

    int ChargeAddTime;

    /**
     * 插枪状态
     * 0x00 否
     * 0x01 是
     */
    int muzzleLink;

    /**
     * 充电剩余时间 单位min
     */
    int LeftTime;

    /**
     * 充电枪输出电压 待机0 不正常-1.0 保留1位(float)
     */
    double muzzleVolt;
    /**
     * 充电枪输出电流 待机0 不正常-1.0 保留1位(float)
     */
    double muzzleEC;

    /**
     * 充电度数 待机0 不正常-1.0 保留4位(float)
     */
    double SumCharge;
    /**
     * 电池组最高温度 偏移量-50℃ (int) 待机置零；
     * 交流桩置零
     */
    int BatteryHighTemp;

    /**
     * 16位
     * 交易流水号：交易流水号为一次充电操作过程的统一标记，从远程启动充电或者卡鉴权回复
     * 时产生到最后桩结束充电的交易记录均使用同一个流水号，由平台产生（离线模式由桩按此
     * 规则生成），生成规则为 格式桩号（7bytes）+枪号（1byte）+年月日时分秒（6bytes）+自
     * 增序号（2bytes）；示例：32010600019236012001061803423060。
     */
    byte[] PayData;

    /**
     * 启动结果 0x00失败 0x01成功
     */
    int StartMuzzleResult;

    /**
     * 启动失败原因
     * 0x00 无
     * 0x01 设备编号不匹配
     * 0x02 枪已在充电
     * 0x03 设备故障
     * 0x04 设备离线
     * 0x05 未插枪
     */
    int MuzzleError;



    public void init(ByteBuf byteBuf) {
        try {
            this.byteBuf = byteBuf;
            int index = 0;
            int byteDataLength = byteBuf.readableBytes();
            byteBuf.resetReaderIndex();
            byte[] bytes = new byte[byteDataLength];
            //起始标志1字节
            this.start = new byte[1];
            byteBuf.readBytes(this.start);
            for (byte a: this.start) {
                bytes[index] = a;
                index ++;
            }

            //数据长度1字节
            this.DataLengths = new byte[1];
            byteBuf.readBytes(this.DataLengths);
            for(byte b: this.DataLengths) {
                bytes[index] = b;
                index ++;
            }

            //序列号2字节
            this.byte_sequence = new byte[2];
            byteBuf.readBytes(this.byte_sequence);
            this.int_sequence = ByteUtils.toInt(this.byte_sequence);
            for (byte c: this.byte_sequence) {
                bytes[index] = c;
                index ++;
            }

            //加密标志1字节
            this.encryption = new byte[1];
            byteBuf.readBytes(this.encryption);
            for (byte d: this.encryption) {
                bytes[index] = d;
                index ++;
            }

            //类型1字节
            this.TypeData = new byte[1];
            byteBuf.readBytes(this.TypeData);
            for (byte e: this.TypeData) {
                bytes[index] = e;
                index ++;
            }

            //消息内容
            this.message_body = new byte[byteDataLength - 8];
            byteBuf.readBytes(this.message_body);
            for (byte f:this.message_body) {
                bytes[index] = f;
                index ++;
            }

            //帧校验
            this.frameType = new byte[2];
            byteBuf.readBytes(this.frameType);
            for (byte g:this.frameType) {
                bytes[index] = g;
                index ++;
            }

            //BCD编码
            this.BCD = new byte[7];
            int type = ByteUtils.toInt(this.TypeData);

            /*
              按照协议规范消息体是包含BCD编码，但是在消息体内容中有些是前16位是交易信息，而不是BCD
              特作如下处理：
             */
            ArrayList<Integer> ints = new ArrayList<>();
            ints.add(DaHuaCmdEnum.上传实时监测数据.getCmd());
            ints.add(DaHuaCmdEnum.充电握手.getCmd());
            ints.add(DaHuaCmdEnum.参数配置.getCmd());
            ints.add(DaHuaCmdEnum.充电结束.getCmd());
            ints.add(DaHuaCmdEnum.错误报文.getCmd());
            ints.add(DaHuaCmdEnum.充电阶段充电机中止.getCmd());
            ints.add(DaHuaCmdEnum.充电过程BMS需求与充电机输出.getCmd());
            ints.add(DaHuaCmdEnum.充电过程BMS信息.getCmd());
            ints.add(DaHuaCmdEnum.运营平台确认启动充电.getCmd());
            ints.add(DaHuaCmdEnum.运营平台远程控制启机.getCmd());
            ints.add(DaHuaCmdEnum.远程启机命令回复.getCmd());
            ints.add(DaHuaCmdEnum.交易记录.getCmd());
            ints.add(DaHuaCmdEnum.交易记录确认.getCmd());
            if (ints.contains(type)) {
                System.arraycopy(this.message_body, 16, this.BCD, 0, 7);
            }else {
                System.arraycopy(this.message_body, 0, this.BCD, 0, 7);
            }

            //原报文
            this.SourceData = new byte[byteDataLength];
            byteBuf.resetReaderIndex();
            for (int i = 0; i < byteDataLength; i++) {
                this.SourceData[i] = byteBuf.readByte();
            }

            //StringBCD编码
            String sb = ByteUtils.bytesToHexFun2(this.BCD);
            //去除多余空格
            this.StrBCD = sb.replace(" ", "");

            /*
              处理0x13充电桩状态码
              处理如下内容
              交易流水号
              枪号
              枪状态
              累计充电时间
              充电剩余时间
              插枪状态
              充电枪输出电压
              充电枪输出电流
              充电度数
              电池组最高温度
             */

            try {
                /*
                   0x33 远程启动充电命令回复
                   写着感觉又长又臭了
                   2023年1月16日22:10:26
                 */
                if (DaHuaCmdEnum.远程启机命令回复.getCmd() == ByteUtils.toInt(this.TypeData)) {
                    this.PayData = new byte[16];
                    System.arraycopy(this.message_body, 0, this.PayData, 0, 16);
                    byte[] muzzle = new byte[1];
                    muzzle[0] = this.message_body[23];
                    this.muzzleNum = ByteUtils.toInt(muzzle);
                    byte[] sms = new byte[1];
                    sms[0] = this.message_body[24];
                    this.StartMuzzleResult = ByteUtils.toInt(sms);
                    byte[] error = new byte[1];
                    error[0] = this.message_body[25];
                    this.MuzzleError = ByteUtils.toInt(error);
                }
            } catch (Exception e) {
                log.info("0x33 远程启动充电初始化失败");
            }

            try {
                /*
                  0x3b交易记录，感觉自己越写越长了。先不管了，让代码跑起来先
                  找个时间再优化
                  2023年1月16日21:06:48
                 */
                if (DaHuaCmdEnum.交易记录.getCmd() == ByteUtils.toInt(this.TypeData)) {
                    this.PayData = new byte[16];
                    System.arraycopy(this.message_body, 0, this.PayData, 0, 16);
                }
            } catch (Exception e) {
                log.info("0x3b交易记录初始化失败");
            }

            try {
                /*
                  0x13桩实时状态码，如果请求超时会出现Null情况。有时间要处理
                  2023年1月16日17:30:09
                 */
                if (DaHuaCmdEnum.上传实时监测数据.getCmd() == ByteUtils.toInt(this.TypeData)) {
                    this.PayData = new byte[16];
                    System.arraycopy(this.message_body, 0, this.PayData, 0, 16);
                    byte[] number = new byte[1];
                    number[0] = this.message_body[23];
                    this.muzzleNum = ByteUtils.toInt(number);
                    crts.setMuzzleNum(this.muzzleNum);
                    byte[] status = new byte[1];
                    status[0] = this.message_body[24];
                    this.muzzleStatus = ByteUtils.toInt(status);
                    /*
                      枪状态
                      0x00 离线
                      0x01 故障
                      0x02 空闲
                      0x03 充电
                     */
                    if (this.muzzleStatus == 0) {
                        crts.setMuzzleStatus("离线");
                    } else if (this.muzzleStatus == 1) {
                        crts.setMuzzleStatus("故障");
                    } else if (this.muzzleStatus == 2) {
                        crts.setMuzzleStatus("空闲");
                    } else if (this.muzzleStatus == 3) {
                        crts.setMuzzleStatus("充电中");
                    }
                    byte[] cat = new byte[2];
                    cat[0] = this.message_body[42];
                    cat[1] = this.message_body[43];
                    this.ChargeAddTime = ByteUtils.toInt(cat, false);
                    byte[] link = new byte[1];
                    link[0] = this.message_body[26];
                    this.muzzleLink = ByteUtils.toInt(link);
                    crts.setMuzzleLink(this.muzzleLink > 0 ? "是":"否");
                    byte[] ltime = new byte[2];
                    ltime[0] = this.message_body[44];
                    ltime[1] = this.message_body[45];
                    this.LeftTime = ByteUtils.toInt(ltime, false);
                    crts.setLeftTime(this.LeftTime);
                    byte[] volt = new byte[2];
                    volt[0] = this.message_body[27];
                    volt[1] = this.message_body[28];
                    this.muzzleVolt = (double) ByteUtils.toInt(volt, false) / 10;
                    crts.setMuzzleVolt(this.muzzleVolt);
                    byte[] ec = new byte[2];
                    ec[0] = this.message_body[29];
                    ec[1] = this.message_body[30];
                    this.muzzleEC = (double) ByteUtils.toInt(ec, false) / 10;
                    crts.setMuzzleEC(this.muzzleEC);
                    byte[] sum = new byte[4];
                    sum[0] = this.message_body[46];
                    sum[1] = this.message_body[47];
                    sum[2] = this.message_body[48];
                    sum[3] = this.message_body[49];
                    this.SumCharge = (double) ByteUtils.toInt(sum, false) / 10000;
                    crts.setSumCharge(this.SumCharge);
                    byte[] bht = new byte[1];
                    bht[0] = this.message_body[41];
                    this.BatteryHighTemp = ByteUtils.toInt(bht) - 86;
                    crts.setBatteryHighTemp(this.BatteryHighTemp);
                }
            } catch (Exception e) {
                log.info("0x13桩实时状态初始化失败");
            }
        } catch (Exception e) {
            log.info("传入报文格式不正确");
        }
    }
}
