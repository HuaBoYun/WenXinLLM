package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglPracticeApplyExtOracle;

import java.util.List;

public interface TblFwglPracticeApplyExtOracleService {

	/**
	 * 根据执业申请-简历ID 执业申请-简历列表 查询
	 * @param practiceApplyExtId 执业申请-简历ID
	 * @return
	 */
	List<TblFwglPracticeApplyExtOracle> getList(String practiceApplyExtId);

	/**
	 * 执业申请-简历 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPracticeApplyExtOracle saveOrUpdate(TblFwglPracticeApplyExtOracle param);

	/**
	 * 执业申请-简历详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPracticeApplyExtOracle findById(Long id);

	/**
	 * 执业申请-简历 删除
	 * @param id
	 */
	void delete(Long id);

}

