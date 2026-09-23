package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExpatriateApplyOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExpatriateApplyQueryParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.UserAllResult;

public interface CeaExpatriateApplyService {

	/**
	 * 外派任务 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApplyList(TblCeaExpatriateApplyQueryParam param);

	/**
	 * 外派任务 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaExpatriateApplyOracle> saveOrUpdateTblCeaExpatriateApply(TblCeaExpatriateApplyOracle param);

	/**
	 * 外派任务 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaExpatriateApply(Long id);

	/**
	 * 外派任务 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApply(Long id);

	/**
	 * 外派人员台账 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApplyAllList(UserAllQueryParam param);
}
