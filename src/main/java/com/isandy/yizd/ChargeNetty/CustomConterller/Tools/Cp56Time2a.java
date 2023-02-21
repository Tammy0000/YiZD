package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * CP56time2a
 * Calendar中，月份是从0开始变化的，Calendar中获取月份获取到的值是8，说明当前是9月，
 * 如果是与C进行交互，发送的时候需要在获取到的月份的数值上加1.解析时需要在获取的数值上减1；
 **/
public class Cp56Time2a {

    public static Date toDate(byte[] bytes) {
        int milliseconds1 = bytes[0] < 0 ? 256 + bytes[0] : bytes[0];
        int milliseconds2 = bytes[1] < 0 ? 256 + bytes[1] : bytes[1];
        int milliseconds = milliseconds1 + milliseconds2 * 256;
        // 位于 0011 1111
        int minutes = bytes[2] & 0x3f;
        // 位于 0001 1111
        int hours = bytes[3] & 0x1f;
        // 位于 0001 1111
        int days = bytes[4] & 0x1f;
        // 位于 0000 1111
        int months = bytes[5] & 0x0f;
        // 位于 0111 1111
        int years = bytes[6] & 0x7f;
        final Calendar aTime = Calendar.getInstance();
        aTime.set(Calendar.MILLISECOND, milliseconds);
        aTime.set(Calendar.MINUTE, minutes);
        aTime.set(Calendar.HOUR_OF_DAY, hours);
        aTime.set(Calendar.DAY_OF_MONTH, days);
        aTime.set(Calendar.MONTH, months);
        aTime.set(Calendar.YEAR, years + 2000);
        // 如果是与C进行交互，发送的时候需要在获取到的月份的数值上加1.解析时需要在获取的数值上减1；
        aTime.add(Calendar.MONTH,-1);
        return aTime.getTime();
    }

    public static byte[] toBytes(Date aDate) {
        byte[] result = new byte[7];
        final Calendar aTime = Calendar.getInstance();
        aTime.setTime(aDate);
        // 如果是与C进行交互，发送的时候需要在获取到的月份的数值上加1.解析时需要在获取的数值上减1；
        aTime.add(Calendar.MONTH,1);
        final int milliseconds = aTime.get(Calendar.MILLISECOND);
        result[0] = (byte) (milliseconds % 256);
        result[1] = (byte) (milliseconds / 256);
        result[2] = (byte) aTime.get(Calendar.MINUTE);
        result[3] = (byte) aTime.get(Calendar.HOUR_OF_DAY);
        result[4] = (byte) aTime.get(Calendar.DAY_OF_MONTH);
        result[5] = (byte) aTime.get(Calendar.MONTH);
        result[6] = (byte) (aTime.get(Calendar.YEAR) % 100);
        return result;
    }

    public static Date byte2Date(byte[] source, int startIndex ,int byteLength) {
        if(startIndex+ byteLength > source.length){
            return null;
        }
        byte[] bcd = new byte[byteLength];
        for(int i = startIndex, len = startIndex+byteLength; i< len;i++){
            bcd[i-startIndex] = source[i];
        }
        return toDate(bcd);
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date aDate = sdf.parse("2022-08-13 16:54:25");
        System.out.println(aDate);
        System.out.println(sdf.format(aDate));
        final byte[] bytes = toBytes(aDate);
        //final Date date = toDate(bytes);
        final Date date = toDate(new byte[]{0x68, 0x42, 0x02, 0x10, 0x6d, 0x07, 0x16});
        final Date date1 = toDate(new byte[]{(byte) 0xb0, (byte) 0xb3,0x35 ,0x10 ,0x6d ,0x07 ,0x16});

    }

}
