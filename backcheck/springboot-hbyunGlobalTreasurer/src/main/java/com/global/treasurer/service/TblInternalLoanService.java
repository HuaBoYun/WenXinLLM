package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblInternalLoan;

import java.util.List;

/**
 * 内部借贷Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblInternalLoanService {

    /**
     * 分页查询内部借贷列表
     */
    PageInfo<TblInternalLoan> getLoanPage(Integer pageNum, Integer pageSize,
                                          String loanNo, String loanStatus,
                                          String startDate, String endDate);

    /**
     * 根据ID查询内部借贷
     */
    TblInternalLoan getLoanById(String loanId);

    /**
     * 保存内部借贷申请
     */
    TblInternalLoan saveLoan(TblInternalLoan loan);

    /**
     * 更新内部借贷
     */
    void updateLoan(TblInternalLoan loan);

    /**
     * 提交借贷申请
     */
    void submitLoan(String loanId);

    /**
     * 审批借贷申请
     */
    void approveLoan(String loanId, String approveResult, String approveRemark);

    /**
     * 放款
     */
    String disburseLoan(String loanId);

    /**
     * 还款
     */
    String repayLoan(String loanId, String repayAmount);

    /**
     * 取消借贷申请
     */
    void cancelLoan(String loanId);

    /**
     * 删除借贷申请（逻辑删除，仅草稿状态可删除）
     */
    void deleteLoan(String loanId);

    /**
     * 获取待审批的借贷申请
     */
    List<TblInternalLoan> getPendingApprovalList();

    /**
     * 获取待还款的借贷
     */
    List<TblInternalLoan> getPendingRepaymentList();

    /**
     * 统计总数
     */
    long count();

    /**
     * 按状态统计数量
     */
    long countByStatus(String status);

    /**
     * 统计今日申请数
     */
    long countTodayApplications();
}

