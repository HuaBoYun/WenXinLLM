package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectNoticeExt;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectNoticeExtQueryParam;

import java.util.List;

public interface TblCeaProjectNoticeExtService {

	/**
	 * 项目评优-通知-下发 列表查询
	 * @param projectNoticeId
	 * @return
	 */
	List<TblCeaProjectNoticeExt> getList(Long projectNoticeId);

	/**
	 * 项目评优-通知-下发 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaProjectNoticeExt saveOrUpdate(TblCeaProjectNoticeExt param);

	/**
	 * 项目评优-通知-下发 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 项目评优-通知-下发 详情查询
	 * @param id
	 * @return
	 */
	TblCeaProjectNoticeExt findById(Long id);

	/**
	 * 项目评优-通知-下发-批量新增
	 * @param projectNoticeId
	 * @param param
	 */
	void updatesTblCeaProjectNoticeExt(Long projectNoticeId, List<TblCeaProjectNoticeExt> param);

	/**
	 * 项目评优-通知-首页展示 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaProjectNoticeExt> getTblCeaProjectNoticeExtHomeList(TblCeaProjectNoticeExtQueryParam param);

	/**
	 * 项目评优-通知-首页展示-同意
	 * @param id
	 */
	void submitTblCeaProjectNoticeExtHomeList(Long id);
}
