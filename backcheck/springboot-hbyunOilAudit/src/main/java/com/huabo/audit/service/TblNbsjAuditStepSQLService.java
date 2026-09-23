package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepSQLEntity;

public interface TblNbsjAuditStepSQLService extends IService<TblNbsjAuditStepSQLEntity>{
	public  List<TblNbsjAuditStepSQLEntity> findByStep(BigDecimal stepId);
	public void deleteByExperId(BigDecimal experId);
	public void deleteByStepId(BigDecimal stepId);
	public void delete(TblNbsjAuditStepSQLEntity tblNbsjAuditStepSQL);
	public void merge(TblNbsjAuditStepSQLEntity auditStepSQL);
}
