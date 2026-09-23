package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglAnnualExamineOracle;
import com.huabo.legal.vo.param.TblFwglAnnualExamineQueryParam;

public interface TblFwglAnnualExamineOracleService {

	/**
	 * 年度考核列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglAnnualExamineOracle> getList(TblFwglAnnualExamineQueryParam param);

	/**
	 * 年度考核 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglAnnualExamineOracle saveOrUpdate(TblFwglAnnualExamineOracle param);

	/**
	 * 年度考核 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 年度考核详情 查询
	 * @param id
	 * @return
	 */
	TblFwglAnnualExamineOracle findById(Long id);

	/**
	 * 判断题目事务id是否存在
	 * @param topicTransactionId
	 * @return
	 */
	Boolean isTopicTransactionId(String topicTransactionId);
}
