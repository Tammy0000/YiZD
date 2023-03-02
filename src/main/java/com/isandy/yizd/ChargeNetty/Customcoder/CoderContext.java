package com.isandy.yizd.ChargeNetty.Customcoder;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class CoderContext {
    byte[] start;

    /**
     * 数据长度
     */
    byte[] DataLengths;

    /**
     * 序列号域（byte）
     */
    byte[] byte_sequence;

    /**
     * 加密标志
     */
    byte[] encryption;

    /**
     * 帧类型
     */
    byte[] TypeData;

    /**
     * 消息体
     */
    byte[] message_body;

    /**
     * 帧校验域
     */
    byte[] frameType;

    /**
     * 电桩BCD编码
     */
    byte[] BCD;

    /**
     * 数据长度
     */
    int DataLength;

    public void initCode(ByteBuf byteBuf) {
        int index = 0;
        this.DataLength = byteBuf.readableBytes();
        byteBuf.resetReaderIndex();
        byte[] bytes = new byte[this.DataLength];
        //起始标志1字节
        try {
            this.start = new byte[1];
            byteBuf.readBytes(this.start);
            for (byte a: this.start) {
                bytes[index] = a;
                index ++;
            }

            //数据长度1字节
            this.DataLengths = new byte[1];
            byteBuf.readBytes(this.DataLengths);
            for(byte b: this.DataLengths) {
                bytes[index] = b;
                index ++;
            }

            //序列号2字节
            this.byte_sequence = new byte[2];
            byteBuf.readBytes(this.byte_sequence);
            for (byte c: this.byte_sequence) {
                bytes[index] = c;
                index ++;
            }

            //加密标志1字节
            this.encryption = new byte[1];
            byteBuf.readBytes(this.encryption);
            for (byte d: this.encryption) {
                bytes[index] = d;
                index ++;
            }

            //类型1字节
            this.TypeData = new byte[1];
            byteBuf.readBytes(this.TypeData);
            for (byte e: this.TypeData) {
                bytes[index] = e;
                index ++;
            }

            //消息内容
            this.message_body = new byte[this.DataLength - 8];
            byteBuf.readBytes(this.message_body);
            for (byte f:this.message_body) {
                bytes[index] = f;
                index ++;
            }

            //帧校验
            this.frameType = new byte[2];
            byteBuf.readBytes(this.frameType);
            for (byte g:this.frameType) {
                bytes[index] = g;
                index ++;
            }
        } catch (Exception e) {
            this.TypeData = new byte[1];
            this.TypeData[0] = (byte) 0xff;

        }
    }
}
