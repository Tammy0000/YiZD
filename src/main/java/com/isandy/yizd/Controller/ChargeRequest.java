package com.isandy.yizd.Controller;

import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiChargeCustomerStartChargeService;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiChargeCustomerStopChargeService;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeReboot;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargeStatusRequest;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.dao.ChannelRealTimeHashtable;
import com.isandy.yizd.dao.ChargeActiveStatusRedis;
import com.isandy.yizd.dao.ChargeRealMaps;
import com.isandy.yizd.dao.ChargeRealTimeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@RestController
@Slf4j
public class ChargeRequest {
    @Value("${custom.RedisAddress}")
    String RedisAddress;

    @Value("${custom.RedisPort}")
    int RedisPort;

    Jedis jedis;

    @Resource
    ChannelRealTimeHashtable channelRealTimeHashtable;
    @Resource
    YiDaHuaChargeStatusRequest yirequest;

    @Resource
    YiDaHuaChargeReboot yiReboot;

    @Resource
    YiChargeCustomerStopChargeService css;

    @Resource
    ChargeActiveStatusRedis cars;

    @Resource
    YiChargeCustomerStartChargeService ccs;

    @Bean
    void ChargeRequestinit() {
        try {
            jedis = new Jedis(RedisAddress, RedisPort);
            jedis.set("test", "test");
        } catch (Exception e) {
           log.error("web中的Redis初始化失败");
        }
    }

    @GetMapping("/status/{BCD}")
    String OnLink(@PathVariable("BCD") String BCD) {
        boolean b = channelRealTimeHashtable.CheckChannel(BCD);
        if (b) {
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
        ChargeRealMaps chargeMaps = channelRealTimeHashtable.chargeMaps(BCD);
        try {
            String s = BCD+muzzle;
            ChargeRealTimeStatus crts = cars.Get(s);
                return String.format("""
                                电桩编号: %s
                                电桩枪号: %d
                                枪状态：%s
                                插枪状态: %s
                                累计充电时间: %d
                                剩余时间: %d
                                输出电压：%f
                                输出电流：%f
                                累计充电度数：%f
                                电池组温度：%f""",
                        BCD, crts.getMuzzleNum(), crts.getMuzzleStatus(), crts.getMuzzleLink(), crts.getChargeAddTime(),
                        crts.getLeftTime(), crts.getMuzzleVolt(), crts.getMuzzleEC(), crts.getSumCharge(), crts.getBatteryHighTemp()
                );
        } catch (Exception e) {
            return "暂时没查到该电桩信息";
        }
        /*
        这里提供一个思路，单独处理0x13,避免出现获取到null
         */

    }

    @GetMapping("/start/{BCD}/{muzzle}")
    String adds(@PathVariable("BCD") String BCD, @PathVariable("muzzle") String muzzle) {
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
        ChargeRealMaps chargeMaps = channelRealTimeHashtable.chargeMaps(BCD);
        int i;
        try {
            i = Integer.parseInt(muzzle);
            if (i > 2 || i == 0) {
                return "输入枪号或BCD编码有误";
            }
        } catch (NumberFormatException e) {
            return "输入枪号或BCD编码有误";
        }
        byte[] temp = new byte[2];
        temp[0] = 0x46;
        temp[1] = 0x22;
        int it = ByteUtils.toInt(temp);
        ccs.Start(chargeMaps.getContext(), chargeMaps.getContext().getBCD(), i,
                chargeMaps.getChannel(), it);
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
        ChargeRealMaps chargeMaps = channelRealTimeHashtable.chargeMaps(BCD);
        int i;
        try {
            i = Integer.parseInt(muzzle);
            if (i > 2 || i == 0) {
                return "输入枪号或BCD编码有误";
            }
        } catch (NumberFormatException e) {
            return "输入枪号或BCD编码有误";
        }
        css.Start(chargeMaps.getContext(), chargeMaps.getContext().getBCD(), i, chargeMaps.getChannel(),
                chargeMaps.getContext().getInt_sequence());
        return "发送停止充电命令完成";
    }

    @GetMapping("/reboot/{BCD}")
    String Reboot(@PathVariable("BCD") String BCD) {
        ChargeRealMaps chargeMaps = channelRealTimeHashtable.chargeMaps(BCD);
        yiReboot.Start(chargeMaps.getContext(), chargeMaps.getContext().getBCD(),
                chargeMaps.getChannel(), Integer.parseInt(jedis.get(BCD)));
        return "发送重启电桩命令成功";
    }

    @GetMapping("/test/{BCD}")
    String Test(@PathVariable("BCD") String BCD) {
        ChargeRealMaps chargeMaps = channelRealTimeHashtable.chargeMaps(BCD);
        yirequest.Start(chargeMaps.getContext(), chargeMaps.getContext().getBCD(),
                1, chargeMaps.getChannel(), Integer.parseInt(jedis.get(BCD)));
        return "test";
    }

    @GetMapping("/find/")
    String checks() {
        return "";
    }
}
