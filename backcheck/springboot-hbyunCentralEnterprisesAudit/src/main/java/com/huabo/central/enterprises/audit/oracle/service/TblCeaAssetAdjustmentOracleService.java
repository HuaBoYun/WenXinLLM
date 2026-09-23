package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetAdjustmentOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetAdjustmentQueryParam;

public interface TblCeaAssetAdjustmentOracleService {

	/**
	 * 资产调剂申请 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaAssetAdjustmentOracle> getList(TblCeaAssetAdjustmentQueryParam param);

	/**
	 * 资产调剂申请 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaAssetAdjustmentOracle saveOrUpdate(TblCeaAssetAdjustmentOracle param);

	/**
	 * 资产调剂申请 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 资产调剂申请 详情查询
	 * @param id
	 * @return
	 */
	TblCeaAssetAdjustmentOracle findById(Long id);
}
