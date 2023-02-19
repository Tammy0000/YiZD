package com.isandy.yizd.dao;

public interface ChargeImpl {
    public void insert(Charge charge);
    public boolean updateSeq(String BCD, int MuzzleNum, int Seq);
    public boolean updateChargeStatus(String BCD, int MuzzleNum, Charge charge);
    public int findSeq(String BCD, int MuzzleNum);
    public Charge findContext(String BCD, int Muzzle);
    public boolean RemoveCharge(String BCD, int Muzzle);
}
