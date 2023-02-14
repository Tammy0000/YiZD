package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

import java.text.DecimalFormat;

/**
 * @Introduce 类简介
 * @Module 模块说明
 * @Author ASUS
 * @Date 2022/3/15 15:29
 * @Desc 功能描述: doc 文档 工具
 **/
public class BCDCodeUtils {
    /**
     * <编码>
     * <数字字符串编成BCD格式字节数组>
     *
     * @param bcd 数字字符串
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static byte[] str2bcd(String bcd) {
        if (bcd.isEmpty()) {
            return null;
        } else {
            if(bcd.length() == 1){
                bcd = "0"+bcd;
            }
            // 获取字节数组长度
            int size = bcd.length() / 2;
            int remainder = bcd.length() % 2;

            // 存储BCD码字节
            byte[] bcdByte = new byte[size + remainder];

            // 转BCD码
            for (int i = 0; i < size; i++) {
                bcdByte[i] = (byte)Integer.parseInt(bcd.substring(2 * i, 2 * i + 2),16);
            }

            // 如果存在余数，需要填F
            if (remainder > 0) {
                bcdByte[bcdByte.length - 1] = (byte) 0;
            }

            // 返回BCD码字节数组
            return bcdByte;
        }
    }
//    public static byte[] str2bcd(String bcd) {
//        if (bcd.isEmpty()) {
//            return null;
//        } else {
//            // 获取字节数组长度
//            int size = bcd.length() / 2;
//            int remainder = bcd.length() % 2;
//
//            // 存储BCD码字节
//            byte[] bcdByte = new byte[size + remainder];
//
//            // 转BCD码
//            for (int i = 0; i < size; i++) {
//                int low = Integer.parseInt(bcd.substring(2 * i, 2 * i + 1));
//                int high = Integer.parseInt(bcd.substring(2 * i + 1, 2 * i + 2));
//                bcdByte[i] = (byte) ((high << 4) | low);
//            }
//
//            // 如果存在余数，需要填F
//            if (remainder > 0) {
//                int low = Integer.parseInt(bcd.substring(bcd.length() - 1));
//                bcdByte[bcdByte.length - 1] = (byte) ((0xf << 4) | low);
//            }
//
//            // 返回BCD码字节数组
//            return bcdByte;
//        }
//    }

    /**
     * <解码>
     * <BCD格式的字节数组解成数字字符串>
     *
     * @param bcd 字节数组
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String bcd2str(byte[] bcd) {
        if (null == bcd || bcd.length == 0) {
            return "";
        } else {
            // 存储转码后的字符串
            StringBuilder sb = new StringBuilder();

            // 循环数组解码
            for (int i = 0; i < bcd.length; i++) {
                String s = Integer.toHexString(bcd[i] & 0xFF);
                if(s.length() == 1){
                    sb.append("0");
                }
                // 转换低字节
                sb.append(s);
            }

            // 返回解码字符串
            return sb.toString();
        }
    }

    public static String bcd2str(byte[] source, int startIndex ,int byteLength) {
        if(startIndex+ byteLength > source.length){
            return "";
        }
        byte[] bcd = new byte[byteLength];
        for(int i = startIndex, len = startIndex+byteLength; i< len;i++){
            bcd[i-startIndex] = source[i];
        }
        return bcd2str(bcd);
    }

//    public static String bcd2str(byte[] bcd) {
//        if (null == bcd || bcd.length == 0) {
//            return "";
//        } else {
//            // 存储转码后的字符串
//            StringBuilder sb = new StringBuilder();
//
//            // 循环数组解码
//            for (int i = 0; i < bcd.length; i++) {
//                // 转换低字节
//                int low = (bcd[i] & 0x0f);
//                sb.append(low);
//
//                // 转换高字节
//                int high = ((bcd[i] & 0xf0) >> 4);
//
//                // 如果高字节等于0xf说明是补的字节，直接抛掉
//                if (high != 0xf) {
//                    sb.append(high);
//                }
//            }
//
//            // 返回解码字符串
//            return sb.toString();
//        }
//    }


    public static Double bcd2Double(byte[] bcd,int decimal) {
        String s = bcd2str(bcd);
        if(decimal <= 0){
            return Double.valueOf(s);
        }
        return Double.valueOf(s.substring(0,s.length()-decimal)+"."+s.substring(s.length()-decimal));
    }


    public static byte[] double2bcd(Double toBcd, int byteLength,int decimal ) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ;i< byteLength;i++){
            sb.append("00");
        }
        String f = sb.toString();
        sb = null;
        DecimalFormat g=new DecimalFormat(f.substring(0, f.length()-decimal)+"."+f.substring(f.length()-decimal));
        return str2bcd(g.format(toBcd).replace(".", ""));

    }






}