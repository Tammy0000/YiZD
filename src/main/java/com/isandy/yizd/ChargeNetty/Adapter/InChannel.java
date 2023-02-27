package com.isandy.yizd.ChargeNetty.Adapter;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeChannel;
import com.isandy.yizd.ChargeNetty.Filter.ChargeFilter;
import com.isandy.yizd.ChargeNetty.Filter.CustomAdapter;
import com.isandy.yizd.ChargeNetty.Filter.FilterAdapter;
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
    YiChargeChannel chargeChannel;

    @Resource
    ChargeFilter filter;

    @Resource
    CustomAdapter adapter;

    ByteBuf byteBuf = Unpooled.directBuffer();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String s = StringUtils.substringBetween(ctx.channel().toString(), "R:/", "]");
        log.info(s + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String s = StringUtils.substringBetween(ctx.channel().toString(), "R:/", "]");
        chargeChannel.removeChannel(ctx.channel());
        log.info(s + ":离线了");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byteBuf = (ByteBuf) msg;
        Channel channel = ctx.channel();
        filter.initaddFilterAdapter(channel, byteBuf, adapter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("异常信息： "+cause.toString());
        if (!ctx.channel().isActive()) {
            ctx.channel().close();
        }
        /*
        个人猜想就是异常离线会触发这里
        待测试
        2023年1月18日14:22:58
         */
//        crts.remove(ctx.channel());
    }
}
