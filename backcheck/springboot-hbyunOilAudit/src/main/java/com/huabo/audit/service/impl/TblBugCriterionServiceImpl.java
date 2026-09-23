package com.huabo.audit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblBugCriterionEntity;
import com.huabo.audit.oracle.mapper.TblBugCriterionMapper;
import com.huabo.audit.service.TblBugCriterionService;
@Service
public class TblBugCriterionServiceImpl extends ServiceImpl<TblBugCriterionMapper, TblBugCriterionEntity> implements TblBugCriterionService {

	@Override
	public TblBugCriterionEntity findByTblBugCriterion(String bugid) {
		TblBugCriterionEntity tblBugCriterionEntity = baseMapper.findByTblBugCriterion(bugid);
		return tblBugCriterionEntity;
	}

	@Override
	public List<TblBugCriterionEntity> findAll() {
		List<TblBugCriterionEntity> list = baseMapper.findAll("");
		return list;
	}

	@Override
	public List<TblBugCriterionEntity> findAll(String orgid) {
		List<TblBugCriterionEntity> list = baseMapper.findAll(orgid);
		return list;
	}

	@Override
	public TblBugCriterionEntity findByid(String id) {
		TblBugCriterionEntity tblBugCriterionEntity = baseMapper.findByid(id);
		return tblBugCriterionEntity;
	}

	@Override
	public List<TblBugCriterionEntity> fingByLevel(String orgid, String level) {
		List<TblBugCriterionEntity> list = baseMapper.fingByLevel(orgid,level);
		return list;
	}

}
