package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicFirstOracle;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicOracle;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicSecondOracle;
import com.huabo.legal.vo.param.TblFwglExamineTopicFirstQueryParam;
import com.huabo.legal.vo.param.TblFwglExamineTopicQueryParam;
import com.huabo.legal.vo.param.TblFwglExamineTopicSecondQueryParam;

import java.util.List;

public interface ExamineTopicService {

	/**
	 * 考核题目首部分列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglExamineTopicFirstList(TblFwglExamineTopicFirstQueryParam param);

	/**
	 * 考核题目首部分 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglExamineTopicFirst(TblFwglExamineTopicFirstOracle param);

	/**
	 * 考核题目首部分 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblFwglExamineTopicFirst(Long id);

	/**
	 * 考核题目首部分详情
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglExamineTopicFirst(Long id);

	/**
	 * 考核题目第二部分列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglExamineTopicSecondList(TblFwglExamineTopicSecondQueryParam param);

	/**
	 * 考核题目第二部分列表 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglExamineTopicSecond(List<TblFwglExamineTopicSecondOracle> param);

	/**
	 * 考核题目第二部分 刪除
	 * @param transactionId
	 * @return
	 */
	JsonBean deleteTblFwglExamineTopicSecond(String transactionId);

	/**
	 * 考核题目第二部分详情 查询
	 * @param transactionId
	 * @return
	 */
	JsonBean getTblFwglExamineTopicSecond(String transactionId);

	/**
	 * 考核题目列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglExamineTopicList(TblFwglExamineTopicQueryParam param);

	/**
	 * 考核题目 新增/更新
	 * @param param
	 * @param scoreTransaction
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglExamineTopic(String scoreTransaction, List<TblFwglExamineTopicOracle> param);

	/**
	 * 考核题目 刪除
	 * @param scoreTransaction
	 * @return
	 */
	JsonBean deleteTblFwglExamineTopic(String scoreTransaction);

	/**
	 * 考核题目详情 查询
	 * @param scoreTransaction
	 * @return
	 */
	JsonBean getTblFwglExamineTopic(String scoreTransaction);

	/**
	 * 评分项明细列表 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglExamineTopicSecondInfoList(Long id);

	/**
	 * 考核题目详情列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglExamineTopicInfoList(TblFwglExamineTopicQueryParam param);

	/**
	 * 题目是否被使用 true-是 false-否
	 * @param topicTransactionId
	 * @return
	 */
	JsonBean isTopicTransactionId(String topicTransactionId);

}
