package com.huabo.audit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblNbsjAuditDatumEntity;

public interface TblNbsjAuditDatumService extends IService<TblNbsjAuditDatumEntity>{
//	public PageBean findByExperId(BigDecimal experId,Integer pageNumber,int pageSize);
	public void deleteByExperId(BigDecimal experId);
	public void merge(TblNbsjAuditDatumEntity auditDatum);
	public void delete(TblNbsjAuditDatumEntity datum);
}
