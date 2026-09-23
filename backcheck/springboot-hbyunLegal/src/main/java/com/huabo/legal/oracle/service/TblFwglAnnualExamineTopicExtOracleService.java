package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglAnnualExamineTopicExtOracle;
import com.huabo.legal.vo.param.TblFwglAnnualExamineTopicExtBatchAdd;
import com.huabo.legal.vo.param.TblFwglAnnualExamineTopicExtQueryParam;

import java.util.List;

public interface TblFwglAnnualExamineTopicExtOracleService {

	/**
	 * 年度考核-考核题目列表 查询
	 * @param param
	 * @return
	 */
	List<TblFwglAnnualExamineTopicExtOracle> getList(TblFwglAnnualExamineTopicExtQueryParam param);

	/**
	 * 年度考核-考核题目 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglAnnualExamineTopicExtOracle saveOrUpdate(TblFwglAnnualExamineTopicExtOracle param);

	/**
	 * 年度考核-考核题目 新增/更新
	 * @param param
	 * @return
	 */
	void saveOrUpdate(TblFwglAnnualExamineTopicExtBatchAdd param);

	/**
	 * 年度考核-考核题目 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 年度考核-考核题目详情 查询
	 * @param id
	 * @return
	 */
	TblFwglAnnualExamineTopicExtOracle findById(Long id);
}
