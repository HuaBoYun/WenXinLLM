package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglRegisterManagementMySql;
import com.huabo.legal.vo.param.TblFwglRegisterManagementQueryParam;

public interface TblFwglRegisterManagementMySqlService {

	/**
	 * 登记管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglRegisterManagementMySql> getList(TblFwglRegisterManagementQueryParam param);

	/**
	 * 登记管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglRegisterManagementMySql saveOrUpdate(TblFwglRegisterManagementMySql param);

	/**
	 * 登记管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglRegisterManagementMySql findById(Integer id);

	/**
	 * 登记管理 刪除
	 * @param id
	 */
	void delete(Integer id);


}
