package com.huabo.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.constant.YesNo;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblAuditModelExcelOracle;
import com.huabo.audit.oracle.mapper.TblAuditModelExcelOracleMapper;
import com.huabo.audit.oracle.service.TblAuditModelExcelOracleService;
import com.huabo.audit.vo.param.TblAuditModelExcelQueryParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class TblAuditModelExcelOracleServiceImpl implements TblAuditModelExcelOracleService {

	@Resource
	private TblAuditModelExcelOracleMapper tblAuditModelExcelOracleMapper;

	@Override
	public PageInfo<TblAuditModelExcelOracle> getList(TblAuditModelExcelQueryParam param) {
		Example example = new Example(TblAuditModelExcelOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getDataBaseId() != null) {
			criteria.andEqualTo("dataBaseId", param.getDataBaseId());
		}
		if (param.getBelongGroup() != null) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		example.setOrderByClause(" id desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblAuditModelExcelOracleMapper.selectByExample(example));
	}

	@Override
	public TblAuditModelExcelOracle saveOrUpdate(TblAuditModelExcelOracle param) {
		Date now = new Date();
		if (StringUtils.isNotBlank(param.getErrorMsg())) {
			if (param.getErrorMsg().length() >= 4000) {
				param.setErrorMsg(param.getErrorMsg().substring(0, 4000));
			}
		}
		if (param.getId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblAuditModelExcelOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblAuditModelExcelOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(BigDecimal id) {
		tblAuditModelExcelOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblAuditModelExcelOracle findById(BigDecimal id) {
		TblAuditModelExcelOracle model = tblAuditModelExcelOracleMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	private Boolean idById(BigDecimal id) {
		int count = tblAuditModelExcelOracleMapper.selectCount(TblAuditModelExcelOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
