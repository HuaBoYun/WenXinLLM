package com.huabo.finance.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.huabo.finance.dto.ColumnInfoDto;
import com.huabo.finance.dto.TableExistsDto;
import com.huabo.finance.dto.TableInfoDto;

/**
 * 数据库表查询工具 Mapper接口
 * 
 * @author system
 * @since 2025-11-22
 */
public interface TableQueryMapper {

	/**
	 * 检查表是否存在
	 * 
	 * @param tableName 表名
	 * @return TableExistsDto
	 */
	@Select("SELECT " +
			"  t.TABLE_NAME AS tableName, " +
			"  CASE WHEN t.TABLE_NAME IS NOT NULL THEN 1 ELSE 0 END AS \"exists\", " +
			"  c.COMMENTS AS tableComment " +
			"FROM USER_TABLES t " +
			"LEFT JOIN USER_TAB_COMMENTS c ON t.TABLE_NAME = c.TABLE_NAME " +
			"WHERE t.TABLE_NAME = #{tableName}")
	TableExistsDto checkTableExists(@Param("tableName") String tableName);
	
	/**
	 * 获取表备注
	 * 
	 * @param tableName 表名
	 * @return 表备注
	 */
	@Select("SELECT COMMENTS FROM USER_TAB_COMMENTS WHERE TABLE_NAME = #{tableName}")
	String getTableComment(@Param("tableName") String tableName);
	
	/**
	 * 获取表字段信息
	 * 
	 * @param tableName 表名
	 * @return 字段信息列表
	 */
	@Select("SELECT " +
			"  c.COLUMN_NAME AS columnName, " +
			"  cc.COMMENTS AS columnComment, " +
			"  c.DATA_TYPE AS dataType, " +
			"  c.NULLABLE AS nullable " +
			"FROM USER_TAB_COLUMNS c " +
			"LEFT JOIN USER_COL_COMMENTS cc ON c.TABLE_NAME = cc.TABLE_NAME AND c.COLUMN_NAME = cc.COLUMN_NAME " +
			"WHERE c.TABLE_NAME = #{tableName} " +
			"ORDER BY c.COLUMN_ID")
	List<ColumnInfoDto> getTableColumns(@Param("tableName") String tableName);
	
	/**
	 * 查询表数据
	 * 注意: 此方法使用动态SQL,需要在Service层构建SQL
	 * 
	 * @param sql 动态SQL
	 * @return 数据列表
	 */
	List<Map<String, Object>> queryTableData(@Param("sql") String sql);
	
	/**
	 * 查询表数据总数
	 * 注意: 此方法使用动态SQL,需要在Service层构建SQL
	 * 
	 * @param sql 动态SQL
	 * @return 总数
	 */
	Long queryTableDataCount(@Param("sql") String sql);
	
	/**
	 * 获取已查询表列表(从查询历史表)
	 * 
	 * @param userId 用户ID
	 * @return 表列表
	 */
	List<TableInfoDto> getQueriedTableList(@Param("userId") String userId);
	
	/**
	 * 保存查询历史
	 *
	 * @param userId 用户ID
	 * @param tableName 表名
	 * @param tableComment 表备注
	 */
	void saveQueryHistory(@Param("userId") String userId,
	                      @Param("tableName") String tableName,
	                      @Param("tableComment") String tableComment);

	/**
	 * 更新表分类
	 *
	 * @param userId 用户ID
	 * @param tableName 表名
	 * @param category 分类名称
	 * @param sortOrder 排序序号
	 */
	void updateTableCategory(@Param("userId") String userId,
	                         @Param("tableName") String tableName,
	                         @Param("category") String category,
	                         @Param("sortOrder") Integer sortOrder);

}

