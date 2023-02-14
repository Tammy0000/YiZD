package com.isandy.yizd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.Cp56Time2a;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.CustomTime;
import com.isandy.yizd.dao.ChannelRealTimeHashtable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class YiZdApplicationTests {
    //98B70E11100314

    @Test
    void contextLoads() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = sdf.parse(CustomTime.time());
        byte[] bytes = Cp56Time2a.toBytes(parse);
        System.out.println(ByteUtils.bytesToHexFun2(bytes));
        Date date = Cp56Time2a.toDate(bytes);
        System.out.println(date);
    }

}
