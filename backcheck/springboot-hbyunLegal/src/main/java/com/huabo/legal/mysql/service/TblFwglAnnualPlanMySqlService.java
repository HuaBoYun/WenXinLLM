package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglAnnualPlanMySql;
import com.huabo.legal.vo.param.TblFwglAnnualPlanQueryParam;

public interface TblFwglAnnualPlanMySqlService {

	/**
	 * 年度计划列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglAnnualPlanMySql> getList(TblFwglAnnualPlanQueryParam param);

	/**
	 * 年度计划 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglAnnualPlanMySql saveOrUpdate(TblFwglAnnualPlanMySql param);

	/**
	 * 年度计划 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 年度计划详情 查询
	 * @param id
	 * @return
	 */
	TblFwglAnnualPlanMySql findById(Integer id);
}
