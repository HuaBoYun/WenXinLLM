package com.huabo.audit.service;
import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;
import com.huabo.audit.oracle.vo.TblNbsjPlanProjectVo;

/**
 * @MEMO   审计计划管理  审计计划  中审计项目接口
 */
public interface TblNbsjPlanProjectService {
	
	JsonBean mergePlanProjectManageInfo(TblNbsjPlanProject project, BigDecimal planId, String plancode, String token) throws Exception;

	JsonBean removePlanProjectInfo(String token, Integer planprojectid) throws Exception;

	JsonBean mergePlanProjectManageInfoList(List<TblNbsjPlanProject> projectList, String planId, String plancode,
			String token) throws Exception;
	
	JsonBean findPlanProjectListInfoByPlanId(String token, Integer planId) throws Exception;

	JsonBean getPlanProjectListByPlanIdPageInfo(String token, TblNbsjPlanProjectVo project) throws Exception;
	
	JsonBean findProjectListInfoByWspJhw(String token,Integer planId) throws Exception;

}
