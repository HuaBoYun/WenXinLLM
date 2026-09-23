package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaVpnMgtOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaVpnMgtQueryParam;

public interface CeaVpnMgtService {

	/**
	 * VPN账号管理 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaVpnMgtOracle> getTblCeaVpnMgtList(TblCeaVpnMgtQueryParam param);

	/**
	 * VPN账号管理 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaVpnMgtOracle> saveOrUpdateTblCeaVpnMgt(TblCeaVpnMgtOracle param);

	/**
	 * VPN账号管理 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaVpnMgt(Long id);

	/**
	 * VPN账号管理 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaVpnMgtOracle> getTblCeaVpnMgt(Long id);
}
