package com.huabo.audit.service;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;

public interface AuditControlAnalysisService {

	/**
	 * 审计预警结果指标汇总
	 * @param staff
	 * @param limitStr 
	 * @return
	 * @throws Exception
	 */
	JsonBean sumIndicatorAnalysisSummary(TblStaffUtil staff, String limitStr) throws Exception;

}
