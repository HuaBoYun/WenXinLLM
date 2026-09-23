package com.global.treasurer.service;

import com.global.treasurer.entity.TblLeaseReturn;

import java.util.List;
import java.util.Map;

/**
 * 退租申请服务接口
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
public interface LeaseReturnService {

    /**
     * 根据租赁ID获取退租申请
     */
    TblLeaseReturn getReturnByLeaseId(Long leaseId);

    /**
     * 根据退租ID获取退租详情
     */
    TblLeaseReturn getReturnById(Long returnId);

    /**
     * 保存退租申请（草稿）
     */
    TblLeaseReturn saveReturn(TblLeaseReturn leaseReturn);

    /**
     * 提交退租申请
     */
    void submitReturn(Long returnId);

    /**
     * 审批通过退租申请
     */
    void approveReturn(Long returnId, String comments, Long approvedBy);

    /**
     * 拒绝退租申请
     */
    void rejectReturn(Long returnId, String comments, Long approvedBy);

    /**
     * 完成退租
     */
    void completeReturn(Long returnId);

    /**
     * 获取待审批的退租申请列表
     */
    List<TblLeaseReturn> getPendingApprovalList();

    /**
     * 根据租赁ID删除退租申请
     */
    void deleteReturnByLeaseId(Long leaseId);

    /**
     * 计算退租费用
     */
    Map<String, Object> calculateReturnFees(Long leaseId, String returnType, String assetDisposal);
}

