package com.management.accountant.service.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.eps.EpsBudgetFormula;

import java.util.List;
import java.util.Map;

/**
 * 预算公式计算服务接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
public interface EpsBudgetFormulaService {

    /**
     * 分页查询预算公式
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param formulaName 公式名称
     * @param formulaType 公式类型
     * @param systemId 预算体系ID
     * @param status 状态
     * @return 分页结果
     */
    IPage<EpsBudgetFormula> queryBudgetFormulaPage(Long current, Long size, String formulaName, 
                                                  String formulaType, Long systemId, String status);

    /**
     * 创建预算公式
     * 
     * @param budgetFormula 预算公式
     * @return 创建结果
     */
    boolean createBudgetFormula(EpsBudgetFormula budgetFormula);

    /**
     * 更新预算公式
     * 
     * @param budgetFormula 预算公式
     * @return 更新结果
     */
    boolean updateBudgetFormula(EpsBudgetFormula budgetFormula);

    /**
     * 删除预算公式
     * 
     * @param formulaId 公式ID
     * @return 删除结果
     */
    boolean deleteBudgetFormula(Long formulaId);

    /**
     * 根据ID查询预算公式详情
     * 
     * @param formulaId 公式ID
     * @return 预算公式
     */
    EpsBudgetFormula getBudgetFormulaById(Long formulaId);

    /**
     * 验证公式语法
     * 
     * @param formulaExpression 公式表达式
     * @param formulaType 公式类型
     * @return 验证结果
     */
    Map<String, Object> validateFormulaExpression(String formulaExpression, String formulaType);

    /**
     * 计算公式结果
     * 
     * @param formulaId 公式ID
     * @param parameters 计算参数
     * @return 计算结果
     */
    Map<String, Object> calculateFormulaResult(Long formulaId, Map<String, Object> parameters);

    /**
     * 批量计算公式
     * 
     * @param formulaIds 公式ID列表
     * @param parameters 计算参数
     * @return 计算结果列表
     */
    List<Map<String, Object>> batchCalculateFormulas(List<Long> formulaIds, Map<String, Object> parameters);

    /**
     * 测试公式计算
     * 
     * @param formulaExpression 公式表达式
     * @param testParameters 测试参数
     * @return 测试结果
     */
    Map<String, Object> testFormulaCalculation(String formulaExpression, Map<String, Object> testParameters);

    /**
     * 获取公式依赖关系
     * 
     * @param formulaId 公式ID
     * @return 依赖关系
     */
    Map<String, Object> getFormulaDependencies(Long formulaId);

    /**
     * 根据预算体系查询公式
     * 
     * @param systemId 预算体系ID
     * @return 公式列表
     */
    List<EpsBudgetFormula> getBudgetFormulasBySystemId(Long systemId);

    /**
     * 根据公式类型查询公式
     * 
     * @param formulaType 公式类型
     * @param systemId 预算体系ID
     * @return 公式列表
     */
    List<EpsBudgetFormula> getBudgetFormulasByType(String formulaType, Long systemId);

    /**
     * 复制公式
     * 
     * @param sourceFormulaId 源公式ID
     * @param targetFormulaCode 目标公式编码
     * @param targetFormulaName 目标公式名称
     * @return 复制结果
     */
    boolean copyBudgetFormula(Long sourceFormulaId, String targetFormulaCode, String targetFormulaName);

    /**
     * 激活公式
     * 
     * @param formulaId 公式ID
     * @return 激活结果
     */
    boolean activateBudgetFormula(Long formulaId);

    /**
     * 停用公式
     * 
     * @param formulaId 公式ID
     * @return 停用结果
     */
    boolean deactivateBudgetFormula(Long formulaId);

    /**
     * 获取公式变量列表
     * 
     * @param systemId 预算体系ID
     * @param variableType 变量类型
     * @return 变量列表
     */
    List<Map<String, Object>> getFormulaVariables(Long systemId, String variableType);

    /**
     * 获取公式函数列表
     * 
     * @param functionCategory 函数分类
     * @return 函数列表
     */
    List<Map<String, Object>> getFormulaFunctions(String functionCategory);

    /**
     * 导出公式配置
     * 
     * @param systemId 预算体系ID
     * @param formulaIds 公式ID列表
     * @return 配置数据
     */
    String exportFormulaConfig(Long systemId, List<Long> formulaIds);

    /**
     * 导入公式配置
     * 
     * @param formulaConfigData 公式配置数据
     * @param systemId 预算体系ID
     * @return 导入结果
     */
    boolean importFormulaConfig(String formulaConfigData, Long systemId);

    /**
     * 解析公式表达式
     * 
     * @param formulaExpression 公式表达式
     * @return 解析结果
     */
    Map<String, Object> parseFormulaExpression(String formulaExpression);

    /**
     * 编译公式
     * 
     * @param formulaExpression 公式表达式
     * @return 编译结果
     */
    Map<String, Object> compileFormula(String formulaExpression);

    /**
     * 执行公式计算
     * 
     * @param compiledFormula 编译后的公式
     * @param variables 变量值
     * @return 计算结果
     */
    Object executeFormula(Object compiledFormula, Map<String, Object> variables);

    /**
     * 获取公式中的变量
     * 
     * @param formulaExpression 公式表达式
     * @return 变量列表
     */
    List<String> extractFormulaVariables(String formulaExpression);

    /**
     * 获取公式中的函数
     * 
     * @param formulaExpression 公式表达式
     * @return 函数列表
     */
    List<String> extractFormulaFunctions(String formulaExpression);

    /**
     * 验证公式变量
     * 
     * @param variables 变量列表
     * @param systemId 预算体系ID
     * @return 验证结果
     */
    Map<String, Object> validateFormulaVariables(List<String> variables, Long systemId);

    /**
     * 验证公式函数
     * 
     * @param functions 函数列表
     * @return 验证结果
     */
    Map<String, Object> validateFormulaFunctions(List<String> functions);

    /**
     * 构建公式依赖图
     * 
     * @param systemId 预算体系ID
     * @return 依赖图
     */
    Map<String, Object> buildFormulaDependencyGraph(Long systemId);

    /**
     * 检查循环依赖
     * 
     * @param formulaId 公式ID
     * @param dependentFormulaIds 依赖公式ID列表
     * @return 检查结果
     */
    boolean checkCircularDependency(Long formulaId, List<Long> dependentFormulaIds);

    /**
     * 获取公式计算顺序
     * 
     * @param formulaIds 公式ID列表
     * @return 计算顺序
     */
    List<Long> getFormulaCalculationOrder(List<Long> formulaIds);

    /**
     * 批量验证公式
     * 
     * @param formulaIds 公式ID列表
     * @return 验证结果
     */
    Map<String, Object> batchValidateFormulas(List<Long> formulaIds);

    /**
     * 获取公式性能统计
     * 
     * @param formulaId 公式ID
     * @return 性能统计
     */
    Map<String, Object> getFormulaPerformanceStats(Long formulaId);

    /**
     * 优化公式表达式
     * 
     * @param formulaExpression 公式表达式
     * @return 优化后的表达式
     */
    String optimizeFormulaExpression(String formulaExpression);

    /**
     * 格式化公式表达式
     * 
     * @param formulaExpression 公式表达式
     * @return 格式化后的表达式
     */
    String formatFormulaExpression(String formulaExpression);

    /**
     * 获取公式帮助信息
     * 
     * @param formulaType 公式类型
     * @return 帮助信息
     */
    Map<String, Object> getFormulaHelp(String formulaType);

    /**
     * 获取公式示例
     * 
     * @param formulaType 公式类型
     * @return 示例列表
     */
    List<Map<String, Object>> getFormulaExamples(String formulaType);

    /**
     * 保存公式计算历史
     * 
     * @param formulaId 公式ID
     * @param parameters 计算参数
     * @param result 计算结果
     * @param duration 计算耗时
     * @return 保存结果
     */
    boolean saveFormulaCalculationHistory(Long formulaId, Map<String, Object> parameters, 
                                        Object result, Long duration);

    /**
     * 查询公式计算历史
     * 
     * @param formulaId 公式ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 计算历史
     */
    List<Map<String, Object>> getFormulaCalculationHistory(Long formulaId, String startDate, String endDate);

    /**
     * 清理公式计算历史
     * 
     * @param retentionDays 保留天数
     * @return 清理数量
     */
    int cleanupFormulaCalculationHistory(Integer retentionDays);
}
