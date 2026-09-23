package com.huabo.compliance.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblComplianceManualMgtOracle;
import com.huabo.compliance.vo.param.TblComplianceManualMgtQueryParam;

public interface TblComplianceManualMgtOracleService {

	/**
	 * 合规手册管理 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblComplianceManualMgtOracle> getList(TblComplianceManualMgtQueryParam param);

	/**
	 * 合规手册管理 新增/更新
	 * @param param
	 * @return
	 */
	TblComplianceManualMgtOracle saveOrUpdate(TblComplianceManualMgtOracle param);

	/**
	 * 合规手册管理 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 合规手册管理 详情查询
	 * @param id
	 * @return
	 */
	TblComplianceManualMgtOracle findById(Integer id);
}
