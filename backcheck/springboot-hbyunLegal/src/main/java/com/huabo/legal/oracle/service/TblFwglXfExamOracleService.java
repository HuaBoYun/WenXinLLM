package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglXfExamMySql;
import com.huabo.legal.oracle.entity.TblFwglXfExamOracle;
import com.huabo.legal.vo.param.TblFwglXfExamQueryParam;

public interface TblFwglXfExamOracleService {

	/**
	 * 学法考试列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglXfExamOracle> getList(TblFwglXfExamQueryParam param);

	/**
	 * 学法考试 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglXfExamOracle saveOrUpdate(TblFwglXfExamOracle param);

	/**
	 * 学法考试详情 查询
	 * @param id
	 * @return
	 */
	TblFwglXfExamOracle findById(Long id);

	/**
	 * 学法考试 刪除
	 * @param id
	 */
	void delete(Long id);

}
