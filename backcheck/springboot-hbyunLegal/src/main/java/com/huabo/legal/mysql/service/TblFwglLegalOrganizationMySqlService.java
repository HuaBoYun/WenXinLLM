package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglLegalOrganizationMySql;
import com.huabo.legal.vo.param.TblFwglLegalOrganizationQueryParam;

public interface TblFwglLegalOrganizationMySqlService {

	/**
	 * 法务机构及负责人列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglLegalOrganizationMySql> getList(TblFwglLegalOrganizationQueryParam param);

	/**
	 * 法务机构及负责人 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalOrganizationMySql saveOrUpdate(TblFwglLegalOrganizationMySql param);

	/**
	 * 法务机构及负责人详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalOrganizationMySql findById(Integer id);

	/**
	 * 法务机构及负责人 刪除
	 * @param id
	 */
	void delete(Integer id);
}
