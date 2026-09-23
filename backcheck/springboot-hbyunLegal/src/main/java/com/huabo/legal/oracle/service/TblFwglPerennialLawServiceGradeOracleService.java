package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglPerennialLawServiceGradeOracle;

import java.util.List;

public interface TblFwglPerennialLawServiceGradeOracleService {

	/**
	 * 根据 常年法律服务-评分列表 查询
	 * @param gradeId
	 * @return
	 */
	List<TblFwglPerennialLawServiceGradeOracle> getList(String gradeId);

	/**
	 * 常年法律服务-评分 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPerennialLawServiceGradeOracle saveOrUpdate(TblFwglPerennialLawServiceGradeOracle param);

	/**
	 * 常年法律服务-评分详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPerennialLawServiceGradeOracle findById(Long id);

	/**
	 * 常年法律服务-评分 删除
	 * @param id
	 */
	void delete(Long id);

}
