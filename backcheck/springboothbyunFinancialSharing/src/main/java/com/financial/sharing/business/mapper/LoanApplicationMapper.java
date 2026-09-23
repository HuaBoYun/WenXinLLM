package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblLoanApplication;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 借款单 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizLoanApplicationMapper")
public interface LoanApplicationMapper extends BaseMapper<TblLoanApplication> {

    /**
     * 分页查询借款单
     *
     * @param page 分页参数
     * @param loanCode 借款单号
     * @param applicantName 申请人
     * @param loanStatus 状态
     * @param loanType 借款类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param applicantDeptId 部门ID
     * @return 分页结果
     */
    IPage<TblLoanApplication> selectLoanApplicationPage(Page<TblLoanApplication> page,
                                                        @Param("loanCode") String loanCode,
                                                        @Param("applicantName") String applicantName,
                                                        @Param("loanStatus") String loanStatus,
                                                        @Param("loanType") String loanType,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate,
                                                        @Param("applicantDeptId") String applicantDeptId);

    /**
     * 根据借款单号查询
     *
     * @param loanCode 借款单号
     * @return 借款单
     */
    TblLoanApplication selectByLoanCode(@Param("loanCode") String loanCode);

    /**
     * 批量删除借款单
     *
     * @param ids 借款单ID列表
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<String> ids);
}
