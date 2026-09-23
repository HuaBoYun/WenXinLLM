package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglActivityManagementMySql;
import com.huabo.legal.vo.param.TblFwglActivityManagementQueryParam;

public interface TblFwglActivityManagementMySqlService {

	/**
	 * 活动管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglActivityManagementMySql> getList(TblFwglActivityManagementQueryParam param);

	/**
	 * 活动管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglActivityManagementMySql saveOrUpdate(TblFwglActivityManagementMySql param);

	/**
	 * 活动管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglActivityManagementMySql findById(Integer id);

	/**
	 * 活动管理 刪除
	 * @param id
	 */
	void delete(Integer id);

}
