package com.isandy.yizd.Controller.WebApi;

import com.alibaba.fastjson2.JSON;
import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeChannel;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeStartChargeService;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeOnTimer;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeReboot;
import com.isandy.yizd.ChargeNetty.Pojo.ChargeStatusMongo;
import com.isandy.yizd.dao.ChannelRealTimeHashtable;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

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
    MongoTemplate mongoTemplate;

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
        ArrayList<String> allStrBCD = chargeChannel.getAllStrBCD();
        ArrayList<String> online = new ArrayList<>();
        for (String s: allStrBCD) {
            List<ChargeStatusMongo> statusMongos = mongoTemplate.find(Query.query(Criteria.where("BCD").is(s)),
                    ChargeStatusMongo.class);
            for (ChargeStatusMongo cg: statusMongos) {
                if (cg.getMuzzleStatus().equals("空闲")) {
                    online.add(JSON.toJSONString(cg));
                }
            }
        }
        return String.valueOf(online);
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

    @GetMapping("/check/all")
    String checkall() {
        ArrayList<String> strings = new ArrayList<>();
        Hashtable<String, Channel> hashChannel = chargeChannel.getHashChannel();
        Set<Map.Entry<String, Channel>> entries = hashChannel.entrySet();
        for (Map.Entry<String, Channel> e:entries) {
            Criteria criteria = new Criteria();
            criteria.and("BCD").is(e.getKey());
            List<ChargeStatusMongo> mongoList = mongoTemplate.find(Query.query(criteria), ChargeStatusMongo.class);
            for (ChargeStatusMongo go: mongoList) {
                strings.add(JSON.toJSONString(go));
            }
        }
        return String.valueOf(strings);
    }

    @GetMapping("/check/{bcd}/{num}")
    String checkone(@PathVariable String bcd, @PathVariable int num) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(bcd)
                .and("MuzzleNum").is(num);
        ChargeStatusMongo one = mongoTemplate.findOne(Query.query(criteria), ChargeStatusMongo.class);
        return JSON.toJSONString(one);
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
}
