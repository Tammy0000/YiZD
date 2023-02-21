package com.isandy.yizd.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@Slf4j
public class RedisTemplate implements RedisImpl{
    @Value("${custom.RedisAddress}")
    String RedisAddress;

    @Value("${custom.RedisPort}")
    int RedisPort;

    Jedis jedis;

    @Bean
    public void RedisTemplateinit() {
        try {
            jedis = new Jedis(RedisAddress, RedisPort);
        } catch (Exception e) {
            log.info("RedisTemplate初始化失败");
        }
    }

    @Override
    public void setSeq(String BCD, int Seq) {
        jedis.set(BCD, String.valueOf(Seq));
    }

    @Override
    public int getSeq(String BCD) {
        String s = jedis.get(BCD);
        return Integer.parseInt(s);
    }

    @Override
    public void setPublicSeq(String BCD, int Seq) {
        jedis.set(BCD, String.valueOf(Seq));
    }

    @Override
    public int getPublicSeq(String BCD) {
        String s = jedis.get(BCD);
        return Integer.parseInt(s);
    }
}
