package com.isandy.yizd.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
/*
  2023年1月15日16:35:18
  充电桩实时信息
  返回String明文信息
  只限简单的网页使用，不作其他用途
 */
public class ChargeRealTimeStatus {

    /**
     * 电桩枪号
     */
    int muzzleNum;

    /**
     * 枪状态
     */
    String muzzleStatus;

    /**
     * 累计充电时间
     */
    int ChargeAddTime;

    /**
     * 插枪状态
     */
    String muzzleLink;

    /**
     * 充电剩余时间
     */
    int LeftTime;

    /**
     * 充电枪输出电压
     */
    double muzzleVolt;

    /**
     * 充电枪输出电流
     */
    double muzzleEC;

    /**
     * 充电度数
     */
    double SumCharge;

    /**
     * 电池组最高温度
     */
    float BatteryHighTemp;

}
