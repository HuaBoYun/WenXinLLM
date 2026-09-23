package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.fxgl.dto.TableQueryDTO;
import java.util.List;
import java.util.Map;

/**
 * 表管理服务接口
 * @author 华博云
 * @since 2025-01-21
 */
public interface ITableService {

    /**
     * 分页查询表列表
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    JsonBean getTableList(TableQueryDTO queryDTO);

    /**
     * 获取数据源表统计信息
     * @param dataSourceId 数据源ID
     * @return 统计信息
     */
    JsonBean getTableStatistics(String dataSourceId);

    /**
     * 同步数据源表结构
     * @param dataSourceId 数据源ID
     * @return 同步结果
     */
    JsonBean syncTableStructures(String dataSourceId);

    /**
     * 删除表结构信息
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @return 删除结果
     */
    JsonBean deleteTableStructure(String dataSourceId, String tableName);

    /**
     * 获取表详细信息（包含列信息）
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @return 表详细信息
     */
    JsonBean getTableDetail(String dataSourceId, String tableName);

    /**
     * 调试表查询结果
     * @param dataSourceId 数据源ID
     * @return 调试信息
     */
    JsonBean debugTableQuery(String dataSourceId);

    /**
     * 获取表的列信息
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @return 列信息列表
     */
    JsonBean getTableColumns(String dataSourceId, String tableName);

    /**
     * 获取表结构详情列表（包含列信息）
     * @param queryDTO 查询参数
     * @return 表结构详情列表
     */
    JsonBean getTableStructureDetails(TableQueryDTO queryDTO);

    /**
     * 预览表数据
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @param limit 限制条数
     * @param offset 偏移量
     * @return 表数据预览
     */
    JsonBean previewTableData(String dataSourceId, String tableName, Integer limit, Integer offset);

    /**
     * 生成表DDL语句
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @return DDL语句
     */
    JsonBean generateTableDDL(String dataSourceId, String tableName);

    /**
     * 分页查询SQL模板列表
     * @param params 查询参数
     * @return 分页结果
     */
    JsonBean getSqlTemplateList(Map<String, Object> params);

    /**
     * 获取SQL模板统计信息
     * @return 统计信息
     */
    JsonBean getSqlTemplateStatistics();

    /**
     * 获取SQL模板详情
     * @param templateId 模板ID
     * @param staff 当前用户
     * @return 模板详情
     */
    String getSqlTemplateDetail(String templateId, TblStaffUtil staff);

    /**
     * 保存SQL模板
     * @param templateData 模板数据
     * @param staff 当前用户
     * @return 保存结果
     */
    String saveSqlTemplate(Map<String, Object> templateData, TblStaffUtil staff);

    /**
     * 删除SQL模板
     * @param templateId 模板ID
     * @param staff 当前用户
     * @return 删除结果
     */
    String deleteSqlTemplate(String templateId, TblStaffUtil staff);

    /**
     * 批量删除SQL模板
     * @param templateIds 模板ID列表
     * @param staff 当前用户
     * @return 删除结果
     */
    String batchDeleteSqlTemplate(List<String> templateIds, TblStaffUtil staff);

    /**
     * 批量更新SQL模板状态
     * @param templateIds 模板ID列表
     * @param status 状态(Y:启用,N:禁用)
     * @param updateUser 更新用户
     * @return 更新结果
     */
    boolean batchUpdateSqlTemplateStatus(List<String> templateIds, String status, String updateUser);

    /**
     * 使用SQL模板（更新使用次数和最后使用时间）
     * @param templateId 模板ID
     * @param staff 当前用户
     * @return 使用结果
     */
    String useSqlTemplate(String templateId, TblStaffUtil staff);

    /**
     * 复制SQL模板
     * @param templateId 源模板ID
     * @param newTemplateName 新模板名称
     * @param staff 当前用户
     * @return 复制结果
     */
    String copySqlTemplate(String templateId, String newTemplateName, TblStaffUtil staff);

    /**
     * 测试SQL语句执行
     * @param dataSourceId 数据源ID
     * @param sqlContent SQL内容
     * @param parameters 参数列表
     * @param staff 当前用户
     * @return 测试结果
     */
    String testSqlExecution(String dataSourceId, String sqlContent, List<Map<String, Object>> parameters, TblStaffUtil staff);

    /**
     * 验证SQL语法
     * @param sqlContent SQL内容
     * @param databaseType 数据库类型
     * @param staff 当前用户
     * @return 验证结果
     */
    JsonBean validateSqlSyntax(String sqlContent, String databaseType, TblStaffUtil staff);
}
