package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BankLoanDTO;
import com.global.treasurer.dto.BankLoanQueryDTO;
import com.global.treasurer.entity.TblBankLoan;
import com.global.treasurer.mapper.BankLoanMapper;
import com.global.treasurer.service.BankLoanService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 银行贷款服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class BankLoanServiceImpl implements BankLoanService {
    @Autowired
    private BankLoanMapper bankLoanMapper;

    @Override
    public PageInfo<TblBankLoan> getLoanList(BankLoanQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("applicationNo", queryDTO.getApplicationNo());
        params.put("loanType", queryDTO.getLoanType());
        params.put("bankCode", queryDTO.getBankCode());
        params.put("applicationStatus", queryDTO.getApplicationStatus());
        params.put("companyId", queryDTO.getCompanyId());
        params.put("currencyCode", queryDTO.getCurrencyCode());
        List<TblBankLoan> list = bankLoanMapper.selectLoanList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblBankLoan getLoanById(Long loanId) {
        TblBankLoan loan = bankLoanMapper.selectLoanById(loanId);
        if (loan == null) {
            throw new ServiceException(404, "银行贷款不存在");
        }
        return loan;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblBankLoan saveLoan(BankLoanDTO dto) {
        TblBankLoan loan = new TblBankLoan();

        // 手动设置字段，避免BeanUtils类型转换问题
        loan.setLoanId(dto.getLoanId());
        loan.setApplicationNo(dto.getApplicationNo());
        loan.setPlanId(dto.getPlanId());
        loan.setLoanType(dto.getLoanType());
        // bankCode 是 String，bankId 是 Long，需要转换
        if (dto.getBankCode() != null && !dto.getBankCode().isEmpty()) {
            try {
                loan.setBankId(Long.parseLong(dto.getBankCode()));
            } catch (NumberFormatException e) {
                // 如果转换失败，设置为null
                loan.setBankId(null);
            }
        }
        loan.setBankName(dto.getBankName());
        loan.setLoanAmount(dto.getLoanAmount());
        loan.setCurrencyCode(dto.getCurrencyCode());
        loan.setLoanTerm(dto.getLoanTerm());
        loan.setTermUnit(dto.getTermUnit());
        loan.setInterestRate(dto.getInterestRate());
        loan.setRateType(dto.getRateType());
        loan.setRepaymentMethod(dto.getRepaymentMethod());
        loan.setGuaranteeType(dto.getGuaranteeType());
        loan.setGuaranteeValue(dto.getGuaranteeValue());
        loan.setLoanPurpose(dto.getLoanPurpose());
        loan.setApplicationStatus(dto.getApplicationStatus());
        loan.setCompanyId(dto.getCompanyId());
        loan.setCompanyName(dto.getCompanyName());
        loan.setRemark(dto.getRemark());

        if (dto.getLoanId() == null) {
            // 如果前端没有传递申请编号，则自动生成
            if (loan.getApplicationNo() == null || loan.getApplicationNo().isEmpty()) {
                loan.setApplicationNo(generateApplicationNo());
            }
            // 如果前端传递了状态，使用前端的值；否则默认为PENDING
            if (loan.getApplicationStatus() == null || loan.getApplicationStatus().isEmpty()) {
                loan.setApplicationStatus("PENDING");
            }
            loan.setApplicationDate(new Date());
            loan.setOutstandingAmount(loan.getLoanAmount() != null ? loan.getLoanAmount() : BigDecimal.ZERO);
            loan.setDeleteFlag(0);
            loan.setCreatedTime(new Date());
            bankLoanMapper.insert(loan);
        } else {
            loan.setUpdatedTime(new Date());
            bankLoanMapper.updateById(loan);
        }
        return loan;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLoan(Long loanId) {
        TblBankLoan loan = getLoanById(loanId);
        if (!"DRAFT".equals(loan.getApplicationStatus())) {
            throw new ServiceException(400, "只能删除草稿状态的贷款申请");
        }
        loan.setDeleteFlag(1);
        loan.setUpdatedTime(new Date());
        bankLoanMapper.updateById(loan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteLoans(List<Long> loanIds) {
        bankLoanMapper.batchDeleteByIds(loanIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long loanId) {
        TblBankLoan loan = getLoanById(loanId);
        if (!"DRAFT".equals(loan.getApplicationStatus())) {
            throw new ServiceException(400, "只能提交草稿状态的贷款申请");
        }
        bankLoanMapper.updateLoanStatus(loanId, "PENDING");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long loanId, String comments) {
        TblBankLoan loan = getLoanById(loanId);
        if (!"PENDING".equals(loan.getApplicationStatus())) {
            throw new ServiceException(400, "只能审批待审批状态的贷款申请");
        }
        loan.setApplicationStatus("APPROVED");
        loan.setApprovalDate(new Date());
        loan.setUpdatedTime(new Date());
        bankLoanMapper.updateById(loan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long loanId, String comments) {
        TblBankLoan loan = getLoanById(loanId);
        if (!"PENDING".equals(loan.getApplicationStatus())) {
            throw new ServiceException(400, "只能审批待审批状态的贷款申请");
        }
        loan.setApplicationStatus("REJECTED");
        loan.setApprovalDate(new Date());
        loan.setUpdatedTime(new Date());
        bankLoanMapper.updateById(loan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmDrawdown(Long loanId, Map<String, Object> params) {
        TblBankLoan loan = getLoanById(loanId);
        loan.setValueDate(new Date());
        loan.setDrawdownAmount(loan.getLoanAmount());
        loan.setOutstandingAmount(loan.getLoanAmount());
        loan.setApplicationStatus("ACTIVE");
        loan.setUpdatedTime(new Date());
        bankLoanMapper.updateById(loan);
    }

    @Override
    public Map<String, Object> getLoanSummary(Long companyId) {
        return bankLoanMapper.selectLoanSummary(companyId);
    }

    @Override
    public List<TblBankLoan> getExpiringLoans(Integer days) {
        return bankLoanMapper.selectExpiringLoans(days);
    }

    private String generateApplicationNo() {
        return "BL" + System.currentTimeMillis();
    }
}

