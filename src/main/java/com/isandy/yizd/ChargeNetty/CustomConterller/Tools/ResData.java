package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;


import com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext.YiChargeContext;

public class ResData {
    public static byte[] responseData(YiChargeContext context, DaHuaCmdEnum cmdEnum, byte[] data, int sequence){
        // 头部长度 +
        byte[] responseData = new byte[8 +data.length];
        byte[] validByte = new byte[4+data.length];
        // 起始标志
        responseData[0] = ByteUtils.toByte(0x68,1)[0];
        // 数据长度 去除头尾
        responseData[1] = ByteUtils.toByte(responseData.length-4, 1)[0];
        // 序列号域
        byte[] seq = ByteUtils.toByte(sequence, 2);
        responseData[2] = seq[0];
        responseData[3] = seq[1];
        validByte[0] = seq[0];
        validByte[1] = seq[1];
        // 加密标志
        responseData[4] = context.getEncryption()[0];
        validByte[2] = context.getEncryption()[0];
        // 类型
        responseData[5] = ByteUtils.toByte(cmdEnum.getCmd(),1)[0];
        validByte[3] = ByteUtils.toByte(cmdEnum.getCmd(),1)[0];
        // 数据
        if(data.length >0 ){
            for(int i =0 ;i < data.length; i++){
                responseData[(6+i)] = data[(i)];
                validByte[(4+i)] = data[(i)];
            }
        }
        // 帧校验域
        int crc16 = CRC16Util.calcCrc16(validByte);
        byte[] vail = ByteUtils.toByte(crc16, 2);
        responseData[(6+data.length)] = vail[0];
        responseData[(6+data.length+1)] = vail[1];
        return responseData;
    }

    public static byte[] responseData(int Encryption, DaHuaCmdEnum cmdEnum, byte[] data, int sequence){
        // 头部长度 +
        byte[] responseData = new byte[8 +data.length];
        byte[] validByte = new byte[4+data.length];
        // 起始标志
        responseData[0] = ByteUtils.toByte(0x68,1)[0];
        // 数据长度 去除头尾
        responseData[1] = ByteUtils.toByte(responseData.length-4, 1)[0];
        // 序列号域
        byte[] seq = ByteUtils.toByte(sequence, 2);
        responseData[2] = seq[0];
        responseData[3] = seq[1];
        validByte[0] = seq[0];
        validByte[1] = seq[1];
        // 加密标志
        byte[] en = ByteUtils.toByte(Encryption, 1);
        responseData[4] = en[0];
        validByte[2] = en[0];
        // 类型
        responseData[5] = ByteUtils.toByte(cmdEnum.getCmd(),1)[0];
        validByte[3] = ByteUtils.toByte(cmdEnum.getCmd(),1)[0];
        // 数据
        if(data.length >0 ){
            for(int i =0 ;i < data.length; i++){
                responseData[(6+i)] = data[(i)];
                validByte[(4+i)] = data[(i)];
            }
        }
        // 帧校验域
        int crc16 = CRC16Util.calcCrc16(validByte);
        byte[] vail = ByteUtils.toByte(crc16, 2);
        responseData[(6+data.length)] = vail[0];
        responseData[(6+data.length+1)] = vail[1];
        return responseData;
    }

    public static byte[] responseData(DaHuaCmdEnum cmdEnum, byte[] data, int sequence){
        // 头部长度 +
        byte[] responseData = new byte[8 +data.length];
        byte[] validByte = new byte[4+data.length];
        // 起始标志
        responseData[0] = ByteUtils.toByte(0x68,1)[0];
        // 数据长度 去除头尾
        responseData[1] = ByteUtils.toByte(responseData.length-4, 1)[0];
        // 序列号域
        byte[] seq = ByteUtils.toByte(sequence, 2);
        responseData[2] = seq[0];
        responseData[3] = seq[1];
        validByte[0] = seq[0];
        validByte[1] = seq[1];
        // 加密标志
        byte[] en = ByteUtils.toByte(0, 1);
        responseData[4] = en[0];
        validByte[2] = en[0];
        // 类型
        responseData[5] = ByteUtils.toByte(cmdEnum.getCmd(),1)[0];
        validByte[3] = ByteUtils.toByte(cmdEnum.getCmd(),1)[0];
        // 数据
        if(data.length >0 ){
            for(int i =0 ;i < data.length; i++){
                responseData[(6+i)] = data[(i)];
                validByte[(4+i)] = data[(i)];
            }
        }
        // 帧校验域
        int crc16 = CRC16Util.calcCrc16(validByte);
        byte[] vail = ByteUtils.toByte(crc16, 2);
        responseData[(6+data.length)] = vail[0];
        responseData[(6+data.length+1)] = vail[1];
        return responseData;
    }
}
