package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglPracticeExamineMySql;
import com.huabo.legal.vo.param.TblFwglPracticeExamineQueryParam;

public interface TblFwglPracticeExamineMySqlService {

	/**
	 * 执业考核列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPracticeExamineMySql> getList(TblFwglPracticeExamineQueryParam param);

	/**
	 * 执业考核 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPracticeExamineMySql saveOrUpdate(TblFwglPracticeExamineMySql param);

	/**
	 * 执业考核 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 执业考核详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPracticeExamineMySql findById(Integer id);
}
