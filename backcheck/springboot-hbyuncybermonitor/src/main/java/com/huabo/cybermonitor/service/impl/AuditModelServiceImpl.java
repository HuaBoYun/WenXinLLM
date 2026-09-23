package com.huabo.cybermonitor.service.impl;


import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.cybermonitor.config.DateBaseConfig;
import com.huabo.cybermonitor.entity.TblAuditModelDataSourceOracle;
import com.huabo.cybermonitor.service.AuditModelService;
import com.huabo.cybermonitor.service.TblAuditModelDataSourceOracleService;
import com.huabo.cybermonitor.util.OracleSqlProperties;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblAuditModelDataSourceQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class AuditModelServiceImpl implements AuditModelService {

	@Resource
	private TblAuditModelDataSourceOracleService tblAuditModelDataSourceOracleService;

	@Resource
	private OracleSqlProperties oracleSqlProperties;

	/**
	 * 数据源管理 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelDataSourceList(TblAuditModelDataSourceQueryParam param) {
		PageInfo<TblAuditModelDataSourceOracle> pageInfo = tblAuditModelDataSourceOracleService.getList(param);
		if (CollectionUtil.isNotEmpty(pageInfo.getTlist())) {
			PageResult<TblAuditModelDataSourceOracle> build = new PageResult<TblAuditModelDataSourceOracle>().build(pageInfo);
			return ResponseFormat.retParam(200, 200, build);
		}
		return ResponseFormat.retParam(200, 200, PageResult.buildNoData());

	}

	/**
	 * 数据源管理 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean saveOrUpdateTblAuditModelDataSource(TblAuditModelDataSourceOracle param) throws Exception {
//		Integer id = param.getId();
		TblAuditModelDataSourceOracle model = tblAuditModelDataSourceOracleService.saveOrUpdate(param);
//			if (id == null && model.getCreateType() == 2) {
//				//查询用户名是否在数据库已存在
//				String getUserSql = "select * from all_users where username='" + model.getDataBaseUsers() + "'";
//				JsonBean jsonBean = oracleSqlProperties.execSql(getUserSql);
//				List<Map<String, Integer>> data = (List<Map<String, Integer>>) jsonBean.getData();
//				if (CollectionUtil.isNotEmpty(data)) {
//					throw new ServiceException(200, "数据库用户已存在数据库中");
//				}
//				// 创建表用户
//				String userSql = "CREATE USER " + model.getDataBaseUsers() + " IDENTIFIED BY " + "\"" + model.getDataBasePassWord() + "\"";
//				oracleSqlProperties.execSql(userSql);
//				//赋予ABC用户创建新表的权限
//				String createSql = "GRANT CREATE TABLE TO " + model.getDataBaseUsers();
//				oracleSqlProperties.execSql(createSql);
//				//给ABC用户赋予权限
//				String sessionSql = "GRANT create session to " + model.getDataBaseUsers();
//				oracleSqlProperties.execSql(sessionSql);
//				//给ABC用户表空间授权 grant connect,resource,dba to crs;
//				String sql = "grant connect,resource to " + model.getDataBaseUsers();
//				oracleSqlProperties.execSql(sql);
//			}
		return ResponseFormat.retParam(200, 200, model);
	}

	/**
	 * 数据源管理 刪除
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean deleteTblAuditModelDataSource(Integer id) {
		tblAuditModelDataSourceOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 数据源管理 查询
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean getTblAuditModelDataSource(Integer id) {
		TblAuditModelDataSourceOracle auditModelDataSource = tblAuditModelDataSourceOracleService.findById(id);
		return ResponseFormat.retParam(200, 200, auditModelDataSource);
	}

	



	
	

	
}
