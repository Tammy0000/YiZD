package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomTime {
    public static String time() {
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return simple.format(date);
    }
}
