package com.isandy.yizd.ChargeNetty.Pojo;

import org.springframework.stereotype.Component;

@Component
public interface ChargeImpl {
    public void insertLogin(Charge NewCharge);
    public void insertSumMuzzle(String BCD,int sumMuzzle);
    public boolean updateChargeStatus(String BCD, int MuzzleNum, Charge charge);
    public Charge findContext(String BCD, int Muzzle);
    public boolean RemoveCharge(String BCD, int Muzzle);
    public int findSeq(String BCD, int MuzzleNum);
}
