package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglPracticeExamineMySql;
import com.huabo.legal.oracle.entity.TblFwglPracticeExamineOracle;
import com.huabo.legal.vo.param.TblFwglPracticeExamineQueryParam;

public interface TblFwglPracticeExamineOracleService {

	/**
	 * 执业考核列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglPracticeExamineOracle> getList(TblFwglPracticeExamineQueryParam param);

	/**
	 * 执业考核 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPracticeExamineOracle saveOrUpdate(TblFwglPracticeExamineOracle param);

	/**
	 * 执业考核 删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 执业考核详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPracticeExamineOracle findById(Long id);
}
