package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglPopularizeLawPlanOracle;
import com.huabo.legal.vo.param.TblFwglPopularizeLawPlanQueryParam;

public interface TblFwglPopularizeLawPlanOracleService {

	/**
	 * 普法培训列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPopularizeLawPlanOracle> getList(TblFwglPopularizeLawPlanQueryParam param);

	/**
	 * 普法培训 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPopularizeLawPlanOracle saveOrUpdate(TblFwglPopularizeLawPlanOracle param);

	/**
	 * 普法培训 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 普法培训详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPopularizeLawPlanOracle findById(Long id);
}
