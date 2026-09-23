package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaWeekly;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.RemindTblCeaWeeklyParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaWeeklyQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.huabo.central.enterprises.audit.vo.result.RemindTblCeaWeeklyResult;

public interface CeaWeeklyService {

	/**
	 * 周报 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaWeekly> getTblCeaWeeklyList(TblCeaWeeklyQueryParam param);

	/**
	 * 周报 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaWeekly> saveOrUpdateTblCeaWeekly(TblCeaWeekly param);

	/**
	 * 周报 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaWeekly(Long id);

	/**
	 * 周报 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaWeekly>> getTblCeaWeekly(Long id);

	/**
	 * 周报每周四提醒-指定角色 查询
	 * @param param
	 * @return
	 */
	MyJsonBean<RemindTblCeaWeeklyResult> remindTblCeaWeekly(RemindTblCeaWeeklyParam param);
}
