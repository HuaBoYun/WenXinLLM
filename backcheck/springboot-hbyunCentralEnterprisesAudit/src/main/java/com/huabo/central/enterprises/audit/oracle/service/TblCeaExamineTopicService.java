package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopic;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicQueryParam;

import java.util.List;

public interface TblCeaExamineTopicService {

	/**
	 * 考核题目 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaExamineTopic> getList(TblCeaExamineTopicQueryParam param);

	/**
	 * 考核题目 新增/更新
	 * @param param
	 * @param scoreTransaction
	 * @return
	 */
	void saveOrUpdate(String scoreTransaction, List<TblCeaExamineTopic> param);

	/**
	 * 考核题目 删除
	 * @param scoreTransaction
	 */
	void delete(String scoreTransaction);

	/**
	 * 考核题目 详情 查询
	 * @param scoreTransaction
	 * @return
	 */
	List<TblCeaExamineTopic> findById(String scoreTransaction);

	/**
	 * 根据事务id查询题目列表
	 * @param param
	 * @return
	 */
	List<TblCeaExamineTopic> getTblCeaExamineTopic(TblCeaExamineTopicQueryParam param);
}
