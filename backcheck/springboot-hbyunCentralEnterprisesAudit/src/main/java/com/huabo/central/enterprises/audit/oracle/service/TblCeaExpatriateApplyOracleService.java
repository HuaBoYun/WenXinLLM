package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExpatriateApplyOracle;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExpatriateApplyQueryParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.UserAllResult;

public interface TblCeaExpatriateApplyOracleService {

	/**
	 * 外派任务 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaExpatriateApplyOracle> getList(TblCeaExpatriateApplyQueryParam param);

	/**
	 * 外派任务 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaExpatriateApplyOracle saveOrUpdate(TblCeaExpatriateApplyOracle param);

	/**
	 * 外派任务 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 外派任务 详情查询
	 * @param id
	 * @return
	 */
	TblCeaExpatriateApplyOracle findById(Long id);

	/**
	 * 外派人员台账 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApplyAllList(UserAllQueryParam param);
}
