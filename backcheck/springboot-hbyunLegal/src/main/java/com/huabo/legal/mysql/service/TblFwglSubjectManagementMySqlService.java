package com.huabo.legal.mysql.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.mysql.entity.TblFwglSubjectManagementMySql;
import com.huabo.legal.vo.param.TblFwglSubjectManagementQueryParam;

public interface TblFwglSubjectManagementMySqlService {

	/**
	 * 课题管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglSubjectManagementMySql> getList(TblFwglSubjectManagementQueryParam param);

	/**
	 * 课题管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglSubjectManagementMySql saveOrUpdate(TblFwglSubjectManagementMySql param);

	/**
	 * 课题管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglSubjectManagementMySql findById(Integer id);

	/**
	 * 课题管理 刪除
	 * @param id
	 */
	void delete(Integer id);

}
