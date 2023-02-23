package com.isandy.yizd.ChargeNetty.Pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
@Accessors(chain = true)
public class Charge {
    @MongoId
    String id;
    String BCD;
    int seq; //校验域(对应每支充电枪)
    int muzzleNum; //枪号
                    /*
                      枪状态(周期上传)
                      0x00 离线
                      0x01 故障
                      0x02 空闲
                      0x03 充电
                     */
    String MuzzleWork; //充电枪状态
    double MuzzleVolt; //充电枪输出电压
    double MuzzleEC; //充电枪输出电流
    int SumMuzzle; //充电枪数量
    String MuzzleLink; //插枪状态
    String StartMuzzleResult; //启动结果
    String MuzzleError; //启动充电枪结果
    int ChargeAddTime; //累计充电时间
    double SumCharge; //充电度数
    int LeftTime; //充电剩余时间
    int BatteryHighTemp; //电池组最高温度
    String LoginTime;
    int PublicSeq; //公共校验域
    int Encryption; //加密标志
    String ChargeSeries; //电桩类型
    String Network; // 网络类型
    String SIMCard; //运营商
    String muzzleStatus; //枪状态（心跳包）
    double Consume; //消费金额
    int OldSeq; // 保存上一个seq值，方便对照
}
