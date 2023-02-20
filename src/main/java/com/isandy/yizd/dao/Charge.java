package com.isandy.yizd.dao;

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
    int Seq;
    int MuzzleNum; //枪号
                    /*
                      枪状态
                      0x00 离线
                      0x01 故障
                      0x02 空闲
                      0x03 充电
                     */
    String MuzzleStatus; //充电枪状态
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
}
