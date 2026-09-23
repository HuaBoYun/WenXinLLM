package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblFundAllocation;

import java.util.List;

/**
 * 资金下拨Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblFundAllocationService {

    /**
     * 分页查询资金下拨列表
     */
    PageInfo<TblFundAllocation> getAllocationPage(Integer pageNum, Integer pageSize,
                                                  String allocationNo, String allocationStatus,
                                                  String startDate, String endDate);

    /**
     * 根据ID查询资金下拨
     */
    TblFundAllocation getAllocationById(String allocationId);

    /**
     * 保存资金下拨申请
     */
    TblFundAllocation saveAllocation(TblFundAllocation allocation);

    /**
     * 更新资金下拨
     */
    void updateAllocation(TblFundAllocation allocation);

    /**
     * 删除资金下拨
     */
    void deleteAllocation(String allocationId);

    /**
     * 取消资金下拨
     */
    void cancelAllocation(String allocationId);

    /**
     * 执行资金下拨
     */
    String executeAllocation(String allocationId);

    /**
     * 重试资金下拨
     */
    String retryAllocation(String allocationId);

    /**
     * 审批资金下拨
     */
    void approveAllocation(String allocationId, String approveResult, String approveRemark);

    /**
     * 获取待审批的下拨申请
     */
    List<TblFundAllocation> getPendingApprovalList();

    /**
     * 统计总数
     */
    long count();

    /**
     * 按状态统计数量
     */
    long countByStatus(String status);

    /**
     * 统计今日执行数
     */
    long countTodayExecutions();
}

