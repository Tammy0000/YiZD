package com.isandy.yizd;

import com.isandy.yizd.ChargeNetty.Pojo.ChargeMongo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class YiZdApplicationTests {
    @Autowired
    ChargeMongo chargeMongo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {

    }
}
