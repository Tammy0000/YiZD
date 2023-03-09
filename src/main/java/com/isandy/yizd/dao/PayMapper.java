package com.isandy.yizd.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.isandy.yizd.Pojo.PayCharge;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Lazy;

@Mapper
@Lazy
public interface PayMapper extends BaseMapper<PayCharge> {
}
