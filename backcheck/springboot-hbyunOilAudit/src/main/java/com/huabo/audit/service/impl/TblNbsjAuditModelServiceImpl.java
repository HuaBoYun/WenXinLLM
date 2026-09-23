package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblNbsjAuditModelEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAuditModelMapper;
import com.huabo.audit.service.TblNbsjAuditModelService;

@Service
public class TblNbsjAuditModelServiceImpl extends ServiceImpl<TblNbsjAuditModelMapper, TblNbsjAuditModelEntity> implements TblNbsjAuditModelService{

	@Override
	public TblNbsjAuditModelEntity getByExperId(BigDecimal experId) {
		TblNbsjAuditModelEntity tblNbsjAuditModelEntity = baseMapper.getByExperId(experId+"");
		return tblNbsjAuditModelEntity;
	}

	@Override
	public void deleteByExperId(BigDecimal experId) {
		baseMapper.deleteByExperId(experId+"");
		
	}

	@Override
	public List<TblNbsjAuditModelEntity> findAll(String id) {
		List<TblNbsjAuditModelEntity> list = baseMapper.findAll(id);
		return list;
	}

	@Override
	public void merge(TblNbsjAuditModelEntity model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean saveBatch(Collection<TblNbsjAuditModelEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<TblNbsjAuditModelEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<TblNbsjAuditModelEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(TblNbsjAuditModelEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TblNbsjAuditModelEntity getOne(Wrapper<TblNbsjAuditModelEntity> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<TblNbsjAuditModelEntity> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<TblNbsjAuditModelEntity> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblNbsjAuditModelMapper getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<TblNbsjAuditModelEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
