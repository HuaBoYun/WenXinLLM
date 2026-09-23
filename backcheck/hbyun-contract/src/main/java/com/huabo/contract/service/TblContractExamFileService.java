package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huabo.contract.entity.TblContractExamFile;


public interface TblContractExamFileService {


	Map<String, Object> findFileListByContractIdPageInfo(BigDecimal id, Integer pageNumber, Integer pageSize, TblContractExamFile tca);

	TblContractExamFile findInfoById(BigDecimal id);

    void removeInfo(BigDecimal id);

    Map<String, Object> saveEntity(TblContractExamFile file);
    
    List<TblContractExamFile> findInfoByContractId(BigDecimal contractId);
}
