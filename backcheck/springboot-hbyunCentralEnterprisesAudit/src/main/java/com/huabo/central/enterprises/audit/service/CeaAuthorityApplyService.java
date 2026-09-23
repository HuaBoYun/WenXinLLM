package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAuthorityApplyOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAuthorityApplyQueryParam;

public interface CeaAuthorityApplyService {

	/**
	 * 权限申请 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaAuthorityApplyOracle> getTblCeaAuthorityApplyList(TblCeaAuthorityApplyQueryParam param);

	/**
	 * 权限申请 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaAuthorityApplyOracle> saveOrUpdateTblCeaAuthorityApply(TblCeaAuthorityApplyOracle param);

	/**
	 * 权限申请 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaAuthorityApply(Long id);

	/**
	 * 权限申请 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaAuthorityApplyOracle> getTblCeaAuthorityApply(Long id);
}
