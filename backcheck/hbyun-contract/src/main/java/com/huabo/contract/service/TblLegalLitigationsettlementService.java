package com.huabo.contract.service;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalLitigationsettlement;

public interface TblLegalLitigationsettlementService {
	
    void findListByPageInfo(PageInfo<TblLegalLitigationsettlement> pageInfo, TblLegalLitigationsettlement litigation,BigDecimal disputeid) throws Exception;

    TblLegalLitigationsettlement findById(BigDecimal litigationId) throws Exception;

    void addLitigationSettlement(TblLegalLitigationsettlement litigation) throws Exception;

    void updateModifyLitigationSettlement(TblLegalLitigationsettlement oldLitigation) throws Exception;

    void removeLitigationSettlement(BigDecimal litigationid) throws Exception;

    TblLegalLitigationsettlement findByLitigationid(BigDecimal litigationid) throws Exception;
}
