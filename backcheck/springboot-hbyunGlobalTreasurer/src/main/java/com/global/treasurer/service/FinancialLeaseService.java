package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancialLeaseDTO;
import com.global.treasurer.dto.FinancialLeaseQueryDTO;
import com.global.treasurer.entity.TblFinancialLease;

import java.util.List;
import java.util.Map;

/**
 * 融资租赁服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface FinancialLeaseService {

    PageInfo<TblFinancialLease> getLeaseList(FinancialLeaseQueryDTO queryDTO);

    TblFinancialLease getLeaseById(Long leaseId);

    TblFinancialLease saveLease(FinancialLeaseDTO dto);

    void deleteLease(Long leaseId);

    void batchDeleteLeases(List<Long> leaseIds);

    void submitForApproval(Long leaseId);

    void approve(Long leaseId, String comments);

    void reject(Long leaseId, String comments);

    void activateLease(Long leaseId);

    void terminateLease(Long leaseId, String reason);

    List<TblFinancialLease> getExpiringLeases(Integer days);

    Map<String, Object> getLeaseSummary(Long companyId);

    /**
     * 获取统计概览数据
     */
    Map<String, Object> getOverviewStatistics();

    /**
     * 获取租赁类型分布统计
     */
    List<Map<String, Object>> getTypeDistribution();

    /**
     * 获取租赁申请趋势统计
     */
    Map<String, Object> getTrendStatistics(String period);

    /**
     * 获取导出数据列表（不分页）
     */
    List<TblFinancialLease> getLeaseListForExport(FinancialLeaseQueryDTO queryDTO);
}

