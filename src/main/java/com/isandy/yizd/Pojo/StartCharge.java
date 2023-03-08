package com.isandy.yizd.Pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "t_startCharge")
public class StartCharge {
    @TableId
    Integer id;
    String strbcd;
    int muzzlenum;
    String ordercode;
    Date logintime;
    String result;
}
