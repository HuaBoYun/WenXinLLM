package com.financial.sharing.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblPrepayment;
import com.financial.sharing.business.entity.TblPrepaymentRefund;
import com.financial.sharing.business.entity.TblPrepaymentWriteoff;
import com.financial.sharing.business.mapper.PrepaymentMapper;
import com.financial.sharing.business.mapper.PrepaymentRefundMapper;
import com.financial.sharing.business.mapper.PrepaymentWriteoffMapper;
import com.financial.sharing.business.service.PrepaymentServiceNew;
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
 * 预付款服务实现类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Slf4j
@Service("prepaymentServiceNew")
public class PrepaymentServiceNewImpl implements PrepaymentServiceNew {

    @Autowired
    private PrepaymentMapper prepaymentMapper;

    @Autowired
    private PrepaymentWriteoffMapper prepaymentWriteoffMapper;

    @Autowired
    private PrepaymentRefundMapper prepaymentRefundMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public MyJsonBean getList(Map<String, Object> param) {
        try {
            log.info("查询预付款列表，参数：{}", param);

            int pageNum = param.get("pageNum") != null ? (Integer) param.get("pageNum") : 1;
            int pageSize = param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10;

            Page<TblPrepayment> page = new Page<>(pageNum, pageSize);

            com.baomidou.mybatisplus.core.metadata.IPage<TblPrepayment> resultPage =
                prepaymentMapper.selectPrepaymentPage(page,
                    (String) param.get("prepaymentNumber"),
                    (String) param.get("applicant"),
                    (String) param.get("status"),
                    (String) param.get("prepaymentType"),
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
            log.error("查询预付款列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String prepaymentId) {
        try {
            if (!StringUtils.hasText(prepaymentId)) {
                return MyJsonBean.errorData("预付款ID不能为空");
            }

            log.info("查询预付款详情，prepaymentId={}", prepaymentId);

            TblPrepayment prepayment = prepaymentMapper.selectById(prepaymentId);
            if (prepayment == null) {
                return MyJsonBean.errorData("预付款不存在");
            }

            // 查询核销记录
            BigDecimal writeoffAmount = prepaymentWriteoffMapper.sumWriteoffAmount(prepaymentId);
            prepayment.setWriteoffAmount(writeoffAmount != null ? writeoffAmount : BigDecimal.ZERO);

            // 判断核销状态
            if (writeoffAmount != null && writeoffAmount.compareTo(BigDecimal.ZERO) > 0) {
                if (writeoffAmount.compareTo(prepayment.getPrepaymentAmount()) >= 0) {
                    prepayment.setWriteoffStatus("WRITEOFF");
                } else {
                    prepayment.setWriteoffStatus("PARTIAL");
                }
            }

            return MyJsonBean.successData("查询成功", prepayment);
        } catch (Exception e) {
            log.error("查询预付款详情失败，prepaymentId={}", prepaymentId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblPrepayment prepayment) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            log.info("保存或更新预付款，参数：{}", prepayment);

            LocalDateTime now = LocalDateTime.now();

            if (!StringUtils.hasText(prepayment.getPrepaymentId())) {
                // 新增
                prepayment.setPrepaymentId(UUID.randomUUID().toString().replace("-", ""));
                prepayment.setCreateTime(now);
                prepayment.setCreateUser(currentUser.getStaffid().toString());
                prepayment.setPrepaymentStatus("DRAFT");
                prepayment.setPaymentStatus("UNPAID");
                prepayment.setWriteoffStatus("UNWRITEOFF");
                prepayment.setWriteoffAmount(BigDecimal.ZERO);

                // 生成预付款单号
                prepayment.setPrepaymentCode("PREP" + System.currentTimeMillis());

                // 设置申请人信息
                prepayment.setApplicantId(currentUser.getStaffid().toString());
                prepayment.setApplicantName(currentUser.getUsername());
                prepayment.setApplicantDeptId(currentUser.getLinkDetp().getOrgid().toString());
                prepayment.setApplicantDeptName(currentUser.getLinkDetp().getOrgname());
            } else {
                // 更新
                prepayment.setUpdateTime(now);
                prepayment.setUpdateUser(currentUser.getStaffid().toString());
            }

            int result = StringUtils.hasText(prepayment.getPrepaymentId()) &&
                prepaymentMapper.selectById(prepayment.getPrepaymentId()) != null
                ? prepaymentMapper.updateById(prepayment)
                : prepaymentMapper.insert(prepayment);

            if (result > 0) {
                return MyJsonBean.successData("保存成功", prepayment.getPrepaymentId());
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存或更新预付款失败", e);
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String prepaymentId) {
        try {
            if (!StringUtils.hasText(prepaymentId)) {
                return MyJsonBean.errorData("预付款ID不能为空");
            }

            log.info("删除预付款，prepaymentId={}", prepaymentId);

            TblPrepayment prepayment = prepaymentMapper.selectById(prepaymentId);
            if (prepayment == null) {
                return MyJsonBean.errorData("预付款不存在");
            }

            // 只有草稿状态可以删除
            if (!"DRAFT".equals(prepayment.getPrepaymentStatus())) {
                return MyJsonBean.errorData("只有草稿状态的预付款可以删除");
            }

            int result = prepaymentMapper.deleteById(prepaymentId);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预付款失败，prepaymentId={}", prepaymentId, e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDelete(List<String> prepaymentIds) {
        try {
            if (prepaymentIds == null || prepaymentIds.isEmpty()) {
                return MyJsonBean.errorData("预付款ID列表不能为空");
            }

            log.info("批量删除预付款，prepaymentIds={}", prepaymentIds);

            int count = 0;
            for (String prepaymentId : prepaymentIds) {
                TblPrepayment prepayment = prepaymentMapper.selectById(prepaymentId);
                if (prepayment != null && "DRAFT".equals(prepayment.getPrepaymentStatus())) {
                    count += prepaymentMapper.deleteById(prepaymentId);
                }
            }

            return MyJsonBean.successData("批量删除成功，共删除 " + count + " 条记录");
        } catch (Exception e) {
            log.error("批量删除预付款失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean submit(String prepaymentId) {
        try {
            if (!StringUtils.hasText(prepaymentId)) {
                return MyJsonBean.errorData("预付款ID不能为空");
            }

            log.info("提交预付款，prepaymentId={}", prepaymentId);

            TblPrepayment prepayment = prepaymentMapper.selectById(prepaymentId);
            if (prepayment == null) {
                return MyJsonBean.errorData("预付款不存在");
            }

            if (!"DRAFT".equals(prepayment.getPrepaymentStatus())) {
                return MyJsonBean.errorData("只有草稿状态的预付款可以提交");
            }

            prepayment.setPrepaymentStatus("SUBMITTED");

            int result = prepaymentMapper.updateById(prepayment);
            if (result > 0) {
                return MyJsonBean.successData("提交成功");
            } else {
                return MyJsonBean.errorData("提交失败");
            }
        } catch (Exception e) {
            log.error("提交预付款失败，prepaymentId={}", prepaymentId, e);
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean approve(String prepaymentId, String action, String opinion) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(prepaymentId)) {
                return MyJsonBean.errorData("预付款ID不能为空");
            }

            log.info("审批预付款，prepaymentId={}, action={}, opinion={}", prepaymentId, action, opinion);

            TblPrepayment prepayment = prepaymentMapper.selectById(prepaymentId);
            if (prepayment == null) {
                return MyJsonBean.errorData("预付款不存在");
            }

            if (!"SUBMITTED".equals(prepayment.getPrepaymentStatus()) && !"APPROVING".equals(prepayment.getPrepaymentStatus())) {
                return MyJsonBean.errorData("只有待审批状态的预付款可以审批");
            }

            LocalDateTime now = LocalDateTime.now();
            prepayment.setApproveTime(now);
            prepayment.setApproveOpinion(opinion);
            prepayment.setApproverId(currentUser.getStaffid().toString());
            prepayment.setApproverName(currentUser.getUsername());

            if ("APPROVE".equals(action)) {
                prepayment.setPrepaymentStatus("APPROVED");
            } else if ("REJECT".equals(action)) {
                prepayment.setPrepaymentStatus("REJECTED");
            } else {
                return MyJsonBean.errorData("无效的审批动作");
            }

            int result = prepaymentMapper.updateById(prepayment);
            if (result > 0) {
                return MyJsonBean.successData("审批成功");
            } else {
                return MyJsonBean.errorData("审批失败");
            }
        } catch (Exception e) {
            log.error("审批预付款失败，prepaymentId={}", prepaymentId, e);
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean pay(String prepaymentId, String voucherNo) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(prepaymentId)) {
                return MyJsonBean.errorData("预付款ID不能为空");
            }

            log.info("支付，prepaymentId={}, voucherNo={}", prepaymentId, voucherNo);

            TblPrepayment prepayment = prepaymentMapper.selectById(prepaymentId);
            if (prepayment == null) {
                return MyJsonBean.errorData("预付款不存在");
            }

            if (!"APPROVED".equals(prepayment.getPrepaymentStatus())) {
                return MyJsonBean.errorData("只有已审批状态的预付款可以支付");
            }

            if ("PAID".equals(prepayment.getPaymentStatus())) {
                return MyJsonBean.errorData("该预付款已支付");
            }

            LocalDateTime now = LocalDateTime.now();
            prepayment.setPaymentStatus("PAID");
            prepayment.setPaymentTime(now);
            prepayment.setPaymentVoucherNo(voucherNo);
            prepayment.setUpdateUser(currentUser.getStaffid().toString());
            prepayment.setUpdateTime(now);

            int result = prepaymentMapper.updateById(prepayment);
            if (result > 0) {
                return MyJsonBean.successData("支付成功");
            } else {
                return MyJsonBean.errorData("支付失败");
            }
        } catch (Exception e) {
            log.error("支付失败，prepaymentId={}", prepaymentId, e);
            return MyJsonBean.errorData("支付失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean writeoff(String prepaymentId, BigDecimal amount, String relatedBillId, String contractId) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(prepaymentId)) {
                return MyJsonBean.errorData("预付款ID不能为空");
            }

            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return MyJsonBean.errorData("核销金额必须大于0");
            }

            log.info("核销，prepaymentId={}, amount={}, relatedBillId={}, contractId={}", prepaymentId, amount, relatedBillId, contractId);

            TblPrepayment prepayment = prepaymentMapper.selectById(prepaymentId);
            if (prepayment == null) {
                return MyJsonBean.errorData("预付款不存在");
            }

            if (!"PAID".equals(prepayment.getPaymentStatus())) {
                return MyJsonBean.errorData("只有已支付的预付款可以核销");
            }

            // 检查核销金额是否超过未核销金额
            BigDecimal writeoffAmount = prepayment.getWriteoffAmount() != null ? prepayment.getWriteoffAmount() : BigDecimal.ZERO;
            BigDecimal remainingAmount = prepayment.getPrepaymentAmount().subtract(writeoffAmount);
            if (amount.compareTo(remainingAmount) > 0) {
                return MyJsonBean.errorData("核销金额不能超过未核销金额");
            }

            // 创建核销记录
            TblPrepaymentWriteoff writeoff = new TblPrepaymentWriteoff();
            writeoff.setWriteoffId(UUID.randomUUID().toString().replace("-", ""));
            writeoff.setPrepaymentId(prepaymentId);
            writeoff.setWriteoffType("INVOICE");
            writeoff.setWriteoffAmount(amount);
            writeoff.setWriteoffDate(LocalDate.now());
            writeoff.setBusinessId(relatedBillId);
            writeoff.setCreateTime(LocalDateTime.now());
            writeoff.setCreateUser(currentUser.getStaffid().toString());

            prepaymentWriteoffMapper.insert(writeoff);

            // 更新预付款的已核销金额
            BigDecimal newWriteoffAmount = writeoffAmount.add(amount);
            prepayment.setWriteoffAmount(newWriteoffAmount);
            prepayment.setUpdateTime(LocalDateTime.now());
            prepayment.setUpdateUser(currentUser.getStaffid().toString());

            if (newWriteoffAmount.compareTo(prepayment.getPrepaymentAmount()) >= 0) {
                prepayment.setWriteoffStatus("WRITEOFF");
            } else {
                prepayment.setWriteoffStatus("PARTIAL");
            }

            int result = prepaymentMapper.updateById(prepayment);
            if (result > 0) {
                return MyJsonBean.successData("核销成功");
            } else {
                return MyJsonBean.errorData("核销失败");
            }
        } catch (Exception e) {
            log.error("核销失败，prepaymentId={}", prepaymentId, e);
            return MyJsonBean.errorData("核销失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean refund(String prepaymentId, BigDecimal amount, String reason) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(prepaymentId)) {
                return MyJsonBean.errorData("预付款ID不能为空");
            }

            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return MyJsonBean.errorData("退款金额必须大于0");
            }

            log.info("退款，prepaymentId={}, amount={}, reason={}", prepaymentId, amount, reason);

            TblPrepayment prepayment = prepaymentMapper.selectById(prepaymentId);
            if (prepayment == null) {
                return MyJsonBean.errorData("预付款不存在");
            }

            // 创建退款记录
            TblPrepaymentRefund refund = new TblPrepaymentRefund();
            refund.setRefundId(UUID.randomUUID().toString().replace("-", ""));
            refund.setPrepaymentId(prepaymentId);
            refund.setRefundAmount(amount);
            refund.setRefundDate(LocalDate.now());
            refund.setRefundReason(reason);
            refund.setCreateTime(LocalDateTime.now());
            refund.setCreateUser(currentUser.getStaffid().toString());

            prepaymentRefundMapper.insert(refund);

            // 更新预付款状态为已退款
            prepayment.setPrepaymentStatus("CANCELLED");
            prepayment.setUpdateTime(LocalDateTime.now());
            prepayment.setUpdateUser(currentUser.getStaffid().toString());

            int result = prepaymentMapper.updateById(prepayment);
            if (result > 0) {
                return MyJsonBean.successData("退款成功");
            } else {
                return MyJsonBean.errorData("退款失败");
            }
        } catch (Exception e) {
            log.error("退款失败，prepaymentId={}", prepaymentId, e);
            return MyJsonBean.errorData("退款失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getByPrepaymentCode(String prepaymentCode) {
        try {
            if (!StringUtils.hasText(prepaymentCode)) {
                return MyJsonBean.errorData("预付款单号不能为空");
            }

            log.info("根据预付款单号查询，prepaymentCode={}", prepaymentCode);

            TblPrepayment prepayment = prepaymentMapper.selectByPrepaymentCode(prepaymentCode);
            if (prepayment == null) {
                return MyJsonBean.errorData("预付款不存在");
            }

            // 查询核销记录
            BigDecimal writeoffAmount = prepaymentWriteoffMapper.sumWriteoffAmount(prepayment.getPrepaymentId());
            prepayment.setWriteoffAmount(writeoffAmount != null ? writeoffAmount : BigDecimal.ZERO);

            return MyJsonBean.successData("查询成功", prepayment);
        } catch (Exception e) {
            log.error("根据预付款单号查询失败，prepaymentCode={}", prepaymentCode, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean export(Map<String, Object> param) {
        log.warn("PrepaymentServiceNew.export 暂未实现，param={}", param);
        return MyJsonBean.errorData("导出功能开发中");
    }

    @Override
    public MyJsonBean getStatistics(Map<String, Object> param) {
        log.warn("PrepaymentServiceNew.getStatistics 暂未实现，param={}", param);
        return MyJsonBean.successData("查询成功", new HashMap<>());
    }
}
