package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepSQLEntity;
import com.huabo.audit.service.TblNbsjAuditStepSQLService;
@Service
public class TblNbsjAuditStepSQLServiceImpl implements TblNbsjAuditStepSQLService {

	@Override
	public boolean saveBatch(Collection<TblNbsjAuditStepSQLEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<TblNbsjAuditStepSQLEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<TblNbsjAuditStepSQLEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(TblNbsjAuditStepSQLEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TblNbsjAuditStepSQLEntity getOne(Wrapper<TblNbsjAuditStepSQLEntity> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<TblNbsjAuditStepSQLEntity> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<TblNbsjAuditStepSQLEntity> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseMapper<TblNbsjAuditStepSQLEntity> getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<TblNbsjAuditStepSQLEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjAuditStepSQLEntity> findByStep(BigDecimal stepId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByExperId(BigDecimal experId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteByStepId(BigDecimal stepId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(TblNbsjAuditStepSQLEntity tblNbsjAuditStepSQL) {
		// TODO Auto-generated method stub

	}

	@Override
	public void merge(TblNbsjAuditStepSQLEntity auditStepSQL) {
		// TODO Auto-generated method stub

	}

}
