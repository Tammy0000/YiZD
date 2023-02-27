package com.isandy.yizd.ChargeNetty.Filter;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeChannel;
import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.*;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.CustomTime;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;

/*
记录一下 Jedis引发的Attempting to read from a broken connection问题
 */
@Component
@Slf4j
public class ChargeFilter {
    @Resource
    YiChargeContext chargeContext;

    @Resource
    YiChargeChannel chargeChannel;

    @Resource
    YiDaHuaChargingCheckPileService checkPileService;

    @Resource
    YiDaHuaLoginRequest loginRequest;

    @Resource
    YiDaHuaPayResponse payResponse;
    
    @Resource
    YiDaHuaChargeOnTimer onTimer;

    @Resource
    YiDaHuaHeartbeatPileService pileService;

    Channel channel;

    ByteBuf byteBuf;

    FilterAdapter adapter;

    public void initaddFilterAdapter(Channel channel, ByteBuf byteBuf, FilterAdapter adapter){
        this.channel = channel;
        this.byteBuf = byteBuf;
        this.adapter = adapter;
        try {
            Start();
        } catch (ParseException e) {
            log.error("FilterAdapter初始化失败");
        }
    }

    private void Start() throws ParseException {
        chargeContext.init(this.byteBuf);
        String strBCD = chargeContext.getStrBCD();
        chargeContext.setChannel(this.channel);
        this.byteBuf.resetReaderIndex();
        log.info("报文内容长度是: " + this.byteBuf.readableBytes());
        log.info("报文内容是： " + ByteUtils.bytesToHexFun2(chargeContext.getSourceData()));
        log.info(CustomTime.time() + " 桩编号:" + strBCD);
        byte[] typeData = chargeContext.getTypeData();
        int Int_typeData = 0;
        try {
            Int_typeData = ByteUtils.toInt(typeData);
        } catch (Exception e) {
            log.info("初始化报文数据类型失败,发送过来的内容有问题...");
        }
        log.info("报文类型是："+ByteUtils.bytesToHexFun2(typeData));
        //开始判别类型做出回应
        if (Int_typeData == DaHuaCmdEnum.登陆.getCmd()) {
            adapter.cmd0x01(chargeContext, this.channel);
        } else if (Int_typeData == DaHuaCmdEnum.计费模型验证请求.getCmd()) {
            adapter.cmd0x05(chargeContext, this.channel);
        } else if (Int_typeData == DaHuaCmdEnum.心跳包Ping.getCmd()) {
            adapter.cmd0x03(chargeContext, this.channel);
        } else if (Int_typeData == DaHuaCmdEnum.上传实时监测数据.getCmd()) {
            adapter.cmd0x13(chargeContext, this.channel);
        } else if (Int_typeData == DaHuaCmdEnum.远程启机命令回复.getCmd()) {
            adapter.cmd0x33(chargeContext, this.channel);
        } else if (Int_typeData == DaHuaCmdEnum.交易记录.getCmd()) {
            log.info("开始验证交易请求回应");
            payResponse.Start(chargeContext, this.channel);
            log.info("交易验证请求发送完成");
            adapter.cmd0x3B(chargeContext, this.channel);
        } else if (Int_typeData == DaHuaCmdEnum.远程停机命令回复.getCmd()) {
            int b = ByteUtils.toInt(chargeContext.getMessage_body()[7]);
            String s =  b > 0 ? "成功" : "失败";
            log.info("停止充电结果: " + s);
            adapter.cmd0x35(chargeContext, this.channel);
        } else if (Int_typeData == DaHuaCmdEnum.远程重启应答.getCmd()) {
            int i = ByteUtils.toInt(chargeContext.getMessage_body()[7],false);
            String s =  i > 0 ? "成功" : "失败";
            log.info("桩重启结果: " + s);
            adapter.cmd0x91(chargeContext, this.channel);
        } else if (Int_typeData == DaHuaCmdEnum.对时设置应答.getCmd()) {
            adapter.cmd0x55(chargeContext, this.channel);
        }
    }
}
