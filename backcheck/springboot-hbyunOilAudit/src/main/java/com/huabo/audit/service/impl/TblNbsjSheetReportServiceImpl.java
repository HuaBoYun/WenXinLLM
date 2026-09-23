package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjSheetReportEntity;
import com.huabo.audit.service.TblNbsjSheetReportService;
@Service
public class TblNbsjSheetReportServiceImpl implements TblNbsjSheetReportService {

	@Override
	public boolean saveBatch(Collection<TblNbsjSheetReportEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<TblNbsjSheetReportEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<TblNbsjSheetReportEntity> entityList, int batchSize) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(TblNbsjSheetReportEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TblNbsjSheetReportEntity getOne(Wrapper<TblNbsjSheetReportEntity> queryWrapper, boolean throwEx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMap(Wrapper<TblNbsjSheetReportEntity> queryWrapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getObj(Wrapper<TblNbsjSheetReportEntity> queryWrapper, Function<? super Object, V> mapper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseMapper<TblNbsjSheetReportEntity> getBaseMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<TblNbsjSheetReportEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblNbsjSheetReportEntity> findReportListBySheet(String string) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(BigDecimal reportid) {
		// TODO Auto-generated method stub

	}

}
