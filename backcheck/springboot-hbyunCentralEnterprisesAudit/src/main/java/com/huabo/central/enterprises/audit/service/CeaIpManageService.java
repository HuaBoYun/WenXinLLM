package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpManageOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpManageQueryParam;

public interface CeaIpManageService {

	/**
	 * IP地址管理 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaIpManageOracle> getTblCeaIpManageList(TblCeaIpManageQueryParam param);

	/**
	 * IP地址管理 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaIpManageOracle> saveOrUpdateTblCeaIpManage(TblCeaIpManageOracle param);

	/**
	 * IP地址管理 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaIpManage(Long id);

	/**
	 * IP地址管理 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaIpManageOracle> getTblCeaIpManage(Long id);
}
