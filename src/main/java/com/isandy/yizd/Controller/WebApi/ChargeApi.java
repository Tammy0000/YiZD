package com.isandy.yizd.Controller.WebApi;

import com.alibaba.fastjson2.JSON;
import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeBCD;
import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeChannel;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiChargeCustomerStartChargeService;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeOnTimer;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeReboot;
import com.isandy.yizd.dao.ChannelRealTimeHashtable;
import com.isandy.yizd.dao.ChargeActiveStatusRedis;
import com.isandy.yizd.dao.ChargeRealMaps;
import com.isandy.yizd.dao.ChargeRealTimeStatus;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@RequestMapping("/api")
@RestController
@Slf4j
public class ChargeApi {
    @Resource
    ChargeActiveStatusRedis activeStatusRedis;

    @Resource
    ChannelRealTimeHashtable realTimeHashtable;

    @Resource
    YiChargeCustomerStartChargeService startChargeService;

    @Resource
    YiChargeChannel chargeChannel;

    @Resource
    YiDaHuaChargeOnTimer onTimer;

    @Resource
    YiDaHuaChargeReboot chargeReboot;

    @GetMapping("/status")
    String status(@RequestParam String bcd, @RequestParam String muzzleNum) {
        String s = bcd + muzzleNum;
        ChargeRealTimeStatus realTimeStatus = activeStatusRedis.getRealTimeStatus(s);
        return JSON.toJSONString(realTimeStatus);
    }

    /**
     * 
     * @param bcd 电桩编号
     * @param muzzleNum 电桩枪号
     * @return eg:'result:true'
     */
    @GetMapping("/start")
    String checkcharge(@RequestParam String bcd, @RequestParam int muzzleNum) {
        HashMap<String, Boolean> sb = new HashMap<>();
        try {
            ChargeRealMaps chargeMaps = realTimeHashtable.getChargeMaps(bcd);
            startChargeService.Start(bcd, muzzleNum, chargeMaps.getChannel());
            sb.put("result", true);
            return JSON.toJSONString(sb);
        } catch (Exception e) {
            sb.put("result", false);
            return JSON.toJSONString(sb);
        }
    }

    /**
     * 获取所有在线电桩信息
     * @return json格式，eg: '1：BCD编码'
     */
    @GetMapping("/online")
    String online() {
        Hashtable<String, Channel> hashtable = realTimeHashtable.CheckChannel();
        HashMap<Integer, String> is = new HashMap<>();
        if (!hashtable.isEmpty()) {
            int num = 1;
            for(Map.Entry<String, Channel> entry: hashtable.entrySet()) {
                is.put(num, entry.getKey());
                num ++;
            }
            return JSON.toJSONString(is);
        } else {
            is.put(0, null);
            return JSON.toJSONString(is);
        }
    }

    @GetMapping("/onetime")
    String onetime() {
        Hashtable<String, Channel> hashChannel = chargeChannel.getHashChannel();
        Set<Map.Entry<String, Channel>> entries = hashChannel.entrySet();
        for (Map.Entry<String, Channel> e: entries) {
            String BCD = e.getKey();
            Channel ch = e.getValue();
            try {
                onTimer.Start(BCD, ch);
                log.info("对时的桩号是:"+BCD);
            } catch (ParseException ex) {
                log.info("对时失败");
            }
        }
        return "对时完成";
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
}
