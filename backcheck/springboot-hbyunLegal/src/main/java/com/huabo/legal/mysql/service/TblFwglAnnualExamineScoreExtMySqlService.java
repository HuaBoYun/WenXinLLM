package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglAnnualExamineScoreExtMySql;
import com.huabo.legal.mysql.entity.TblFwglAnnualExamineTopicExtMySql;
import com.huabo.legal.vo.param.TblFwglAnnualExamineScoreExtQueryParam;

import java.util.List;

public interface TblFwglAnnualExamineScoreExtMySqlService {

	/**
	 * 年度考核-考核评分列表 查询
	 * @param param
	 * @return
	 */
	List<TblFwglAnnualExamineScoreExtMySql> getList(TblFwglAnnualExamineScoreExtQueryParam param);

	/**
	 * 年度考核-考核评分 批量新增/更新
	 * @param param
	 * @return
	 */
	String saveOrUpdate(List<TblFwglAnnualExamineScoreExtMySql> param);

	/**
	 * 年度考核-考核评分 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 年度考核-考核评分 删除
	 * @param annualExamineId 年度考核ID
	 */
	void deleteTblFwglAnnualExamineScoreExt(Integer annualExamineId);
}
