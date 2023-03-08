package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigInteger;

/**
 * description: 对象必须实现序列化
 * author: ckk
 * create: 2020-08-02 17:26
 */
@Slf4j
public class ByteUtils {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 字节数组转 ASCII码
     * @param bcd
     * @return
     */
    public static String byte2Ascii(byte[] bcd) {
        if (null == bcd || bcd.length == 0) {
            return "";
        } else {
            // 存储转码后的字符串
            StringBuilder sb = new StringBuilder();

            // 循环数组解码
            for (int i = 0; i < bcd.length; i++) {
                if(bcd[i] != 0x00){
                    char c = (char) bcd[i];
                    sb.append(c);
                }
            }

            // 返回解码字符串
            return sb.toString();
        }
    }


    public static String bytesToHexFun2(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            sb.append(HEX_CHAR[b >>> 4 & 0xf]).append(HEX_CHAR[b & 0xf]).append(" ");
        }
        return  sb.toString();
    }

    // 字节总和余256
    public static int remain_256(int preSum, byte[] data){
        int sum = preSum;
        for(byte one : data){
            sum += one & 0xff;
        }
        return sum % 256;
    }


    // 将Java对象序列化为byte[]
    public static byte[] obj2byte(Object obj){
        byte[] ret = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(obj);
            out.close();
            ret = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("对象转化异常");
            return null;
        }

        return ret;
    }

    // 将byte[]反序列化为Java对象
    public static Object byte2obj(byte[] bytes)  {
        Object ret = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bais);
            ret = in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            log.info("对象转化异常");
            return null;
        }

        return ret;
    }

    public static byte[] getSplitByte(byte[] data, int startIndex, int length){
        byte[] result = new byte[length];
        for(int i = 0; i < length;i++){
            result[i] = data[startIndex+i];
        }
        return result;
    }

    /**
     * int转字节
     * @param n         int 数值
     * @param byteLen   转换字节数量
     * @return
     */
    public static byte[] toByte(int n, int byteLen) {
       return toByte(n, byteLen, true);
    }

    /**
     *
     * @param Str string转byte[]，传入字符串长度必须符合16进制格式且长度能整除2
     * @return byte[]
     */

    public static byte[] toByte(String Str) {
        int length = Str.length();
        byte[] bytes = new byte[length / 2];
        int k = 0;
        for (int i = 0; i < length; i+=2) {
            String substring = Str.substring(i, i + 2);
            BigInteger bigInteger = new BigInteger(substring, 16);
            bytes[k] = (byte) bigInteger.intValue();
            k ++;
        }
        return bytes;
    }

    public static byte[] toByte(int n, int byteLen, Boolean isNeedReverse) {
        if(byteLen >8){
            throw new RuntimeException("最大不超过8位");
        }
        byte[] b = new byte[byteLen];
        for(int i = 0 ; i< byteLen; i++){
            int digit = i * 8;
            if(digit == 0){
                b[0] = (byte) ((long)n & 0xff);
            }else{
                b[i] = (byte) ((long)n >> digit & 0xff);
            }
        }
        if(isNeedReverse){
            if(byteLen == 1){
                return b;
            }else{
                b = arrayReverse(b);
            }
        }
        return b;
    }

    public static byte[] arrayReverse(byte[] b){
        byte[] reverseArray = new byte[b.length];
        for (int i = 0, len = b.length; i < len; i++) {
            reverseArray[i] = b[len - i - 1];
        }
        return reverseArray;
    }

    /**
     * 字节数组转 int数值
     * @param b1 字节数组
     * @return
     */
    public static int toInt(byte[] b1){
        return toInt(b1, true);
    }

    public static int toInt(byte[] b1, boolean isNeedReverse){
        byte[] b = b1;
        if(isNeedReverse) {
            b = arrayReverse(b1);
        }
        int res = 0;
        for(int i=0;i<b.length;i++){
            res += (b[i] & 0xff) << (i*8);
        }
        return res;
    }

    /**
     * 字节转 int数值
     * @param b1 字节
     * @return
     */
    public static int toInt(byte b1){
        return toInt(b1, true);
    }

    public static int toInt(byte b1, boolean isNeedReverse){
        byte[] b =new byte[]{b1};
        if(isNeedReverse) {
            b = arrayReverse(new byte[]{b1});
        }
        int res = 0;
        for(int i=0;i<b.length;i++){
            res += (b[i] & 0xff) << (i*8);
        }
        return res;
    }

    public static int toInt(byte[] source , int startIndex ,int byteLength){
        return toInt(source, startIndex, byteLength, true);
    }

    public static int toInt(byte[] source , int startIndex ,int byteLength, boolean isNeedReverse){
        if(startIndex+ byteLength > source.length){
            return 0;
        }
        byte[] data = new byte[byteLength];
        for(int i = startIndex, len = startIndex+byteLength; i< len;i++){
            data[i-startIndex] = source[i];
        }
        return toInt(data,isNeedReverse);
    }

//    public static byte[] toByte(int n, int byteLen) {
//        byte[] b = new byte[byteLen];
//        for(int i = 0 ; i< byteLen; i++){
//            int digit = i * 8;
//            if(digit == 0){
//                b[0] = (byte) (n & 0xff);
//            }else{
//                b[i] = (byte) (n >> digit & 0xff);
//            }
//        }
//        return b;
//    }
//
//    public static int toInt(byte[] b){
//        int res = 0;
//        for(int i=0;i<b.length;i++){
//            res += (b[i] & 0xff) << (i*8);
//        }
//        return res;
//    }


    public static int getHeight4(byte data){//获取高四位
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    public static int getLow4(byte data){//获取低四位
        int low;
        low = (data & 0x0f);
        return low;
    }

    public static int getDigit8(byte data){//获取低四位
        int digit;
        digit = ((data & 0x80) >> 7);
        return digit;
    }

    public static int getDigit7(byte data){//获取低四位
        int digit;
        digit = ((data & 0x40) >> 6);
        return digit;
    }

    public static int getDigit6(byte data){//获取低四位
        int digit;
        digit = ((data & 0x20) >> 5);
        return digit;
    }

    public static int getDigit5(byte data){//获取低四位
        int digit;
        digit = ((data & 0x10) >> 4);
        return digit;
    }

    public static int getDigit4(byte data){//获取低四位
        int digit;
        digit = ((data & 0x08) >> 3);
        return digit;
    }

    public static int getDigit3(byte data){//获取低四位
        int digit;
        digit = ((data & 0x04) >> 2);
        return digit;
    }

    public static int getDigit2(byte data){//获取低四位
        int digit;
        digit = ((data & 0x02) >> 1);
        return digit;
    }

    public static int getDigit1(byte data){//获取低四位
        int digit;
        digit = (data & 0x01);
        return digit;
    }

}
