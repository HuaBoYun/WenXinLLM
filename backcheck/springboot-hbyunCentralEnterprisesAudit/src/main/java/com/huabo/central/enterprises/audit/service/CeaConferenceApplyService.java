package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaConferenceApplyOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaConferenceApplyQueryParam;

public interface CeaConferenceApplyService {

	/**
	 * 会议申请 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaConferenceApplyOracle> getTblCeaConferenceApplyList(TblCeaConferenceApplyQueryParam param);

	/**
	 * 会议申请 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaConferenceApplyOracle> saveOrUpdateTblCeaConferenceApply(TblCeaConferenceApplyOracle param);

	/**
	 * 会议申请 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaConferenceApply(Long id);

	/**
	 * 会议申请 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<TblCeaConferenceApplyOracle> getTblCeaConferenceApply(Long id);
}
