package com.isandy.yizd.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "t_Mange")
public class PayCharge {
    @TableId
    Integer id;
    String strbcd;
    int muzzlenum;
    Date starttime;
    Date endtime;
    String startmethod;
    double consume;
    double sumele;
    Date paytime;
    String ordercode;
}
