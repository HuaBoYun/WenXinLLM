package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblLeasePayment;
import com.global.treasurer.mapper.LeasePaymentMapper;
import com.global.treasurer.service.LeasePaymentService;
import com.global.treasurer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 租金计划服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
@Service
public class LeasePaymentServiceImpl implements LeasePaymentService {

    private static final Logger log = LoggerFactory.getLogger(LeasePaymentServiceImpl.class);

    @Autowired
    private LeasePaymentMapper leasePaymentMapper;

    @Override
    public List<TblLeasePayment> getPaymentsByLeaseId(Long leaseId) {
        return leasePaymentMapper.selectByLeaseId(leaseId);
    }

    @Override
    public TblLeasePayment getPaymentById(Long paymentId) {
        TblLeasePayment payment = leasePaymentMapper.selectByPaymentId(paymentId);
        if (payment == null) {
            throw new ServiceException(404, "租金计划不存在");
        }
        return payment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblLeasePayment savePayment(TblLeasePayment payment) {
        if (payment.getPaymentId() == null) {
            payment.setStatus("PENDING");
            payment.setCreatedTime(new Date());
            leasePaymentMapper.insert(payment);
            log.info("新增租金计划成功, paymentId: {}", payment.getPaymentId());
        } else {
            payment.setUpdatedTime(new Date());
            leasePaymentMapper.updateById(payment);
            log.info("更新租金计划成功, paymentId: {}", payment.getPaymentId());
        }
        return payment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPayment(Long paymentId, BigDecimal paidAmount, Date paidDate) {
        leasePaymentMapper.updatePaymentStatus(paymentId, "PAID", paidAmount, paidDate);
        log.info("确认付款成功, paymentId: {}", paymentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchConfirmPayments(List<Long> paymentIds, Date paidDate) {
        if (paymentIds != null && !paymentIds.isEmpty()) {
            leasePaymentMapper.batchUpdatePaymentStatus(paymentIds, "PAID", paidDate);
            log.info("批量确认付款成功, count: {}", paymentIds.size());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePaymentsByLeaseId(Long leaseId) {
        leasePaymentMapper.deleteByLeaseId(leaseId);
        log.info("删除租赁所有租金计划成功, leaseId: {}", leaseId);
    }

    @Override
    public Map<String, Object> getPaymentSummary(Long leaseId) {
        return leasePaymentMapper.selectPaymentSummary(leaseId);
    }

    @Override
    public List<TblLeasePayment> getOverduePayments(Long leaseId) {
        return leasePaymentMapper.selectOverduePayments(leaseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generatePaymentSchedule(Long leaseId, BigDecimal totalAmount, Integer periods,
                                        BigDecimal interestRate, Date startDate) {
        // 删除现有计划
        leasePaymentMapper.deleteByLeaseId(leaseId);
        
        // 计算每期本金和利息（等额本息）
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal principal = totalAmount.divide(BigDecimal.valueOf(periods), 2, RoundingMode.HALF_UP);
        
        List<TblLeasePayment> payments = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        BigDecimal remainingPrincipal = totalAmount;
        for (int i = 1; i <= periods; i++) {
            TblLeasePayment payment = new TblLeasePayment();
            payment.setLeaseId(leaseId);
            payment.setPeriod(i);
            cal.add(Calendar.MONTH, 1);
            payment.setDueDate(cal.getTime());
            payment.setPrincipal(principal);
            BigDecimal interest = remainingPrincipal.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            payment.setInterest(interest);
            payment.setAmount(principal.add(interest));
            payment.setStatus("PENDING");
            payment.setCreatedTime(new Date());
            payments.add(payment);
            remainingPrincipal = remainingPrincipal.subtract(principal);
        }
        
        batchInsertPayments(payments);
        log.info("生成租金计划成功, leaseId: {}, periods: {}", leaseId, periods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsertPayments(List<TblLeasePayment> payments) {
        if (payments != null && !payments.isEmpty()) {
            leasePaymentMapper.batchInsert(payments);
        }
    }
}

