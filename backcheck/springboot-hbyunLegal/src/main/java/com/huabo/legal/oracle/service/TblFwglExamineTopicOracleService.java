package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicOracle;
import com.huabo.legal.vo.param.TblFwglExamineTopicQueryParam;

import java.util.List;

public interface TblFwglExamineTopicOracleService {

	/**
	 * 年度考核题目 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglExamineTopicOracle> getList(TblFwglExamineTopicQueryParam param);

	/**
	 * 年度考核题目 新增/更新
	 * @param param
	 * @param scoreTransaction
	 * @return
	 */
	void saveOrUpdate(String scoreTransaction, List<TblFwglExamineTopicOracle> param);

	/**
	 * 年度考核题目 删除
	 * @param scoreTransaction
	 */
	void delete(String scoreTransaction);

	/**
	 * 年度考核题目 详情 查询
	 * @param scoreTransaction
	 * @return
	 */
	List<TblFwglExamineTopicOracle> findById(String scoreTransaction);

	/**
	 * 根据事务id查询题目列表
	 * @param param
	 * @return
	 */
	List<TblFwglExamineTopicOracle> getTblFwglExamineTopic(TblFwglExamineTopicQueryParam param);
}
