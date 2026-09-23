package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetMgtOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetMgtQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

import java.util.List;

public interface CeaAssetMgtService {

	/**
	 * 资产管理 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaAssetMgtOracle> getTblCeaAssetMgtList(TblCeaAssetMgtQueryParam param);

	/**
	 * 资产管理 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaAssetMgtOracle> saveOrUpdateTblCeaAssetMgt(TblCeaAssetMgtOracle param);

	/**
	 * 资产管理 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaAssetMgt(Long id);

	/**
	 * 资产管理 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaAssetMgtOracle>> getTblCeaAssetMgt(Long id);

	/**
	 * 批量插入
	 * @param dataList
	 */
	void batchSave(List<TblCeaAssetMgtOracle> dataList);
}
