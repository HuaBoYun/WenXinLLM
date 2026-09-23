package com.huabo.compliance.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblCompliancePlanMgtOracle;
import com.huabo.compliance.vo.param.TblCompliancePlanMgtQueryParam;

public interface TblCompliancePlanMgtOracleService {

	/**
	 * 计划管理 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCompliancePlanMgtOracle> getList(TblCompliancePlanMgtQueryParam param);

	/**
	 * 计划管理 新增/更新
	 * @param param
	 * @return
	 */
	TblCompliancePlanMgtOracle saveOrUpdate(TblCompliancePlanMgtOracle param);

	/**
	 * 计划管理 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 计划管理 详情查询
	 * @param id
	 * @return
	 */
	TblCompliancePlanMgtOracle findById(Integer id);
}
