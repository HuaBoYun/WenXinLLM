package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.ArAgingSnapshotEntity;
import com.financial.sharing.vo.param.ArAgingAnalysisQueryParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 账龄分析快照Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArAgingSnapshotMapper extends BaseMapper<ArAgingSnapshotEntity> {

    /**
     * 查询账龄分析明细
     */
    List<ArAgingSnapshotEntity> selectAgingDetails(@Param("param") ArAgingAnalysisQueryParam param);

    /**
     * 按账龄区间统计
     */
    List<Map<String, Object>> selectAgingRangeSummary(@Param("analysisDate") LocalDate analysisDate, 
                                                       @Param("tenantId") Long tenantId);

    /**
     * 按客户统计账龄
     */
    List<Map<String, Object>> selectAgingByCustomer(@Param("analysisDate") LocalDate analysisDate, 
                                                     @Param("tenantId") Long tenantId);

    /**
     * 按风险等级统计
     */
    List<Map<String, Object>> selectAgingByRiskLevel(@Param("analysisDate") LocalDate analysisDate, 
                                                      @Param("tenantId") Long tenantId);

    /**
     * 批量插入账龄快照
     */
    int batchInsert(@Param("snapshots") List<ArAgingSnapshotEntity> snapshots);

    /**
     * 删除指定日期的快照
     */
    int deleteByAnalysisDate(@Param("analysisDate") LocalDate analysisDate, @Param("tenantId") Long tenantId);

    /**
     * 查询最新快照日期
     */
    LocalDate selectLatestSnapshotDate(@Param("tenantId") Long tenantId);

    /**
     * 计算账龄天数和区间
     */
    List<Map<String, Object>> calculateAgingFromReceivables(@Param("analysisDate") LocalDate analysisDate, 
                                                            @Param("tenantId") Long tenantId);

    /**
     * 查询账龄趋势（按月）
     */
    List<Map<String, Object>> selectAgingTrend(@Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate,
                                                @Param("tenantId") Long tenantId);

    /**
     * 查询逾期金额统计
     */
    BigDecimal selectTotalOverdueAmount(@Param("analysisDate") LocalDate analysisDate, 
                                         @Param("tenantId") Long tenantId);
}

