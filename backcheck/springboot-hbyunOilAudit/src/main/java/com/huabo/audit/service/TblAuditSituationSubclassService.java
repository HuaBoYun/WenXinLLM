package com.huabo.audit.service;


import com.huabo.audit.oracle.entity.TblAuditSituationSubclassEntity;

import java.util.List;

public interface TblAuditSituationSubclassService  {

    List<TblAuditSituationSubclassEntity> situationSubclassDetails(Integer situationId);
}
