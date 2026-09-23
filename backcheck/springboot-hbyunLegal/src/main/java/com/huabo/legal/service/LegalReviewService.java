package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.mysql.entity.TblFwglInstitutionAuditExtMySql;
import com.huabo.legal.mysql.entity.TblFwglInstitutionAuditMySql;
import com.huabo.legal.vo.param.TblFwglInstitutionAuditQueryParam;
import com.huabo.legal.vo.result.TblFwglInstitutionAudit;
import com.huabo.legal.vo.result.TblFwglInstitutionAuditExt;

public interface LegalReviewService {

	/**
	 * 制度审核/经营事项审核列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglInstitutionAuditList(TblFwglInstitutionAuditQueryParam param);

	/**
	 * 制度审核/经营事项审核 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglInstitutionAudit(TblFwglInstitutionAudit param);

	/**
	 * 制度审核/经营事项审核 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglInstitutionAudit(Long id);

	/**
	 * 制度审核/经营事项审核详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglInstitutionAudit(Long id);

	/**
	 * 审核台账列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getAllList(TblFwglInstitutionAuditQueryParam param);

	/**
	 * 制度审核-制度 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglInstitutionAuditExt(TblFwglInstitutionAuditExt param);

	/**
	 * 制度审核-制度 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglInstitutionAuditExt(Long id);

	/**
	 * 制度审核-制度详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglInstitutionAuditExt(Long id);
}
