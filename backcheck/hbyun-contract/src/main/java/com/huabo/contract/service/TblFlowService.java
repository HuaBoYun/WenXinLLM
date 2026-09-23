package com.huabo.contract.service;

import java.math.BigDecimal;

import com.huabo.contract.entity.TblFlow;
import com.huabo.contract.entity.TblFormControllog;

public interface TblFlowService {
    TblFlow findById(String flowid) throws Exception;

    TblFlow findByFlowid(String flowid) throws Exception;

    TblFlow findBy(String flowid) throws Exception;

    TblFlow findByFlow(BigDecimal flowid) throws Exception;

    void saveTfl(TblFormControllog tfl) throws Exception;

    void updateTfl(TblFormControllog tfl) throws Exception;

    String findMappingUrl(BigDecimal flowId) throws Exception;

	TblFlow findFlowInfoByNumberOrgId(String flownumber, BigDecimal orgid) throws Exception;
}
