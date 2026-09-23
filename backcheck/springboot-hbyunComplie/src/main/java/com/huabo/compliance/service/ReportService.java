package com.huabo.compliance.service;

import com.hbfk.util.JsonBean;
import com.huabo.compliance.oracle.entity.TblComplianceReportOracle;
import com.huabo.compliance.vo.param.TblComplianceReportQueryParam;

public interface ReportService {

	/**
	 * 合规报告 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblComplianceReportList(TblComplianceReportQueryParam param);

	/**
	 * 合规报告 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblComplianceReport(TblComplianceReportOracle param);

	/**
	 * 合规报告 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblComplianceReport(Integer id);

	/**
	 * 合规报告 详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblComplianceReport(Integer id);
}
