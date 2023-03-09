package com.isandy.yizd.Controller.WebApi;

import com.alibaba.fastjson2.JSON;
import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeChannel;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaBillChargingSetting;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeOnTimer;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeReboot;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeStartChargeService;
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
    Redis redis;

    /**
     * @param bcd 电桩编号
     * @param muzzleNum 电桩枪号
     * @return eg:'result:true'
     */
    @GetMapping("/start")
    String startcharge(@RequestParam String bcd, @RequestParam int muzzleNum) {
        try {
            Channel channel = chargeChannel.getChannel(bcd);
            startChargeService.Start(bcd, muzzleNum, channel);
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
    String reboot() {
        String s = "32010600107331";
        try {
            Channel channel = chargeChannel.getChannel(s);
            chargeReboot.Start(s, channel);
            return "重启成功";
        } catch (Exception e) {
            return "重启失败";
        }
    }

//    @GetMapping("/check/all")
//    String checkall() {
//        ArrayList<String> strings = new ArrayList<>();
//        Hashtable<String, Channel> hashChannel = chargeChannel.getHashChannel();
//        Set<Map.Entry<String, Channel>> entries = hashChannel.entrySet();
//        for (Map.Entry<String, Channel> e:entries) {
//            ArrayList<String> bcdNum = redis.getBCDNum(e.getKey());
//            for (String str: bcdNum) {
//
//            }
//
//        }
//        return String.valueOf(strings);
//    }

    @GetMapping("/check/{bcd}/{num}")
    String checkone(@PathVariable String bcd, @PathVariable int num) {
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

    @GetMapping("/jf")
    String JF() {
        Hashtable<String, Channel> hashChannel = chargeChannel.getHashChannel();
        Set<Map.Entry<String, Channel>> entries = hashChannel.entrySet();
        for (Map.Entry<String, Channel> e:entries) {
            String BCD = e.getKey();
            Channel channel = e.getValue();
            ChargingSetting.Start(BCD, channel);
        }
        return "successful";
    }
}
