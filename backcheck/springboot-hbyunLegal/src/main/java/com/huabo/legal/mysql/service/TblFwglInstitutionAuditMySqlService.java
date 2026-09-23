package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglInstitutionAuditMySql;
import com.huabo.legal.vo.param.TblFwglInstitutionAuditQueryParam;

public interface TblFwglInstitutionAuditMySqlService {

	/**
	 * 制度审核/经营事项审核列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglInstitutionAuditMySql> getList(TblFwglInstitutionAuditQueryParam param);

	/**
	 * 制度审核/经营事项审核 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglInstitutionAuditMySql saveOrUpdate(TblFwglInstitutionAuditMySql param);

	/**
	 * 制度审核/经营事项审核 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 制度审核/经营事项审核详情 查询
	 * @param id
	 * @return
	 */
	TblFwglInstitutionAuditMySql findById(Integer id);

}
