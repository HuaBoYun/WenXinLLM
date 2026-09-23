package com.management.accountant.mapper.ts;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ts.TsTaxCompliance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 税务合规检查 Mapper 接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Mapper
public interface TsTaxComplianceMapper extends BaseMapper<TsTaxCompliance> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据合规检查编号查询
     */
    TsTaxCompliance selectByComplianceCode(@Param("tenantId") Long tenantId, @Param("complianceCode") String complianceCode);

    /**
     * 根据检查类型查询
     */
    List<TsTaxCompliance> selectByComplianceType(@Param("tenantId") Long tenantId, @Param("complianceType") String complianceType);

    /**
     * 根据检查状态查询
     */
    List<TsTaxCompliance> selectByCheckStatus(@Param("tenantId") Long tenantId, @Param("checkStatus") String checkStatus);

    /**
     * 根据合规状态查询
     */
    List<TsTaxCompliance> selectByComplianceStatus(@Param("tenantId") Long tenantId, @Param("complianceStatus") String complianceStatus);

    /**
     * 根据风险等级查询
     */
    List<TsTaxCompliance> selectByRiskLevel(@Param("tenantId") Long tenantId, @Param("riskLevel") String riskLevel);

    /**
     * 根据优先级查询
     */
    List<TsTaxCompliance> selectByPriority(@Param("tenantId") Long tenantId, @Param("priority") String priority);

    /**
     * 根据检查人员查询
     */
    List<TsTaxCompliance> selectByChecker(@Param("tenantId") Long tenantId, @Param("checker") String checker);

    /**
     * 根据检查部门查询
     */
    List<TsTaxCompliance> selectByCheckDepartment(@Param("tenantId") Long tenantId, @Param("checkDepartment") String checkDepartment);

    /**
     * 根据整改状态查询
     */
    List<TsTaxCompliance> selectByRectificationStatus(@Param("tenantId") Long tenantId, @Param("rectificationStatus") String rectificationStatus);

    /**
     * 根据复查状态查询
     */
    List<TsTaxCompliance> selectByRecheckStatus(@Param("tenantId") Long tenantId, @Param("recheckStatus") String recheckStatus);

    // ==================== 时间范围查询 ====================

    /**
     * 根据检查时间范围查询
     */
    List<TsTaxCompliance> selectByCheckTimeRange(@Param("tenantId") Long tenantId, 
                                                @Param("startTime") LocalDateTime startTime, 
                                                @Param("endTime") LocalDateTime endTime);

    /**
     * 根据创建时间范围查询
     */
    List<TsTaxCompliance> selectByCreateTimeRange(@Param("tenantId") Long tenantId, 
                                                 @Param("startTime") LocalDateTime startTime, 
                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 查询即将到期的检查
     */
    List<TsTaxCompliance> selectExpiringSoon(@Param("tenantId") Long tenantId, @Param("days") Integer days);

    /**
     * 查询逾期的检查
     */
    List<TsTaxCompliance> selectOverdue(@Param("tenantId") Long tenantId);

    /**
     * 查询高风险检查
     */
    List<TsTaxCompliance> selectHighRisk(@Param("tenantId") Long tenantId);

    /**
     * 查询需要整改的检查
     */
    List<TsTaxCompliance> selectNeedRectification(@Param("tenantId") Long tenantId);

    /**
     * 查询需要复查的检查
     */
    List<TsTaxCompliance> selectNeedRecheck(@Param("tenantId") Long tenantId);

    // ==================== 统计分析方法 ====================

    /**
     * 获取合规检查概览统计
     */
    Map<String, Object> selectOverviewStats(@Param("tenantId") Long tenantId);

    /**
     * 按检查状态统计数量
     */
    List<Map<String, Object>> countByCheckStatus(@Param("tenantId") Long tenantId);

    /**
     * 按合规状态统计数量
     */
    List<Map<String, Object>> countByComplianceStatus(@Param("tenantId") Long tenantId);

    /**
     * 按检查类型统计数量
     */
    List<Map<String, Object>> countByComplianceType(@Param("tenantId") Long tenantId);

    /**
     * 按风险等级统计数量
     */
    List<Map<String, Object>> countByRiskLevel(@Param("tenantId") Long tenantId);

    /**
     * 按优先级统计数量
     */
    List<Map<String, Object>> countByPriority(@Param("tenantId") Long tenantId);

    /**
     * 按整改状态统计数量
     */
    List<Map<String, Object>> countByRectificationStatus(@Param("tenantId") Long tenantId);

    /**
     * 获取检查趋势数据
     */
    List<Map<String, Object>> selectCheckTrend(@Param("tenantId") Long tenantId, 
                                              @Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate, 
                                              @Param("groupBy") String groupBy);

    /**
     * 获取合规评分趋势
     */
    List<Map<String, Object>> selectComplianceScoreTrend(@Param("tenantId") Long tenantId, 
                                                         @Param("startDate") LocalDateTime startDate, 
                                                         @Param("endDate") LocalDateTime endDate, 
                                                         @Param("groupBy") String groupBy);

    /**
     * 获取风险分布数据
     */
    List<Map<String, Object>> selectRiskDistribution(@Param("tenantId") Long tenantId);

    /**
     * 获取效果评估数据
     */
    List<Map<String, Object>> selectEffectivenessData(@Param("tenantId") Long tenantId);

    /**
     * 获取检查排行榜
     */
    List<Map<String, Object>> selectCheckRanking(@Param("tenantId") Long tenantId, 
                                                @Param("rankBy") String rankBy, 
                                                @Param("limit") Integer limit);

    /**
     * 获取检查效率统计
     */
    Map<String, Object> selectCheckEfficiencyStats(@Param("tenantId") Long tenantId);

    // ==================== 批量操作方法 ====================

    /**
     * 批量更新检查状态
     */
    int batchUpdateCheckStatus(@Param("tenantId") Long tenantId, 
                              @Param("complianceIds") List<Long> complianceIds, 
                              @Param("checkStatus") String checkStatus, 
                              @Param("updatedBy") String updatedBy);

    /**
     * 批量更新合规状态
     */
    int batchUpdateComplianceStatus(@Param("tenantId") Long tenantId, 
                                   @Param("complianceIds") List<Long> complianceIds, 
                                   @Param("complianceStatus") String complianceStatus, 
                                   @Param("updatedBy") String updatedBy);

    /**
     * 批量更新整改状态
     */
    int batchUpdateRectificationStatus(@Param("tenantId") Long tenantId, 
                                      @Param("complianceIds") List<Long> complianceIds, 
                                      @Param("rectificationStatus") String rectificationStatus, 
                                      @Param("updatedBy") String updatedBy);

    /**
     * 批量删除检查记录
     */
    int batchDeleteByIds(@Param("tenantId") Long tenantId, 
                        @Param("complianceIds") List<Long> complianceIds, 
                        @Param("updatedBy") String updatedBy);

    /**
     * 批量归档检查记录
     */
    int batchArchive(@Param("tenantId") Long tenantId, 
                    @Param("complianceIds") List<Long> complianceIds, 
                    @Param("updatedBy") String updatedBy);

    /**
     * 批量激活检查记录
     */
    int batchActivate(@Param("tenantId") Long tenantId, 
                     @Param("complianceIds") List<Long> complianceIds, 
                     @Param("updatedBy") String updatedBy);

    // ==================== 系统维护方法 ====================

    /**
     * 系统健康检查
     */
    Map<String, Object> systemHealthCheck(@Param("tenantId") Long tenantId);

    /**
     * 数据一致性检查
     */
    List<Map<String, Object>> dataConsistencyCheck(@Param("tenantId") Long tenantId);

    /**
     * 性能统计
     */
    Map<String, Object> performanceStats(@Param("tenantId") Long tenantId);

    /**
     * 清理过期数据
     */
    int cleanupExpiredData(@Param("tenantId") Long tenantId, @Param("days") Integer days);

    /**
     * 重建索引
     */
    void rebuildIndex(@Param("tenantId") Long tenantId);

    /**
     * 优化表结构
     */
    void optimizeTable(@Param("tenantId") Long tenantId);

    // ==================== 分页查询方法 ====================

    /**
     * 分页查询合规检查
     */
    IPage<TsTaxCompliance> selectCompliancePage(Page<TsTaxCompliance> page, 
                                               @Param("tenantId") Long tenantId, 
                                               @Param("params") Map<String, Object> params);

    /**
     * 分页查询待处理检查
     */
    IPage<TsTaxCompliance> selectPendingPage(Page<TsTaxCompliance> page, 
                                            @Param("tenantId") Long tenantId, 
                                            @Param("params") Map<String, Object> params);

    /**
     * 分页查询已完成检查
     */
    IPage<TsTaxCompliance> selectCompletedPage(Page<TsTaxCompliance> page, 
                                              @Param("tenantId") Long tenantId, 
                                              @Param("params") Map<String, Object> params);

    /**
     * 分页查询异常检查
     */
    IPage<TsTaxCompliance> selectAbnormalPage(Page<TsTaxCompliance> page, 
                                             @Param("tenantId") Long tenantId, 
                                             @Param("params") Map<String, Object> params);
}
