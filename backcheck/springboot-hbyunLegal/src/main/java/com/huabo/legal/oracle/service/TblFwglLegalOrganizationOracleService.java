package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglLegalOrganizationOracle;
import com.huabo.legal.vo.param.TblFwglLegalOrganizationQueryParam;

public interface TblFwglLegalOrganizationOracleService {

	/**
	 * 法务机构及负责人列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglLegalOrganizationOracle> getList(TblFwglLegalOrganizationQueryParam param);

	/**
	 * 法务机构及负责人 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglLegalOrganizationOracle saveOrUpdate(TblFwglLegalOrganizationOracle param);

	/**
	 * 法务机构及负责人详情 查询
	 * @param id
	 * @return
	 */
	TblFwglLegalOrganizationOracle findById(Long id);

	/**
	 * 法务机构及负责人 刪除
	 * @param id
	 */
	void delete(Long id);
}
