package com.management.accountant.mapper.eps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.entity.eps.EpsBudgetFormula;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算公式数据访问接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface EpsBudgetFormulaMapper extends BaseMapper<EpsBudgetFormula> {

    /**
     * 根据公式编码查询公式
     * 
     * @param formulaCode 公式编码
     * @return 预算公式
     */
    EpsBudgetFormula selectByFormulaCode(@Param("formulaCode") String formulaCode);

    /**
     * 根据预算体系ID查询公式列表
     * 
     * @param systemId 预算体系ID
     * @return 公式列表
     */
    List<EpsBudgetFormula> selectBySystemId(@Param("systemId") Long systemId);

    /**
     * 根据公式类型查询公式列表
     * 
     * @param formulaType 公式类型
     * @param systemId 预算体系ID
     * @return 公式列表
     */
    List<EpsBudgetFormula> selectByFormulaType(@Param("formulaType") String formulaType,
                                             @Param("systemId") Long systemId);

    /**
     * 根据状态查询公式列表
     * 
     * @param status 状态
     * @return 公式列表
     */
    List<EpsBudgetFormula> selectByStatus(@Param("status") String status);

    /**
     * 检查公式编码是否存在
     * 
     * @param formulaCode 公式编码
     * @param excludeId 排除的ID
     * @return 存在数量
     */
    int checkFormulaCodeExists(@Param("formulaCode") String formulaCode,
                              @Param("excludeId") Long excludeId);

    /**
     * 批量更新状态
     * 
     * @param formulaIds 公式ID列表
     * @param status 状态
     * @param updatedBy 更新人ID
     * @return 更新行数
     */
    int batchUpdateStatus(@Param("formulaIds") List<Long> formulaIds,
                         @Param("status") String status,
                         @Param("updatedBy") Long updatedBy);

    /**
     * 查询公式依赖关系
     * 
     * @param formulaId 公式ID
     * @return 依赖关系
     */
    List<Map<String, Object>> selectFormulaDependencies(@Param("formulaId") Long formulaId);

    /**
     * 查询被依赖的公式
     * 
     * @param formulaId 公式ID
     * @return 被依赖的公式列表
     */
    List<EpsBudgetFormula> selectDependentFormulas(@Param("formulaId") Long formulaId);

    /**
     * 根据变量查询相关公式
     * 
     * @param variable 变量名
     * @return 相关公式列表
     */
    List<EpsBudgetFormula> selectFormulasByVariable(@Param("variable") String variable);

    /**
     * 根据函数查询相关公式
     * 
     * @param function 函数名
     * @return 相关公式列表
     */
    List<EpsBudgetFormula> selectFormulasByFunction(@Param("function") String function);

    /**
     * 查询公式使用统计
     * 
     * @param systemId 预算体系ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 使用统计
     */
    List<Map<String, Object>> selectFormulaUsageStatistics(@Param("systemId") Long systemId,
                                                          @Param("startDate") String startDate,
                                                          @Param("endDate") String endDate);

    /**
     * 查询公式性能统计
     * 
     * @param formulaId 公式ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 性能统计
     */
    Map<String, Object> selectFormulaPerformanceStats(@Param("formulaId") Long formulaId,
                                                     @Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);

    /**
     * 查询热门公式
     * 
     * @param systemId 预算体系ID
     * @param limit 限制数量
     * @return 热门公式列表
     */
    List<EpsBudgetFormula> selectPopularFormulas(@Param("systemId") Long systemId,
                                               @Param("limit") Integer limit);

    /**
     * 查询最近使用的公式
     * 
     * @param systemId 预算体系ID
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近使用公式列表
     */
    List<EpsBudgetFormula> selectRecentlyUsedFormulas(@Param("systemId") Long systemId,
                                                    @Param("userId") Long userId,
                                                    @Param("limit") Integer limit);

    /**
     * 查询复杂公式（包含多个函数或变量）
     * 
     * @param systemId 预算体系ID
     * @param minComplexity 最小复杂度
     * @return 复杂公式列表
     */
    List<EpsBudgetFormula> selectComplexFormulas(@Param("systemId") Long systemId,
                                               @Param("minComplexity") Integer minComplexity);

    /**
     * 查询错误公式（验证失败的公式）
     * 
     * @param systemId 预算体系ID
     * @return 错误公式列表
     */
    List<EpsBudgetFormula> selectErrorFormulas(@Param("systemId") Long systemId);

    /**
     * 更新公式使用次数
     * 
     * @param formulaId 公式ID
     * @return 更新行数
     */
    int updateUsageCount(@Param("formulaId") Long formulaId);

    /**
     * 更新公式最后使用时间
     * 
     * @param formulaId 公式ID
     * @return 更新行数
     */
    int updateLastUsedTime(@Param("formulaId") Long formulaId);

    /**
     * 根据标签查询公式
     * 
     * @param tags 标签列表
     * @param systemId 预算体系ID
     * @return 公式列表
     */
    List<EpsBudgetFormula> selectByTags(@Param("tags") List<String> tags,
                                      @Param("systemId") Long systemId);

    /**
     * 查询共享公式
     * 
     * @param systemId 预算体系ID
     * @return 共享公式列表
     */
    List<EpsBudgetFormula> selectSharedFormulas(@Param("systemId") Long systemId);

    /**
     * 查询系统公式
     * 
     * @return 系统公式列表
     */
    List<EpsBudgetFormula> selectSystemFormulas();

    /**
     * 根据返回类型查询公式
     * 
     * @param returnType 返回类型
     * @param systemId 预算体系ID
     * @return 公式列表
     */
    List<EpsBudgetFormula> selectByReturnType(@Param("returnType") String returnType,
                                            @Param("systemId") Long systemId);

    /**
     * 查询公式分类统计
     * 
     * @param systemId 预算体系ID
     * @return 分类统计
     */
    List<Map<String, Object>> selectFormulaCategoryStatistics(@Param("systemId") Long systemId);

    /**
     * 查询公式状态统计
     * 
     * @param systemId 预算体系ID
     * @return 状态统计
     */
    List<Map<String, Object>> selectFormulaStatusStatistics(@Param("systemId") Long systemId);

    /**
     * 批量删除公式
     * 
     * @param formulaIds 公式ID列表
     * @return 删除行数
     */
    int batchDeleteFormulas(@Param("formulaIds") List<Long> formulaIds);

    /**
     * 恢复已删除的公式
     * 
     * @param formulaId 公式ID
     * @return 更新行数
     */
    int restoreFormula(@Param("formulaId") Long formulaId);

    /**
     * 查询公式版本历史
     * 
     * @param formulaCode 公式编码
     * @return 版本历史
     */
    List<EpsBudgetFormula> selectFormulaVersionHistory(@Param("formulaCode") String formulaCode);

    /**
     * 创建公式版本
     * 
     * @param formula 公式对象
     * @return 插入行数
     */
    int insertFormulaVersion(EpsBudgetFormula formula);

    /**
     * 查询公式计算历史
     * 
     * @param formulaId 公式ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param limit 限制数量
     * @return 计算历史
     */
    List<Map<String, Object>> selectFormulaCalculationHistory(@Param("formulaId") Long formulaId,
                                                             @Param("startDate") String startDate,
                                                             @Param("endDate") String endDate,
                                                             @Param("limit") Integer limit);

    /**
     * 保存公式计算历史
     * 
     * @param calculationHistory 计算历史
     * @return 插入行数
     */
    int insertFormulaCalculationHistory(@Param("calculationHistory") Map<String, Object> calculationHistory);

    /**
     * 清理公式计算历史
     * 
     * @param retentionDays 保留天数
     * @return 删除行数
     */
    int cleanupFormulaCalculationHistory(@Param("retentionDays") Integer retentionDays);

    /**
     * 查询公式错误日志
     * 
     * @param formulaId 公式ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 错误日志
     */
    List<Map<String, Object>> selectFormulaErrorLogs(@Param("formulaId") Long formulaId,
                                                    @Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);

    /**
     * 保存公式错误日志
     * 
     * @param errorLog 错误日志
     * @return 插入行数
     */
    int insertFormulaErrorLog(@Param("errorLog") Map<String, Object> errorLog);

    /**
     * 查询公式优化建议
     * 
     * @param formulaId 公式ID
     * @return 优化建议
     */
    List<Map<String, Object>> selectFormulaOptimizationSuggestions(@Param("formulaId") Long formulaId);

    /**
     * 查询公式测试用例
     * 
     * @param formulaId 公式ID
     * @return 测试用例
     */
    List<Map<String, Object>> selectFormulaTestCases(@Param("formulaId") Long formulaId);

    /**
     * 保存公式测试用例
     * 
     * @param testCase 测试用例
     * @return 插入行数
     */
    int insertFormulaTestCase(@Param("testCase") Map<String, Object> testCase);

    /**
     * 查询公式文档
     * 
     * @param formulaId 公式ID
     * @return 公式文档
     */
    Map<String, Object> selectFormulaDocumentation(@Param("formulaId") Long formulaId);

    /**
     * 保存公式文档
     * 
     * @param documentation 文档内容
     * @return 插入或更新行数
     */
    int saveFormulaDocumentation(@Param("documentation") Map<String, Object> documentation);
}
