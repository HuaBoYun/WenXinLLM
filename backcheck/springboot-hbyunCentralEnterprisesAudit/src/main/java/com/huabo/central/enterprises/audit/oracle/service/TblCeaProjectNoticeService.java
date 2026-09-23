package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectNotice;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectNoticeQueryParam;

public interface TblCeaProjectNoticeService {

	/**
	 * 项目评优-通知 列表查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaProjectNotice> getList(TblCeaProjectNoticeQueryParam param);

	/**
	 * 项目评优-通知 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaProjectNotice saveOrUpdate(TblCeaProjectNotice param);

	/**
	 * 项目评优-通知 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 项目评优-通知 详情查询
	 * @param id
	 * @return
	 */
	TblCeaProjectNotice findById(Long id);
}
