package com.isandy.yizd.ChargeNetty.CustomConterller.SendDataCmd;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ChannelSendData;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.DaHuaCmdEnum;
import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ResData;
import com.isandy.yizd.Service.BillModel;
import com.isandy.yizd.dao.Redis;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

@Component
@Lazy
@Slf4j
public class YiDaHuaBillChargingSetting {
    @Resource
    Redis redis;

    public void Start(String StrBCD, Channel channel) {
        BillModel billModel = new BillModel();
        ArrayList<Integer> S = new ArrayList<>();
        ArrayList<Integer> P = new ArrayList<>();
        ArrayList<Integer> L = new ArrayList<>();
        ArrayList<Integer> V = new ArrayList<>();
        S.add(22);
        S.add(7);
        P.add(7);
        P.add(14);
        L.add(14);
        L.add(19);
        V.add(19);
        V.add(22);
        byte[] jf = billModel.CoverBill(StrBCD, "0100", S, P, L, V, 1.234, 0.2, 1.234, 0.2, 1.234, 0.2, 1.234, 0.2);
        byte[] data = ResData.responseData(DaHuaCmdEnum.计费模型设置, jf, redis.getSeq(StrBCD));
        ChannelSendData.Send(data, channel);
        log.info("发送计费模型完成");
    }
}
