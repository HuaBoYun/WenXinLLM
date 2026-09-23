package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglPopularizeLawPlanMySql;
import com.huabo.legal.vo.param.TblFwglPopularizeLawPlanQueryParam;

public interface TblFwglPopularizeLawPlanMySqlService {

	/**
	 * 普法培训列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPopularizeLawPlanMySql> getList(TblFwglPopularizeLawPlanQueryParam param);

	/**
	 * 普法培训 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPopularizeLawPlanMySql saveOrUpdate(TblFwglPopularizeLawPlanMySql param);

	/**
	 * 普法培训 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 普法培训详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPopularizeLawPlanMySql findById(Integer id);
}
