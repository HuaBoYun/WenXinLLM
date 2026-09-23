package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglPerennialLawServiceGradeMySql;

import java.util.List;

public interface TblFwglPerennialLawServiceGradeMySqlService {

	/**
	 * 根据 常年法律服务-评分列表 查询
	 * @param gradeId
	 * @return
	 */
	List<TblFwglPerennialLawServiceGradeMySql> getList(String gradeId);

	/**
	 * 常年法律服务-评分 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglPerennialLawServiceGradeMySql saveOrUpdate(TblFwglPerennialLawServiceGradeMySql param);

	/**
	 * 常年法律服务-评分详情 查询
	 * @param id
	 * @return
	 */
	TblFwglPerennialLawServiceGradeMySql findById(Integer id);

	/**
	 * 常年法律服务-评分 删除
	 * @param id
	 */
	void delete(Integer id);

}
