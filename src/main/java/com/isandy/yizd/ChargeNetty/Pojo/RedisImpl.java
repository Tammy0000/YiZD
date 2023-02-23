package com.isandy.yizd.ChargeNetty.Pojo;

public interface RedisImpl {
    public void setSeq(String BCD, int Seq);
    public int getSeq(String BCD);
    public void setPublicSeq(String BCD, int Seq);
    public int getPublicSeq(String BCD);
}
