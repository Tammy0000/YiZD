package com.isandy.yizd.ChargeNetty.CustomConterller.Tools;

/**
 * @Author ckk
 * @Date 2022/3/15 16:30
 * @Description 功能描述:
 **/
public enum DaHuaCmdEnum {

    //命令码 命令项 备注
    //0x01 登陆 登陆
    /*

    帧类型码 帧类型码名称 数据传送方向 备注
    0x01 充电桩登录认证 充电桩->运营平台 充电桩每次复位或通信 离线，都需重新登录,并成功后才能进行后续交互
    0x02 登录认证应答 运营平台->充电桩
    0x03 充电桩心跳包 充电桩->运营平台
    0x04 心跳包应答 运营平台->充电桩
    0x05 计费模型验证请求 充电桩->运营平台
    0x06 计费模型验证请求应答 运营平台->充电桩
    0x09 充电桩计费模型请求 充电桩->运营平台
    0x0A 计费模型请求应答 运营平台->充电桩
    0x12 读取实时监测数据 运营平台->充电桩
    0x13 离线监测数据 充电桩->运营平台
    0x15 充电握手 充电桩->运营平台
    0x17 参数配置 充电桩->运营平台
    0x19 充电结束 充电桩->运营平台
    0x1B 错误报文 充电桩->运营平台
    0x1D 充电阶段 BMS 中止 充电桩->运营平台
    0x21 充电阶段充电机中止 充电桩->运营平台
    0x23 充电过程 BMS 需求、充电机输出 充电桩->运营平台
    0x25 充电过程 BMS 信息 充电桩->运营平台
    0x31 充电桩主动申请启动充电 充电桩->运营平台
    0x32 运营平台确认启动充电 运营平台->充电桩
    0x33 远程启机命令回复 充电桩->运营平台
    0x34 运营平台远程控制启机 运营平台->充电桩
    0x35 远程停机命令回复 充电桩->运营平台
    0x36 运营平台远程停机 运营平台->充电桩
    0x3B 交易记录 充电桩->运营平台
    0x40 交易记录确认 运营平台->充电桩
    0x41 余额更新应答 充电桩->运营平台
    0x42 远程账户余额更新 运营平台->充电桩
    0x43 卡数据同步应答 充电桩->运营平台
    0x44 离线卡数据同步 运营平台->充电桩
    0x45 离线卡数据清除应答 充电桩->运营平台
    0x46 离线卡数据清除 运营平台->充电桩
    0x47 离线卡数据查询应答 充电桩->运营平台
    0x48 离线卡数据查询 运营平台->充电桩
    0x51 充电桩工作参数设置应答 充电桩->运营平台
    0x52 充电桩工作参数设置 运营平台->充电桩
    0x55 对时设置应答 充电桩->运营平台
    0x56 对时设置 运营平台->充电桩
    0x57 计费模型应答 充电桩->运营平台
    0x58 计费模型设置 运营平台->充电桩
    0x61 地锁数据上送（充电桩上送） 充电桩->运营平台
    0x62 遥控地锁升锁与降锁命令（下行）运营平台->充电桩
    0x63 充电桩返回数据（上行） 充电桩->运营平台
    0x91 远程重启应答 充电桩->运营平台
    0x92 远程重启 运营平台->充电桩
    0x93 远程更新应答 充电桩->运营平台
    0x94 远程更新 运营平台->充电桩
     */


    登陆(0x01, "登陆认证"),
    登录应答(0x02, "登录认证应答"),
    心跳包Ping(0x03, "心跳包Ping"),
    心跳包Pong(0x04, "心跳包Pong"),
    计费模型验证请求(0x05, "计费模型验证请求"),
    计费模型验证请求应答(0x06, "计费模型验证请求应答"),
    充电桩计费模型请求(0x09, "充电桩计费模型请求"),
    计费模型请求应答(0x0A, "计费模型请求应答"),
    读取实时监测数据(0x12, "读取实时监测数据"),
    上传实时监测数据(0x13, "上传实时监测数据"),
    充电握手(0x15, "充电握手"),
    参数配置(0x17, "参数配置"),
    充电结束(0x19, "充电结束"),
    错误报文(0x1B, "错误报文"),
    充电阶段BMS中止(0x1D, "充电阶段 BMS 中止"),
    充电阶段充电机中止(0x21, "充电阶段充电机中止"),
    充电过程BMS需求与充电机输出(0x23, "充电过程 BMS 需求与充电机输出"),
    充电过程BMS信息(0x25, "充电过程 BMS 信息"),
    充电桩主动申请启动充电(0x31, "充电桩主动申请启动充电"),
    运营平台确认启动充电(0x32, "运营平台确认启动充电"),
    远程启机命令回复(0x33, "远程启机命令回复"),
    运营平台远程控制启机(0x34, "运营平台远程控制启机"),
    远程停机命令回复(0x35, "远程停机命令回复"),
    运营平台远程停机(0x36, "运营平台远程停机"),
    交易记录(0x3B, "交易记录"),
    交易记录确认(0x40, "交易记录确认"),
    余额更新应答(0x41, "余额更新应答"),
    远程账户余额更新(0x42, "远程账户余额更新"),
    卡数据同步应答(0x43, "卡数据同步应答"),
    离线卡数据同步(0x44, "离线卡数据同步"),
    离线卡数据清除应答(0x45, "离线卡数据清除应答"),
    离线卡数据清除(0x46, "离线卡数据清除"),
    离线卡数据查询应答(0x47, "离线卡数据查询应答"),
    离线卡数据查询(0x48, "离线卡数据查询"),
    充电桩工作参数设置应答(0x51, "充电桩工作参数设置应答"),
    充电桩工作参数设置(0x52, "充电桩工作参数设置"),
    对时设置应答(0x55, "对时设置应答"),
    对时设置(0x56, "对时设置"),
    计费模型应答(0x57, "计费模型应答"),
    计费模型设置(0x58, "计费模型设置"),
    地锁数据上送(0x61, "地锁数据上送"),
    遥控地锁升锁与降锁命令(0x62, "遥控地锁升锁与降锁命令"),
    充电桩地锁升降锁返回数据(0x63, "充电桩地锁升降锁返回数据"),
    远程重启应答(0x91, "远程重启应答"),
    远程重启(0x92, "远程重启"),
    远程更新应答(0x93, "远程更新应答"),
    远程更新(0x94, "远程更新"),

    ;

    private int cmd;
    private String desc;

    DaHuaCmdEnum(int cmd, String desc) {
        this.cmd = cmd;
        this.desc = desc;
    }


    public static DaHuaCmdEnum findEnum(int value){
        for (DaHuaCmdEnum item : DaHuaCmdEnum.values()) {
            if (value == item.getCmd()) {
                return item;
            }
        }
        return null;
    }


    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
