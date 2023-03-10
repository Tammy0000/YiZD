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

    /**
     *
     * @param StrBCD BCD
     * @param channel channel
     * @param S 尖时段，注意添加顺序list.add(22),list.add(7)代表晚上22点到早上7点，这里用24小时制
     * @param P 峰时段，注意添加顺序list.add(7),list.add(13)代表晚上7点到下午13点，这里用24小时制
     * @param L 平时段，注意添加顺序list.add(13),list.add(18)代表下午13点到傍晚18点，这里用24小时制
     * @param V 谷时段，注意添加顺序list.add(18),list.add(22)代表傍晚18点到晚上22点，这里用24小时制
     * @param var 八个数值依次是
     *            尖费电费费率>尖服务费费率>峰电费费率>峰服务费费率>平电费费率>平服务费费率>谷电费费率>谷服务费费率
     */
    public void Start(String StrBCD, Channel channel,
                      ArrayList<Integer> S, ArrayList<Integer> P, ArrayList<Integer> L, ArrayList<Integer> V, double ... var) {
        BillModel billModel = new BillModel();
        byte[] jf = billModel.CoverBill(StrBCD, "0100", S, P, L, V, var);
        byte[] data = ResData.responseData(DaHuaCmdEnum.计费模型设置, jf, redis.getSeq(StrBCD));
        ChannelSendData.Send(data, channel);
        log.info("发送计费模型完成");
    }

    /**
     *
     * @param BCD BCD
     * @param channel channel
     * @param cost 全日费用
     * @param costserver 全日服务费
     */
    public void Start(String BCD, Channel channel, double cost, double costserver) {
        BillModel billModel = new BillModel();
        byte[] jf = billModel.CoverBill(BCD, cost, costserver);
        byte[] bytes = ResData.responseData(DaHuaCmdEnum.计费模型设置, jf, redis.getSeq(BCD));
        ChannelSendData.Send(bytes, channel);
    }
}
