package com.huabo.central.enterprises.audit.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicFirst;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicFirstQueryParam;

import java.util.Map;

public interface TblCeaExamineTopicFirstService {

	/**
	 * 考核题目-首部分 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblCeaExamineTopicFirst> getList(TblCeaExamineTopicFirstQueryParam param);

	/**
	 * 考核题目-首部分 新增/更新
	 * @param param
	 * @return
	 */
	TblCeaExamineTopicFirst saveOrUpdate(TblCeaExamineTopicFirst param);

	/**
	 * 考核题目-首部分 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 考核题目-首部分 详情 查询
	 * @param id
	 * @return
	 */
	TblCeaExamineTopicFirst findById(Long id);

	/**
	 * 考核题目-首部分Map 列表 查询
	 * @return
	 */
	Map<Long, TblCeaExamineTopicFirst> getMap();
}
