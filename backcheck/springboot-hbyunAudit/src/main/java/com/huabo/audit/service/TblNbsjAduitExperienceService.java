package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblNbsjAduitExperienceEntity;

public interface TblNbsjAduitExperienceService extends IService<TblNbsjAduitExperienceEntity>{
//	public PageBean findByTargetId(BigDecimal targetId,BigDecimal tempId,Integer pageNumber,int pageSize);
	
	
	public void deleteByTargetId(Integer targetId);
	
	
	public void deleteByTempId(BigDecimal tempId);
		
	public void merge(TblNbsjAduitExperienceEntity aduitTarget);

	public void delete(TblNbsjAduitExperienceEntity tblAduitTarget);
}
