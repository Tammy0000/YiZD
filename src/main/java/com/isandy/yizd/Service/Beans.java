//package com.isandy.yizd.Service;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.nio.NioEventLoopGroup;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.Jedis;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Component
//@Slf4j
//public class Beans {
//    @Value("${custom.RedisAddress}")
//    String RedisAddress;
//    @Value("${custom.RedisPort}")
//    int RedisPort;
//
//    @Bean
//    ServerBootstrap serverBootstrap() {
//        return new ServerBootstrap();
//    }
//
//    @Bean
//    NioEventLoopGroup nioEventLoopGroup() {
//        return new NioEventLoopGroup();
//    }
//
//    @Bean
//    Jedis jedis() {
//        try {
//            Jedis jedis = new Jedis(RedisAddress, RedisPort);
//            jedis.set("Test", "test");
//            jedis.del("Test");
//            log.info("Redis初始化完成");
//            return jedis;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
