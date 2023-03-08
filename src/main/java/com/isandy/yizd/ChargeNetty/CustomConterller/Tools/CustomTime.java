package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomTime {
    public static String time() {
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simple.format(date);
    }

    public static String CreateOrdertime() throws ParseException {
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddHHmmssss");
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
