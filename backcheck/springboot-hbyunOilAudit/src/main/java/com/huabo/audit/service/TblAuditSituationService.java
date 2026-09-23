package com.huabo.audit.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.audit.oracle.entity.TblAuditSituationEntity;

public interface TblAuditSituationService {

    Page<TblAuditSituationEntity> querySituationListPage( Integer pageNumber, Integer pageSize)throws Exception;

    void SituationAdd(TblAuditSituationEntity situationEntity, String json);

    void SituationUpdate(TblAuditSituationEntity situationEntity, String json);

    TblAuditSituationEntity situation_details(Integer situationId);

    void situationDelete(Integer situationId);

    Integer situationValidationYear(Integer year);
}
