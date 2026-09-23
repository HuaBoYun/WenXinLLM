package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalProceedingsrecord;

public interface TblLegalProceedingsrecordService {
    void saveProceedingRecord(TblLegalProceedingsrecord proceed,String attids) throws Exception;

    TblLegalProceedingsrecord findById(BigDecimal proceedid) throws Exception;

    void modifyNegotiateRecord(TblLegalProceedingsrecord oldproceed,String attids) throws Exception;

    void removeNegitiateRecord(BigDecimal proceedId);

    List<TblLegalProceedingsrecord> findListByLitigationid(BigDecimal litigationId) throws Exception;

    void findListByPageInfo(PageInfo<TblLegalProceedingsrecord> pageInfo, TblLegalProceedingsrecord record) throws Exception;

    void findByNegotiaId(PageInfo<TblLegalProceedingsrecord> pageInfo, BigDecimal litigationId) throws Exception;
    
    public JsonBean getAttListBylitigationId(BigDecimal litigationId) throws Exception;
    
    public JsonBean delAttListBylitigationId(BigDecimal attid) throws Exception;
    
	public Map<String, Object> getproceedTimeAxis(String litigationId) throws Exception ;
}
