package com.isandy.yizd.ChargeNetty.ChargeContext;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

@Component
public class YiChargeChannel {
    Hashtable<String, Channel> channels = new Hashtable<>();

    public void setChannel(String BCD, Channel channel) {
        if (!channels.containsKey(BCD)) {
            channels.put(BCD, channel);
        }else {
            if (channels.get(BCD) != channel) {
                channels.replace(BCD, channel);
            }
        }
    }

    public Channel getChannel(String BCD) {
        return channels.get(BCD);
    }

    public boolean checkChannel(String BCD) {
        return channels.containsKey(BCD);
    }

    public void removeChannel(String BCD) {
        channels.remove(BCD);
    }
}
