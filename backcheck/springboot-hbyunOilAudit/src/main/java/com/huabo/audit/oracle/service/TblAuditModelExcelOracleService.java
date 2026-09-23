package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.github.pagehelper.PageInfo;
import com.huabo.audit.oracle.entity.TblAuditModelExcelOracle;
import com.huabo.audit.vo.param.TblAuditModelExcelQueryParam;

public interface TblAuditModelExcelOracleService {

	/**
	 * 审计-excel导入记录 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblAuditModelExcelOracle> getList(TblAuditModelExcelQueryParam param);

	/**
	 * 审计-excel导入记录 新增/更新
	 * @param param
	 * @return
	 */
	TblAuditModelExcelOracle saveOrUpdate(TblAuditModelExcelOracle param);

	/**
	 * 审计-excel导入记录 删除
	 * @param id
	 */
	void delete(BigDecimal id);

	/**
	 * 审计-excel导入记录 详情 查询
	 * @param id
	 * @return
	 */
	TblAuditModelExcelOracle findById(BigDecimal id);
}
