package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglPlanManagementMySql;
import com.huabo.legal.oracle.entity.TblFwglPlanManagementOracle;
import com.huabo.legal.vo.param.TblFwglPlanManagementQueryParam;

public interface TblFwglPlanManagementOracleService {

	/**
	 * 规划管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPlanManagementOracle> getList(TblFwglPlanManagementQueryParam param);

	/**
	 * 规划管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPlanManagementOracle saveOrUpdate(TblFwglPlanManagementOracle param);

	/**
	 * 规划管理 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 规划管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPlanManagementOracle findById(Long id);
}
