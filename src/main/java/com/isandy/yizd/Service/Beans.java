//package com.isandy.yizd.Service;
//
//import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.SearchSeq;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.nio.NioEventLoopGroup;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Lazy;
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
//    SearchSeq seq() {
//        return new SearchSeq();
//    }
//
//}
