package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingApprovalDTO;
import com.global.treasurer.dto.FinancingApprovalQueryDTO;
import com.global.treasurer.entity.TblFinancingApproval;

import java.util.List;
import java.util.Map;

/**
 * 融资审批服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface FinancingApprovalService {

    /**
     * 分页查询审批列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblFinancingApproval> getApprovalList(FinancingApprovalQueryDTO queryDTO);

    /**
     * 根据ID查询审批详情
     *
     * @param approvalId 审批ID
     * @return 融资审批
     */
    TblFinancingApproval getApprovalById(Long approvalId);

    /**
     * 提交审批
     *
     * @param dto 审批DTO
     * @return 保存后的审批记录
     */
    TblFinancingApproval submitApproval(FinancingApprovalDTO dto);

    /**
     * 审批通过
     *
     * @param approvalId 审批ID
     * @param comments 审批意见
     */
    void approve(Long approvalId, String comments);

    /**
     * 审批拒绝
     *
     * @param approvalId 审批ID
     * @param comments 审批意见
     */
    void reject(Long approvalId, String comments);

    /**
     * 查询审批历史
     *
     * @param approvalId 审批ID
     * @return 历史记录列表
     */
    List<Map<String, Object>> getApprovalHistory(Long approvalId);

    /**
     * 批量审批
     *
     * @param approvalIds 审批ID列表
     * @param approved 是否通过
     * @param comments 审批意见
     */
    void batchApprove(List<Long> approvalIds, Boolean approved, String comments);

    /**
     * 撤销审批
     *
     * @param approvalId 审批ID
     */
    void cancelApproval(Long approvalId);

    /**
     * 统计待审批数量
     *
     * @param approverId 审批人ID
     * @return 数量
     */
    int countPendingApprovals(Long approverId);
}
