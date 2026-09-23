package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huabo.contract.entity.TblContractContentPdf;


public interface TblContractContentPdfService {


	Map<String, Object> findFileListByContractIdPageInfo(BigDecimal contractId, Integer pageNumber, Integer pageSize, TblContractContentPdf tca);

    TblContractContentPdf findInfoById(BigDecimal contentPdfId);

    void removeInfo(BigDecimal singingId);

    Map<String, Object> saveEntity(TblContractContentPdf signing);
    
    List<TblContractContentPdf> findInfoByContractId(BigDecimal contractId);
}
