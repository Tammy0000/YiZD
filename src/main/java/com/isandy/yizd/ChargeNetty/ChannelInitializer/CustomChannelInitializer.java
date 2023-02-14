package com.isandy.yizd.ChargeNetty.ChannelInitializer;

import com.isandy.yizd.ChargeNetty.Adapter.InChannel;
import com.isandy.yizd.ChargeNetty.Customcoder.ChargeDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    InChannel inChannel;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChargeDecoder Decoder = new ChargeDecoder();
        socketChannel.pipeline().addLast(new IdleStateHandler(500, 0, 0));
        socketChannel.pipeline().addLast(Decoder);
        socketChannel.pipeline().addLast(inChannel);
    }
}
