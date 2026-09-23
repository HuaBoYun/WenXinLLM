package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglAnnualExamineMySql;
import com.huabo.legal.vo.param.TblFwglAnnualExamineQueryParam;

public interface TblFwglAnnualExamineMySqlService {

	/**
	 * 年度考核列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglAnnualExamineMySql> getList(TblFwglAnnualExamineQueryParam param);

	/**
	 * 年度考核 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglAnnualExamineMySql saveOrUpdate(TblFwglAnnualExamineMySql param);

	/**
	 * 年度考核 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 年度考核详情 查询
	 * @param id
	 * @return
	 */
	TblFwglAnnualExamineMySql findById(Integer id);

}
