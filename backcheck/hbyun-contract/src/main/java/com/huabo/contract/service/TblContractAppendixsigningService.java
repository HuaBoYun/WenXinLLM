package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.Map;

import com.huabo.contract.entity.TblContractAppendixsigning;


public interface TblContractAppendixsigningService {


	Map<String, Object> findFileListByContractIdPageInfo(BigDecimal contractId, Integer pageNumber, Integer pageSize, TblContractAppendixsigning tca);

    TblContractAppendixsigning findInfoById(BigDecimal singingId);

    void removeInfo(BigDecimal singingId);

    Map<String, Object> saveEntity(TblContractAppendixsigning signing,BigDecimal budgetid);
    
}
