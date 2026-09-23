package com.huabo.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.audit.oracle.entity.TblAuditModelExcelTableOracle;
import com.huabo.audit.vo.param.GeneratingTableParam;
import com.huabo.audit.vo.param.TblAuditModelExcelTableQueryParam;

import java.math.BigDecimal;
import java.util.List;

public interface TblAuditModelExcelTableOracleService {

	/**
	 * 审计-excel分析预览表数据 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblAuditModelExcelTableOracle> getList(TblAuditModelExcelTableQueryParam param);

	/**
	 * 审计-excel分析预览表数据 新增/更新
	 * @param param
	 * @return
	 */
	TblAuditModelExcelTableOracle saveOrUpdate(TblAuditModelExcelTableOracle param);

	/**
	 * 审计-excel分析预览表数据 新增/更新 批量
	 * @param param
	 * @return
	 */
	void saveOrUpdates(List<TblAuditModelExcelTableOracle> param);

	/**
	 * 审计-excel分析预览表数据 删除
	 * @param id
	 */
	void delete(BigDecimal id);

	/**
	 * 审计-excel分析预览表数据 详情 查询
	 * @param id
	 * @return
	 */
	TblAuditModelExcelTableOracle findById(BigDecimal id);

	/**
	 * 根据id 获取所需要新建的表字段
	 * @param param
	 * @return
	 */
	List<TblAuditModelExcelTableOracle> getGeneratingTable(GeneratingTableParam param);

	/**
	 * 修改生成表状态
	 * @param param
	 * @param state
	 * @return
	 */
	void updateState(GeneratingTableParam param, Integer state);
}
