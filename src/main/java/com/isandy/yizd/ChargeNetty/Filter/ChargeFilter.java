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
        this.byteBuf.resetReaderIndex();
        log.info("报文内容长度是: " + this.byteBuf.readableBytes());
        log.info("报文内容是： " + ByteUtils.bytesToHexFun2(chargeContext.getSourceData()));
        log.info(CustomTime.time() + " 桩编号:" + strBCD);
        byte[] typeData = chargeContext.getTypeData();
        int Type = ByteUtils.toInt(typeData);
        log.info("报文类型是："+ByteUtils.bytesToHexFun2(typeData));
        //开始判别类型做出回应
        if (Type == DaHuaCmdEnum.登陆.getCmd()) {
            adapter.cmd0x01(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.计费模型验证请求.getCmd()) {
            adapter.cmd0x05(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.心跳包Ping.getCmd()) {
            adapter.cmd0x03(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.上传实时监测数据.getCmd()) {
            adapter.cmd0x13(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.远程启机命令回复.getCmd()) {
            adapter.cmd0x33(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.交易记录.getCmd()) {
            adapter.cmd0x3B(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.远程停机命令回复.getCmd()) {
            adapter.cmd0x35(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.远程重启应答.getCmd()) {
            adapter.cmd0x91(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.对时设置应答.getCmd()) {
            adapter.cmd0x55(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电握手.getCmd()) {
            adapter.cmd0x15(chargeContext, this.channel);
        }else if (Type == DaHuaCmdEnum.参数配置.getCmd()) {
            adapter.cmd0x17(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电结束.getCmd()) {
            adapter.cmd0x19(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.错误报文.getCmd()) {
            adapter.cmd0x1B(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电阶段BMS中止.getCmd()) {
            adapter.cmd0x1D(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电阶段充电机中止.getCmd()) {
            adapter.cmd0x21(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电过程BMS需求与充电机输出.getCmd()) {
            adapter.cmd0x23(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电过程BMS信息.getCmd()) {
            adapter.cmd0x25(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电桩主动申请启动充电.getCmd()) {
            adapter.cmd0x31(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.余额更新应答.getCmd()) {
            adapter.cmd0x41(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.卡数据同步应答.getCmd()) {
            adapter.cmd0x43(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电桩工作参数设置应答.getCmd()) {
            adapter.cmd0x51(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.计费模型应答.getCmd()) {
            adapter.cmd0x57(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.地锁数据上送.getCmd()) {
            adapter.cmd0x61(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电桩地锁升降锁返回数据.getCmd()) {
            adapter.cmd0x63(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.远程更新应答.getCmd()) {
            adapter.cmd0x93(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.离线卡数据清除应答.getCmd()) {
            adapter.cmd0x45(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.离线卡数据查询应答.getCmd()) {
            adapter.cmd0x47(chargeContext, this.channel);
        } else if (Type == DaHuaCmdEnum.充电桩计费模型请求.getCmd()) {
            adapter.cmd0x09(chargeContext, this.channel);
        }
    }
}
