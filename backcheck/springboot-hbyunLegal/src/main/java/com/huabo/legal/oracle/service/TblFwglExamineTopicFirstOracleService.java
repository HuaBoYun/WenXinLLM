package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglExamineTopicFirstOracle;
import com.huabo.legal.vo.param.TblFwglExamineTopicFirstQueryParam;

import java.util.Map;

public interface TblFwglExamineTopicFirstOracleService {

	/**
	 * 年度考核题目-首部分 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglExamineTopicFirstOracle> getList(TblFwglExamineTopicFirstQueryParam param);

	/**
	 * 年度考核题目-首部分 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglExamineTopicFirstOracle saveOrUpdate(TblFwglExamineTopicFirstOracle param);

	/**
	 * 年度考核题目-首部分 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 年度考核题目-首部分 详情 查询
	 * @param id
	 * @return
	 */
	TblFwglExamineTopicFirstOracle findById(Long id);

	/**
	 * 年度考核题目-首部分Map 列表 查询
	 * @return
	 */
	Map<Long, TblFwglExamineTopicFirstOracle> getMap();
}
