package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblLoanRepayment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 借款还款记录 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizLoanRepaymentMapper")
public interface LoanRepaymentMapper extends BaseMapper<TblLoanRepayment> {

    /**
     * 根据借款单ID查询还款记录
     *
     * @param loanId 借款单ID
     * @return 还款记录列表
     */
    List<TblLoanRepayment> selectByLoanId(@Param("loanId") String loanId);

    /**
     * 统计已还金额
     *
     * @param loanId 借款单ID
     * @return 已还金额
     */
    java.math.BigDecimal sumRepaidAmount(@Param("loanId") String loanId);
}
