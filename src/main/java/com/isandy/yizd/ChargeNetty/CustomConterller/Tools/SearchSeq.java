//package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.Jedis;
//
//@Slf4j
//@Lazy
//@Component
//public class SearchSeq {
//    @Value("${custom.RedisAddress}")
//    String RedisAddress;
//
//    @Value("${custom.RedisPort}")
//    int RedisPort;
//
//    public int find(String BCD) {
//        try (Jedis jedis = new Jedis(RedisAddress, RedisPort)) {
//            String s = jedis.get(BCD);
//            return Integer.parseInt(s);
//        } catch (NumberFormatException e) {
//            log.info("SearchSeq初始化失败");
//            return -1;
//        }
//    }
//}
