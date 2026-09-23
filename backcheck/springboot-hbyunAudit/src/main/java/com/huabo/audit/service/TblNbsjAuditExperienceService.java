package com.huabo.audit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperienceEntity;

public interface TblNbsjAuditExperienceService extends IService<TblNbsjAuditExperienceEntity>{
	
	/**
	 * 根据审计经验类型获取列表
	 */
//	public PageBean findByType(BigDecimal typeId,String title,String autor,Integer pageNumber,int pageSize);

	public void merge(TblNbsjAuditExperienceEntity auditExperience);

	public void delete(BigDecimal bigDecimal);
}
