package com.isandy.yizd.dao;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 2023年1月15日21:24:40
 * 有时间记得优化这里
 * 而且调用方法有点怪怪。
 * 时间紧吧，暂且先这样写。
 */
@Component
@Slf4j
public class ChannelRealTimeHashtable {
    @Value("${custom.RedisAddress}")
    String RedisAddress;
    @Value("${custom.RedisPort}")
    int RedisPort;

    Hashtable<String, Channel> ch ;

    Hashtable<String, YiChargeContext> sy ;

    Jedis jedis ;

    boolean flag = false;

    @Resource
    ChargeRealMaps cms;

    public ChannelRealTimeHashtable() {
        ch = new Hashtable<>();
        sy = new Hashtable<>();
    }

    @Bean
    private void ChannelRealTimeHashtableinit() {
        try {
            jedis = new Jedis(RedisAddress, RedisPort);
        } catch (Exception e) {
            log.info("ChannelRealTimeHashtable初始化失败");
        }
    }

    /**
     * 2023-1-15 01:34:18
     * 这个方法建议使用在心跳包判别代码中
     * @param BCD context中的getStrBCD()方法的值
     * @param channel 传入需要判别的Channel
     *
     */

    public void add(String BCD, Channel channel, YiChargeContext context) {
        if (!ch.containsKey(BCD)) {
            ch.put(BCD, channel);
        } else if (ch.get(BCD) != channel) {
            ch.remove(BCD);
            ch.put(BCD, channel);
        }

        if (!sy.containsKey(BCD)) {
            sy.put(BCD, context);
        } else if (sy.get(BCD) != context) {
            sy.remove(BCD);
            sy.put(BCD, context);
        }

        log.info("当前通道还有:"+ch.size());
        log.info("当前context还有:"+sy.size());
    }

    /**
     * 2023年1月15日01:33:14
     * 方法建议使用在ChannelInboundHandlerAdapter或ChannelOutboundHandlerAdapter中的channelInactive
     * @param channel 需要删除的通道
     *
     */
    public void remove(Channel channel) {
        if (ch.containsValue(channel)) {
            Set<Map.Entry<String, Channel>> entries = ch.entrySet();
            for (Map.Entry<String, Channel> e: entries) {
                if (channel == e.getValue()){
//                    e.getValue().close();
                    ch.remove(e.getKey());
                    sy.remove(e.getKey());
                    log.info("删除通道成功");
                    log.info("当前删除后通道还有:"+ch.size());
                    log.info("当前删除后context还有:"+sy.size());
                }
            }
        }
    }

    /**
     * 2023年1月15日01:33:32
     * @param BCD 需要查询的BCD码
     * @return 返回ChargeMaps对象，分别从对象中获取Sequence和相对应的Channel
     */
    public ChargeRealMaps getChargeMaps(String BCD) {
        if (ch.containsKey(BCD)) {
            cms.setChannel(ch.get(BCD));
            cms.setContext(sy.get(BCD));
            cms.setStrBCD(BCD);
            String s = jedis.get(BCD);
            cms.setStr_sequence(s);
            cms.setInt_sequence(Integer.parseInt(s));
            return cms;
        }else {
            return null;
        }
    }

    /**
     * 2023-1-15 09:45:44
     * @param BCD 判断桩是否存在
     * @return TRUE存在，反之
     */
    public boolean CheckChannel(String BCD) {
        return ch.containsKey(BCD);
    }

    public int Size() {
        return ch.size();
    }

    /**
     * 2023年1月28日22:58:20
     * 定时检查不活跃通道，并且删除
     * 5分钟执行一次
     */
//    @Scheduled(cron = "0 0/3 * * * ?")
    public void AutoRemove() throws InterruptedException {
        try (Jedis Beanjedis = new Jedis(RedisAddress, RedisPort)) {
        /*
        解决定时任务Unexpected error occurred in scheduled task
        2023年2月3日09:39:21
        测试用
         */
            Thread.sleep(1000);
            log.info("--^_^--开启定时任务");
            if (!ch.isEmpty() && !sy.isEmpty() && flag) {
                Set<Map.Entry<String, Channel>> entries = ch.entrySet();
                for (Map.Entry<String, Channel> e : entries) {
                    String key = "check" + e.getKey();
                    if (Beanjedis.get(key) == null) {
                        remove(ch.get(e.getKey()));
                    } else if (Objects.equals(Beanjedis.get(key), Beanjedis.get(e.getKey()))) {
                        remove(ch.get(e.getKey()));
                        Beanjedis.del(key);
                    }
                }
            }
        }
        flag = true;
    }

    public Hashtable<String, Channel> CheckChannel() {
        return ch;
    }
}
