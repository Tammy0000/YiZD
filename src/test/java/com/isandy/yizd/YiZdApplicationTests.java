package com.isandy.yizd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.Cp56Time2a;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.CustomTime;
import com.isandy.yizd.ChargeNetty.Pojo.ChargeMongo;
import com.isandy.yizd.dao.Charge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class YiZdApplicationTests {
    @Autowired
    ChargeMongo chargeMongo;
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        byte[] bytes = ByteUtils.toByte(100000, 4, false);
        System.out.println(ByteUtils.bytesToHexFun2(bytes));
    }

}
