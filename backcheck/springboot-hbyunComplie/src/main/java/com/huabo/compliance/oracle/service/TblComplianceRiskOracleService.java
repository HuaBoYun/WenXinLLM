package com.huabo.compliance.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblComplianceRiskOracle;
import com.huabo.compliance.vo.param.TblComplianceRiskQueryParam;

public interface TblComplianceRiskOracleService {

	/**
	 * 合规风险 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblComplianceRiskOracle> getList(TblComplianceRiskQueryParam param);

	/**
	 * 合规风险 新增/更新
	 * @param param
	 * @return
	 */
	TblComplianceRiskOracle saveOrUpdate(TblComplianceRiskOracle param);

	/**
	 * 合规风险 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 合规风险 详情查询
	 * @param id
	 * @return
	 */
	TblComplianceRiskOracle findById(Integer id);
}
