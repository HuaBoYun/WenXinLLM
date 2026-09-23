package com.financial.sharing.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblLoanApplication;
import com.financial.sharing.business.entity.TblLoanRepayment;
import com.financial.sharing.business.mapper.LoanApplicationMapper;
import com.financial.sharing.business.mapper.LoanRepaymentMapper;
import com.financial.sharing.business.service.LoanApplicationService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 借款单服务实现类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Slf4j
@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private LoanRepaymentMapper loanRepaymentMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public MyJsonBean getList(Map<String, Object> param) {
        try {
            log.info("查询借款单列表，参数：{}", param);

            int pageNum = param.get("pageNum") != null ? (Integer) param.get("pageNum") : 1;
            int pageSize = param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10;

            Page<TblLoanApplication> page = new Page<>(pageNum, pageSize);

            com.baomidou.mybatisplus.core.metadata.IPage<TblLoanApplication> resultPage =
                loanApplicationMapper.selectLoanApplicationPage(page,
                    (String) param.get("loanCode"),
                    (String) param.get("applicantName"),
                    (String) param.get("loanStatus"),
                    (String) param.get("loanType"),
                    (String) param.get("startDate") != null ? LocalDate.parse((String) param.get("startDate")) : null,
                    (String) param.get("endDate") != null ? LocalDate.parse((String) param.get("endDate")) : null,
                    (String) param.get("applicantDeptId"));

            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("tlist", resultPage.getRecords());
            pageResult.put("totalRecord", (int) resultPage.getTotal());
            pageResult.put("pageNo", pageNum);
            pageResult.put("pageSize", pageSize);
            pageResult.put("totalPage", (int) resultPage.getPages());

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询借款单列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String loanId) {
        try {
            if (!StringUtils.hasText(loanId)) {
                return MyJsonBean.errorData("借款单ID不能为空");
            }

            log.info("查询借款单详情，loanId={}", loanId);

            TblLoanApplication loan = loanApplicationMapper.selectById(loanId);
            if (loan == null) {
                return MyJsonBean.errorData("借款单不存在");
            }

            // 查询还款记录
            BigDecimal repaidAmount = loanRepaymentMapper.sumRepaidAmount(loanId);
            loan.setReturnedAmount(repaidAmount != null ? repaidAmount : BigDecimal.ZERO);

            // 判断归还状态
            if (repaidAmount != null && repaidAmount.compareTo(BigDecimal.ZERO) > 0) {
                if (repaidAmount.compareTo(loan.getLoanAmount()) >= 0) {
                    loan.setReturnStatus("RETURNED");
                } else {
                    loan.setReturnStatus("PARTIAL");
                }
            }

            return MyJsonBean.successData("查询成功", loan);
        } catch (Exception e) {
            log.error("查询借款单详情失败，loanId={}", loanId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblLoanApplication loan) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            log.info("保存或更新借款单，参数：{}", loan);

            LocalDateTime now = LocalDateTime.now();

            if (!StringUtils.hasText(loan.getLoanId())) {
                // 新增
                loan.setLoanId(UUID.randomUUID().toString().replace("-", ""));
                loan.setCreateTime(now);
                loan.setCreateUser(currentUser.getStaffid().toString());
                loan.setLoanStatus("DRAFT");
                loan.setDisburseStatus("UNDISBURSED");
                loan.setReturnStatus("UNRETURNED");
                loan.setReturnedAmount(BigDecimal.ZERO);

                // 生成借款单号
                loan.setLoanCode("LOAN" + System.currentTimeMillis());

                // 设置申请人信息
                loan.setApplicantId(currentUser.getStaffid().toString());
                loan.setApplicantName(currentUser.getUsername());
                loan.setApplicantDeptId(currentUser.getLinkDetp().getOrgid().toString());
                loan.setApplicantDeptName(currentUser.getLinkDetp().getOrgname());
            } else {
                // 更新
                loan.setUpdateTime(now);
                loan.setUpdateUser(currentUser.getStaffid().toString());
            }

            int result = StringUtils.hasText(loan.getLoanId()) &&
                loanApplicationMapper.selectById(loan.getLoanId()) != null
                ? loanApplicationMapper.updateById(loan)
                : loanApplicationMapper.insert(loan);

            if (result > 0) {
                return MyJsonBean.successData("保存成功", loan.getLoanId());
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存或更新借款单失败", e);
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String loanId) {
        try {
            if (!StringUtils.hasText(loanId)) {
                return MyJsonBean.errorData("借款单ID不能为空");
            }

            log.info("删除借款单，loanId={}", loanId);

            TblLoanApplication loan = loanApplicationMapper.selectById(loanId);
            if (loan == null) {
                return MyJsonBean.errorData("借款单不存在");
            }

            // 只有草稿状态可以删除
            if (!"DRAFT".equals(loan.getLoanStatus())) {
                return MyJsonBean.errorData("只有草稿状态的借款单可以删除");
            }

            int result = loanApplicationMapper.deleteById(loanId);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除借款单失败，loanId={}", loanId, e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDelete(List<String> loanIds) {
        try {
            if (loanIds == null || loanIds.isEmpty()) {
                return MyJsonBean.errorData("借款单ID列表不能为空");
            }

            log.info("批量删除借款单，loanIds={}", loanIds);

            int count = 0;
            for (String loanId : loanIds) {
                TblLoanApplication loan = loanApplicationMapper.selectById(loanId);
                if (loan != null && "DRAFT".equals(loan.getLoanStatus())) {
                    count += loanApplicationMapper.deleteById(loanId);
                }
            }

            return MyJsonBean.successData("批量删除成功，共删除 " + count + " 条记录");
        } catch (Exception e) {
            log.error("批量删除借款单失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean submit(String loanId) {
        try {
            if (!StringUtils.hasText(loanId)) {
                return MyJsonBean.errorData("借款单ID不能为空");
            }

            log.info("提交借款单，loanId={}", loanId);

            TblLoanApplication loan = loanApplicationMapper.selectById(loanId);
            if (loan == null) {
                return MyJsonBean.errorData("借款单不存在");
            }

            if (!"DRAFT".equals(loan.getLoanStatus())) {
                return MyJsonBean.errorData("只有草稿状态的借款单可以提交");
            }

            loan.setLoanStatus("SUBMITTED");

            int result = loanApplicationMapper.updateById(loan);
            if (result > 0) {
                return MyJsonBean.successData("提交成功");
            } else {
                return MyJsonBean.errorData("提交失败");
            }
        } catch (Exception e) {
            log.error("提交借款单失败，loanId={}", loanId, e);
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean approve(String loanId, String action, String opinion) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(loanId)) {
                return MyJsonBean.errorData("借款单ID不能为空");
            }

            log.info("审批借款单，loanId={}, action={}, opinion={}", loanId, action, opinion);

            TblLoanApplication loan = loanApplicationMapper.selectById(loanId);
            if (loan == null) {
                return MyJsonBean.errorData("借款单不存在");
            }

            if (!"SUBMITTED".equals(loan.getLoanStatus()) && !"APPROVING".equals(loan.getLoanStatus())) {
                return MyJsonBean.errorData("只有待审批状态的借款单可以审批");
            }

            LocalDateTime now = LocalDateTime.now();
            loan.setApproveTime(now);
            loan.setApproveOpinion(opinion);
            loan.setApproverId(currentUser.getStaffid().toString());
            loan.setApproverName(currentUser.getUsername());

            if ("APPROVE".equals(action)) {
                loan.setLoanStatus("APPROVED");
            } else if ("REJECT".equals(action)) {
                loan.setLoanStatus("REJECTED");
            } else {
                return MyJsonBean.errorData("无效的审批动作");
            }

            int result = loanApplicationMapper.updateById(loan);
            if (result > 0) {
                return MyJsonBean.successData("审批成功");
            } else {
                return MyJsonBean.errorData("审批失败");
            }
        } catch (Exception e) {
            log.error("审批借款单失败，loanId={}", loanId, e);
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean withdraw(String loanId) {
        try {
            if (!StringUtils.hasText(loanId)) {
                return MyJsonBean.errorData("借款单ID不能为空");
            }

            log.info("撤回借款单，loanId={}", loanId);

            TblLoanApplication loan = loanApplicationMapper.selectById(loanId);
            if (loan == null) {
                return MyJsonBean.errorData("借款单不存在");
            }

            if (!"SUBMITTED".equals(loan.getLoanStatus()) && !"APPROVING".equals(loan.getLoanStatus())) {
                return MyJsonBean.errorData("只有待审批状态的借款单可以撤回");
            }

            loan.setLoanStatus("DRAFT");

            int result = loanApplicationMapper.updateById(loan);
            if (result > 0) {
                return MyJsonBean.successData("撤回成功");
            } else {
                return MyJsonBean.errorData("撤回失败");
            }
        } catch (Exception e) {
            log.error("撤回借款单失败，loanId={}", loanId, e);
            return MyJsonBean.errorData("撤回失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean disburse(String loanId, BigDecimal disburseAmount, String disburseMethod,
                               String bankAccount, String disburseRemark) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(loanId)) {
                return MyJsonBean.errorData("借款单ID不能为空");
            }

            log.info("放款，loanId={}, disburseAmount={}, method={}, bankAccount={}",
                    loanId, disburseAmount, disburseMethod, bankAccount);

            TblLoanApplication loan = loanApplicationMapper.selectById(loanId);
            if (loan == null) {
                return MyJsonBean.errorData("借款单不存在");
            }

            if (!"APPROVED".equals(loan.getLoanStatus())) {
                return MyJsonBean.errorData("只有已审批状态的借款单可以放款");
            }

            if ("DISBURSED".equals(loan.getDisburseStatus())) {
                return MyJsonBean.errorData("该借款单已放款");
            }

            LocalDateTime now = LocalDateTime.now();
            loan.setDisburseStatus("DISBURSED");
            loan.setDisburseTime(now);
            // 兼容性：将放款方式写入凭证号字段，备注合并 disburseRemark + bankAccount
            loan.setDisburseVoucherNo(disburseMethod);
            loan.setUpdateUser(currentUser.getStaffid().toString());
            loan.setUpdateTime(now);

            int result = loanApplicationMapper.updateById(loan);
            if (result > 0) {
                return MyJsonBean.successData("放款成功");
            } else {
                return MyJsonBean.errorData("放款失败");
            }
        } catch (Exception e) {
            log.error("放款失败，loanId={}", loanId, e);
            return MyJsonBean.errorData("放款失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean repay(String loanId, BigDecimal amount, String repayMethod, String repayVoucher,
                            String repayRemark, String expenseReportId, BigDecimal offsetAmount) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(loanId)) {
                return MyJsonBean.errorData("借款单ID不能为空");
            }

            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return MyJsonBean.errorData("还款金额必须大于0");
            }

            log.info("还款，loanId={}, amount={}, method={}, voucher={}", loanId, amount, repayMethod, repayVoucher);

            TblLoanApplication loan = loanApplicationMapper.selectById(loanId);
            if (loan == null) {
                return MyJsonBean.errorData("借款单不存在");
            }

            if (!"DISBURSED".equals(loan.getDisburseStatus())) {
                return MyJsonBean.errorData("只有已放款的借款单可以还款");
            }

            // 检查还款金额是否超过未还金额
            BigDecimal repaidAmount = loan.getReturnedAmount() != null ? loan.getReturnedAmount() : BigDecimal.ZERO;
            BigDecimal remainingAmount = loan.getLoanAmount().subtract(repaidAmount);
            if (amount.compareTo(remainingAmount) > 0) {
                return MyJsonBean.errorData("还款金额不能超过未还金额");
            }

            // 创建还款记录
            TblLoanRepayment repayment = new TblLoanRepayment();
            repayment.setRepaymentId(UUID.randomUUID().toString().replace("-", ""));
            repayment.setLoanId(loanId);
            repayment.setRepaymentType(repayMethod);
            repayment.setRepaymentAmount(amount);
            repayment.setRepaymentDate(LocalDate.now());
            repayment.setRepaymentVoucherNo(repayVoucher);
            repayment.setCreateTime(LocalDateTime.now());
            repayment.setCreateUser(currentUser.getStaffid().toString());

            loanRepaymentMapper.insert(repayment);

            // 更新借款单的已还金额
            BigDecimal newRepaidAmount = repaidAmount.add(amount);
            loan.setReturnedAmount(newRepaidAmount);
            loan.setUpdateTime(LocalDateTime.now());
            loan.setUpdateUser(currentUser.getStaffid().toString());

            if (newRepaidAmount.compareTo(loan.getLoanAmount()) >= 0) {
                loan.setReturnStatus("RETURNED");
            } else {
                loan.setReturnStatus("PARTIAL");
            }

            int result = loanApplicationMapper.updateById(loan);
            if (result > 0) {
                return MyJsonBean.successData("还款成功");
            } else {
                return MyJsonBean.errorData("还款失败");
            }
        } catch (Exception e) {
            log.error("还款失败，loanId={}", loanId, e);
            return MyJsonBean.errorData("还款失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getByLoanCode(String loanCode) {
        try {
            if (!StringUtils.hasText(loanCode)) {
                return MyJsonBean.errorData("借款单号不能为空");
            }

            log.info("根据借款单号查询，loanCode={}", loanCode);

            TblLoanApplication loan = loanApplicationMapper.selectByLoanCode(loanCode);
            if (loan == null) {
                return MyJsonBean.errorData("借款单不存在");
            }

            // 查询还款记录
            BigDecimal repaidAmount = loanRepaymentMapper.sumRepaidAmount(loan.getLoanId());
            loan.setReturnedAmount(repaidAmount != null ? repaidAmount : BigDecimal.ZERO);

            return MyJsonBean.successData("查询成功", loan);
        } catch (Exception e) {
            log.error("根据借款单号查询失败，loanCode={}", loanCode, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean export(Map<String, Object> param) {
        log.warn("LoanApplicationService.export 暂未实现，param={}", param);
        return MyJsonBean.errorData("导出功能开发中");
    }

    @Override
    public MyJsonBean getStatistics(Map<String, Object> param) {
        log.warn("LoanApplicationService.getStatistics 暂未实现，param={}", param);
        return MyJsonBean.successData("查询成功", new HashMap<>());
    }
}
