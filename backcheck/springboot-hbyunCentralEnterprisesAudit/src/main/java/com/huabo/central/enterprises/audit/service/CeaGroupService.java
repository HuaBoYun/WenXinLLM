package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaGroupOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaGroupQueryParam;

public interface CeaGroupService {

	/**
	 * 群组 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaGroupOracle> getTblCeaGroupList(TblCeaGroupQueryParam param);

	/**
	 * 群组 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaGroupOracle> saveOrUpdateTblCeaGroup(TblCeaGroupOracle param);

	/**
	 * 群组 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaGroup(Long id);

	/**
	 * 群组 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaGroupOracle> getTblCeaGroup(Long id);

}
