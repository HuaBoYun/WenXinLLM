package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFinancialLease;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 融资租赁Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface FinancialLeaseMapper extends BaseMapper<TblFinancialLease> {

    List<TblFinancialLease> selectLeaseList(Map<String, Object> params);

    TblFinancialLease selectLeaseById(@Param("leaseId") Long leaseId);

    int updateLeaseStatus(@Param("leaseId") Long leaseId, @Param("status") String status);

    int batchDeleteByIds(@Param("leaseIds") List<Long> leaseIds);

    List<TblFinancialLease> selectExpiringLeases(@Param("days") Integer days);

    Map<String, Object> selectLeaseSummary(@Param("companyId") Long companyId);

    /**
     * 统计概览查询
     */
    Map<String, Object> selectOverviewStatistics();

    /**
     * 租赁类型分布统计
     */
    List<Map<String, Object>> selectTypeDistribution();

    /**
     * 租赁申请趋势统计
     */
    List<Map<String, Object>> selectTrendStatistics(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

