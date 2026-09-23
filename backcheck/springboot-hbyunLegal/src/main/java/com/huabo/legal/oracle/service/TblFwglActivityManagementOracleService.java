package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglActivityManagementOracle;
import com.huabo.legal.vo.param.TblFwglActivityManagementQueryParam;

public interface TblFwglActivityManagementOracleService {

	/**
	 * 活动管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglActivityManagementOracle> getList(TblFwglActivityManagementQueryParam param);

	/**
	 * 活动管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglActivityManagementOracle saveOrUpdate(TblFwglActivityManagementOracle param);

	/**
	 * 活动管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglActivityManagementOracle findById(Long id);

	/**
	 * 活动管理 刪除
	 * @param id
	 */
	void delete(Long id);

}
