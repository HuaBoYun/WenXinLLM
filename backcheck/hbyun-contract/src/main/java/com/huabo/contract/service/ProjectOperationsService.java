package com.huabo.contract.service;

import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.ProjectOperations;
import com.huabo.contract.vo.ProjectOperationsQueryParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 项目经营管理Service接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface ProjectOperationsService {

    /**
     * 分页查询项目经营管理列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<ProjectOperations> getProjectOperationsList(ProjectOperationsQueryParam param);

    /**
     * 根据ID获取项目经营管理详情
     *
     * @param id 主键ID
     * @return 项目经营管理详情
     */
    ProjectOperations getProjectOperationsById(Long id);

    /**
     * 保存项目经营管理（新增或修改）
     *
     * @param projectOperations 项目经营管理
     * @return 保存结果
     */
    boolean saveProjectOperations(ProjectOperations projectOperations);

    /**
     * 删除项目经营管理
     *
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteProjectOperations(Long id);

    /**
     * 批量删除项目经营管理
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteProjectOperations(List<Long> ids);

    /**
     * 检查经营编号是否存在
     *
     * @param operationsNo 经营编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsOperationsNo(String operationsNo, Long excludeId);

    /**
     * 根据项目ID查询项目经营管理列表
     *
     * @param projectId 项目ID
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getProjectOperationsByProjectId(Long projectId);

    /**
     * 根据经营类型查询项目经营管理列表
     *
     * @param operationsType 经营类型
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getProjectOperationsByOperationsType(Integer operationsType);

    /**
     * 根据经营状态查询项目经营管理列表
     *
     * @param operationsStatus 经营状态
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getProjectOperationsByOperationsStatus(Integer operationsStatus);

    /**
     * 根据风险等级查询项目经营管理列表
     *
     * @param riskLevel 风险等级
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getProjectOperationsByRiskLevel(Integer riskLevel);

    /**
     * 根据负责人ID查询项目经营管理列表
     *
     * @param managerId 负责人ID
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getMyProjectOperations(Long managerId);

    /**
     * 获取正常状态的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getNormalOperations();

    /**
     * 获取预警状态的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getWarningOperations();

    /**
     * 获取异常状态的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getAbnormalOperations();

    /**
     * 获取高风险的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getHighRiskOperations();

    /**
     * 获取盈利的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getProfitableOperations();

    /**
     * 获取亏损的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> getLossOperations();

    /**
     * 模糊搜索项目经营管理
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目经营管理列表
     */
    List<ProjectOperations> searchProjectOperations(String keyword, Integer limit);

    /**
     * 统计项目经营管理数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getProjectOperationsStatistics(ProjectOperationsQueryParam param);

    /**
     * 统计经营类型分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getOperationsTypeDistribution(ProjectOperationsQueryParam param);

    /**
     * 统计经营状态分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getOperationsStatusDistribution(ProjectOperationsQueryParam param);

    /**
     * 统计风险等级分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getRiskLevelDistribution(ProjectOperationsQueryParam param);

    /**
     * 统计月度收入趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getMonthlyRevenueTrend(ProjectOperationsQueryParam param);

    /**
     * 统计月度成本趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getMonthlyCostTrend(ProjectOperationsQueryParam param);

    /**
     * 统计月度利润趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getMonthlyProfitTrend(ProjectOperationsQueryParam param);

    /**
     * 统计部门经营数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getDepartmentOperations(ProjectOperationsQueryParam param);

    /**
     * 统计项目经营数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getProjectOperationsData(ProjectOperationsQueryParam param);

    /**
     * 统计负责人经营数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getManagerOperations(ProjectOperationsQueryParam param);

    /**
     * 计算总收入
     *
     * @param param 查询参数
     * @return 总收入
     */
    BigDecimal calculateTotalRevenue(ProjectOperationsQueryParam param);

    /**
     * 计算总成本
     *
     * @param param 查询参数
     * @return 总成本
     */
    BigDecimal calculateTotalCost(ProjectOperationsQueryParam param);

    /**
     * 计算总利润
     *
     * @param param 查询参数
     * @return 总利润
     */
    BigDecimal calculateTotalProfit(ProjectOperationsQueryParam param);

    /**
     * 计算平均利润率
     *
     * @param param 查询参数
     * @return 平均利润率
     */
    BigDecimal calculateAverageProfitRate(ProjectOperationsQueryParam param);

    /**
     * 计算平均毛利率
     *
     * @param param 查询参数
     * @return 平均毛利率
     */
    BigDecimal calculateAverageGrossProfitRate(ProjectOperationsQueryParam param);

    /**
     * 计算平均净利率
     *
     * @param param 查询参数
     * @return 平均净利率
     */
    BigDecimal calculateAverageNetProfitRate(ProjectOperationsQueryParam param);

    /**
     * 计算平均投资回报率
     *
     * @param param 查询参数
     * @return 平均投资回报率
     */
    BigDecimal calculateAverageROI(ProjectOperationsQueryParam param);

    /**
     * 计算总现金流
     *
     * @param param 查询参数
     * @return 总现金流
     */
    BigDecimal calculateTotalCashFlow(ProjectOperationsQueryParam param);

    /**
     * 计算平均风险评分
     *
     * @param param 查询参数
     * @return 平均风险评分
     */
    BigDecimal calculateAverageRiskScore(ProjectOperationsQueryParam param);

    /**
     * 计算平均绩效评分
     *
     * @param param 查询参数
     * @return 平均绩效评分
     */
    BigDecimal calculateAveragePerformanceScore(ProjectOperationsQueryParam param);

    /**
     * 获取经营健康度分析
     *
     * @param param 查询参数
     * @return 健康度分析数据
     */
    Map<String, Object> getOperationsHealthAnalysis(ProjectOperationsQueryParam param);

    /**
     * 获取风险预警列表
     *
     * @param param 查询参数
     * @return 风险预警列表
     */
    List<Map<String, Object>> getRiskWarningList(ProjectOperationsQueryParam param);

    /**
     * 获取绩效排名
     *
     * @param param 查询参数
     * @return 绩效排名列表
     */
    List<Map<String, Object>> getPerformanceRanking(ProjectOperationsQueryParam param);

    /**
     * 导出项目经营管理数据
     *
     * @param param 查询参数
     * @return 项目经营管理列表
     */
    List<ProjectOperations> exportProjectOperations(ProjectOperationsQueryParam param);

    /**
     * 批量更新经营状态
     *
     * @param ids 主键ID列表
     * @param operationsStatus 经营状态
     * @param updateBy 更新人
     * @return 更新结果
     */
    boolean batchUpdateOperationsStatus(List<Long> ids, Integer operationsStatus, Long updateBy);

    /**
     * 批量更新风险等级
     *
     * @param ids 主键ID列表
     * @param riskLevel 风险等级
     * @param updateBy 更新人
     * @return 更新结果
     */
    boolean batchUpdateRiskLevel(List<Long> ids, Integer riskLevel, Long updateBy);

    /**
     * 恢复删除的项目经营管理
     *
     * @param ids 主键ID列表
     * @param updateBy 更新人
     * @return 恢复结果
     */
    boolean restoreProjectOperations(List<Long> ids, Long updateBy);

    /**
     * 物理删除项目经营管理
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean physicalDeleteProjectOperations(List<Long> ids);
}
