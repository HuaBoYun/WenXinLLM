package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpManageOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpManageQueryParam;

public interface TblCeaIpManageOracleService {

	/**
	 * IP地址管理 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaIpManageOracle> getList(TblCeaIpManageQueryParam param);

	/**
	 * IP地址管理 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaIpManageOracle saveOrUpdate(TblCeaIpManageOracle param);

	/**
	 * IP地址管理 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * IP地址管理 详情查询
	 * @param id
	 * @return
	 */
	TblCeaIpManageOracle findById(Long id);
}
