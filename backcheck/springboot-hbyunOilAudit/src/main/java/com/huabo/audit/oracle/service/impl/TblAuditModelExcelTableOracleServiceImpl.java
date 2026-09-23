package com.huabo.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.constant.YesNo;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblAuditModelExcelTableOracle;
import com.huabo.audit.oracle.mapper.TblAuditModelExcelTableOracleMapper;
import com.huabo.audit.oracle.service.TblAuditModelExcelTableOracleService;
import com.huabo.audit.vo.param.GeneratingTableParam;
import com.huabo.audit.vo.param.TblAuditModelExcelTableQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TblAuditModelExcelTableOracleServiceImpl implements TblAuditModelExcelTableOracleService {

	@Resource
	private TblAuditModelExcelTableOracleMapper tblAuditModelExcelTableOracleMapper;

	@Override
	public PageInfo<TblAuditModelExcelTableOracle> getList(TblAuditModelExcelTableQueryParam param) {
		Example example = new Example(TblAuditModelExcelTableOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getTableNameEn())) {
			criteria.andEqualTo("tableNameEn", param.getTableNameEn());
		}
		if (param.getExcelId() != null) {
			criteria.andEqualTo("excelId", param.getExcelId());
		}
		if (param.getBelongGroup() != null) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		example.setOrderByClause(" id desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblAuditModelExcelTableOracleMapper.selectByExample(example));
	}

	@Override
	public TblAuditModelExcelTableOracle saveOrUpdate(TblAuditModelExcelTableOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblAuditModelExcelTableOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblAuditModelExcelTableOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void saveOrUpdates(List<TblAuditModelExcelTableOracle> param) {
		param.forEach(x -> saveOrUpdate(x));
	}

	@Override
	public void delete(BigDecimal id) {
		tblAuditModelExcelTableOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblAuditModelExcelTableOracle findById(BigDecimal id) {
		TblAuditModelExcelTableOracle model = tblAuditModelExcelTableOracleMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 根据id 获取所需要新建的表字段
	 * @param param
	 * @return
	 */
	@Override
	public List<TblAuditModelExcelTableOracle> getGeneratingTable(GeneratingTableParam param) {
		Example example = new Example(TblAuditModelExcelTableOracle.class);
		example.createCriteria().andEqualTo("tableNameEn", param.getTableNameEn()).andEqualTo("excelId", param.getExcelId())
				.andEqualTo("isSelected", YesNo.YES);
		return tblAuditModelExcelTableOracleMapper.selectByExample(example);
	}

	@Override
	public void updateState(GeneratingTableParam param, Integer state) {
		Example example = new Example(TblAuditModelExcelTableOracle.class);
		example.createCriteria().andEqualTo("tableNameEn", param.getTableNameEn()).andEqualTo("excelId", param.getExcelId());
		TblAuditModelExcelTableOracle model = new TblAuditModelExcelTableOracle();
		model.setState(state);
		tblAuditModelExcelTableOracleMapper.updateByExampleSelective(model, example);
	}

	private Boolean idById(BigDecimal id) {
		int count = tblAuditModelExcelTableOracleMapper.selectCount(TblAuditModelExcelTableOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
