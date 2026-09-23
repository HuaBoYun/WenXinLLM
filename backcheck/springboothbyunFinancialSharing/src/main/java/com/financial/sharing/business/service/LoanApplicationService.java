package com.financial.sharing.business.service;

import com.financial.sharing.business.entity.TblLoanApplication;
import com.financial.sharing.util.MyJsonBean;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 借款单服务接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
public interface LoanApplicationService {

    /**
     * 分页查询借款单列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean getList(Map<String, Object> param);

    /**
     * 根据ID查询借款单详情
     *
     * @param loanId 借款单ID
     * @return 借款单详情
     */
    MyJsonBean getById(String loanId);

    /**
     * 保存或更新借款单
     *
     * @param loan 借款单对象
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblLoanApplication loan);

    /**
     * 删除借款单
     *
     * @param loanId 借款单ID
     * @return 操作结果
     */
    MyJsonBean delete(String loanId);

    /**
     * 批量删除借款单
     *
     * @param loanIds 借款单ID列表
     * @return 操作结果
     */
    MyJsonBean batchDelete(java.util.List<String> loanIds);

    /**
     * 提交借款单
     *
     * @param loanId 借款单ID
     * @return 操作结果
     */
    MyJsonBean submit(String loanId);

    /**
     * 审批借款单
     *
     * @param loanId 借款单ID
     * @param action 审批动作 (APPROVE-通过，REJECT-拒绝)
     * @param opinion 审批意见
     * @return 操作结果
     */
    MyJsonBean approve(String loanId, String action, String opinion);

    /**
     * 撤回借款单
     *
     * @param loanId 借款单ID
     * @return 操作结果
     */
    MyJsonBean withdraw(String loanId);

    /**
     * 放款
     *
     * @param loanId 借款单ID
     * @param disburseAmount 放款金额
     * @param disburseMethod 放款方式
     * @param bankAccount 收款银行账户
     * @param disburseRemark 放款备注
     * @return 操作结果
     */
    MyJsonBean disburse(String loanId, BigDecimal disburseAmount, String disburseMethod,
                        String bankAccount, String disburseRemark);

    /**
     * 还款
     *
     * @param loanId 借款单ID
     * @param amount 还款金额
     * @param repayMethod 还款方式
     * @param repayVoucher 还款凭证号
     * @param repayRemark 还款备注
     * @param expenseReportId 关联报销单ID
     * @param offsetAmount 抵冲金额
     * @return 操作结果
     */
    MyJsonBean repay(String loanId, BigDecimal amount, String repayMethod, String repayVoucher,
                     String repayRemark, String expenseReportId, BigDecimal offsetAmount);

    /**
     * 根据借款单号查询
     *
     * @param loanCode 借款单号
     * @return 借款单详情
     */
    MyJsonBean getByLoanCode(String loanCode);

    /**
     * 导出借款单
     */
    MyJsonBean export(Map<String, Object> param);

    /**
     * 借款单统计
     */
    MyJsonBean getStatistics(Map<String, Object> param);
}
