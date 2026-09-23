package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglInstitutionAuditOracle;
import com.huabo.legal.vo.param.TblFwglInstitutionAuditQueryParam;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.result.LegalInstitutionResult;
import com.huabo.legal.vo.result.TblFwglComplianceResult;

import java.util.Date;
import java.util.List;

public interface TblFwglInstitutionAuditOracleService {

	/**
	 * 制度审核/经营事项审核列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglInstitutionAuditOracle> getList(TblFwglInstitutionAuditQueryParam param);

	/**
	 * 制度审核/经营事项审核 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglInstitutionAuditOracle saveOrUpdate(TblFwglInstitutionAuditOracle param);

	/**
	 * 制度审核/经营事项审核 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 制度审核/经营事项审核详情 查询
	 * @param id
	 * @return
	 */
	TblFwglInstitutionAuditOracle findById(Long id);

	/**
	 * 本年度制度法律合规审查数量
	 * @param param
	 * @param beginOfYear
	 * @param endYear
	 * @return
	 */
	List<TblFwglComplianceResult> getInstitutionAuditCompliance(TblFwglParam param, Date beginOfYear, Date endYear);

	/**
	 * 本年重大决策法律合规审查数量
	 * @param param
	 * @param beginOfYear
	 * @param endYear
	 * @return
	 */
	List<TblFwglComplianceResult> getBusinessInstitutionAuditCompliance(TblFwglParam param, Date beginOfYear, Date endYear);

	/**
	 * 全体系经营事项及制度审核数量
	 * @param param
	 * @return
	 */
	List<LegalInstitutionResult> getLegalInstitution(TblFwglParam param);
}
