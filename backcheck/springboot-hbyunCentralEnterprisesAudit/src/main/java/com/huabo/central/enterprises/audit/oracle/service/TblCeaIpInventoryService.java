package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpInventory;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpInventoryQueryParam;

public interface TblCeaIpInventoryService {

	/**
	 * IP清单 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaIpInventory> getList(TblCeaIpInventoryQueryParam param);

	/**
	 * IP清单 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaIpInventory saveOrUpdate(TblCeaIpInventory param);

	/**
	 * IP清单 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * IP清单 详情查询
	 * @param id
	 * @return
	 */
	TblCeaIpInventory findById(Long id);
}
