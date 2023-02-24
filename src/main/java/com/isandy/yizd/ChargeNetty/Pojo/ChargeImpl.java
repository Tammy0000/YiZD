package com.isandy.yizd.ChargeNetty.Pojo;

import org.springframework.stereotype.Component;

@Component
public interface ChargeImpl {
    void insertLogin(Charge NewCharge);
    void insertSumMuzzle(String BCD,int sumMuzzle);
    boolean updateChargeStatus(String BCD, int MuzzleNum, Charge charge);
    Charge findContext(String BCD, int Muzzle);
    boolean RemoveCharge(String BCD, int Muzzle);
    int findSeq(String BCD, int MuzzleNum);
    int findSeq(String strBCD);
}
