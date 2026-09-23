package com.huabo.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.constant.YesNo;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblAuditModelDataSourceOracle;
import com.huabo.audit.oracle.mapper.TblAuditModelDataSourceOracleMapper;
import com.huabo.audit.oracle.service.TblAuditModelDataSourceOracleService;
import com.huabo.audit.vo.param.ExcelCheckParam;
import com.huabo.audit.vo.param.TblAuditModelDataSourceQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TblAuditModelDataSourceOracleServiceImpl implements TblAuditModelDataSourceOracleService {

	@Resource
	private TblAuditModelDataSourceOracleMapper tblAuditModelDataSourceOracleMapper;

	@Value("${spring.datasource.oracle.url:}")
	private String dataBaseConnectionAddress;

	@Override
	public PageInfo<TblAuditModelDataSourceOracle> getList(TblAuditModelDataSourceQueryParam param) {
		Example example = new Example(TblAuditModelDataSourceOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getCreateType() != null) {
			criteria.andEqualTo("createType", param.getCreateType());
		}
		if (param.getBelongGroup() != null) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		example.setOrderByClause(" id desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblAuditModelDataSourceOracleMapper.selectByExample(example));
	}

	@Override
	public TblAuditModelDataSourceOracle saveOrUpdate(TblAuditModelDataSourceOracle param) {
		Date now = new Date();
		//excel类型手动给与默认值
		if (param.getCreateType() == 2) {
			param.setDataBaseType(DateBaseConfig.DATABASETYPE);
			param.setDataBaseConnectionAddress(dataBaseConnectionAddress);
		}
		if (param.getId() == null) {
			int count = tblAuditModelDataSourceOracleMapper.selectCount(TblAuditModelDataSourceOracle.ofDataBaseUsers(param.getDataBaseUsers()));
			if (count > 0) {
				throw new ServiceException(400, "数据库用户已存在");
			}
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			param.setId(RandomUtil.uuBigDecimalId());
			tblAuditModelDataSourceOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblAuditModelDataSourceOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(BigDecimal id) {
		tblAuditModelDataSourceOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblAuditModelDataSourceOracle findById(BigDecimal id) {
		TblAuditModelDataSourceOracle auditModelDataSource = tblAuditModelDataSourceOracleMapper.selectByPrimaryKey(id);
		if (auditModelDataSource == null) {
			throw new ServiceException(400, 50001);
		}
		return auditModelDataSource;
	}

	/**
	 * 获取map数据
	 * @return
	 */
	@Override
	public Map<BigDecimal, TblAuditModelDataSourceOracle> getAllMap() {
		List<TblAuditModelDataSourceOracle> auditModelDataSourceList = tblAuditModelDataSourceOracleMapper.selectAll();
		if (CollectionUtil.isNotEmpty(auditModelDataSourceList)) {
			return auditModelDataSourceList.stream().collect(Collectors.toMap(TblAuditModelDataSourceOracle::getId, item -> item));
		}
		return Collections.emptyMap();
	}

	/**
	 * excel工作副本名校验
	 * @param param
	 * @return
	 */
	@Override
	public boolean excelCheck(ExcelCheckParam param) {
		Integer count = tblAuditModelDataSourceOracleMapper.excelCheck(param);
		return count == 0;
	}

	@Override
	public TblAuditModelDataSourceOracle getExcelIdDataSource(BigDecimal excelId) {
		return tblAuditModelDataSourceOracleMapper.findExcelIdDataSource(excelId);
	}

	private Boolean idById(BigDecimal id) {
		int count = tblAuditModelDataSourceOracleMapper.selectCount(TblAuditModelDataSourceOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
