package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglConferenceManagementMySql;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;

public interface TblFwglConferenceManagementMySqlService {

	/**
	 * 会议管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglConferenceManagementMySql> getList(TblFwglConferenceManagementQueryParam param);

	/**
	 * 会议管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglConferenceManagementMySql saveOrUpdate(TblFwglConferenceManagementMySql param);

	/**
	 * 会议管理 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 会议管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglConferenceManagementMySql findById(Integer id);
}
