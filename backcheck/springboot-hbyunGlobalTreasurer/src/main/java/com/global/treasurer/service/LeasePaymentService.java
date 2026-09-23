package com.global.treasurer.service;

import com.global.treasurer.entity.TblLeasePayment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 租金计划服务接口
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
public interface LeasePaymentService {

    /**
     * 根据租赁ID获取租金计划列表
     */
    List<TblLeasePayment> getPaymentsByLeaseId(Long leaseId);

    /**
     * 根据付款ID获取付款详情
     */
    TblLeasePayment getPaymentById(Long paymentId);

    /**
     * 保存租金计划（新增或更新）
     */
    TblLeasePayment savePayment(TblLeasePayment payment);

    /**
     * 确认付款
     */
    void confirmPayment(Long paymentId, BigDecimal paidAmount, Date paidDate);

    /**
     * 批量确认付款
     */
    void batchConfirmPayments(List<Long> paymentIds, Date paidDate);

    /**
     * 根据租赁ID删除所有租金计划
     */
    void deletePaymentsByLeaseId(Long leaseId);

    /**
     * 获取租金汇总信息
     */
    Map<String, Object> getPaymentSummary(Long leaseId);

    /**
     * 获取逾期租金列表
     */
    List<TblLeasePayment> getOverduePayments(Long leaseId);

    /**
     * 生成租金计划
     */
    void generatePaymentSchedule(Long leaseId, BigDecimal totalAmount, Integer periods, 
                                 BigDecimal interestRate, Date startDate);

    /**
     * 批量插入租金计划
     */
    void batchInsertPayments(List<TblLeasePayment> payments);
}

