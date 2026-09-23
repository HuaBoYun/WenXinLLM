package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglXfExamMySql;
import com.huabo.legal.vo.param.TblFwglXfExamQueryParam;

public interface TblFwglXfExamMySqlService {

	/**
	 * 学法考试列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglXfExamMySql> getList(TblFwglXfExamQueryParam param);

	/**
	 * 学法考试 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglXfExamMySql saveOrUpdate(TblFwglXfExamMySql param);

	/**
	 * 学法考试详情 查询
	 * @param id
	 * @return
	 */
	TblFwglXfExamMySql findById(Integer id);

	/**
	 * 学法考试 刪除
	 * @param id
	 */
	void delete(Integer id);

}
