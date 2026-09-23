package com.huabo.audit.oracle.service.impl;

import com.huabo.audit.constant.YesNo;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblAuditModelExcelContentOracle;
import com.huabo.audit.oracle.mapper.TblAuditModelExcelContentOracleMapper;
import com.huabo.audit.oracle.service.TblAuditModelExcelContentOracleService;
import com.huabo.audit.vo.param.TblAuditModelExcelContentQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class TblAuditModelExcelContentOracleServiceImpl implements TblAuditModelExcelContentOracleService {

	@Resource
	private TblAuditModelExcelContentOracleMapper tblAuditModelExcelContentOracleMapper;

	@Override
	public List<TblAuditModelExcelContentOracle> getList(TblAuditModelExcelContentQueryParam param) {
		Example example = new Example(TblAuditModelExcelContentOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getExcelId() != null) {
			criteria.andEqualTo("excelId", param.getExcelId());
		}
		if (StringUtils.isNotBlank(param.getTableNameEn())) {
			criteria.andEqualTo("tableNameEn", param.getTableNameEn());
		}
		if (param.getBelongGroup() != null) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		example.setOrderByClause(" id desc ");
		return tblAuditModelExcelContentOracleMapper.selectByExample(example);
	}

	@Override
	public TblAuditModelExcelContentOracle saveOrUpdate(TblAuditModelExcelContentOracle param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblAuditModelExcelContentOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblAuditModelExcelContentOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(BigDecimal id) {
		tblAuditModelExcelContentOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblAuditModelExcelContentOracle findById(BigDecimal id) {
		TblAuditModelExcelContentOracle auditModelExcelContent = tblAuditModelExcelContentOracleMapper.selectByPrimaryKey(id);
		if (auditModelExcelContent == null) {
			throw new ServiceException(400, 50001);
		}
		return auditModelExcelContent;
	}


	private Boolean idById(BigDecimal id) {
		int count = tblAuditModelExcelContentOracleMapper.selectCount(TblAuditModelExcelContentOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
