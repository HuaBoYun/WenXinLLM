package com.huabo.contract.service;

import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.DebtManagement;
import com.huabo.contract.vo.DebtManagementQueryParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 债权管理Service接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface DebtManagementService {

    /**
     * 分页查询债权管理列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<DebtManagement> getDebtManagementList(DebtManagementQueryParam param);

    /**
     * 根据ID获取债权管理详情
     *
     * @param id 主键ID
     * @return 债权管理详情
     */
    DebtManagement getDebtManagementById(Long id);

    /**
     * 保存债权管理（新增或修改）
     *
     * @param debtManagement 债权管理
     * @return 保存结果
     */
    boolean saveDebtManagement(DebtManagement debtManagement);

    /**
     * 删除债权管理
     *
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteDebtManagement(Long id);

    /**
     * 批量删除债权管理
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteDebtManagement(List<Long> ids);

    /**
     * 检查债权编号是否存在
     *
     * @param debtNo 债权编号
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsDebtNo(String debtNo, Long excludeId);

    /**
     * 根据项目ID查询债权管理列表
     *
     * @param projectId 项目ID
     * @return 债权管理列表
     */
    List<DebtManagement> getDebtManagementByProjectId(Long projectId);

    /**
     * 根据债权类型查询债权管理列表
     *
     * @param debtType 债权类型
     * @return 债权管理列表
     */
    List<DebtManagement> getDebtManagementByDebtType(Integer debtType);

    /**
     * 根据债权状态查询债权管理列表
     *
     * @param debtStatus 债权状态
     * @return 债权管理列表
     */
    List<DebtManagement> getDebtManagementByDebtStatus(Integer debtStatus);

    /**
     * 根据负责人ID查询债权管理列表
     *
     * @param responsiblePersonId 负责人ID
     * @return 债权管理列表
     */
    List<DebtManagement> getMyDebtManagement(Long responsiblePersonId);

    /**
     * 获取正常状态的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getNormalDebts();

    /**
     * 获取逾期的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getOverdueDebts();

    /**
     * 获取预警的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getWarningDebts();

    /**
     * 获取已结清的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getSettledDebts();

    /**
     * 获取已核销的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getWrittenOffDebts();

    /**
     * 获取争议中的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getDisputedDebts();

    /**
     * 获取法律程序中的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getLegalProcedureDebts();

    /**
     * 获取高风险债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getHighRiskDebts();

    /**
     * 获取大额债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getLargeAmountDebts();

    /**
     * 获取长期债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> getLongTermDebts();

    /**
     * 模糊搜索债权管理
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 债权管理列表
     */
    List<DebtManagement> searchDebtManagement(String keyword, Integer limit);

    /**
     * 统计债权管理数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getDebtManagementStatistics(DebtManagementQueryParam param);

    /**
     * 统计债权类型分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getDebtTypeDistribution(DebtManagementQueryParam param);

    /**
     * 统计债权状态分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getDebtStatusDistribution(DebtManagementQueryParam param);

    /**
     * 统计风险等级分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getRiskLevelDistribution(DebtManagementQueryParam param);

    /**
     * 统计月度债权趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getMonthlyDebtTrend(DebtManagementQueryParam param);

    /**
     * 统计月度回收趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getMonthlyRecoveryTrend(DebtManagementQueryParam param);

    /**
     * 统计部门债权数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getDepartmentDebts(DebtManagementQueryParam param);

    /**
     * 统计项目债权数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getProjectDebts(DebtManagementQueryParam param);

    /**
     * 统计债务人债权数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> getDebtorDebts(DebtManagementQueryParam param);

    /**
     * 计算总债权金额
     *
     * @param param 查询参数
     * @return 总债权金额
     */
    BigDecimal calculateTotalDebtAmount(DebtManagementQueryParam param);

    /**
     * 计算总已回收金额
     *
     * @param param 查询参数
     * @return 总已回收金额
     */
    BigDecimal calculateTotalRecoveredAmount(DebtManagementQueryParam param);

    /**
     * 计算总未回收金额
     *
     * @param param 查询参数
     * @return 总未回收金额
     */
    BigDecimal calculateTotalUnrecoveredAmount(DebtManagementQueryParam param);

    /**
     * 计算平均回收率
     *
     * @param param 查询参数
     * @return 平均回收率
     */
    BigDecimal calculateAverageRecoveryRate(DebtManagementQueryParam param);

    /**
     * 计算平均逾期天数
     *
     * @param param 查询参数
     * @return 平均逾期天数
     */
    Integer calculateAverageOverdueDays(DebtManagementQueryParam param);

    /**
     * 计算平均风险评分
     *
     * @param param 查询参数
     * @return 平均风险评分
     */
    BigDecimal calculateAverageRiskScore(DebtManagementQueryParam param);

    /**
     * 获取债权风险分析
     *
     * @param param 查询参数
     * @return 风险分析数据
     */
    Map<String, Object> getDebtRiskAnalysis(DebtManagementQueryParam param);

    /**
     * 获取回收效果评估
     *
     * @param param 查询参数
     * @return 回收效果数据
     */
    Map<String, Object> getRecoveryEffectivenessEvaluation(DebtManagementQueryParam param);

    /**
     * 导出债权管理数据
     *
     * @param param 查询参数
     * @return 债权管理列表
     */
    List<DebtManagement> exportDebtManagement(DebtManagementQueryParam param);

    /**
     * 批量更新债权状态
     *
     * @param ids 主键ID列表
     * @param debtStatus 债权状态
     * @param updateBy 更新人
     * @return 更新结果
     */
    boolean batchUpdateDebtStatus(List<Long> ids, Integer debtStatus, Long updateBy);

    /**
     * 批量更新催收难度
     *
     * @param ids 主键ID列表
     * @param collectionDifficulty 催收难度
     * @param updateBy 更新人
     * @return 更新结果
     */
    boolean batchUpdateCollectionDifficulty(List<Long> ids, Integer collectionDifficulty, Long updateBy);

    /**
     * 恢复删除的债权管理
     *
     * @param ids 主键ID列表
     * @param updateBy 更新人
     * @return 恢复结果
     */
    boolean restoreDebtManagement(List<Long> ids, Long updateBy);

    /**
     * 物理删除债权管理
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean physicalDeleteDebtManagement(List<Long> ids);

    /**
     * 获取债权账龄分析
     *
     * @param projectId 项目ID（可选）
     * @return 账龄分析结果
     */
    Map<String, Object> getDebtAgingAnalysis(Long projectId);
}
