package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.DebtManagement;
import com.huabo.contract.vo.DebtManagementQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 债权管理Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface DebtManagementMapper extends BaseMapper<DebtManagement> {

    /**
     * 分页查询债权管理列表
     *
     * @param param 查询参数
     * @return 债权管理列表
     */
    List<DebtManagement> selectDebtManagementList(@Param("param") DebtManagementQueryParam param);

    /**
     * 根据债权编号查询债权管理
     *
     * @param debtNo 债权编号
     * @param excludeId 排除的ID
     * @return 债权管理
     */
    DebtManagement selectByDebtNo(@Param("debtNo") String debtNo, @Param("excludeId") Long excludeId);

    /**
     * 根据项目ID查询债权管理列表
     *
     * @param projectId 项目ID
     * @return 债权管理列表
     */
    List<DebtManagement> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据债权类型查询债权管理列表
     *
     * @param debtType 债权类型
     * @return 债权管理列表
     */
    List<DebtManagement> selectByDebtType(@Param("debtType") Integer debtType);

    /**
     * 根据债权状态查询债权管理列表
     *
     * @param debtStatus 债权状态
     * @return 债权管理列表
     */
    List<DebtManagement> selectByDebtStatus(@Param("debtStatus") Integer debtStatus);

    /**
     * 根据负责人ID查询债权管理列表
     *
     * @param responsiblePersonId 负责人ID
     * @return 债权管理列表
     */
    List<DebtManagement> selectByResponsiblePersonId(@Param("responsiblePersonId") Long responsiblePersonId);

    /**
     * 获取正常状态的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectNormalDebts();

    /**
     * 获取逾期的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectOverdueDebts();

    /**
     * 获取预警的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectWarningDebts();

    /**
     * 获取已结清的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectSettledDebts();

    /**
     * 获取已核销的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectWrittenOffDebts();

    /**
     * 获取争议中的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectDisputedDebts();

    /**
     * 获取法律程序中的债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectLegalProcedureDebts();

    /**
     * 获取高风险债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectHighRiskDebts();

    /**
     * 获取大额债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectLargeAmountDebts();

    /**
     * 获取长期债权列表
     *
     * @return 债权列表
     */
    List<DebtManagement> selectLongTermDebts();

    /**
     * 模糊搜索债权管理
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 债权管理列表
     */
    List<DebtManagement> searchDebtManagement(@Param("keyword") String keyword, @Param("limit") Integer limit);

    /**
     * 统计债权管理数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> statisticsDebtManagement(@Param("param") DebtManagementQueryParam param);

    /**
     * 统计债权类型分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsDebtTypeDistribution(@Param("param") DebtManagementQueryParam param);

    /**
     * 统计债权状态分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsDebtStatusDistribution(@Param("param") DebtManagementQueryParam param);

    /**
     * 统计风险等级分布
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsRiskLevelDistribution(@Param("param") DebtManagementQueryParam param);

    /**
     * 统计月度债权趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsMonthlyDebtTrend(@Param("param") DebtManagementQueryParam param);

    /**
     * 统计月度回收趋势
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsMonthlyRecoveryTrend(@Param("param") DebtManagementQueryParam param);

    /**
     * 统计部门债权数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsDepartmentDebts(@Param("param") DebtManagementQueryParam param);

    /**
     * 统计项目债权数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsProjectDebts(@Param("param") DebtManagementQueryParam param);

    /**
     * 统计债务人债权数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    List<Map<String, Object>> statisticsDebtorDebts(@Param("param") DebtManagementQueryParam param);

    /**
     * 计算总债权金额
     *
     * @param param 查询参数
     * @return 总债权金额
     */
    BigDecimal calculateTotalDebtAmount(@Param("param") DebtManagementQueryParam param);

    /**
     * 计算总已回收金额
     *
     * @param param 查询参数
     * @return 总已回收金额
     */
    BigDecimal calculateTotalRecoveredAmount(@Param("param") DebtManagementQueryParam param);

    /**
     * 计算总未回收金额
     *
     * @param param 查询参数
     * @return 总未回收金额
     */
    BigDecimal calculateTotalUnrecoveredAmount(@Param("param") DebtManagementQueryParam param);

    /**
     * 计算平均回收率
     *
     * @param param 查询参数
     * @return 平均回收率
     */
    BigDecimal calculateAverageRecoveryRate(@Param("param") DebtManagementQueryParam param);

    /**
     * 计算平均逾期天数
     *
     * @param param 查询参数
     * @return 平均逾期天数
     */
    Integer calculateAverageOverdueDays(@Param("param") DebtManagementQueryParam param);

    /**
     * 计算平均风险评分
     *
     * @param param 查询参数
     * @return 平均风险评分
     */
    BigDecimal calculateAverageRiskScore(@Param("param") DebtManagementQueryParam param);

    /**
     * 获取债权风险分析
     *
     * @param param 查询参数
     * @return 风险分析数据
     */
    Map<String, Object> getDebtRiskAnalysis(@Param("param") DebtManagementQueryParam param);

    /**
     * 获取回收效果评估
     *
     * @param param 查询参数
     * @return 回收效果数据
     */
    Map<String, Object> getRecoveryEffectivenessEvaluation(@Param("param") DebtManagementQueryParam param);

    /**
     * 导出债权管理数据
     *
     * @param param 查询参数
     * @return 债权管理列表
     */
    List<DebtManagement> exportDebtManagement(@Param("param") DebtManagementQueryParam param);

    /**
     * 批量更新债权状态
     *
     * @param ids 主键ID列表
     * @param debtStatus 债权状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateDebtStatus(@Param("ids") List<Long> ids, 
                             @Param("debtStatus") Integer debtStatus, 
                             @Param("updateBy") Long updateBy);

    /**
     * 批量更新催收难度
     *
     * @param ids 主键ID列表
     * @param collectionDifficulty 催收难度
     * @param updateBy 更新人
     * @return 更新数量
     */
    int batchUpdateCollectionDifficulty(@Param("ids") List<Long> ids,
                                       @Param("collectionDifficulty") Integer collectionDifficulty,
                                       @Param("updateBy") Long updateBy);

    /**
     * 批量删除债权管理
     *
     * @param ids 主键ID列表
     * @param updateBy 更新人
     * @return 删除数量
     */
    int batchDeleteDebtManagement(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);

    /**
     * 物理删除债权管理
     *
     * @param ids 主键ID列表
     * @return 删除数量
     */
    int physicalDeleteDebtManagement(@Param("ids") List<Long> ids);

    /**
     * 恢复删除的债权管理
     *
     * @param ids 主键ID列表
     * @param updateBy 更新人
     * @return 恢复数量
     */
    int restoreDebtManagement(@Param("ids") List<Long> ids, @Param("updateBy") Long updateBy);
}
