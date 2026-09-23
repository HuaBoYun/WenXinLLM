package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblLoanChange;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 借款变更记录 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizLoanChangeMapper")
public interface LoanChangeMapper extends BaseMapper<TblLoanChange> {

    /**
     * 根据借款单ID查询变更记录
     *
     * @param loanId 借款单ID
     * @return 变更记录列表
     */
    List<TblLoanChange> selectByLoanId(@Param("loanId") String loanId);
}
