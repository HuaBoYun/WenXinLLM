package com.huabo.legal.service;

import com.hbfk.util.JsonBean;
import com.huabo.legal.mysql.entity.TblFwglRegisterManagementMySql;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.param.TblFwglRegisterManagementQueryParam;
import com.huabo.legal.vo.result.TblFwglRegisterManagement;

public interface IntellectualPropertyService {

	/**
	 * 登记管理列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglRegisterManagementList(TblFwglRegisterManagementQueryParam param);

	/**
	 * 登记管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblFwglRegisterManagement(TblFwglRegisterManagement param);

	/**
	 * 登记管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblTblFwglRegisterManagement(Long id);

	/**
	 * 登记管理详情 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblFwglRegisterManagement(Long id);

	/**
	 * 登记管理-台账管理列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getTblFwglRegisterManagementAllList(TblFwglRegisterManagementQueryParam param);
}
