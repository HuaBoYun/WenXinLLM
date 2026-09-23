package com.huabo.audit.service.business;

import com.huabo.audit.util.MyJsonBean;
import com.huabo.audit.vo.param.ProjectSettlementCompletionQueryParam;
import com.huabo.audit.vo.param.ProjectSettlementCostIntermediateQueryParam;
import com.huabo.audit.vo.result.ProjectSettlementCompletionResult;
import com.huabo.audit.vo.result.ProjectSettlementCostIntermediateResult;

import java.math.BigDecimal;

public interface ProjectSettlementService {

	/**
	 * 工程结算审计项目汇总 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<ProjectSettlementCostIntermediateResult> getProjectSettlementCostIntermediateList(ProjectSettlementCostIntermediateQueryParam param);

	/**
	 * 工程结算审计项目汇总 详情查询
	 * @param gcxmzjzjbid
	 * @return
	 */
	MyJsonBean<ProjectSettlementCostIntermediateResult> getProjectSettlementCostIntermediate(BigDecimal gcxmzjzjbid);

	/**
	 * 竣工决算审计项目汇总 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<ProjectSettlementCompletionResult> getProjectSettlementCompletionList(ProjectSettlementCompletionQueryParam param);

	/**
	 * 竣工决算审计项目汇总 详情查询
	 * @param jsxmtzwcqkid
	 * @return
	 */
	MyJsonBean<ProjectSettlementCompletionResult> getProjectSettlementCompletion(Long jsxmtzwcqkid);
}
