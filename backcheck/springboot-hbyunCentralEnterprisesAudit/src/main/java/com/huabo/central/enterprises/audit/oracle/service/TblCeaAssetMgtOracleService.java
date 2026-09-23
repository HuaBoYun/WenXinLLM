package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetMgtOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetMgtQueryParam;

import java.util.List;

public interface TblCeaAssetMgtOracleService {

	/**
	 * 资产管理 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaAssetMgtOracle> getList(TblCeaAssetMgtQueryParam param);

	/**
	 * 资产管理 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaAssetMgtOracle saveOrUpdate(TblCeaAssetMgtOracle param);

	/**
	 * 资产管理 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 资产管理 详情查询
	 * @param id
	 * @return
	 */
	TblCeaAssetMgtOracle findById(Long id);
}
