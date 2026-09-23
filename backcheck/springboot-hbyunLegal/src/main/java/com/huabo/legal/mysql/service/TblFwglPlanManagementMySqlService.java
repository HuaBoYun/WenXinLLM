package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglPlanManagementMySql;
import com.huabo.legal.vo.param.TblFwglPlanManagementQueryParam;

public interface TblFwglPlanManagementMySqlService {

	/**
	 * 规划管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPlanManagementMySql> getList(TblFwglPlanManagementQueryParam param);

	/**
	 * 规划管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPlanManagementMySql saveOrUpdate(TblFwglPlanManagementMySql param);

	/**
	 * 规划管理 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 规划管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPlanManagementMySql findById(Integer id);
}
