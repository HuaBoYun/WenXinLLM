package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblNbsjAuditModelEntity;

public interface TblNbsjAuditModelService extends IService<TblNbsjAuditModelEntity>{
	public TblNbsjAuditModelEntity getByExperId(BigDecimal experId);
	public void deleteByExperId(BigDecimal experId);
	public List<TblNbsjAuditModelEntity> findAll(String id);
	public void merge(TblNbsjAuditModelEntity model);
}
