package com.isandy.yizd.dao;

import com.isandy.yizd.ChargeNetty.CustomConterller.ChargeContext.YiChargeContext;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

@Data
@NoArgsConstructor
@Component
public class ChargeRealMaps {
    String StrBCD;
    Channel channel;
    int Int_sequence;
    String Str_sequence;
    YiChargeContext context;
    Hashtable<String, Channel> sch;
    Hashtable<String, YiChargeContext> syi;
}
