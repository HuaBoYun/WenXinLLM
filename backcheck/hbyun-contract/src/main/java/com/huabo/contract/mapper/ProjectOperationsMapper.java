package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.ProjectOperations;
import com.huabo.contract.vo.ProjectOperationsQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 项目经营管理Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface ProjectOperationsMapper extends BaseMapper<ProjectOperations> {

    /**
     * 分页查询项目经营管理列表
     *
     * @param param 查询参数
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectProjectOperationsList(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 根据经营编号查询项目经营管理
     *
     * @param operationsNo 经营编号
     * @param excludeId 排除的ID
     * @return 项目经营管理
     */
    ProjectOperations selectByOperationsNo(@Param("operationsNo") String operationsNo, @Param("excludeId") Long excludeId);

    /**
     * 根据项目ID查询项目经营管理列表
     *
     * @param projectId 项目ID
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据经营类型查询项目经营管理列表
     *
     * @param operationsType 经营类型
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectByOperationsType(@Param("operationsType") Integer operationsType);

    /**
     * 根据经营状态查询项目经营管理列表
     *
     * @param operationsStatus 经营状态
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectByOperationsStatus(@Param("operationsStatus") Integer operationsStatus);

    /**
     * 根据风险等级查询项目经营管理列表
     *
     * @param riskLevel 风险等级
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectByRiskLevel(@Param("riskLevel") Integer riskLevel);

    /**
     * 根据负责人ID查询项目经营管理列表
     *
     * @param managerId 负责人ID
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectByManagerId(@Param("managerId") Long managerId);

    /**
     * 获取正常状态的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectNormalOperations();

    /**
     * 获取预警状态的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectWarningOperations();

    /**
     * 获取异常状态的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectAbnormalOperations();

    /**
     * 获取高风险的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectHighRiskOperations();

    /**
     * 获取盈利的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectProfitableOperations();

    /**
     * 获取亏损的项目经营管理列表
     *
     * @return 项目经营管理列表
     */
    List<ProjectOperations> selectLossOperations();

    /**
     * 模糊搜索项目经营管理
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 项目经营管理列表
     */
    List<ProjectOperations> searchProjectOperations(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 统计项目经营管理数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> statisticsProjectOperations(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 统计经营类型分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsOperationsTypeDistribution(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 统计经营状态分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsOperationsStatusDistribution(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 统计风险等级分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsRiskLevelDistribution(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 统计月度收入趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsMonthlyRevenueTrend(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 统计月度成本趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsMonthlyCostTrend(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 统计月度利润趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsMonthlyProfitTrend(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 统计部门经营数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsDepartmentOperations(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 统计项目经营数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsProjectOperationsData(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 统计负责人经营数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsManagerOperations(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算总收入
     *
     * @param param 查询参数
     * @return 总收入
     */
    BigDecimal calculateTotalRevenue(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算总成本
     *
     * @param param 查询参数
     * @return 总成本
     */
    BigDecimal calculateTotalCost(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算总利润
     *
     * @param param 查询参数
     * @return 总利润
     */
    BigDecimal calculateTotalProfit(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算平均利润率
     *
     * @param param 查询参数
     * @return 平均利润率
     */
    BigDecimal calculateAverageProfitRate(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算平均毛利率
     *
     * @param param 查询参数
     * @return 平均毛利率
     */
    BigDecimal calculateAverageGrossProfitRate(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算平均净利率
     *
     * @param param 查询参数
     * @return 平均净利率
     */
    BigDecimal calculateAverageNetProfitRate(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算平均投资回报率
     *
     * @param param 查询参数
     * @return 平均投资回报率
     */
    BigDecimal calculateAverageROI(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算总现金流
     *
     * @param param 查询参数
     * @return 总现金流
     */
    BigDecimal calculateTotalCashFlow(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算平均风险评分
     *
     * @param param 查询参数
     * @return 平均风险评分
     */
    BigDecimal calculateAverageRiskScore(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 计算平均绩效评分
     *
     * @param param 查询参数
     * @return 平均绩效评分
     */
    BigDecimal calculateAveragePerformanceScore(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 获取经营健康度分析
     *
     * @param param 查询参数
     * @return 健康度分析数据
     */
    Map<String, Object> getOperationsHealthAnalysis(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 获取风险预警列表
     *
     * @param param 查询参数
     * @return 风险预警列表
     */
    List<Map<String, Object>> getRiskWarningList(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 获取绩效排名
     *
     * @param param 查询参数
     * @return 绩效排名列表
     */
    List<Map<String, Object>> getPerformanceRanking(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 导出项目经营管理数据
     *
     * @param param 查询参数
     * @return 项目经营管理列表
     */
    List<ProjectOperations> exportProjectOperations(@Param("param") ProjectOperationsQueryParam param);

    /**
     * 批量更新经营状态
     *
     * @param ids 主键ID列表
     * @param operationsStatus 经营状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateOperationsStatus(@Param("ids") List<Long> ids, 
                                   @Param("operationsStatus") Integer operationsStatus, 
                                   @Param("updateBy") Long updateBy);

    /**
     * 批量更新风险等级
     *
     * @param ids 主键ID列表
     * @param riskLevel 风险等级
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateRiskLevel(@Param("ids") List<Long> ids, 
                            @Param("riskLevel") Integer riskLevel, 
                            @Param("updateBy") Long updateBy);

    /**
     * 批量删除项目经营管理
     *
     * @param ids 主键ID列表
     * @param updateBy 更新人
     * @return 删除数量
     */
    int batchDeleteProjectOperations(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 物理删除项目经营管理
     *
     * @param ids 主键ID列表
     * @return 删除数量
     */
    int physicalDeleteProjectOperations(@Param("ids") List<Long> ids);

    /**
     * 恢复删除的项目经营管理
     *
     * @param ids 主键ID列表
     * @param updateBy 更新人
     * @return 恢复数量
     */
    int restoreProjectOperations(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);
}
