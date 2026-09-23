package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetAdjustmentOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetAdjustmentQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

public interface CeaAssetAdjustmentService {

	/**
	 * 资产调剂申请 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaAssetAdjustmentOracle> getTblCeaAssetAdjustmentList(TblCeaAssetAdjustmentQueryParam param);

	/**
	 * 资产调剂申请 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaAssetAdjustmentOracle> saveOrUpdateTblCeaAssetAdjustment(TblCeaAssetAdjustmentOracle param);

	/**
	 * 资产调剂申请 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaAssetAdjustment(Long id);

	/**
	 * 资产调剂申请 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaAssetAdjustmentOracle>> getTblCeaAssetAdjustment(Long id);
}
