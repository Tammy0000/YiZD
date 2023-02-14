package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class ArrayListChargebyteBuf {

    ArrayList<ByteBuf> channelArrayList;

    public ArrayListChargebyteBuf(ByteBuf byteBuf) {
        byteBuf.resetReaderIndex();
        this.channelArrayList = new ArrayList<>();
        channelArrayList.add(byteBuf);
    }
}
