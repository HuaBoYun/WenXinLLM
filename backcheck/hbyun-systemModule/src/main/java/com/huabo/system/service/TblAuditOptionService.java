package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblAuditOption;

public interface TblAuditOptionService {
	
    List<TblAuditOption> findOptionByRelationId(BigDecimal planid);

    List<TblAuditOption> findOptionByRelation(String sheetid, String cyid);

}
