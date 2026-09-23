package com.huabo.cybermonitor.service;


import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.TblAuditModelDataSourceOracle;
import com.huabo.cybermonitor.vo.TblAuditModelDataSourceQueryParam;


public interface TblAuditModelDataSourceOracleService {

	/**
	 * 审计-数据源管理 列表 查询
	 * @param param
	 * @return
	 */
	PageInfo<TblAuditModelDataSourceOracle> getList(TblAuditModelDataSourceQueryParam param);

	/**
	 * 审计-数据源管理 新增/更新
	 * @param param
	 * @return
	 */
	TblAuditModelDataSourceOracle saveOrUpdate(TblAuditModelDataSourceOracle param);

	/**
	 * 审计-数据源管理 删除
	 * @param id
	 */
	void delete(Integer id);

	/**
	 * 审计-数据源管理 详情 查询
	 * @param id
	 * @return
	 */
	TblAuditModelDataSourceOracle findById(Integer id);
	
	
	/**
	 * SQL执行
	 * @param token
	 * @param rule
	 * @return
	 * @throws Exception
	 */
	public JsonBean zxsql(String token,  MonitorRule rule) throws Exception; 
	
	
	/**
	 * 查询执行记录
	 * @param token
	 * @param stepId
	 * @return
	 * @throws Exception
	 */
	public JsonBean getxjjgList(String token, BigDecimal stepId) throws Exception;
	
	
	/**
	 * 查询sql执行结果
	 * @param token
	 * @param resultid
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public JsonBean getDatelistt(String token, BigDecimal resultid,MonitorRule rule,Integer pageNumber,Integer pageSize) throws Exception;

	
}
