package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglInstitutionAuditExtMySql;

import java.util.List;

public interface TblFwglInstitutionAuditExtMySqlService {

	/**
	 * 制度审核-制度列表 查询
	 * @param institutionAuditExtId
	 * @return
	 */
	List<TblFwglInstitutionAuditExtMySql> getList(String institutionAuditExtId);

	/**
	 * 制度审核-制度新增/更新
	 * @param param
	 * @return
	 */
	TblFwglInstitutionAuditExtMySql saveOrUpdate(TblFwglInstitutionAuditExtMySql param);

	/**
	 * 制度审核-制度 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 制度审核-制度 查询
	 * @param id
	 * @return
	 */
	TblFwglInstitutionAuditExtMySql findById(Integer id);
}
