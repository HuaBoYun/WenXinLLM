package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperienceEntity;
import com.huabo.audit.oracle.mapper.TblNbsjAuditExperienceMapper;
import com.huabo.audit.service.TblNbsjAuditExperienceService;
@Service
public class TblNbsjAuditExperienceServiceImpl extends ServiceImpl<TblNbsjAuditExperienceMapper, TblNbsjAuditExperienceEntity> implements TblNbsjAuditExperienceService {

	@Override
	public void merge(TblNbsjAuditExperienceEntity auditExperience) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(BigDecimal bigDecimal) {
		baseMapper.delete(bigDecimal+"");

	}
	
	

	@Override
	public boolean saveBatch(Collection<TblNbsjAuditExperienceEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<TblNbsjAuditExperienceEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<TblNbsjAuditExperienceEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(TblNbsjAuditExperienceEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TblNbsjAuditExperienceEntity getOne(Wrapper<TblNbsjAuditExperienceEntity> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<TblNbsjAuditExperienceEntity> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<TblNbsjAuditExperienceEntity> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblNbsjAuditExperienceMapper getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<TblNbsjAuditExperienceEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
