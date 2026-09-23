package com.huabo.compliance.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.compliance.oracle.entity.TblComplianceReportOracle;
import com.huabo.compliance.vo.param.TblComplianceReportQueryParam;

public interface TblComplianceReportOracleService {

	/**
	 * 合规报告 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblComplianceReportOracle> getList(TblComplianceReportQueryParam param);

	/**
	 * 合规报告 新增/更新
	 * @param param
	 * @return
	 */
	TblComplianceReportOracle saveOrUpdate(TblComplianceReportOracle param);

	/**
	 * 合规报告 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 合规报告 详情查询
	 * @param id
	 * @return
	 */
	TblComplianceReportOracle findById(Integer id);
}
