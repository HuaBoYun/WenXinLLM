package com.huabo.legal.oracle.service;

import com.huabo.legal.mysql.entity.TblFwglInstitutionAuditExtMySql;
import com.huabo.legal.oracle.entity.TblFwglInstitutionAuditExtOracle;

import java.util.List;

public interface TblFwglInstitutionAuditExtOracleService {

	/**
	 * 制度审核-制度列表 查询
	 * @param institutionAuditExtId
	 * @return
	 */
	List<TblFwglInstitutionAuditExtOracle> getList(String institutionAuditExtId);

	/**
	 * 制度审核-制度新增/更新
	 * @param param
	 * @return
	 */
	TblFwglInstitutionAuditExtOracle saveOrUpdate(TblFwglInstitutionAuditExtOracle param);

	/**
	 * 制度审核-制度 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 制度审核-制度 查询
	 * @param id
	 * @return
	 */
	TblFwglInstitutionAuditExtOracle findById(Long id);
}
