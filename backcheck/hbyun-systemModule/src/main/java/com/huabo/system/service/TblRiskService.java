package com.huabo.system.service;

import com.huabo.system.entity.TblRisk;

import java.math.BigDecimal;

public interface TblRiskService {
    TblRisk findTblRiskByFlowId(BigDecimal flowid);

    TblRisk findById(String toString);

    void saveRisk(TblRisk var1);

    void delRisk(BigDecimal riskid);
}
