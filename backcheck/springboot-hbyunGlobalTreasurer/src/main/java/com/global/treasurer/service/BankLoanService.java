package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BankLoanDTO;
import com.global.treasurer.dto.BankLoanQueryDTO;
import com.global.treasurer.entity.TblBankLoan;

import java.util.List;
import java.util.Map;

/**
 * 银行贷款服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface BankLoanService {

    /**
     * 分页查询银行贷款列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblBankLoan> getLoanList(BankLoanQueryDTO queryDTO);

    /**
     * 根据ID查询银行贷款详情
     *
     * @param loanId 贷款ID
     * @return 银行贷款
     */
    TblBankLoan getLoanById(Long loanId);

    /**
     * 保存银行贷款（新增或更新）
     *
     * @param dto 银行贷款DTO
     * @return 保存后的银行贷款
     */
    TblBankLoan saveLoan(BankLoanDTO dto);

    /**
     * 删除银行贷款
     *
     * @param loanId 贷款ID
     */
    void deleteLoan(Long loanId);

    /**
     * 批量删除银行贷款
     *
     * @param loanIds 贷款ID列表
     */
    void batchDeleteLoans(List<Long> loanIds);

    /**
     * 提交审批
     *
     * @param loanId 贷款ID
     */
    void submitForApproval(Long loanId);

    /**
     * 审批通过
     *
     * @param loanId 贷款ID
     * @param comments 审批意见
     */
    void approve(Long loanId, String comments);

    /**
     * 审批拒绝
     *
     * @param loanId 贷款ID
     * @param comments 审批意见
     */
    void reject(Long loanId, String comments);

    /**
     * 放款确认
     *
     * @param loanId 贷款ID
     * @param params 放款参数
     */
    void confirmDrawdown(Long loanId, Map<String, Object> params);

    /**
     * 查询贷款汇总统计
     *
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> getLoanSummary(Long companyId);

    /**
     * 查询即将到期的贷款
     *
     * @param days 天数
     * @return 贷款列表
     */
    List<TblBankLoan> getExpiringLoans(Integer days);
}

