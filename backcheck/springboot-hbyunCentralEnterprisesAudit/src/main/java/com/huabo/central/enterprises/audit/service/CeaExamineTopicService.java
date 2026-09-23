package com.huabo.central.enterprises.audit.service;

import com.hbfk.util.JsonBean;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopic;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicFirst;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicSecond;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicFirstQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicSecondQueryParam;

import java.util.List;

public interface CeaExamineTopicService {

	/**
	 * 考核题目首部分列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblCeaExamineTopicFirstList(TblCeaExamineTopicFirstQueryParam param);

	/**
	 * 考核题目首部分 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblCeaExamineTopicFirst(TblCeaExamineTopicFirst param);

	/**
	 * 考核题目首部分 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblCeaExamineTopicFirst(Long id);

	/**
	 * 考核题目首部分详情
	 * @param id
	 * @return
	 */
	JsonBean getTblCeaExamineTopicFirst(Long id);

	/**
	 * 考核题目第二部分列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblCeaExamineTopicSecondList(TblCeaExamineTopicSecondQueryParam param);

	/**
	 * 考核题目第二部分列表 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblCeaExamineTopicSecond(List<TblCeaExamineTopicSecond> param);

	/**
	 * 考核题目第二部分 刪除
	 * @param transactionId
	 * @return
	 */
	JsonBean deleteTblCeaExamineTopicSecond(String transactionId);

	/**
	 * 考核题目第二部分详情 查询
	 * @param transactionId
	 * @return
	 */
	JsonBean getTblCeaExamineTopicSecond(String transactionId);

	/**
	 * 考核题目列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblCeaExamineTopicList(TblCeaExamineTopicQueryParam param);

	/**
	 * 考核题目 新增/更新
	 * @param param
	 * @param scoreTransaction
	 * @return
	 */
	JsonBean saveOrUpdateTblCeaExamineTopic(String scoreTransaction, List<TblCeaExamineTopic> param);

	/**
	 * 考核题目 刪除
	 * @param scoreTransaction
	 * @return
	 */
	JsonBean deleteTblCeaExamineTopic(String scoreTransaction);

	/**
	 * 考核题目详情 查询
	 * @param scoreTransaction
	 * @return
	 */
	JsonBean getTblCeaExamineTopic(String scoreTransaction);

	/**
	 * 评分项明细列表 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblCeaExamineTopicSecondInfoList(Long id);

	/**
	 * 考核题目详情列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblCeaExamineTopicInfoList(TblCeaExamineTopicQueryParam param);
}
