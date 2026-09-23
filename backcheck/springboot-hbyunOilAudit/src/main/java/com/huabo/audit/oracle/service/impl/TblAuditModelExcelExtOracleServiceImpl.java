package com.huabo.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.constant.YesNo;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblAuditModelExcelExtOracle;
import com.huabo.audit.oracle.entity.TblAuditModelExcelTableOracle;
import com.huabo.audit.oracle.mapper.TblAuditModelExcelExtOracleMapper;
import com.huabo.audit.oracle.service.TblAuditModelExcelExtOracleService;
import com.huabo.audit.vo.param.GeneratingTableParam;
import com.huabo.audit.vo.param.TblAuditModelExcelExtQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class TblAuditModelExcelExtOracleServiceImpl implements TblAuditModelExcelExtOracleService {

	@Resource
	private TblAuditModelExcelExtOracleMapper tblAuditModelExcelExtOracleMapper;

	@Override
	public PageInfo<TblAuditModelExcelExtOracle> getList(TblAuditModelExcelExtQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblAuditModelExcelExtOracleMapper.getList(param));
	}

	@Override
	public TblAuditModelExcelExtOracle saveOrUpdate(TblAuditModelExcelExtOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblAuditModelExcelExtOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblAuditModelExcelExtOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(BigDecimal id) {
		tblAuditModelExcelExtOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblAuditModelExcelExtOracle findById(BigDecimal id) {
		TblAuditModelExcelExtOracle auditModelExcelExt = tblAuditModelExcelExtOracleMapper.selectByPrimaryKey(id);
		if (auditModelExcelExt == null) {
			throw new ServiceException(400, 50001);
		}
		return auditModelExcelExt;
	}

	@Override
	public void updateState(GeneratingTableParam param, Integer state) {
		Example example = new Example(TblAuditModelExcelExtOracle.class);
		example.createCriteria().andEqualTo("tableNameEn", param.getTableNameEn()).andEqualTo("excelId", param.getExcelId());
		TblAuditModelExcelExtOracle model = new TblAuditModelExcelExtOracle();
		model.setState(state);
		if (StringUtils.isNotBlank(param.getErrorMsg())) {
			model.setErrorMsg(param.getErrorMsg());
			if (param.getErrorMsg().length() > 1000) {
				model.setErrorMsg(param.getErrorMsg().substring(0, 1000));
			}
		}
		tblAuditModelExcelExtOracleMapper.updateByExampleSelective(model, example);
	}

	private Boolean idById(BigDecimal id) {
		int count = tblAuditModelExcelExtOracleMapper.selectCount(TblAuditModelExcelExtOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
