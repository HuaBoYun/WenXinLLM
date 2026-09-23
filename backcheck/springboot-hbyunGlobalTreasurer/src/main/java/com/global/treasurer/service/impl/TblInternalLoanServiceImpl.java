package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblInternalLoan;
import com.global.treasurer.mapper.TblInternalLoanMapper;
import com.global.treasurer.service.TblInternalLoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 内部借贷Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblInternalLoanServiceImpl implements TblInternalLoanService {
    private static final Logger log = LoggerFactory.getLogger(TblInternalLoanServiceImpl.class);

    @Resource
    private TblInternalLoanMapper tblInternalLoanMapper;

    @Override
    public PageInfo<TblInternalLoan> getLoanPage(Integer pageNum, Integer pageSize,
                                                 String loanNo, String loanStatus,
                                                 String startDate, String endDate) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblInternalLoan> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(loanNo), TblInternalLoan::getLoanCode, loanNo)
               .eq(StringUtils.isNotBlank(loanStatus), TblInternalLoan::getLoanStatus, loanStatus)
               .orderByDesc(TblInternalLoan::getCreateTime);
        List<TblInternalLoan> list = tblInternalLoanMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblInternalLoan getLoanById(String loanId) {
        return tblInternalLoanMapper.selectById(loanId);
    }

    @Override
    public TblInternalLoan saveLoan(TblInternalLoan loan) {
        // loanId 为 null 或空字符串时手动生成 UUID（达梦数据库不支持 ASSIGN_UUID 自动生成）
        if (loan.getLoanId() == null || loan.getLoanId().trim().isEmpty()) {
            loan.setLoanId(java.util.UUID.randomUUID().toString().replace("-", ""));
        }
        loan.setLoanCode("IL" + System.currentTimeMillis());
        loan.setCreateTime(new Date());
        if (loan.getLoanStatus() == null) {
            loan.setLoanStatus("DRAFT");
        }
        // 使用自定义 insertLoan，显式指定 jdbcType，兼容达梦数据库 JDBC 驱动
        tblInternalLoanMapper.insertLoan(loan);
        return loan;
    }

    @Override
    public void updateLoan(TblInternalLoan loan) {
        loan.setUpdateTime(new Date());
        tblInternalLoanMapper.updateById(loan);
    }

    @Override
    public void submitLoan(String loanId) {
        TblInternalLoan loan = new TblInternalLoan();
        loan.setLoanId(loanId);
        loan.setLoanStatus("PENDING_APPROVAL");
        loan.setUpdateTime(new Date());
        tblInternalLoanMapper.updateById(loan);
    }

    @Override
    public void approveLoan(String loanId, String approveResult, String approveRemark) {
        TblInternalLoan loan = new TblInternalLoan();
        loan.setLoanId(loanId);
        loan.setApprovalStatus(approveResult);
        loan.setApprovalTime(new Date());
        loan.setUpdateTime(new Date());
        if ("APPROVED".equals(approveResult)) {
            loan.setLoanStatus("APPROVED");
        } else {
            loan.setLoanStatus("REJECTED");
        }
        tblInternalLoanMapper.updateById(loan);
    }

    @Override
    public String disburseLoan(String loanId) {
        TblInternalLoan loan = tblInternalLoanMapper.selectById(loanId);
        if (loan == null) {
            return null;
        }
        loan.setLoanStatus("DISBURSED");
        loan.setUpdateTime(new Date());
        tblInternalLoanMapper.updateById(loan);
        log.info("放款成功: {}", loan.getLoanCode());
        return loan.getLoanCode();
    }

    @Override
    public String repayLoan(String loanId, String repayAmount) {
        TblInternalLoan loan = tblInternalLoanMapper.selectById(loanId);
        if (loan == null) {
            return null;
        }
        // 更新实际还款日期和状态
        loan.setActualRepayDate(new Date());
        loan.setLoanStatus("REPAID");
        loan.setUpdateTime(new Date());
        tblInternalLoanMapper.updateById(loan);
        return loan.getLoanCode();
    }

    @Override
    public void cancelLoan(String loanId) {
        TblInternalLoan loan = new TblInternalLoan();
        loan.setLoanId(loanId);
        loan.setLoanStatus("CANCELLED");
        loan.setUpdateTime(new Date());
        tblInternalLoanMapper.updateById(loan);
    }

    @Override
    public void deleteLoan(String loanId) {
        TblInternalLoan loan = tblInternalLoanMapper.selectById(loanId);
        if (loan == null) {
            throw new RuntimeException("借贷记录不存在");
        }
        if (!"DRAFT".equals(loan.getLoanStatus())) {
            throw new RuntimeException("只能删除草稿状态的借贷申请");
        }
        tblInternalLoanMapper.deleteById(loanId);
    }

    @Override
    public List<TblInternalLoan> getPendingApprovalList() {
        LambdaQueryWrapper<TblInternalLoan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblInternalLoan::getLoanStatus, "PENDING_APPROVAL");
        return tblInternalLoanMapper.selectList(wrapper);
    }

    @Override
    public List<TblInternalLoan> getPendingRepaymentList() {
        LambdaQueryWrapper<TblInternalLoan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblInternalLoan::getLoanStatus, "DISBURSED");
        return tblInternalLoanMapper.selectList(wrapper);
    }

    @Override
    public long count() {
        LambdaQueryWrapper<TblInternalLoan> wrapper = new LambdaQueryWrapper<>();
        return tblInternalLoanMapper.selectCount(wrapper);
    }

    @Override
    public long countByStatus(String status) {
        LambdaQueryWrapper<TblInternalLoan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblInternalLoan::getLoanStatus, status);
        return tblInternalLoanMapper.selectCount(wrapper);
    }

    @Override
    public long countTodayApplications() {
        LambdaQueryWrapper<TblInternalLoan> wrapper = new LambdaQueryWrapper<>();
        // 简化实现：统计所有申请
        return tblInternalLoanMapper.selectCount(wrapper);
    }
}

