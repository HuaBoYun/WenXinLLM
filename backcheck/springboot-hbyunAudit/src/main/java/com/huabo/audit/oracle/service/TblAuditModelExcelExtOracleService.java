package com.huabo.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.audit.oracle.entity.TblAuditModelExcelExtOracle;
import com.huabo.audit.vo.param.GeneratingTableParam;
import com.huabo.audit.vo.param.TblAuditModelExcelExtQueryParam;

public interface TblAuditModelExcelExtOracleService {

	/**
	 * 审计-excel表数据 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblAuditModelExcelExtOracle> getList(TblAuditModelExcelExtQueryParam param);

	/**
	 * 审计-excel表数据 新增/更新
	 * @param param
	 * @return
	 */
	TblAuditModelExcelExtOracle saveOrUpdate(TblAuditModelExcelExtOracle param);

	/**
	 * 审计-excel表数据 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 审计-excel表数据 详情 查询
	 * @param id
	 * @return
	 */
	TblAuditModelExcelExtOracle findById(Long id);

	/**
	 * 修改生成表状态
	 * @param param
	 * @param state
	 * @return
	 */
	void updateState(GeneratingTableParam param, Integer state);
}
