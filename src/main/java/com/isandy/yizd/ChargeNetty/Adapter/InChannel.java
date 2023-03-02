package com.isandy.yizd.ChargeNetty.Adapter;

import com.isandy.yizd.ChargeNetty.Filter.ChargeFilter;
import com.isandy.yizd.dao.ChannelRealTimeHashtable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@ChannelHandler.Sharable
public class InChannel extends ChannelInboundHandlerAdapter {
    @Resource
    ChannelRealTimeHashtable realTimeHashtable;

    @Resource
    ChargeFilter filter;

    ByteBuf byteBuf = Unpooled.directBuffer();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String s = StringUtils.substringBetween(ctx.channel().toString(), "R:/", "]");
        log.info(s + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String s = StringUtils.substringBetween(ctx.channel().toString(), "R:/", "]");
        realTimeHashtable.remove(ctx.channel());
        log.info(s + ":离线了");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byteBuf = (ByteBuf) msg;
        Channel channel = ctx.channel();
        filter.Start(channel, byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("异常信息： "+cause.toString());
        if (!ctx.channel().isActive()) {
            ctx.channel().close();
        }
    }
}
