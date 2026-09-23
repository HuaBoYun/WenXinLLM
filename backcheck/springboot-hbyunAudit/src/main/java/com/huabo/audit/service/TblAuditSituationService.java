package com.huabo.audit.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.audit.oracle.entity.TblAuditSituationEntity;
import com.huabo.audit.util.PageResult;

import java.math.BigDecimal;

public interface TblAuditSituationService {

    PageResult<TblAuditSituationEntity> querySituationListPage(String token,Integer pageNumber, Integer pageSize,String year)throws Exception;

    void SituationAdd(TblAuditSituationEntity situationEntity, String json);

    void SituationUpdate(TblAuditSituationEntity situationEntity, String json);

    TblAuditSituationEntity situation_details(BigDecimal situationId);

    void situationDelete(BigDecimal situationId);

    Integer situationValidationYear(Integer year);
}
