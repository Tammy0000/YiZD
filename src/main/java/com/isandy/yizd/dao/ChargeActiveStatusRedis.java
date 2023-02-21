package com.isandy.yizd.dao;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext.YiChargeContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * 解决网页前端查询问题，避免过于频繁向充电桩发送桩状态信息
 * 将0x13状态保存
 */
@Slf4j
@Component
public class ChargeActiveStatusRedis {

    @Value("${custom.RedisAddress}")
    String RedisAddress;

    @Value("${custom.RedisPort}")
    int RedisPort;

    Jedis jedis;

    @Bean
    void ChargeActiveStatusRedisinit() {
        try {
            jedis = new Jedis(RedisAddress, RedisPort);
        } catch (Exception e) {
            log.info("ChargeActiveStatusRedisinit初始化失败");
        }
    }

    public void Set(YiChargeContext context) {
        JSONObject json = new JSONObject();
        json.put("muzzleNum", context.getMuzzleNum());
        int muzzleStatus = context.getMuzzleWork();
        /*
          枪状态
          0x00 离线
          0x01 故障
          0x02 空闲
          0x03 充电
         */
        if (muzzleStatus == 0) {
            json.put("muzzleStatus", "离线");
        } else if (muzzleStatus == 1) {
            json.put("muzzleStatus", "故障");
        } else if (muzzleStatus == 2) {
            json.put("muzzleStatus", "空闲");
        } else if (muzzleStatus == 3) {
            json.put("muzzleStatus", "充电中");
        }
        int chargeAddTime = context.getChargeAddTime();
        json.put("ChargeAddTime", chargeAddTime);
        int muzzleLink = context.getMuzzleLink();
        json.put("muzzleLink", muzzleLink > 0 ? "是":"否");
        json.put("LeftTime", context.getLeftTime());
        json.put("muzzleVolt", context.getMuzzleVolt());
        json.put("muzzleEC", context.getMuzzleEC());
        json.put("SumCharge", context.getSumCharge());
        json.put("BatteryHighTemp", context.getBatteryHighTemp());
        /*
          在Redis存储中名字均采用BCD桩号+枪号
          注意最后一位是枪号，
         */
        String name = context.getStrBCD() + context.getMuzzleNum();
        jedis.set(name, String.valueOf(json));
    }

    /**
     *
     * @param name bcd+枪号，例如bcd是1234567，枪号是1号枪。则传入名字是12345671
     * @return JSON格式
     */
    public ChargeRealTimeStatus getRealTimeStatus(String name) {
        return JSON.parseObject(jedis.get(name), ChargeRealTimeStatus.class);
    }
}
