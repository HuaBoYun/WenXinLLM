package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblFlowBussiness;

public interface TblFlowBussinessService {
    List<TblFlowBussiness> findByFlowud(String flowid,TblFlowBussiness bussiness);

    void delete(TblFlowBussiness var1);

    void save(TblFlowBussiness var1);

    TblFlowBussiness findUniqueByFlowId(BigDecimal flowid);

}
