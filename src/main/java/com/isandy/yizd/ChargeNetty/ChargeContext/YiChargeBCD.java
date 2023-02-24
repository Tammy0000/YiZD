package com.isandy.yizd.ChargeNetty.ChargeContext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

@Component
@Slf4j
public class YiChargeBCD {

    Hashtable<String, byte[]> sb = new Hashtable<>();

    public void setBytesBCD(String strBCD, byte[] bytesBCD) {
        if (!sb.containsKey(strBCD)){
            sb.put(strBCD, bytesBCD);
        }
    }

    public byte[] getBytesBCD(String strBCD) {
        try {
            return sb.get(strBCD);
        } catch (Exception e) {
            log.error("没有byte[]的BCD");
            return null;
        }
    }
}
