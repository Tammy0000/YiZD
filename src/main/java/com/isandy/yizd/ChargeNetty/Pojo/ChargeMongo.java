package com.isandy.yizd.ChargeNetty.Pojo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChargeMongo implements ChargeImpl {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void insertLogin(Charge charge) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(charge.getBCD());
        mongoTemplate.remove(Query.query(criteria), Charge.class);
        for (int i = 1; i < charge.getSumMuzzle() + 1; i++) {
            Charge c = new Charge();
            /*
            必须要浅拷贝，
            直接赋值会报错
             */
            BeanUtils.copyProperties(charge, c);
            c.setMuzzleNum(i);
            mongoTemplate.insert(c);
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
        return mongoTemplate.upsert(query, update, Charge.class).wasAcknowledged();
    }

    @Override
    public Charge findContext(String BCD, int MuzzleNum) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("MuzzleNum").is(MuzzleNum);
        Query query = Query.query(criteria);
        return mongoTemplate.findOne(query, Charge.class);
    }

    @Override
    public boolean RemoveCharge(String BCD, int MuzzleNum) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("MuzzleNum").is(MuzzleNum);
        return mongoTemplate.remove(Query.query(criteria), Charge.class).wasAcknowledged();
    }

    @Override
    public void insertSumMuzzle(String BCD, int sumMuzzle) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("SumMuzzle").is(sumMuzzle);
        Query query = Query.query(criteria);
        Charge one = mongoTemplate.findOne(query, Charge.class);
        if (one == null) {
            Charge charge = new Charge();
            charge.setSumMuzzle(sumMuzzle);
            charge.setBCD(BCD);
            mongoTemplate.insert(charge);
        }
    }

    @Override
    public int findSeq(String BCD, int muzzleNum) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("muzzleNum").is(muzzleNum);
        try {
            Charge one = mongoTemplate.findOne(Query.query(criteria), Charge.class);
            assert one != null;
            return one.getSeq();
        } catch (Exception e) {
            log.error("提取Seq失败,电桩不存在");
            return -1;
        }
    }

    /**
     * 新建一个提取公共Seq方法，理论上来说，1枪跟2枪Seq都可以用，只不过需要细化，如果不确定，就不写，留空
     * @param strBCD strBCD
     * @return int
     * 2023年2月24日16:20:08
     */
    @Override
    public int findSeq(String strBCD) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(strBCD);
        try {
            Charge one = mongoTemplate.findOne(Query.query(criteria), Charge.class);
            assert one != null;
            return one.getPublicSeq();
        } catch (Exception e) {
            log.error("提取Seq失败,电桩不存在");
            return -1;
        }
    }
}
