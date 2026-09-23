package com.huabo.finance.service;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.vo.TableDataQueryVo;

/**
 * 数据库表查询工具 Service接口
 * 
 * @author system
 * @since 2025-11-22
 */
public interface TableQueryService {

	/**
	 * 检查表是否存在
	 * 
	 * @param staff 用户信息
	 * @param tableName 表名
	 * @return JsonBean
	 * @throws Exception
	 */
	JsonBean checkTableExists(TblStaffUtil staff, String tableName) throws Exception;
	
	/**
	 * 获取表结构信息(包含表备注、字段信息)
	 * 
	 * @param staff 用户信息
	 * @param tableName 表名
	 * @return JsonBean
	 * @throws Exception
	 */
	JsonBean getTableStructure(TblStaffUtil staff, String tableName) throws Exception;
	
	/**
	 * 查询表数据
	 * 
	 * @param staff 用户信息
	 * @param vo 查询参数
	 * @return JsonBean
	 * @throws Exception
	 */
	JsonBean getTableData(TblStaffUtil staff, TableDataQueryVo vo) throws Exception;
	
	/**
	 * 获取已查询表列表
	 *
	 * @param staff 用户信息
	 * @return JsonBean
	 * @throws Exception
	 */
	JsonBean getQueriedTableList(TblStaffUtil staff) throws Exception;

	/**
	 * 更新表分类
	 *
	 * @param staff 用户信息
	 * @param tableName 表名
	 * @param category 分类名称
	 * @param sortOrder 排序序号
	 * @return JsonBean
	 * @throws Exception
	 */
	JsonBean updateTableCategory(TblStaffUtil staff, String tableName, String category, Integer sortOrder) throws Exception;

}
