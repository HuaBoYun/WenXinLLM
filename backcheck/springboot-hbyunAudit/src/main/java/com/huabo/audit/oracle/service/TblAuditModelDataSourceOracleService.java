package com.huabo.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.audit.oracle.entity.TblAuditModelDataSourceOracle;
import com.huabo.audit.vo.param.ExcelCheckParam;
import com.huabo.audit.vo.param.TblAuditModelDataSourceQueryParam;

import java.math.BigDecimal;
import java.util.Map;

public interface TblAuditModelDataSourceOracleService {

	/**
	 * 审计-数据源管理 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblAuditModelDataSourceOracle> getList(TblAuditModelDataSourceQueryParam param);

	/**
	 * 审计-数据源管理 新增/更新
	 * @param param
	 * @return
	 */
	TblAuditModelDataSourceOracle saveOrUpdate(TblAuditModelDataSourceOracle param);

	/**
	 * 审计-数据源管理 删除
	 * @param id
	 */
	void delete(BigDecimal id);

	/**
	 * 审计-数据源管理 详情 查询
	 * @param id
	 * @return
	 */
	TblAuditModelDataSourceOracle findById(BigDecimal id);

	/**
	 * 获取map数据
	 * @return
	 */
	Map<BigDecimal, TblAuditModelDataSourceOracle> getAllMap();

	/**
	 * excel工作副本名校验
	 * @param param
	 * @return
	 */
	boolean excelCheck(ExcelCheckParam param);

	/**
	 * 根据excelId 查询数据库源
	 * @param excelId
	 * @return
	 */
	TblAuditModelDataSourceOracle getExcelIdDataSource(BigDecimal excelId);
}
