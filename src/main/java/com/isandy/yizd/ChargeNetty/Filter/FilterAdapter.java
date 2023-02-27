package com.isandy.yizd.ChargeNetty.Filter;

import com.isandy.yizd.ChargeNetty.ChargeContext.YiChargeContext;
import io.netty.channel.Channel;

public abstract class FilterAdapter {
    public void cmd0x01(YiChargeContext context, Channel channel){}
    public void cmd0x03(YiChargeContext context, Channel channel){}
    public void cmd0x3B(YiChargeContext context, Channel channel){}
    public void cmd0x13(YiChargeContext context, Channel channel){}
    public void cmd0x33(YiChargeContext context, Channel channel){}
    public void cmd0x35(YiChargeContext context, Channel channel){}
    public void cmd0x91(YiChargeContext context, Channel channel){}
    public void cmd0x05(YiChargeContext context, Channel channel){}
    public void cmd0x40(YiChargeContext context, Channel channel){}
    public void cmd0x41(YiChargeContext context, Channel channel){}
    public void cmd0x43(YiChargeContext context, Channel channel){}
    public void cmd0x45(YiChargeContext context, Channel channel){}
    public void cmd0x47(YiChargeContext context, Channel channel){}
    public void cmd0x51(YiChargeContext context, Channel channel){}
    public void cmd0x55(YiChargeContext context, Channel channel){}
    public void cmd0x57(YiChargeContext context, Channel channel){}
    public void cmd0x93(YiChargeContext context, Channel channel){}
}
