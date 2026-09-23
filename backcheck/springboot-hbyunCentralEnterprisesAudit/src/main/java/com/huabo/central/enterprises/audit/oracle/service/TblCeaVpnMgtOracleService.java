package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaVpnMgtOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaVpnMgtQueryParam;

public interface TblCeaVpnMgtOracleService {

	/**
	 * VPN账号管理 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaVpnMgtOracle> getList(TblCeaVpnMgtQueryParam param);

	/**
	 * VPN账号管理 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaVpnMgtOracle saveOrUpdate(TblCeaVpnMgtOracle param);

	/**
	 * VPN账号管理 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * VPN账号管理 详情查询
	 * @param id
	 * @return
	 */
	TblCeaVpnMgtOracle findById(Long id);
}
