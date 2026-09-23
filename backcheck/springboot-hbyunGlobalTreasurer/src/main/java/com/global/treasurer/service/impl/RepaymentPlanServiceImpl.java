package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RepaymentPlanDTO;
import com.global.treasurer.entity.TblRepaymentPlan;
import com.global.treasurer.mapper.RepaymentPlanMapper;
import com.global.treasurer.service.RepaymentPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 还款计划Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@Service
public class RepaymentPlanServiceImpl implements RepaymentPlanService {

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    @Override
    public PageInfo<TblRepaymentPlan> getPlanList(RepaymentPlanDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("loanId", dto.getLoanId());
        params.put("contractId", dto.getContractId());
        params.put("paymentStatus", dto.getPaymentStatus());
        List<TblRepaymentPlan> list = repaymentPlanMapper.selectPlanList(params);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblRepaymentPlan> getPlansByLoanId(String loanId) {
        return repaymentPlanMapper.selectByLoanId(loanId);
    }

    @Override
    public TblRepaymentPlan getPlanById(Long planId) {
        return repaymentPlanMapper.selectById(planId);
    }

    @Override
    @Transactional
    public TblRepaymentPlan savePlan(RepaymentPlanDTO dto) {
        TblRepaymentPlan plan = new TblRepaymentPlan();
        BeanUtils.copyProperties(dto, plan);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (StringUtils.hasText(dto.getDueDate())) {
                plan.setDueDate(sdf.parse(dto.getDueDate()));
            }
            if (StringUtils.hasText(dto.getPaymentDate())) {
                plan.setPaymentDate(sdf.parse(dto.getPaymentDate()));
            }
        } catch (Exception e) {
            throw new RuntimeException("日期格式错误", e);
        }

        if (dto.getPlanId() != null) {
            plan.setUpdatedTime(new Date());
            repaymentPlanMapper.updateById(plan);
        } else {
            if (!StringUtils.hasText(plan.getPaymentStatus())) {
                plan.setPaymentStatus("PENDING");
            }
            plan.setDeleteFlag(0);
            plan.setCreatedTime(new Date());
            repaymentPlanMapper.insert(plan);
        }
        return plan;
    }

    @Override
    @Transactional
    public void deletePlan(Long planId) {
        TblRepaymentPlan plan = repaymentPlanMapper.selectById(planId);
        if (plan != null) {
            plan.setDeleteFlag(1);
            plan.setUpdatedTime(new Date());
            repaymentPlanMapper.updateById(plan);
        }
    }

    @Override
    @Transactional
    public List<TblRepaymentPlan> generateRepaymentPlans(RepaymentPlanDTO dto) {
        // 先删除原有还款计划
        repaymentPlanMapper.deleteByLoanId(dto.getLoanId() != null ? dto.getLoanId().toString() : null);

        List<TblRepaymentPlan> plans = new ArrayList<>();
        BigDecimal loanAmount = dto.getLoanAmount();
        BigDecimal annualRate = dto.getInterestRate();
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                                           .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        int periods = dto.getPeriods();
        String method = dto.getRepaymentMethod();

        Calendar calendar = Calendar.getInstance();
        BigDecimal remainingPrincipal = loanAmount;

        for (int i = 1; i <= periods; i++) {
            TblRepaymentPlan plan = new TblRepaymentPlan();
            plan.setLoanId(dto.getLoanId());
            plan.setContractId(dto.getContractId());
            plan.setPeriodNo(i);

            calendar.add(Calendar.MONTH, 1);
            plan.setDueDate(calendar.getTime());

            BigDecimal principal, interest;
            if ("EQUAL_PRINCIPAL".equals(method)) {
                // 等额本金
                principal = loanAmount.divide(BigDecimal.valueOf(periods), 2, RoundingMode.HALF_UP);
                interest = remainingPrincipal.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            } else {
                // 等额本息
                BigDecimal factor = monthlyRate.add(BigDecimal.ONE).pow(periods);
                BigDecimal monthlyPayment = loanAmount.multiply(monthlyRate).multiply(factor)
                        .divide(factor.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
                interest = remainingPrincipal.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
                principal = monthlyPayment.subtract(interest);
            }

            plan.setPrincipalAmount(principal);
            plan.setInterestAmount(interest);
            plan.setTotalAmount(principal.add(interest));
            plan.setPaidPrincipal(BigDecimal.ZERO);
            plan.setPaidInterest(BigDecimal.ZERO);
            plan.setPaidTotal(BigDecimal.ZERO);
            remainingPrincipal = remainingPrincipal.subtract(principal);
            plan.setRemainingPrincipal(remainingPrincipal.max(BigDecimal.ZERO));
            plan.setPaymentStatus("PENDING");
            plan.setOverdueDays(0);
            plan.setOverdueInterest(BigDecimal.ZERO);
            plan.setCurrencyCode(dto.getCurrencyCode() != null ? dto.getCurrencyCode() : "CNY");
            plan.setCompanyId(dto.getCompanyId());
            plan.setCompanyName(dto.getCompanyName());
            plan.setDeleteFlag(0);
            plan.setCreatedTime(new Date());

            repaymentPlanMapper.insert(plan);
            plans.add(plan);
        }
        return plans;
    }

    @Override
    @Transactional
    public void executeRepayment(Long planId, BigDecimal payAmount) {
        TblRepaymentPlan plan = repaymentPlanMapper.selectById(planId);
        if (plan == null) {
            throw new RuntimeException("还款计划不存在");
        }

        BigDecimal unpaidTotal = plan.getTotalAmount().subtract(plan.getPaidTotal());
        if (payAmount.compareTo(unpaidTotal) > 0) {
            payAmount = unpaidTotal;
        }

        // 先还利息，再还本金
        BigDecimal unpaidInterest = plan.getInterestAmount().subtract(plan.getPaidInterest());
        BigDecimal paidInterest = payAmount.min(unpaidInterest);
        BigDecimal paidPrincipal = payAmount.subtract(paidInterest);

        plan.setPaidInterest(plan.getPaidInterest().add(paidInterest));
        plan.setPaidPrincipal(plan.getPaidPrincipal().add(paidPrincipal));
        plan.setPaidTotal(plan.getPaidTotal().add(payAmount));
        plan.setPaymentDate(new Date());

        // 更新状态
        if (plan.getPaidTotal().compareTo(plan.getTotalAmount()) >= 0) {
            plan.setPaymentStatus("PAID");
        } else {
            plan.setPaymentStatus("PARTIAL");
        }

        plan.setUpdatedTime(new Date());
        repaymentPlanMapper.updateById(plan);
    }

    @Override
    public List<TblRepaymentPlan> getPendingPlans(String loanId) {
        return repaymentPlanMapper.selectPendingPlans(loanId);
    }

    @Override
    public List<TblRepaymentPlan> getOverduePlans(String loanId) {
        return repaymentPlanMapper.selectOverduePlans(loanId);
    }

    @Override
    @Transactional
    public void updateOverdueStatus() {
        // 查询所有待还款且已过期的计划
        Map<String, Object> params = new HashMap<>();
        params.put("paymentStatus", "PENDING");
        List<TblRepaymentPlan> plans = repaymentPlanMapper.selectPlanList(params);

        Date today = new Date();
        for (TblRepaymentPlan plan : plans) {
            if (plan.getDueDate() != null && plan.getDueDate().before(today)) {
                long diffDays = (today.getTime() - plan.getDueDate().getTime()) / (1000 * 60 * 60 * 24);
                plan.setOverdueDays((int) diffDays);
                plan.setPaymentStatus("OVERDUE");
                plan.setUpdatedTime(new Date());
                repaymentPlanMapper.updateById(plan);
            }
        }
    }
}