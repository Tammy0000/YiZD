package com.isandy.yizd.ChargeNetty.Pojo;

import com.isandy.yizd.dao.Charge;
import com.isandy.yizd.dao.ChargeImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class ChargeMongo implements ChargeImpl {
    @Autowired
    MongoTemplate mongo;

    @Override
    public void insertLogin(Charge charge) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(charge.getBCD());
        mongo.remove(Query.query(criteria), Charge.class);
        for (int i = 1; i < charge.getSumMuzzle() + 1; i++) {
            Charge c = new Charge();
            /*
            必须要浅拷贝，
            直接赋值会报错
             */
            BeanUtils.copyProperties(charge, c);
            c.setMuzzleNum(i);
            mongo.insert(c);
        }
    }

    @Override
    public boolean updateChargeStatus(String BCD, int MuzzleNum, Charge charge) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("MuzzleNum").is(MuzzleNum);
        Query query = Query.query(criteria);
        Update update = new Update();
        update
        .set("MuzzleLink", charge.getMuzzleLink())
        .set("MuzzleEC", charge.getMuzzleEC())
        .set("MuzzleStatus", charge.getMuzzleStatus())
        .set("MuzzleVolt", charge.getMuzzleVolt())
        .set("LeftTime", charge.getLeftTime())
        .set("SumCharge", charge.getSumCharge())
        .set("ChargeAddTime", charge.getChargeAddTime())
        .set("BatteryHighTemp", charge.getBatteryHighTemp());
        return mongo.upsert(query, update, Charge.class).wasAcknowledged();
    }

    @Override
    public Charge findContext(String BCD, int MuzzleNum) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("MuzzleNum").is(MuzzleNum);
        Query query = Query.query(criteria);
        return mongo.findOne(query, Charge.class);
    }

    @Override
    public boolean RemoveCharge(String BCD, int MuzzleNum) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("MuzzleNum").is(MuzzleNum);
        return mongo.remove(Query.query(criteria), Charge.class).wasAcknowledged();
    }

    @Override
    public void insertSumMuzzle(String BCD, int sumMuzzle) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("SumMuzzle").is(sumMuzzle);
        Query query = Query.query(criteria);
        Charge one = mongo.findOne(query, Charge.class);
        if (one == null) {
            Charge charge = new Charge();
            charge.setSumMuzzle(sumMuzzle);
            charge.setBCD(BCD);
            mongo.insert(charge);
        }
    }
}
