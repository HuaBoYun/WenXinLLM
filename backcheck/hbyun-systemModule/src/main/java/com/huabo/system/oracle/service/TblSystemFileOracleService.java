package com.huabo.system.oracle.service;


import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblSystemFileOracle;

public interface TblSystemFileOracleService {

	/**
	 * 新增 文件存储详情
	 * @param param
	 * @return
	 */
	TblSystemFileOracle saveOrUpdate(TblSystemFileOracle param);

	/**
	 * 查询 文件存储详情
	 * @param id
	 * @return
	 */
	TblSystemFileOracle findById(BigDecimal id);

	/**
	 * 批量查询 文件存储详情
	 * @param ids
	 * @return
	 */
	List<TblSystemFileOracle> findByIds(String ids);

	/**
	 * 删除 文件存储详情
	 * @param fileId
	 */
	void delete(BigDecimal fileId);
}
