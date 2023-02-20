package com.isandy.yizd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.CustomTime;
import com.isandy.yizd.ChargeNetty.Pojo.ChargeMongo;
import com.isandy.yizd.dao.Charge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest
class YiZdApplicationTests {
    @Autowired
    ChargeMongo chargeMongo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        Charge charge = new Charge();
        charge.setBCD("123456789");
        charge.setSumMuzzle(2);
        charge.setMuzzleVolt(387);
        charge.setMuzzleEC(65.2);
        charge.setMuzzleNum(1);
        Update update = new Update();
        Criteria criteria = new Criteria();
        chargeMongo.insertLogin(charge);
    }

}
