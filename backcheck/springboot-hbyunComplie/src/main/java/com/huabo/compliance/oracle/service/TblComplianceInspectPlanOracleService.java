package com.huabo.compliance.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblComplianceInspectPlanOracle;
import com.huabo.compliance.vo.param.TblComplianceInspectPlanQueryParam;

public interface TblComplianceInspectPlanOracleService {

	/**
	 * 检查方案 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblComplianceInspectPlanOracle> getList(TblComplianceInspectPlanQueryParam param);

	/**
	 * 检查方案 新增/更新
	 * @param param
	 * @return
	 */
	TblComplianceInspectPlanOracle saveOrUpdate(TblComplianceInspectPlanOracle param);

	/**
	 * 检查方案 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 检查方案 详情查询
	 * @param id
	 * @return
	 */
	TblComplianceInspectPlanOracle findById(Integer id);

	/**
	 * 检查方案-自动编码
	 * @param year
	 * @return
	 */
	String getTblComplianceInspectPlanAutoNum(String year);
}
