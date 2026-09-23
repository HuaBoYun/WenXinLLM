package com.financial.sharing.business.service;

import com.financial.sharing.business.entity.TblPrepayment;
import com.financial.sharing.util.MyJsonBean;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 预付款服务接口（新）
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
public interface PrepaymentServiceNew {

    /**
     * 分页查询预付款列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean getList(Map<String, Object> param);

    /**
     * 根据ID查询预付款详情
     *
     * @param prepaymentId 预付款ID
     * @return 预付款详情
     */
    MyJsonBean getById(String prepaymentId);

    /**
     * 保存或更新预付款
     *
     * @param prepayment 预付款对象
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblPrepayment prepayment);

    /**
     * 删除预付款
     *
     * @param prepaymentId 预付款ID
     * @return 操作结果
     */
    MyJsonBean delete(String prepaymentId);

    /**
     * 批量删除预付款
     *
     * @param prepaymentIds 预付款ID列表
     * @return 操作结果
     */
    MyJsonBean batchDelete(java.util.List<String> prepaymentIds);

    /**
     * 提交预付款
     *
     * @param prepaymentId 预付款ID
     * @return 操作结果
     */
    MyJsonBean submit(String prepaymentId);

    /**
     * 审批预付款
     *
     * @param prepaymentId 预付款ID
     * @param action 审批动作 (APPROVE-通过，REJECT-拒绝)
     * @param opinion 审批意见
     * @return 操作结果
     */
    MyJsonBean approve(String prepaymentId, String action, String opinion);

    /**
     * 支付
     *
     * @param prepaymentId 预付款ID
     * @param voucherNo 支付凭证号
     * @return 操作结果
     */
    MyJsonBean pay(String prepaymentId, String voucherNo);

    /**
     * 核销
     *
     * @param prepaymentId 预付款ID
     * @param amount 核销金额
     * @param relatedBillId 关联单据ID
     * @return 操作结果
     */
    MyJsonBean writeoff(String prepaymentId, BigDecimal amount, String relatedBillId, String contractId);

    /**
     * 退款
     *
     * @param prepaymentId 预付款ID
     * @param amount 退款金额
     * @param reason 退款原因
     * @return 操作结果
     */
    MyJsonBean refund(String prepaymentId, BigDecimal amount, String reason);

    /**
     * 根据预付款单号查询
     *
     * @param prepaymentCode 预付款单号
     * @return 预付款详情
     */
    MyJsonBean getByPrepaymentCode(String prepaymentCode);

    /**
     * 导出预付款
     */
    MyJsonBean export(Map<String, Object> param);

    /**
     * 预付款统计
     */
    MyJsonBean getStatistics(Map<String, Object> param);
}
