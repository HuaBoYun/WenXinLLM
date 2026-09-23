package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicSecond;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicSecondQueryParam;

import java.util.List;

public interface TblCeaExamineTopicSecondService {

	/**
	 * 考核题目-第二部分 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaExamineTopicSecond> getList(TblCeaExamineTopicSecondQueryParam param);

	/**
	 * 考核题目-第二部分 新增/更新
	 * @param param
	 * @return
	 */
	void saveOrUpdate(List<TblCeaExamineTopicSecond> param);

	/**
	 * 考核题目-第二部分 删除
	 * @param transactionId
	 */
	void delete(String transactionId);

	/**
	 * 考核题目-第二部分 详情 查询
	 * @param transactionId
	 * @return
	 */
	List<TblCeaExamineTopicSecond> findById(String transactionId);

	/**
	 * 根据题目-首部主键ID 查询评分项列表
	 * @param id
	 * @return
	 */
	List<TblCeaExamineTopicSecond> getTblCeaExamineTopicSecond(Long id);
}
