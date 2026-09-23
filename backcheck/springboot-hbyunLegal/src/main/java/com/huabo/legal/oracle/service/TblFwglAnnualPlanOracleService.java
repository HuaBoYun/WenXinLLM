package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglAnnualPlanOracle;
import com.huabo.legal.vo.param.TblFwglAnnualPlanQueryParam;

public interface TblFwglAnnualPlanOracleService {

	/**
	 * 年度计划列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglAnnualPlanOracle> getList(TblFwglAnnualPlanQueryParam param);

	/**
	 * 年度计划 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglAnnualPlanOracle saveOrUpdate(TblFwglAnnualPlanOracle param);

	/**
	 * 年度计划 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 年度计划详情 查询
	 * @param id
	 * @return
	 */
	TblFwglAnnualPlanOracle findById(Long id);
}
