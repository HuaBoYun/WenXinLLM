package com.huabo.compliance.oracle.service;

import com.huabo.compliance.oracle.entity.TblComplianceFileOracle;

import java.util.List;

public interface TblComplianceFileOracleService {

	/**
	 * 新增 法务管理-文件存储详情
	 * @param param
	 * @return
	 */
	TblComplianceFileOracle saveOrUpdate(TblComplianceFileOracle param);

	/**
	 * 查询 法务管理-文件存储详情
	 * @param id
	 * @return
	 */
	TblComplianceFileOracle findById(Integer id);

	/**
	 * 批量查询 法务管理-文件存储详情
	 * @param ids
	 * @return
	 */
	List<TblComplianceFileOracle> findByIds(String ids);

	/**
	 * 删除 法务管理-文件存储详情
	 * @param fileId
	 */
	void delete(Integer fileId);
}
