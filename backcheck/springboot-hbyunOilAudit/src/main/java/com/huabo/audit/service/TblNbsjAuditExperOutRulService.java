package com.huabo.audit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperOutRulEntity;

public interface TblNbsjAuditExperOutRulService extends IService<TblNbsjAuditExperOutRulEntity>{
	public TblNbsjAuditExperOutRulEntity findByExperIdAndOutId(BigDecimal experId,BigDecimal outId);
	
//	public PageBean findByExperId(BigDecimal experId,Integer pageNumber,int pageSize);
	
	public void deleteByExperId(BigDecimal experId);

	public void delete(BigDecimal bigDecimal);
}
