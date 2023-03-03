package com.isandy.yizd.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@Slf4j
public class Redis {

    Jedis jedis;

    @Value("${custom.RedisAddress}")
    String address;

    @Value("${custom.RedisPort}")
    int port;

    @Bean
    public void initRedis() {
        try {
            jedis = new Jedis(address, port);
        } catch (Exception e) {
            log.error("初始化Redis失败");
        }
    }

    public void hset(String key, String field, String value) {
        jedis.hset(key, field, value);
    }

    public String hget(String key, String field) {
        return jedis.hget(key, field);
    }

    public void hdel(String key, String... fields) {
        jedis.hdel(key, fields);
    }

    public void del(String... key){
        jedis.del(key);
    }
}
