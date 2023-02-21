package com.isandy.yizd.dao;

public interface RedisImpl {
    public void setSeq(String BCD, int Seq);
    public int getSeq(String BCD);
    public void setPublicSeq(String BCD, int Seq);
    public int getPublicSeq(String BCD);
}
