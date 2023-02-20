package com.isandy.yizd;

import com.isandy.yizd.ChargeNetty.Pojo.ChargeMongo;
import com.isandy.yizd.dao.Charge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@SpringBootTest
class YiZdApplicationTests {
    @Autowired
    ChargeMongo chargeMongo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        System.out.println(chargeMongo.RemoveCharge("1234567", 1));
    }

}
