package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomTime {
    public static String time() {
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simple.format(date);
    }

    public static String Paytime() {
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyMMddHHmmss");
        return simple.format(date);
    }

    public static Date toTime() {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simple.parse(time());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String toTime(Date date) {
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simple.format(date);
    }
}
