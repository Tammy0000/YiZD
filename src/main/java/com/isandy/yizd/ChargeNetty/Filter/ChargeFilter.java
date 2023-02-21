package com.isandy.yizd.ChargeNetty.Filter;

import com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.*;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.CustomTime;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.dao.ChannelRealTimeHashtable;
import com.isandy.yizd.dao.ChargeActiveStatusRedis;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.text.ParseException;

/*
记录一下 Jedis引发的Attempting to read from a broken connection问题
 */
@Component
@Slf4j
public class ChargeFilter {

    @Value("${custom.RedisAddress}")
    String RedisAddress;

    @Value("${custom.RedisPort}")
    int RedisPort;

    Jedis jedis;

    @Resource
    ChannelRealTimeHashtable realTimeHashtable;

    @Resource
    YiDaHuaChargingCheckPileService checkPileService;

    @Resource
    ChargeActiveStatusRedis activeStatusRedis;

    @Resource
    YiDaHuaLoginRequest loginRequest;

    @Resource
    YiDaHuaPayResponse payResponse;
    
    @Resource
    YiDaHuaChargeOnTimer onTimer;

    @Resource
    YiDaHuaHeartbeatPileService pileService;

    @Bean
    private void ChargeFilterinit() {
        try {
            jedis = new Jedis(RedisAddress, RedisPort);
        } catch (Exception e) {
            log.info("ChargeFilter初始化失败");
        }
    }

    public void Start(Channel channel, ByteBuf byteBuf) throws ParseException {
        YiChargeContext context = new YiChargeContext();
        context.init(byteBuf);
        String strBCD = context.getStrBCD();
        context.setChannel(channel);
        byteBuf.resetReaderIndex();
        log.info("报文内容长度是: " + byteBuf.readableBytes());
        log.info("报文内容是： " + ByteUtils.bytesToHexFun2(context.getSourceData()));
        log.info(CustomTime.time() + " 桩编号:" + strBCD);
        byte[] typeData = context.getTypeData();
        int Int_typeData = 0;
        try {
            Int_typeData = ByteUtils.toInt(typeData);
        } catch (Exception e) {
            log.info("初始化报文数据类型失败,发送过来的内容有问题...");
        }
        log.info("报文类型是："+Int_typeData);
        //开始判别类型做出回应
        if (Int_typeData == DaHuaCmdEnum.登陆.getCmd()) {
            log.info(CustomTime.time() + "开始请求登陆认证");
            loginRequest.Start(context, channel);
            log.info(CustomTime.time() + "请求登陆发送完成");
        } else if (Int_typeData == DaHuaCmdEnum.计费模型验证请求.getCmd()) {
            log.info(CustomTime.time() + "开始请求计费模型验证请求认证");
            checkPileService.Start(context, channel);
            log.info(CustomTime.time() + "请求计费模型验证请求发送完成");
        } else if (Int_typeData == DaHuaCmdEnum.心跳包Ping.getCmd()) {
            realTimeHashtable.add(context.getStrBCD(), channel, context);
            String s = "check" + context.getStrBCD();
            if (jedis.get(s) == null) {
                jedis.set(s, String.valueOf(context.getInt_sequence()));
            }
            jedis.set(strBCD, String.valueOf(context.getInt_sequence()));
            pileService.Start(context, channel);
            log.info(CustomTime.time() +
                    "心跳包发送完成");
        } else if (Int_typeData == DaHuaCmdEnum.上传实时监测数据.getCmd()) {
            log.info("实时检测数据BCD：" + ByteUtils.bytesToHexFun2(context.getBCD()));
            log.info("实时检测数据插枪状态：" + context.getMuzzleLink());
            log.info("实时检测数据枪状态：" + context.getMuzzleWork());
            activeStatusRedis.Set(context);
        } else if (Int_typeData == DaHuaCmdEnum.远程启机命令回复.getCmd()) {
            int b = ByteUtils.toInt(context.getMessage_body()[24]);
            String s = b > 0 ? "成功" : "失败";
            log.info("启动充电结果:" + s);
        } else if (Int_typeData == DaHuaCmdEnum.交易记录.getCmd()) {
            log.info("开始验证交易请求回应");
            payResponse.Start(context, channel);
            log.info("交易验证请求发送完成");
        } else if (Int_typeData == DaHuaCmdEnum.远程停机命令回复.getCmd()) {
            int b = ByteUtils.toInt(context.getMessage_body()[7]);
            String s =  b > 0 ? "成功" : "失败";
            log.info("停止充电结果: " + s);
        } else if (Int_typeData == DaHuaCmdEnum.远程重启应答.getCmd()) {
            int i = ByteUtils.toInt(context.getMessage_body()[13]);
            String s =  i > 0 ? "成功" : "失败";
            log.info("桩重启结果: " + s);
        } else if (Int_typeData == DaHuaCmdEnum.对时设置.getCmd()) {
            onTimer.Start(context, channel);
            log.info(context.getStrBCD()+"对时完成！");
        }
    }
}
