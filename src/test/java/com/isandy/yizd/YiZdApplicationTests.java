package com.isandy.yizd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.CustomTime;
import com.isandy.yizd.Service.BillModel;
import com.isandy.yizd.Service.CreateOrder;
import com.isandy.yizd.dao.PayMapper;
import com.isandy.yizd.dao.Redis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
class YiZdApplicationTests {
    @Autowired
    Redis redis;
    @Test
    void contextLoads() throws ParseException {
    }
}
