package com.huabo.legal.oracle.service;


import com.huabo.legal.oracle.entity.TblFwglAnnualExamineScoreExtOracle;
import com.huabo.legal.vo.param.TblFwglAnnualExamineScoreExtFileParam;
import com.huabo.legal.vo.param.TblFwglAnnualExamineScoreExtQueryParam;

import java.util.List;

public interface TblFwglAnnualExamineScoreExtOracleService {

	/**
	 * 年度考核-考核评分列表 查询
	 * @param param
	 * @return
	 */
	List<TblFwglAnnualExamineScoreExtOracle> getList(TblFwglAnnualExamineScoreExtQueryParam param);

	/**
	 * 年度考核-考核评分 批量新增/更新
	 * @param param
	 * @return
	 */
	String saveOrUpdate(List<TblFwglAnnualExamineScoreExtOracle> param);

	/**
	 * 年度考核-考核评分-附件-确定按钮
	 * @param param
	 * @return
	 */
	TblFwglAnnualExamineScoreExtOracle saveOrUpdate(TblFwglAnnualExamineScoreExtFileParam param);

	/**
	 * 年度考核-考核评分 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 年度考核-考核评分 删除
	 * @param annualExamineId 年度考核ID
	 */
	void deleteTblFwglAnnualExamineScoreExt(Long annualExamineId);

	/**
	 * 查询
	 * @param id
	 * @return
	 */
	TblFwglAnnualExamineScoreExtOracle findById(Long id);
}
