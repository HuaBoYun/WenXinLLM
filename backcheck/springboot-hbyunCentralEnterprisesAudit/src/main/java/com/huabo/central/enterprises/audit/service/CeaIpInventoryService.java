package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetMgtOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpInventory;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpInventoryQueryParam;
import com.huabo.central.enterprises.audit.vo.result.ExportTblCeaIpInventory;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

import java.util.List;

public interface CeaIpInventoryService {

	/**
	 * IP清单 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaIpInventory> getTblCeaIpInventoryList(TblCeaIpInventoryQueryParam param);

	/**
	 * IP清单 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaIpInventory> saveOrUpdateTblCeaIpInventory(TblCeaIpInventory param);

	/**
	 * IP清单 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaIpInventory(Long id);

	/**
	 * IP清单 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaIpInventory>> getTblCeaIpInventory(Long id);

	/**
	 * 批量插入
	 * @param dataList
	 */
	void batchSave(List<ExportTblCeaIpInventory> dataList);
}
