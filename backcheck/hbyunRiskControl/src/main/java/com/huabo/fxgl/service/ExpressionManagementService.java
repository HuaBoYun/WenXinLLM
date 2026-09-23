package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.dto.ExpressionQueryDTO;
import com.huabo.fxgl.dto.ExpressionSaveDTO;
import com.huabo.fxgl.entity.ExpressionManagement;
import com.huabo.fxgl.vo.ExpressionVO;

import java.util.List;
import java.util.Map;

/**
 * 表达式管理服务接口
 * 
 * @author AI Assistant
 * @since 2025-01-21
 */
public interface ExpressionManagementService extends IService<ExpressionManagement> {

    /**
     * 分页查询表达式列表
     * 
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<ExpressionVO> getExpressionPage(ExpressionQueryDTO queryDTO);

    /**
     * 根据ID查询表达式详情
     * 
     * @param expressionId 表达式ID
     * @return 表达式详情
     */
    ExpressionVO getExpressionById(String expressionId);

    /**
     * 保存表达式
     * 
     * @param saveDTO 保存参数
     * @param currentUser 当前用户
     * @return 表达式ID
     */
    String saveExpression(ExpressionSaveDTO saveDTO, String currentUser);

    /**
     * 删除表达式
     * 
     * @param expressionId 表达式ID
     * @return 是否成功
     */
    boolean deleteExpression(String expressionId);

    /**
     * 启用/禁用表达式
     * 
     * @param expressionId 表达式ID
     * @param isEnabled 是否启用
     * @param currentUser 当前用户
     * @return 是否成功
     */
    boolean toggleExpressionStatus(String expressionId, String isEnabled, String currentUser);

    /**
     * 获取数据源列表
     * 
     * @return 数据源列表
     */
    List<Map<String, Object>> getDataSourceList();

    /**
     * 根据数据源ID获取表列表
     * 
     * @param dataSourceId 数据源ID
     * @return 表列表
     */
    List<Map<String, Object>> getTableList(String dataSourceId);

    /**
     * 根据数据源ID和表名获取字段列表
     * 
     * @param dataSourceId 数据源ID
     * @param tableName 表名
     * @return 字段列表
     */
    List<Map<String, Object>> getFieldList(String dataSourceId, String tableName);

    /**
     * 验证字段比对表达式
     * 
     * @param expressionContent 表达式内容
     * @param sourceDataConfig 源数据配置
     * @param targetDataConfig 目标数据配置
     * @param comparisonRules 比对规则
     * @return 验证结果
     */
    Map<String, Object> validateFieldComparisonExpression(String expressionContent, 
                                                          String sourceDataConfig,
                                                          String targetDataConfig, 
                                                          String comparisonRules);

    /**
     * 测试字段比对表达式
     * 
     * @param expressionContent 表达式内容
     * @param testData 测试数据
     * @return 测试结果
     */
    Map<String, Object> testFieldComparisonExpression(String expressionContent, String testData);

    /**
     * 根据比对规则生成SQL
     * 
     * @param sourceDataConfig 源数据配置
     * @param targetDataConfig 目标数据配置
     * @param comparisonRules 比对规则
     * @return 生成的SQL
     */
    Map<String, Object> generateFieldComparisonSQL(String sourceDataConfig, 
                                                   String targetDataConfig, 
                                                   String comparisonRules);

    /**
     * 获取表达式统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getExpressionStatistics();

    /**
     * 批量删除表达式
     *
     * @param expressionIds 表达式ID列表
     * @param currentUser 当前用户
     * @return 是否成功
     */
    boolean batchDeleteExpressions(List<String> expressionIds, String currentUser);

    /**
     * 批量更新表达式状态
     *
     * @param expressionIds 表达式ID列表
     * @param isEnabled 是否启用
     * @param currentUser 当前用户
     * @return 是否成功
     */
    boolean batchUpdateExpressionStatus(List<String> expressionIds, String isEnabled, String currentUser);

    /**
     * 复制表达式
     *
     * @param expressionId 源表达式ID
     * @param newName 新表达式名称
     * @param newCode 新表达式编码
     * @param currentUser 当前用户
     * @return 新表达式ID
     */
    String copyExpression(String expressionId, String newName, String newCode, String currentUser);

    /**
     * 设置表达式为模板
     *
     * @param expressionId 表达式ID
     * @param isTemplate 是否为模板
     * @param currentUser 当前用户
     * @return 是否成功
     */
    boolean setExpressionAsTemplate(String expressionId, String isTemplate, String currentUser);

    /**
     * 获取最近活动记录
     *
     * @return 最近活动列表
     */
    List<Map<String, Object>> getRecentActivity();

    /**
     * 执行表达式测试
     *
     * @param params 测试参数
     * @return 测试结果
     */
    Map<String, Object> executeExpressionTest(Map<String, Object> params);

    /**
     * 校验表达式语法
     *
     * @param expressionContent 表达式内容
     * @return 校验结果
     */
    Map<String, Object> validateExpressionSyntax(String expressionContent);

    /**
     * 保存测试报告
     *
     * @param expressionId 表达式ID
     * @param testResults 测试结果
     * @param testParams 测试参数
     * @return 报告ID
     */
    String saveTestReport(String expressionId, Map<String, Object> testResults, Map<String, Object> testParams);
}
