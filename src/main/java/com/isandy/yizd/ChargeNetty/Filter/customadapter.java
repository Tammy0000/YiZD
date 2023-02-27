package com.isandy.yizd.ChargeNetty.Filter;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeChannel;
import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaChargingCheckPileService;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaHeartbeatPileService;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaLoginRequest;
import com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd.YiDaHuaPayResponse;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.Cp56Time2a;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.CustomTime;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class CustomAdapter extends FilterAdapter {
    @Resource
    YiDaHuaHeartbeatPileService pService;

    @Resource
    YiDaHuaLoginRequest loginRequest;

    @Resource
    YiDaHuaPayResponse payResponse;

    @Resource
    YiChargeChannel chargeChannel;

    @Resource
    YiDaHuaChargingCheckPileService checkPileService;

    @Override
    public void cmd0x01(YiChargeContext context, Channel channel) {
        log.info(CustomTime.time() + "开始请求登陆认证");
        loginRequest.Start(context.getStrBCD(), channel);
        log.info(CustomTime.time() + "请求登陆发送完成");
    }

    @Override
    public void cmd0x03(YiChargeContext context, Channel channel) {
        chargeChannel.setChannel(context.getStrBCD(), channel);
        log.info("0x03");
        pService.Start(context, channel);
        log.info(CustomTime.time() +
                "心跳包发送完成");
    }

    @Override
    public void cmd0x3B(YiChargeContext context, Channel channel) {
        log.info("开始验证交易请求回应");
        payResponse.Start(context, channel);
    }

    @Override
    public void cmd0x13(YiChargeContext context, Channel channel) {
        log.info("实时检测数据BCD：" + ByteUtils.bytesToHexFun2(context.getBCD()));
        log.info("实时检测数据插枪状态：" + context.getMuzzleLink());
        log.info("实时检测数据枪状态：" + context.getMuzzleWork());
    }

    @Override
    public void cmd0x05(YiChargeContext context, Channel channel) {
        log.info(CustomTime.time() + "开始请求计费模型验证请求认证");
        checkPileService.Start(context.getStrBCD(), channel);
        log.info(CustomTime.time() + "请求计费模型验证请求发送完成");
    }

    @Override
    public void cmd0x33(YiChargeContext context, Channel channel) {
        int b = ByteUtils.toInt(context.getMessage_body()[24]);
        String s = b > 0 ? "成功" : "失败";
        log.info("启动充电结果:" + s);
    }

    @Override
    public void cmd0x55(YiChargeContext context, Channel channel) {
        byte[] message_body = context.getMessage_body();
        byte[] time = new byte[7];
        System.arraycopy(message_body, 7, time, 0, 7);
        Date date = Cp56Time2a.toDate(time);
        String s = CustomTime.toTime(date);
        log.info("对时完成,当前时间是:"+s);
    }
}
