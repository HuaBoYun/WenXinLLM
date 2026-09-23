package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaNetworkAgentOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaNetworkAgentQueryParam;

public interface NetworkAgentService {

	/**
	 * 外网代理服务 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaNetworkAgentOracle> getTblCeaNetworkAgentList(TblCeaNetworkAgentQueryParam param);

	/**
	 * 外网代理服务 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaNetworkAgentOracle> saveOrUpdateTblCeaNetworkAgent(TblCeaNetworkAgentOracle param);

	/**
	 * 外网代理服务 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaNetworkAgent(Long id);

	/**
	 * 外网代理服务 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaNetworkAgentOracle> getTblCeaNetworkAgent(Long id);
}
