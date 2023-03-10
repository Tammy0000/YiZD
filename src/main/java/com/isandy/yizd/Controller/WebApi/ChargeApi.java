package com.isandy.yizd.Controller.WebApi;

import com.alibaba.fastjson2.JSON;
import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeChannel;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.*;
import com.isandy.yizd.Pojo.FreeCharge;
import com.isandy.yizd.dao.Redis;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@RequestMapping("/api")
@RestController
@Slf4j
public class ChargeApi {

    @Resource
    YiDaHuaChargeStartChargeService startChargeService;

    @Resource
    YiChargeChannel chargeChannel;

    @Resource
    YiDaHuaChargeOnTimer onTimer;

    @Resource
    YiDaHuaChargeReboot chargeReboot;

    @Resource
    YiDaHuaBillChargingSetting ChargingSetting;

    @Resource
    YiDaHuaChargeStopChargeService stopChargeService;

    @Resource
    Redis redis;

    /**
     * @param bcd 电桩编号
     * @param muzzleNum 电桩枪号
     * @return eg:'result:true'
     */
    @GetMapping("/start")
    String startcharge(@RequestParam String bcd, @RequestParam int muzzleNum, @RequestParam double money) {
        try {
            Channel channel = chargeChannel.getChannel(bcd);
            startChargeService.Start(bcd, muzzleNum, channel, money);
            return "successful";
        } catch (Exception e) {
            log.error("充电失败");
            return "Error";
        }
    }

    /**
     * 获取所有在线电桩空闲枪
     * (故障，充电中的电枪不算在内)
     * @return json格式
     */
    @GetMapping("/freecharge")
    String online() {
        FreeCharge freeCharge = new FreeCharge();
        freeCharge.setMuzzleWork("空闲");
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> allStrBCD = chargeChannel.getAllStrBCD();
        for (String str:allStrBCD) {
            String sumMuzzle = redis.hget(str, "SumMuzzle");
            int i = Integer.parseInt(sumMuzzle);
            for (int j = 1; j < i + 1; j++) {
                String muzzleWork = redis.hget(str + j, "MuzzleWork");
                if (muzzleWork.equals("空闲")) {
                    freeCharge.setStrBCD(str);
                    freeCharge.setMuzzleNum(redis.hget(str + j, "MuzzleNum"));
                    arrayList.add(JSON.toJSONString(freeCharge));
                }
            }
        }
        return String.valueOf(arrayList);
    }

    @GetMapping("/reboot")
    String reboot(@RequestParam String bcd) {
        try {
            Channel channel = chargeChannel.getChannel(bcd);
            chargeReboot.Start(bcd, channel);
            return "发送重启命令成功";
        } catch (Exception e) {
            return "发送重启命令失败";
        }
    }

    @GetMapping("/check")
    String checkone(@RequestParam String bcd, @RequestParam int num) {
        if (num > 2 || bcd.length() != 14) {
            return "输入枪号或桩号有误";
        }else {
            String s = bcd + num;
            return String.format("""
                                电桩类型: %s
                                网络类型: %s
                                运营商: %s
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
                                电池组温度：%s""",
                    redis.hget(s, "ChargeSeries"), redis.hget(s,"Network"),
                    redis.hget(s, "SIMCard"),
                    redis.hget(s, "StrBCD"), redis.hget(s, "MuzzleNum"),
                    redis.hget(s, "MuzzleWork"), redis.hget(s, "MuzzleLink"),
                    redis.hget(s, "ChargeAddTime"), redis.hget(s, "LeftTime"),
                    redis.hget(s, "MuzzleVolt"),
                    redis.hget(s, "MuzzleEC"), redis.hget(s, "SumCharge"),
                    redis.hget(s, "Consume"), redis.hget(s, "BatteryHighTemp")

            );
        }
    }

    @GetMapping("/onetime")
    String OnTimer() throws ParseException {
        Hashtable<String, Channel> hashChannel = chargeChannel.getHashChannel();
        Set<Map.Entry<String, Channel>> entries = hashChannel.entrySet();
        for (Map.Entry<String, Channel> e:entries) {
            String BCD = e.getKey();
            Channel channel = e.getValue();
            onTimer.Start(BCD, channel);
        }
        return "successful";
    }

    @GetMapping("/jf/all")
    String JF(
            @RequestParam int t1,
            @RequestParam int t2,
            @RequestParam int t3,
            @RequestParam int t4,
            @RequestParam int t5,
            @RequestParam int t6,
            @RequestParam int t7,
            @RequestParam int t8,
            @RequestParam double c1,
            @RequestParam double c2,
            @RequestParam double c3,
            @RequestParam double c4,
            @RequestParam double c5,
            @RequestParam double c6,
            @RequestParam double c7,
            @RequestParam double c8
    ) {
        ArrayList<Integer> S = new ArrayList<>();
        ArrayList<Integer> P = new ArrayList<>();
        ArrayList<Integer> L = new ArrayList<>();
        ArrayList<Integer> V = new ArrayList<>();
        S.add(t1);S.add(t2);
        P.add(t3);P.add(t4);
        L.add(t5);L.add(t6);
        V.add(t7);V.add(t8);
        Hashtable<String, Channel> hashChannel = chargeChannel.getHashChannel();
        Set<Map.Entry<String, Channel>> entries = hashChannel.entrySet();
        for (Map.Entry<String, Channel> e:entries) {
            String BCD = e.getKey();
            Channel channel = e.getValue();
            ChargingSetting.Start(BCD, channel, S, P, L, V, c1, c2, c3, c4, c5, c6, c7, c8);
        }
        return "successful";
    }

    @GetMapping("/jf")
    String customjf(@RequestParam String bcd, @RequestParam double cost, @RequestParam double costserver) {
        try {
            Channel channel = chargeChannel.getChannel(bcd);
            ChargingSetting.Start(bcd, channel, cost, costserver);
            return "successful";
        } catch (Exception e) {
            return "PathError";
        }
    }

    @GetMapping("/stop")
    String stop(@RequestParam String bcd, @RequestParam int muzzle) {
        Channel channel = chargeChannel.getChannel(bcd);
        stopChargeService.Start(bcd, muzzle, channel);
        return "successful";
    }
}
