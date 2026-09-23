package com.huabo.legal.oracle.service;

import com.huabo.legal.oracle.entity.TblFwglFileOracle;

import java.util.List;

public interface TblFwglFileOracleService {

	/**
	 * 新增 法务管理-文件存储详情
	 * @param param
	 * @return
	 */
	TblFwglFileOracle saveOrUpdate(TblFwglFileOracle param);

	/**
	 * 查询 法务管理-文件存储详情
	 * @param id
	 * @return
	 */
	TblFwglFileOracle findById(Long id);

	/**
	 * 批量查询 法务管理-文件存储详情
	 * @param ids
	 * @return
	 */
	List<TblFwglFileOracle> findByIds(String ids);

	/**
	 * 删除 法务管理-文件存储详情
	 * @param fileId
	 */
	void delete(Long fileId);
}
