package com.isandy.yizd.Service;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.CustomTime;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Slf4j
public class CreateOrder {
    public static byte[] Create(String StrBCD, int MuzzleNum) throws ParseException {
        byte[] order = new byte[16];
        if (StrBCD.length() == 14 && MuzzleNum < 3) {
            byte[] bcd = ByteUtils.toByte(StrBCD);
            byte[] num = ByteUtils.toByte("0" + MuzzleNum);
            System.arraycopy(bcd, 0, order, 0, 7);
            System.arraycopy(num, 0, order, 7, 1);
            String cpt = CustomTime.CreateOrdertime();
            byte[] Ctime = ByteUtils.toByte(cpt);
            System.arraycopy(Ctime, 0, order, 8, 8);
            return order;
        }else {
            log.error("生成订单的BCD或枪号错误");
            return order;
        }
    }
}
