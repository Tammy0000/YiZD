package com.isandy.yizd;

import com.isandy.yizd.ChargeNetty.Pojo.ChargeMongo;
import com.isandy.yizd.ChargeNetty.Pojo.Charge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Hashtable;

@SpringBootTest
class YiZdApplicationTests {
    @Autowired
    ChargeMongo chargeMongo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        Hashtable<String, String> s = new Hashtable<>();
        boolean ss = !s.containsKey("ss");
    }
}
