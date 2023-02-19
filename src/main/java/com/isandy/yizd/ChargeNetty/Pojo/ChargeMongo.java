package com.isandy.yizd.ChargeNetty.Pojo;

import com.isandy.yizd.dao.Charge;
import com.isandy.yizd.dao.ChargeImpl;
import lombok.extern.slf4j.Slf4j;
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
    MongoTemplate mongo;

    @Override
    public void insert(Charge charge) {
        mongo.insert(charge);
    }

    @Override
    public boolean updateSeq(String BCD, int MuzzleNum, int Seq) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD);
        criteria.and("MuzzleNum").is(MuzzleNum);
        Query query = Query.query(criteria);
        Update seq = Update.update("Seq", Seq);
        return mongo.updateMulti(query, seq, Charge.class).wasAcknowledged();
    }

    @Override
    public boolean updateChargeStatus(String BCD, int MuzzleNum, Charge charge) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD);
        criteria.and("MuzzleNum").is(MuzzleNum);
        Query query = Query.query(criteria);
        Update update = new Update();
        update.set("MuzzleEC", charge.getMuzzleEC());
        update.set("MuzzleStatus", charge.getMuzzleStatus());
        update.set("MuzzleVolt", charge.getMuzzleVolt());
        update.set("LeftTime", charge.getLeftTime());
        update.set("SumCharge", charge.getSumCharge());
        update.set("ChargeAddTime", charge.getChargeAddTime());
        update.set("BatteryHighTemp", charge.getBatteryHighTemp());
        update.set("SumMuzzle", charge.getSumMuzzle());
        return true;
    }

    @Override
    public int findSeq(String BCD, int MuzzleNum) {
        Criteria criteria = new Criteria();
        criteria.and("BCD").is(BCD);
        criteria.and("MuzzleNum").is(MuzzleNum);
        Query query = Query.query(criteria);
        Charge one = mongo.findOne(query, Charge.class);
        assert one != null;
        return one.getSeq();
    }

    @Override
    public Charge findContext(String BCD, int Muzzle) {
        return null;
    }

    @Override
    public boolean RemoveCharge(String BCD, int Muzzle) {
        return false;
    }
}
