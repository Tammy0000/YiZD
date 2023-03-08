package com.isandy.yizd.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;

@Component
@Slf4j
public class Redis {

    Jedis jedis;

    @Value("${custom.RedisAddress}")
    String address;

    @Value("${custom.RedisPort}")
    int port;

    @Value("${custom.RedisPassword}")
    String Password;

    @Bean
    public void initRedis() {
        try {
            jedis = new Jedis(address, port);
            if (!address.equals("10.0.0.6")){
                jedis.auth(Password);
            }
            jedis.set("Hello", "Java");
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

    public int getSeq(String StrBCD) {
        String seq = jedis.hget(StrBCD, "Seq");
        try {
            return Integer.parseInt(seq);
        } catch (NumberFormatException e) {
            log.error("Seq提取失败");
            return -1;
        }
    }

    public ArrayList<String> getBCDNum(String StrBCD) {
        ArrayList<String> list = new ArrayList<>();
        String sumMuzzle = jedis.hget(StrBCD, "SumMuzzle");
        int sum = Integer.parseInt(sumMuzzle);
        for (int i = 1; i < sum + 1; i++) {
            list.add(StrBCD + i);
        }
        return list;
    }

    private void Test() {

    }
}
