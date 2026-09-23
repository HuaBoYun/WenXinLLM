package com.huabo.legal.oracle.service;

import com.huabo.legal.mysql.entity.TblFwglLegalOrganizationExtMySql;
import com.huabo.legal.oracle.entity.TblFwglLegalOrganizationExtOracle;

import java.util.List;

public interface TblFwglLegalOrganizationExtOracleService {

	/**
	 * 法务机构及负责人-年度法律审核情况 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalOrganizationExtOracle saveOrUpdate(TblFwglLegalOrganizationExtOracle param);

	/**
	 * 法务机构及负责人-年度法律审核情况详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalOrganizationExtOracle findById(Long id);

	/**
	 * 法务机构及负责人-年度法律审核情况 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 根据法务机构及负责人扩展ID 法务机构及负责人-年度法律审核情况列表 查询
	 * @param organizationExtId 法务机构及负责人扩展ID
	 */
	List<TblFwglLegalOrganizationExtOracle> getList(String organizationExtId);
}
