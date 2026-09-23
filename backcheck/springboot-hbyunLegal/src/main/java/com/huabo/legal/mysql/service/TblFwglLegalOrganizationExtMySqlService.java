package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglLegalOrganizationExtMySql;

import java.util.List;

public interface TblFwglLegalOrganizationExtMySqlService {

	/**
	 * 法务机构及负责人-年度法律审核情况 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalOrganizationExtMySql saveOrUpdate(TblFwglLegalOrganizationExtMySql param);

	/**
	 * 法务机构及负责人-年度法律审核情况详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalOrganizationExtMySql findById(Integer id);

	/**
	 * 法务机构及负责人-年度法律审核情况 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 根据法务机构及负责人扩展ID 法务机构及负责人-年度法律审核情况列表 查询
	 * @param organizationExtId 法务机构及负责人扩展ID
	 */
	List<TblFwglLegalOrganizationExtMySql> getList(String organizationExtId);
}
