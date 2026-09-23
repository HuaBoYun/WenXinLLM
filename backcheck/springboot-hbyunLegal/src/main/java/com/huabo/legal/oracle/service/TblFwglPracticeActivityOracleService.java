package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglPracticeActivityOracle;
import com.huabo.legal.vo.param.TblFwglPracticeActivityQueryParam;

import java.util.List;

public interface TblFwglPracticeActivityOracleService {

	/**
	 * 执业活动列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPracticeActivityOracle> getList(TblFwglPracticeActivityQueryParam param);

	/**
	 * 执业活动 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPracticeActivityOracle saveOrUpdate(TblFwglPracticeActivityOracle param);

	/**
	 * 执业活动 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 执业活动详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPracticeActivityOracle findById(Long id);

	/**
	 * 根据人员ID,类型 查询执业活动列表
	 * @param staffId
	 * @param type 1-文章发表 2-法律审核 3-法律尽调
	 */
	List<TblFwglPracticeActivityOracle> getPracticeActivityList(Long staffId, Integer type);

}
