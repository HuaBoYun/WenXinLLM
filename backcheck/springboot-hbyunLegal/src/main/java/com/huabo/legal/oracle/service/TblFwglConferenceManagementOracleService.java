package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglConferenceManagementOracle;
import com.huabo.legal.vo.param.TblFwglConferenceManagementQueryParam;

public interface TblFwglConferenceManagementOracleService {

	/**
	 * 会议管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglConferenceManagementOracle> getList(TblFwglConferenceManagementQueryParam param);

	/**
	 * 会议管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglConferenceManagementOracle saveOrUpdate(TblFwglConferenceManagementOracle param);

	/**
	 * 会议管理 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 会议管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglConferenceManagementOracle findById(Long id);
}
