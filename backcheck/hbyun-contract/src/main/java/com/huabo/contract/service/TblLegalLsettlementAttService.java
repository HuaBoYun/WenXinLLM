package com.huabo.contract.service;

import java.math.BigDecimal;

public interface TblLegalLsettlementAttService {
    void deleteRelation(String attid)  throws Exception;

    void insertAttRelation(BigDecimal litigationid, String id)  throws Exception;
}
