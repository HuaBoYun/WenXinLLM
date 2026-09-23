package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblLoanContract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 贷款合同Mapper接口
 * 对应数据库表: TBL_LOAN_CONTRACT
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@Mapper
public interface LoanContractMapper extends BaseMapper<TblLoanContract> {

    /**
     * 查询合同列表
     */
    List<TblLoanContract> selectContractList(Map<String, Object> params);

    /**
     * 根据贷款ID查询合同列表
     */
    List<TblLoanContract> selectByLoanId(@Param("loanId") String loanId);

    /**
     * 根据合同编号查询
     */
    TblLoanContract selectByContractNo(@Param("contractNo") String contractNo);

    /**
     * 统计贷款的合同数量
     */
    int countByLoanId(@Param("loanId") String loanId);
}

