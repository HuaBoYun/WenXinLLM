package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglPracticeActivityMySql;
import com.huabo.legal.vo.param.TblFwglPracticeActivityQueryParam;

import java.util.List;

public interface TblFwglPracticeActivityMySqlService {

	/**
	 * 执业活动列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPracticeActivityMySql> getList(TblFwglPracticeActivityQueryParam param);

	/**
	 * 执业活动 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPracticeActivityMySql saveOrUpdate(TblFwglPracticeActivityMySql param);

	/**
	 * 执业活动 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 执业活动详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPracticeActivityMySql findById(Integer id);

	/**
	 * 根据人员ID,类型 查询执业活动列表
	 * @param staffId
	 * @param type 1-文章发表 2-法律培训 3-法律尽调
	 */
	List<TblFwglPracticeActivityMySql> getPracticeActivityList(Integer staffId, Integer type);

}
