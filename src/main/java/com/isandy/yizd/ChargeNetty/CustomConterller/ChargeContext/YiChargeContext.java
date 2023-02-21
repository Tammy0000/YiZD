package com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.dao.Charge;
import com.isandy.yizd.dao.ChargeRealTimeStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.ArrayList;

@Getter
@Setter
@Slf4j
public class YiChargeContext {
    @Resource
    MongoTemplate mongo;

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
    int MuzzleNum;

    //
    /**
     * 这里指的是充电枪准备工作状态
     * 枪状态
     * 0x00 离线
     * 0x01 故障
     * 0x02 空闲
     * 0x03 充电
     */
    int MuzzleWork;

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
    double MuzzleVolt;
    /**
     * 充电枪输出电流 待机0 不正常-1.0 保留1位(float)
     */
    double MuzzleEC;

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
     * 消费金额
     */
    double Consume;

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
     * 运行商
     * 0x00 移动
     * 0x02 电信
     * 0x03 联通
     * 0x04 其他
     */
    String SIMCard;

    /**
     * 网络链接类型
     * 0x00 SIM
     * 0x01 LAN
     * 0x02 WAN
     * 0x03 其他
     */
    String Network;

    /**
     * 电桩类型 0 表示直流桩，1 表示交流桩
     */
    String ChargeSeries;

    /**
     * 公共验证seq
     */
    int PublicSeq;

    /**
     * 充电枪数量
     */
    int SumMuzzle;

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
        Charge charge = new Charge();
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
                    this.MuzzleNum = ByteUtils.toInt(muzzle);
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

            if (DaHuaCmdEnum.登陆.getCmd() == ByteUtils.toInt(this.TypeData)) {
                byte[] chargeseries = new byte[1];
                chargeseries[0] = this.message_body[7];
                this.ChargeSeries = ByteUtils.toInt(chargeseries, false) > 0 ? "交流桩":"直流桩";
                byte[] net = new byte[1];
                net[0] = this.message_body[18];
                int n = ByteUtils.toInt(net, false);
                if (n == 0) {
                    this.Network = "SIM";
                } else if (n == 1) {
                    this.Network = "LAN";
                } else if (n == 2) {
                    this.Network = "WAN";
                }else {
                    this.Network = "Other";
                }
                byte[] sim = new byte[1];
                sim[0] = this.message_body[29];
                int s = ByteUtils.toInt(sim, false);
                if (s == 0) {
                    this.SIMCard = "移动";
                } else if (s == 2) {
                    this.SIMCard = "电信";
                } else if (s == 3) {
                    this.SIMCard = "联通";
                }else {
                    this.SIMCard = "Other";
                }
                byte[] num = new byte[1];
                num[0] = this.message_body[8];
                this.SumMuzzle = ByteUtils.toInt(num, false);
                this.PublicSeq = this.int_sequence;
                charge.setBCD(this.StrBCD)
                        .setChargeSeries(this.ChargeSeries)
                        .setNetwork(this.Network)
                        .setSIMCard(this.SIMCard)
                        .setBCD(this.StrBCD)
                        .setPublicSeq(this.int_sequence)
                        .setEncryption(this.int_sequence)
                        .setSumMuzzle(this.SumMuzzle);
                Criteria criteria = new Criteria();
                criteria.and("BCD").is(charge.getBCD());
                mongo.remove(Query.query(criteria), Charge.class);
                for (int i = 1; i < charge.getSumMuzzle() + 1; i++) {
                    Charge c = new Charge();
                    /*
                    必须要浅拷贝，
                    直接赋值会报错
                    */
                    BeanUtils.copyProperties(charge, c);
                    c.setMuzzleNum(i);
                    mongo.insert(c);
                }
            }

            if (DaHuaCmdEnum.心跳包Ping.getCmd() == ByteUtils.toInt(this.TypeData)) {
                byte[] muzzlenum = new byte[1];
                muzzlenum[0] = this.message_body[7];
                byte[] muzzlestatus = new byte[1];
                muzzlestatus[0] = this.message_body[8];
                int mu = ByteUtils.toInt(muzzlestatus, false);
                String muzzle = mu > 0 ? "故障":"正常";
                int num = ByteUtils.toInt(muzzlenum);
                Criteria criteria = new Criteria();
                criteria.and("BCD").is(this.StrBCD)
                        .and("MuzzleNum").is(num);
                Update update = new Update();
                update.set("Seq", this.int_sequence)
                        .set("MuzzleStatus", muzzle);
                mongo.upsert(Query.query(criteria), update, Charge.class);
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
                    Update update = new Update();
                    this.PayData = new byte[16];
                    System.arraycopy(this.message_body, 0, this.PayData, 0, 16);
                    byte[] number = new byte[1];
                    number[0] = this.message_body[23];
                    this.MuzzleNum = ByteUtils.toInt(number);
                    byte[] status = new byte[1];
                    status[0] = this.message_body[24];
                    this.MuzzleWork = ByteUtils.toInt(status,false);
                    /*
                      枪状态
                      0x00 离线
                      0x01 故障
                      0x02 空闲
                      0x03 充电
                     */
                    if (this.MuzzleWork == 0) {
                        update.set("MuzzleWork", "离线");
                    } else if (this.MuzzleWork == 1) {
                        update.set("MuzzleWork", "故障");
                    } else if (this.MuzzleWork == 2) {
                        update.set("MuzzleWork", "空闲");
                    } else if (this.MuzzleWork == 3) {
                        update.set("MuzzleWork", "充电中");
                    }
                    byte[] cat = new byte[2];
                    cat[0] = this.message_body[42];
                    cat[1] = this.message_body[43];
                    this.ChargeAddTime = ByteUtils.toInt(cat, false);
                    update.set("ChargeAddTime", this.ChargeAddTime);
                    byte[] link = new byte[1];
                    link[0] = this.message_body[26];
                    this.muzzleLink = ByteUtils.toInt(link);
                    update.set("MuzzleLink", this.muzzleLink > 0 ? "是":"否");
                    byte[] ltime = new byte[2];
                    ltime[0] = this.message_body[44];
                    ltime[1] = this.message_body[45];
                    this.LeftTime = ByteUtils.toInt(ltime, false);
                    update.set("LeftTime", this.LeftTime);
                    byte[] volt = new byte[2];
                    volt[0] = this.message_body[27];
                    volt[1] = this.message_body[28];
                    this.MuzzleVolt = (double) ByteUtils.toInt(volt, false) / 10;
                    update.set("MuzzleVolt", this.MuzzleVolt);
                    byte[] ec = new byte[2];
                    ec[0] = this.message_body[29];
                    ec[1] = this.message_body[30];
                    this.MuzzleEC = (double) ByteUtils.toInt(ec, false) / 10;
                    update.set("MuzzleEC", this.MuzzleEC);
                    byte[] sum = new byte[4];
                    sum[0] = this.message_body[46];
                    sum[1] = this.message_body[47];
                    sum[2] = this.message_body[48];
                    sum[3] = this.message_body[49];
                    this.SumCharge = (double) ByteUtils.toInt(sum, false) / 10000;
                    update.set("SumCharge", this.SumCharge);
                    byte[] bht = new byte[1];
                    bht[0] = this.message_body[41];
                    this.BatteryHighTemp = ByteUtils.toInt(bht, false) - 50;
                    update.set("BatteryHighTemp", this.BatteryHighTemp);
                    byte[] consume = new byte[4];
                    consume[0] = this.message_body[54];
                    consume[1] = this.message_body[55];
                    consume[2] = this.message_body[56];
                    consume[3] = this.message_body[57];
                    this.Consume = (double) ByteUtils.toInt(consume, false) / 10000;
                    update.set("Consume", this.Consume);
                    update.set("Seq", this.int_sequence);
                    Criteria criteria = new Criteria();
                    criteria.and("BCD").is(this.getStrBCD())
                            .and("MuzzleNum").is(this.getMuzzleNum());
                    mongo.upsert(Query.query(criteria), update, Charge.class);
                }
            } catch (Exception e) {
                log.info("0x13桩实时状态初始化失败");
            }
        } catch (Exception e) {
            log.info("传入报文格式不正确");
        }
    }
}
