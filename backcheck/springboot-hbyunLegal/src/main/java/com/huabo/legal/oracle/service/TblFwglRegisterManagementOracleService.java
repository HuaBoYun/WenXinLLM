package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglRegisterManagementMySql;
import com.huabo.legal.oracle.entity.TblFwglRegisterManagementOracle;
import com.huabo.legal.vo.param.TblFwglRegisterManagementQueryParam;

public interface TblFwglRegisterManagementOracleService {

	/**
	 * 登记管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglRegisterManagementOracle> getList(TblFwglRegisterManagementQueryParam param);

	/**
	 * 登记管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglRegisterManagementOracle saveOrUpdate(TblFwglRegisterManagementOracle param);

	/**
	 * 登记管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglRegisterManagementOracle findById(Long id);

	/**
	 * 登记管理 刪除
	 * @param id
	 */
	void delete(Long id);


}
