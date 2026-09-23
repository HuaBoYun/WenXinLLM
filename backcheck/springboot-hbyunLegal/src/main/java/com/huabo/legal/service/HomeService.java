package com.huabo.legal.service;

import com.huabo.legal.oracle.entity.TblFwglConferenceManagementOracle;
import com.huabo.legal.util.JsonBean;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.*;

import java.util.List;

public interface HomeService {

	/**
	 * 消息通知
	 * @param param
	 * @return
	 */
	JsonBean<PageResult<TblFwglConferenceManagementOracle>> getTblFwglConferenceManagementList(TblFwglConferenceManagementQueryParam param);

	/**
	 * 法务人员持证上岗率
	 * @param param
	 * @return
	 */
	JsonBean<LegalPersonnelCardEmploymentRateResult> getLegalPersonnelCardEmploymentRate(TblFwglParam param);

	/**
	 * 公司律师人数占比
	 * @param param
	 * @return
	 */
	JsonBean<LegalPersonnelCardEmploymentRateResult> getFirmLegalProportion(TblFwglParam param);

	/**
	 * 本年度制度法律合规审查数量
	 * @param param
	 * @return
	 */
	JsonBean<List<TblFwglComplianceResult>> getInstitutionCompliance(TblFwglParam param);

	/**
	 * 本年重大决策法律合规审查数量
	 * @param param
	 * @return
	 */
	JsonBean<List<TblFwglComplianceResult>> getBusinessInstitutionCompliance(TblFwglParam param);

	/**
	 * 法务人员数量
	 * @param param
	 * @return
	 */
	JsonBean<List<LegalPersonnelCountResult>> getLegalPersonnelCount(TblFwglParam param);

	/**
	 * 公司律师人数
	 * @param param
	 * @return
	 */
	JsonBean<List<LegalCompanyLawyerResult>> getLegalCompanyLawyer(TblFwglParam param);

	/**
	 * 法务人数数量-专职法务人鱼/兼职法务人 占比
	 * @param param
	 * @return
	 */
	JsonBean<LegalFullimePercentageResult> getLegalFullimePercentage(TblFwglParam param);

	/**
	 * 全体系经营事项及制度审核数量
	 * @param param
	 * @return
	 */
	JsonBean<List<LegalInstitutionResult>> getLegalInstitution(TblFwglParam param);
}
