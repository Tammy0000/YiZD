package com.isandy.yizd.Controller.TestApi;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeChannel;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeReboot;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeStartChargeService;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeStatusRequest;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeStopChargeService;
import com.isandy.yizd.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.text.ParseException;

@RestController
@Slf4j
public class ChargeRequest {
    @Resource
    Redis redis;

    @Resource
    YiDaHuaChargeStopChargeService stopChargeService;

    @Resource
    YiDaHuaChargeStartChargeService startChargeService;

    @Resource
    YiChargeChannel chargeChannel;

    @GetMapping("/status/{BCD}")
    String OnLink(@PathVariable("BCD") String BCD) {
        if (chargeChannel.checkChannel(BCD)) {
            return "在线";
        }else {
            return "不在线";
        }
    }

    @GetMapping("/check/{BCD}/{muzzle}")
    String cc(@PathVariable("BCD") String BCD, @PathVariable("muzzle") String muzzle)
            throws InterruptedException {
        /*
        为了方便测试用，这个特作如下如下用途。正式上线，必须删除
         */
        try {
            int pb = Integer.parseInt(BCD);
            if (pb <= 2) {
                BCD = pb > 1 ? "32010600107331":"32010600192522";
            } else {
                return "输入枪号或BCD编码有误";
            }
        } catch (NumberFormatException e) {
            return "输入枪号或BCD编码有误";
        }
        /*
        --------------------------------------------------
         */
        try {
            String s = BCD+muzzle;
                return String.format("""
                                电桩编号: %s
                                电桩枪号: %s
                                枪状态：%s
                                插枪状态: %s
                                累计充电时间: %s
                                剩余时间: %s
                                输出电压：%s
                                输出电流：%s
                                累计充电度数：%s
                                已充金额：%s
                                电池组温度：%s
                                故障状态: %s
                                桩类型： %s
                                运营商: %s
                                网络类型: %s
                                """,
                        redis.hget(s, "StrBCD"), redis.hget(s, "MuzzleNum"),
                        redis.hget(s, "MuzzleWork"), redis.hget(s, "MuzzleLink"),
                        redis.hget(s, "ChargeAddTime"), redis.hget(s, "LeftTime"),
                        redis.hget(s, "MuzzleVolt"),
                        redis.hget(s, "MuzzleEC"), redis.hget(s, "SumCharge"),
                        redis.hget(s, "Consume"), redis.hget(s, "BatteryHighTemp"),
                        redis.hget(s, "HardwareFailure"), redis.hget(s, "ChargeSeries"),
                        redis.hget(s, "SIMCard"), redis.hget(s, "Network")
                );
        } catch (Exception e) {
            return "暂时没查到该电桩信息";
        }
        /*
        这里提供一个思路，单独处理0x13,避免出现获取到null
         */

    }

    @GetMapping("/start/{BCD}/{muzzle}")
    String adds(@PathVariable("BCD") String BCD, @PathVariable("muzzle") String muzzle) throws ParseException {
        /*
        为了方便测试用，这个特作如下如下用途。正式上线，必须删除
         */
        try {
            int pb = Integer.parseInt(BCD);
            if (pb <= 2) {
                BCD = pb > 1 ? "32010600107331":"32010600192522";
            } else {
                return "输入枪号或BCD编码有误";
            }
        } catch (NumberFormatException e) {
            return "输入枪号或BCD编码有误";
        }
        /*
        --------------------------------------------------
         */
        int i;
        try {
            i = Integer.parseInt(muzzle);
            if (i > 2 || i == 0) {
                return "输入枪号或BCD编码有误";
            }
        } catch (NumberFormatException e) {
            return "输入枪号或BCD编码有误";
        }
        startChargeService.Start(BCD, i, chargeChannel.getChannel(BCD), 30.22);
        return BCD+"发送充电命令完成";
    }

    @GetMapping("/stop/{BCD}/{muzzle}")
    String stop(@PathVariable("BCD") String BCD, @PathVariable("muzzle") String muzzle) {
        /*
        为了方便测试用，这个特作如下如下用途。正式上线，必须删除
         */
        try {
            int pb = Integer.parseInt(BCD);
            if (pb <= 2) {
                BCD = pb > 1 ? "32010600107331":"32010600192522";
            } else {
                return "输入枪号或BCD编码有误";
            }
        } catch (NumberFormatException e) {
            return "输入枪号或BCD编码有误";
        }
        /*
        --------------------------------------------------
         */
        int i;
        try {
            i = Integer.parseInt(muzzle);
            if (i > 2 || i == 0) {
                return "输入枪号或BCD编码有误";
            }
        } catch (NumberFormatException e) {
            return "输入枪号或BCD编码有误";
        }
        stopChargeService.Start(BCD, i, chargeChannel.getChannel(BCD));
        return "发送停止充电命令完成";
    }
}
