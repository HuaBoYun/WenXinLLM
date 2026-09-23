package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaNetworkAgentOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaNetworkAgentQueryParam;

public interface TblCeaNetworkAgentOracleService {

	/**
	 * 外网代理服务 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaNetworkAgentOracle> getList(TblCeaNetworkAgentQueryParam param);

	/**
	 * 外网代理服务 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaNetworkAgentOracle saveOrUpdate(TblCeaNetworkAgentOracle param);

	/**
	 * 外网代理服务 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 外网代理服务 详情查询
	 * @param id
	 * @return
	 */
	TblCeaNetworkAgentOracle findById(Long id);
}
