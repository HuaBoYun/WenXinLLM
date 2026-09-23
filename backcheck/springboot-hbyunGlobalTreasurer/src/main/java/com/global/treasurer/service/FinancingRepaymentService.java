package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingRepaymentDTO;
import com.global.treasurer.dto.FinancingRepaymentQueryDTO;
import com.global.treasurer.entity.TblFinancingRepayment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 融资还款服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface FinancingRepaymentService {

    PageInfo<TblFinancingRepayment> getRepaymentList(FinancingRepaymentQueryDTO queryDTO);

    TblFinancingRepayment getRepaymentById(Long repaymentId);

    List<TblFinancingRepayment> getRepaymentsByFinancingId(Long financingId);

    TblFinancingRepayment saveRepayment(FinancingRepaymentDTO dto);

    void deleteRepayment(Long repaymentId);

    void batchDeleteRepayments(List<Long> repaymentIds);

    void submitForApproval(Long repaymentId);

    void approve(Long repaymentId, String comments);

    void reject(Long repaymentId, String comments);

    void confirmRepayment(Long repaymentId, BigDecimal actualAmount);

    List<TblFinancingRepayment> getUpcomingRepayments(Integer days);

    List<TblFinancingRepayment> getOverdueRepayments();

    Map<String, Object> getRepaymentSummary(Long companyId);
}

