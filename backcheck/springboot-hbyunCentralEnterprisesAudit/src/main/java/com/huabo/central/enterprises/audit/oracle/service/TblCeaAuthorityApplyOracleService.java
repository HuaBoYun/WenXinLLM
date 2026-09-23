package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAuthorityApplyOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAuthorityApplyQueryParam;

public interface TblCeaAuthorityApplyOracleService {

	/**
	 * 权限申请 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaAuthorityApplyOracle> getList(TblCeaAuthorityApplyQueryParam param);

	/**
	 * 权限申请 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaAuthorityApplyOracle saveOrUpdate(TblCeaAuthorityApplyOracle param);

	/**
	 * 权限申请 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 权限申请 详情查询
	 * @param id
	 * @return
	 */
	TblCeaAuthorityApplyOracle findById(Long id);
}
