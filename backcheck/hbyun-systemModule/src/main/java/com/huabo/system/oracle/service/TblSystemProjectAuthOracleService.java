package com.huabo.system.oracle.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.system.entity.TblSystemProjectAuthOracle;
import com.huabo.system.vo.param.TblSystemProjectAuthParam;
import com.huabo.system.vo.param.TblSystemProjectAuthQueryParam;

public interface TblSystemProjectAuthOracleService {

	/**
	 * 系统项目授权列表 查询
	 * @return
	 */
	List<TblSystemProjectAuthOracle> getList(TblSystemProjectAuthQueryParam param);

	/**
	 * 系统项目授权 新增
	 * @param param
	 * @return
	 */
	void saveOrUpdate(TblSystemProjectAuthParam param);

	/**
	 * 系统项目授权 详情 查询
	 * @param id
	 * @return
	 */
	TblSystemProjectAuthOracle findById(BigDecimal id);

	/**
	 * 系统项目授权 刪除
	 * @param id
	 */
	void delete(BigDecimal id);

	/**
	 * 删除系统项目相关授权
	 * @param projectId
	 */
	void deleteProjectAuth(BigDecimal projectId);
}
