package com.isandy.yizd.Service;

import com.isandy.yizd.ChargeNetty.CustomConterller.Tools.ByteUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class BillModel {
    /**
     * 尖费电费费率
     */
    byte[] SharpCost;
    /**
     * 尖服务费费率
     */
    byte[] SharpServerCost;
    /**
     * 峰电费费率
     */
    byte[] PeakCost;
    /**
     * 峰服务费费率
     */
    byte[] PeakServerCost;
    /**
     * 平电费费率
     */
    byte[] LevelCost;
    /**
     * 平服务费费率
     */
    byte[] LevelServerCost;
    /**
     * 谷电费费率
     */
    byte[] ValleyCost;
    /**
     * 谷服务费费率
     */
    byte[] ValleyServerCost;

    byte[] time;

    /**
     *
     * @param StrBCD 字符串BCD14位
     * @param JFModel 计费模型，随意填写，或自定义. eg "0010", "0099"
     *                必须字符串且4位数字组成
     * @param Sharp 尖时段，列表形式注意添加时间的先后顺序第一个是开始时间，第二个是结束时间
     *              例如list.add(0), list.add(7)代表是0点到7点，这里全部用24小时计算
     * @param Peak 峰时段，列表形式注意添加时间的先后顺序第一个是开始时间，第二个是结束时间
     *              例如list.add(7), list.add(14)代表是7点到14点，这里全部用24小时计算
     * @param Level 平时段，列表形式注意添加时间的先后顺序第一个是开始时间，第二个是结束时间
    *              例如list.add(14), list.add(20)代表是7点到14点，这里全部用24小时计算
     * @param Valley 谷时段，列表形式注意添加时间的先后顺序第一个是开始时间，第二个是结束时间
     *               例如list.add(20), list.add(24)代表是7点到14点，这里全部用24小时计算
     * @param cost 一个8个费率，必须按照顺序依次填写
     *             尖费电费费率>尖服务费费率>峰电费费率>峰服务费费率>平电费费率>平服务费费率>
     *             谷电费费率>谷服务费费率
     * @return byte[]信息体
     */
    public byte[] CoverBill(String StrBCD, String JFModel,ArrayList<Integer> Sharp, ArrayList<Integer> Peak, ArrayList<Integer> Level, ArrayList<Integer> Valley, double ... cost) {
        int i = 0;
        byte[] data = new byte[90];
        time = new byte[48];
        for (Double d: cost) {
            int v = (int) (d * 100000);
            switch (i) {
                case 0:
                    SharpCost = ByteUtils.toByte(v, 4, false);
                case 1:
                    SharpServerCost = ByteUtils.toByte(v, 4, false);
                case 2:
                    PeakCost = ByteUtils.toByte(v, 4, false);
                case 3:
                    PeakServerCost = ByteUtils.toByte(v, 4, false);
                case 4:
                    LevelCost = ByteUtils.toByte(v, 4, false);
                case 5:
                    LevelServerCost = ByteUtils.toByte(v, 4, false);
                case 6:
                    ValleyCost = ByteUtils.toByte(v, 4, false);
                case 7:
                    ValleyServerCost = ByteUtils.toByte(v, 4, false);
            }
            i ++ ;
        }
        cover(Sharp.get(0), Sharp.get(1), 0);
        cover(Peak.get(0), Peak.get(1), 1);
        cover(Level.get(0), Level.get(1), 2);
        cover(Valley.get(0), Valley.get(1), 3);
        byte[] bcd = ByteUtils.toByte(StrBCD);
        byte[] jf = ByteUtils.toByte(JFModel);
        System.arraycopy(bcd, 0, data, 0, 7);
        System.arraycopy(jf, 0, data, 7, 2);
        System.arraycopy(SharpCost, 0, data, 9, 4);
        System.arraycopy(SharpServerCost, 0, data, 13, 4);
        System.arraycopy(PeakCost, 0, data, 17, 4);
        System.arraycopy(PeakServerCost, 0, data, 21, 4);
        System.arraycopy(LevelCost, 0, data, 25, 4);
        System.arraycopy(LevelServerCost, 0, data, 29, 4);
        System.arraycopy(ValleyCost, 0, data, 33, 4);
        System.arraycopy(ValleyServerCost, 0, data, 37, 4);
        data[41] = ByteUtils.toByte(0, 1, false)[0];
        System.arraycopy(time, 0, data, 42, 48);
        return data;
    }

    public byte[] CoverBill(String StrBCD, double Cost, double CostServer){
        byte[] data = new byte[90];
        int cost = (int) (Cost * 100000);
        int costserver = (int) (CostServer * 100000);
        byte[] sc = ByteUtils.toByte(cost, 4, false);
       byte[] sb = ByteUtils.toByte(costserver, 4, false);
        byte[] bcd = ByteUtils.toByte(StrBCD);
        byte[] jf = ByteUtils.toByte("0101");
        System.arraycopy(bcd, 0, data, 0, 7);
        System.arraycopy(jf, 0, data, 7, 2);
        System.arraycopy(sc, 0, data, 9, 4);
        System.arraycopy(sb, 0, data, 13, 4);
        System.arraycopy(sc, 0, data, 17, 4);
        System.arraycopy(sb, 0, data, 21, 4);
        System.arraycopy(sc, 0, data, 25, 4);
        System.arraycopy(sb, 0, data, 29, 4);
        System.arraycopy(sc, 0, data, 33, 4);
        System.arraycopy(sb, 0, data, 37, 4);
        data[41] = ByteUtils.toByte(0, 1, false)[0];
        for (int i = 42; i < 90; i++) {
            data[i] = ByteUtils.toByte(1, 1, false)[0];
        }
        return data;
    }

    private void cover(int Start, int end, int Series) {
        if (end - Start > 0) {
            for (int i = 0; i < (end - Start) * 2; i++) {
                int k = Start * 2 + i;
                time[k] = ByteUtils.toByte(Series, 1, false)[0];
            }
        }else {
            for (int i = 0; i < (24 - Start) * 2; i++) {
                int k = Start * 2 + i;
                time[k] = ByteUtils.toByte(Series, 1, false)[0];
            }
            for (int i = 0; i < end; i++) {
                time[i] = ByteUtils.toByte(Series, 1, false)[0];
            }
        }
    }
}
