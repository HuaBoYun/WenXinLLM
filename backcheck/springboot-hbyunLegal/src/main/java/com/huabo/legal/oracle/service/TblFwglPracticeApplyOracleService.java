package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglPracticeApplyOracle;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.param.TblFwglPracticeApplyQueryParam;
import com.huabo.legal.vo.result.LegalCompanyLawyerResult;
import com.huabo.legal.vo.result.LegalPersonnelCardEmploymentRateResult;

import java.util.List;

public interface TblFwglPracticeApplyOracleService {

	/**
	 * 执业申请列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPracticeApplyOracle> getList(TblFwglPracticeApplyQueryParam param);

	/**
	 * 执业申请 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPracticeApplyOracle saveOrUpdate(TblFwglPracticeApplyOracle param);

	/**
	 * 执业申请 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 执业申请详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPracticeApplyOracle findById(Long id);

	/**
	 * 法务人员持证上岗率
	 * @param param
	 */
	LegalPersonnelCardEmploymentRateResult getLegalPersonnelCardEmploymentRate(TblFwglParam param);

	/**
	 * 公司律师人数占比
	 * @param param
	 * @return
	 */
	LegalPersonnelCardEmploymentRateResult getFirmLegalProportion(TblFwglParam param);

	/**
	 * 公司律师人数
	 * @param param
	 * @return
	 */
	List<LegalCompanyLawyerResult> getLegalCompanyLawyer(TblFwglParam param);
}
