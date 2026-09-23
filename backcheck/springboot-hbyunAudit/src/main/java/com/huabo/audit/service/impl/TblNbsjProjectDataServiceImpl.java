package com.huabo.audit.service.impl;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjProjectDataEntity;
import com.huabo.audit.service.TblNbsjProjectDataService;
@Service
public class TblNbsjProjectDataServiceImpl implements TblNbsjProjectDataService {

	@Override
	public boolean saveBatch(Collection<TblNbsjProjectDataEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<TblNbsjProjectDataEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<TblNbsjProjectDataEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(TblNbsjProjectDataEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TblNbsjProjectDataEntity getOne(Wrapper<TblNbsjProjectDataEntity> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<TblNbsjProjectDataEntity> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<TblNbsjProjectDataEntity> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseMapper<TblNbsjProjectDataEntity> getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<TblNbsjProjectDataEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
