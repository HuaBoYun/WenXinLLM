package com.huabo.central.enterprises.audit.service;

import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSupervisionNoticeOracle;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSupervisionNoticeQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

public interface CeaSupervisionNotice {

	/**
	 * 督办通知单 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaSupervisionNoticeOracle> getTblCeaSupervisionNoticeList(TblCeaSupervisionNoticeQueryParam param);

	/**
	 * 督办通知单 新增/更新
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaSupervisionNoticeOracle> saveOrUpdateTblCeaSupervisionNotice(TblCeaSupervisionNoticeOracle param);

	/**
	 * 督办通知单 刪除
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> deleteTblCeaSupervisionNotice(Long id);

	/**
	 * 督办通知单 详情 查询
	 * @param id
	 * @return
	 */
	MyJsonBean<FileVo<TblCeaSupervisionNoticeOracle>> getTblCeaSupervisionNotice(Long id);
	
	/**
	 * 督办通知办理 列表查询
	 * @param param
	 * @return
	 */
	MyJsonBean<TblCeaSupervisionNoticeOracle> getBlList(TblCeaSupervisionNoticeQueryParam param);
	
	/**
	 * 督办通知办理-提交
	 * @param id
	 * @return
	 */
	MyJsonBean<Void> saveBlSubmit(Long id);

	
}
