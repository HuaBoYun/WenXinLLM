package com.huabo.legal.oracle.service;

import com.github.pagehelper.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglSubjectManagementOracle;
import com.huabo.legal.vo.param.TblFwglSubjectManagementQueryParam;

public interface TblFwglSubjectManagementOracleService {

	/**
	 * 课题管理列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblFwglSubjectManagementOracle> getList(TblFwglSubjectManagementQueryParam param);

	/**
	 * 课题管理 新增/更新
	 * @param param
	 * @return
	 */
	TblFwglSubjectManagementOracle saveOrUpdate(TblFwglSubjectManagementOracle param);

	/**
	 * 课题管理详情 查询
	 * @param id
	 * @return
	 */
	TblFwglSubjectManagementOracle findById(Long id);

	/**
	 * 课题管理 刪除
	 * @param id
	 */
	void delete(Long id);

}
