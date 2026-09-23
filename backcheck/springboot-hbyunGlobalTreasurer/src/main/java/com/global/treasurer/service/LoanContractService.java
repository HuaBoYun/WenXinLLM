package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.LoanContractDTO;
import com.global.treasurer.entity.TblLoanContract;

import java.util.List;

/**
 * 贷款合同Service接口
 * 对应数据库表: TBL_LOAN_CONTRACT
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
public interface LoanContractService {

    /**
     * 分页查询合同列表
     */
    PageInfo<TblLoanContract> getContractList(LoanContractDTO dto);

    /**
     * 根据贷款ID查询合同列表
     */
    List<TblLoanContract> getContractsByLoanId(String loanId);

    /**
     * 根据ID查询合同详情
     */
    TblLoanContract getContractById(String contractId);

    /**
     * 保存合同（新增或更新）
     */
    TblLoanContract saveContract(LoanContractDTO dto);

    /**
     * 删除合同
     */
    void deleteContract(String contractId);

    /**
     * 更新合同状态
     */
    void updateContractStatus(String contractId, String status);

    /**
     * 生成合同编号
     */
    String generateContractNo();
}

