package com.huabo.cybermonitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.cybermonitor.entity.TblAuditModelDataSourceOracle;
import com.huabo.cybermonitor.vo.TblAuditModelDataSourceQueryParam;



public interface AuditModelService {

	/**
	 * 数据源管理 列表查询
	 * @param param
	 * @return
	 */
	JsonBean getTblAuditModelDataSourceList(TblAuditModelDataSourceQueryParam param);

	/**
	 * 数据源管理 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdateTblAuditModelDataSource(TblAuditModelDataSourceOracle param) throws Exception;

	/**
	 * 数据源管理 刪除
	 * @param id
	 * @return
	 */
	JsonBean deleteTblAuditModelDataSource(Integer id);

	/**
	 * 数据源管理 查询
	 * @param id
	 * @return
	 */
	JsonBean getTblAuditModelDataSource(Integer id);

	 
}
