package com.huabo.audit.service;


import com.huabo.audit.oracle.entity.TblAuditSituationSubclassEntity;

import java.math.BigDecimal;
import java.util.List;

public interface TblAuditSituationSubclassService  {

    List<TblAuditSituationSubclassEntity> situationSubclassDetails(BigDecimal situationId);
}
