package com.isandy.yizd.ChargeNetty.Pojo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class ChargeMongo implements ChargeImpl {
    @Resource
    MongoTemplate mongoTemplate;

    @Override
    public void insertLogin(ChargeStatusMongo chargeStatusMongo) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(chargeStatusMongo.getBCD());
        mongoTemplate.remove(Query.query(criteria), ChargeStatusMongo.class);
        for (int i = 1; i < chargeStatusMongo.getSumMuzzle() + 1; i++) {
            ChargeStatusMongo c = new ChargeStatusMongo();
            /*
            必须要浅拷贝，
            直接赋值会报错
             */
            BeanUtils.copyProperties(chargeStatusMongo, c);
            c.setMuzzleNum(i);
            mongoTemplate.insert(c);
        }
    }

    @Override
    public boolean updateChargeStatus(String BCD, int MuzzleNum, ChargeStatusMongo chargeStatusMongo) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("MuzzleNum").is(MuzzleNum);
        Query query = Query.query(criteria);
        Update update = new Update();
        update
        .set("MuzzleLink", chargeStatusMongo.getMuzzleLink())
        .set("MuzzleEC", chargeStatusMongo.getMuzzleEC())
        .set("MuzzleStatus", chargeStatusMongo.getMuzzleStatus())
        .set("MuzzleVolt", chargeStatusMongo.getMuzzleVolt())
        .set("LeftTime", chargeStatusMongo.getLeftTime())
        .set("SumCharge", chargeStatusMongo.getSumCharge())
        .set("ChargeAddTime", chargeStatusMongo.getChargeAddTime())
        .set("BatteryHighTemp", chargeStatusMongo.getBatteryHighTemp());
        return mongoTemplate.upsert(query, update, ChargeStatusMongo.class).wasAcknowledged();
    }

    @Override
    public ChargeStatusMongo findContext(String BCD, int MuzzleNum) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("MuzzleNum").is(MuzzleNum);
        Query query = Query.query(criteria);
        return mongoTemplate.findOne(query, ChargeStatusMongo.class);
    }

    @Override
    public boolean RemoveCharge(String BCD, int MuzzleNum) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("MuzzleNum").is(MuzzleNum);
        return mongoTemplate.remove(Query.query(criteria), ChargeStatusMongo.class).wasAcknowledged();
    }

    @Override
    public void insertSumMuzzle(String BCD, int sumMuzzle) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("SumMuzzle").is(sumMuzzle);
        Query query = Query.query(criteria);
        ChargeStatusMongo one = mongoTemplate.findOne(query, ChargeStatusMongo.class);
        if (one == null) {
            ChargeStatusMongo chargeStatusMongo = new ChargeStatusMongo();
            chargeStatusMongo.setSumMuzzle(sumMuzzle);
            chargeStatusMongo.setBCD(BCD);
            mongoTemplate.insert(chargeStatusMongo);
        }
    }

    @Override
    public int findSeq(String BCD, int muzzleNum) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD)
                .and("muzzleNum").is(muzzleNum);
        try {
            ChargeStatusMongo one = mongoTemplate.findOne(Query.query(criteria), ChargeStatusMongo.class);
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
            ChargeStatusMongo one = mongoTemplate.findOne(Query.query(criteria), ChargeStatusMongo.class);
            assert one != null;
            return one.getPublicSeq();
        } catch (Exception e) {
            log.error("提取Seq失败,电桩不存在");
            return -1;
        }
    }
}
