package com.huabo.legal.mysql.service;

import com.huabo.legal.mysql.entity.TblFwglFileMySql;

import java.util.List;

public interface TblFwglFileMySqlService {

	/**
	 * 新增 法务管理-文件存储详情
	 * @param param
	 * @return
	 */
	TblFwglFileMySql saveOrUpdate(TblFwglFileMySql param);

	/**
	 * 查询 法务管理-文件存储详情
	 * @param id
	 * @return
	 */
	TblFwglFileMySql findById(Integer id);

	/**
	 * 批量查询 法务管理-文件存储详情
	 * @param ids
	 * @return
	 */
	List<TblFwglFileMySql> findByIds(String ids);

	/**
	 * 删除 法务管理-文件存储详情
	 * @param fileId
	 */
	void delete(Integer fileId);
}
