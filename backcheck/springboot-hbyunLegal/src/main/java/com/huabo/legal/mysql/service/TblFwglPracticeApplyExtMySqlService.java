package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglPracticeApplyExtMySql;

import java.util.List;

public interface TblFwglPracticeApplyExtMySqlService {

	/**
	 * 根据执业申请-简历ID 执业申请-简历列表 查询
	 * @param practiceApplyExtId 执业申请-简历ID
	 * @return
	 */
	List<TblFwglPracticeApplyExtMySql> getList(String practiceApplyExtId);

	/**
	 * 执业申请-简历 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPracticeApplyExtMySql saveOrUpdate(TblFwglPracticeApplyExtMySql param);

	/**
	 * 执业申请-简历详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPracticeApplyExtMySql findById(Integer id);

	/**
	 * 执业申请-简历 删除
	 * @param id
	 */
	void delete(Integer id);

}

