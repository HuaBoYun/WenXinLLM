package com.huabo.audit.service;

import java.util.List;

import com.huabo.audit.oracle.entity.TblBugCriterionEntity;

public interface TblBugCriterionService {
	public TblBugCriterionEntity findByTblBugCriterion(String bugid);
//	public PageBean findAll(Integer startIndex,String orgid,Integer pageSize);
	public List<TblBugCriterionEntity> findAll();
	public List<TblBugCriterionEntity> findAll(String orgid);
	
	public TblBugCriterionEntity findByid(String id);
	
	public List<TblBugCriterionEntity> fingByLevel(String orgid,String level);
}
