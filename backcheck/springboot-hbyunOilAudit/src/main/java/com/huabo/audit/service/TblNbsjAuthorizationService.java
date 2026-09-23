package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.audit.oracle.entity.TblNbsjAuthorizationEntity;

public interface TblNbsjAuthorizationService extends IService<TblNbsjAuthorizationEntity>{
	
	public TblNbsjAuthorizationEntity get(BigDecimal projectId,Integer aduitProGramId);
	
	public void merge(List<TblNbsjAuthorizationEntity> list);
	
	public List<TblNbsjAuthorizationEntity> getByProjectId(BigDecimal integer);

	public void delete(TblNbsjAuthorizationEntity tblNbsjAuthorization);
	
	
	public void save(List<TblNbsjAuthorizationEntity> list);
	
	public void newsave(List<TblNbsjAuthorizationEntity> list);
	
}
