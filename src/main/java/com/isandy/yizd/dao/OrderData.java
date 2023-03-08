package com.isandy.yizd.dao;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.isandy.yizd.Pojo.PayCharge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Lazy
@Component
@Slf4j
public class OrderData {
    UpdateWrapper<PayCharge> wrapper = new UpdateWrapper<>();

    @Resource
    PayMapper pay;

    public void insert(PayCharge charge) {
        pay.insert(charge);
    }
}
