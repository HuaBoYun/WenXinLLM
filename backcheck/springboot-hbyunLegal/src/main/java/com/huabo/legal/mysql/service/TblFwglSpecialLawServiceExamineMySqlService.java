package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglSpecialLawServiceExamineMySql;

import java.util.List;

public interface TblFwglSpecialLawServiceExamineMySqlService {

	/**
	 * 根据 专项法律服务-考核列表 查询
	 * @param examineId
	 * @return
	 */
	List<TblFwglSpecialLawServiceExamineMySql> getList(String examineId);

	/**
	 * 专项法律服务-考核 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglSpecialLawServiceExamineMySql saveOrUpdate(TblFwglSpecialLawServiceExamineMySql param);

	/**
	 * 专项法律服务-考核详情 查询
	 * @param id
	 * @return
	 */
	TblFwglSpecialLawServiceExamineMySql findById(Integer id);

	/**
	 * 专项法律服务-考核 删除
	 * @param id
	 */
	void delete(Integer id);
}
