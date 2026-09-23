package com.global.treasurer.service;

import com.global.treasurer.entity.FundTransfer;

import java.util.List;
import java.util.Map;

/**
 * 资金调拨服务接口
 *
 * @author AI Developer
 * @date 2025-01-15
 */
public interface FundTransferService {

    /**
     * 分页查询资金调拨列表
     */
    Map<String, Object> getTransferPage(Map<String, Object> param);

    /**
     * 根据ID查询资金调拨详情
     */
    FundTransfer getTransferById(Long transferId);

    /**
     * 创建资金调拨单
     */
    int createTransfer(FundTransfer transfer);

    /**
     * 更新资金调拨单
     */
    int updateTransfer(FundTransfer transfer);

    /**
     * 批量删除资金调拨单
     */
    int batchDelete(List<Long> ids);

    /**
     * 批量提交审批
     */
    int batchSubmit(List<Long> ids, String submitByName);

    /**
     * 批量执行调拨
     */
    int batchExecute(List<Long> ids, String executeByName);

    /**
     * 生成调拨单号
     */
    String generateTransferNo(Long orgId);

    /**
     * 获取调拨概览统计
     */
    Map<String, Object> getTransferStatistics(Map<String, Object> params);

    /**
     * 批量审批调拨单
     */
    int batchApprove(List<Long> ids, String approveByName, String approvalComment);

    /**
     * 取消资金调拨
     *
     * @param transferId 调拨ID
     * @param cancelByName 取消人姓名
     * @return 影响行数
     */
    int cancelTransfer(Long transferId, String cancelByName);

    /**
     * 单个审批资金调拨
     *
     * @param transferId 调拨ID
     * @param status 审批状态（APPROVED/REJECTED）
     * @param approveByName 审批人姓名
     * @param approvalComment 审批意见
     * @return 影响行数
     */
    int approveTransfer(Long transferId, String status, String approveByName, String approvalComment);

    /**
     * 执行资金调拨
     *
     * @param transferId 调拨ID
     * @param executeByName 执行人姓名
     * @return 影响行数
     */
    int executeTransfer(Long transferId, String executeByName);

    /**
     * 获取可用账户列表
     */
    List<Map<String, Object>> getAvailableAccounts(Map<String, Object> params);
}
