package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicSecondOracle;
import com.huabo.legal.vo.param.TblFwglExamineTopicSecondQueryParam;

import java.util.List;

public interface TblFwglExamineTopicSecondOracleService {

	/**
	 * 年度考核题目-第二部分 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglExamineTopicSecondOracle> getList(TblFwglExamineTopicSecondQueryParam param);

	/**
	 * 年度考核题目-第二部分 新增/更新
	 * @param param
	 * @return
	 */
	void saveOrUpdate(List<TblFwglExamineTopicSecondOracle> param);

	/**
	 * 年度考核题目-第二部分 删除
	 * @param transactionId
	 */
	void delete(String transactionId);

	/**
	 * 年度考核题目-第二部分 详情 查询
	 * @param transactionId
	 * @return
	 */
	List<TblFwglExamineTopicSecondOracle> findById(String transactionId);

	/**
	 * 根据题目-首部主键ID 查询评分项列表
	 * @param id
	 * @return
	 */
	List<TblFwglExamineTopicSecondOracle> getTblFwglExamineTopicSecond(Long id);
}
