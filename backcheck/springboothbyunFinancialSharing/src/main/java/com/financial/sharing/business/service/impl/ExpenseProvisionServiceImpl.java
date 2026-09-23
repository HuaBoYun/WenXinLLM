package com.financial.sharing.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblExpenseProvision;
import com.financial.sharing.business.entity.TblExpenseProvisionReverse;
import com.financial.sharing.business.mapper.ExpenseProvisionMapper;
import com.financial.sharing.business.mapper.ExpenseProvisionReverseMapper;
import com.financial.sharing.business.service.ExpenseProvisionService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 费用预提服务实现类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Slf4j
@Service
public class ExpenseProvisionServiceImpl implements ExpenseProvisionService {

    @Autowired
    private ExpenseProvisionMapper expenseProvisionMapper;

    @Autowired
    private ExpenseProvisionReverseMapper expenseProvisionReverseMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public MyJsonBean getList(Map<String, Object> param) {
        try {
            log.info("查询费用预提单列表，参数：{}", param);

            int pageNum = param.get("pageNum") != null ? (Integer) param.get("pageNum") : 1;
            int pageSize = param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10;

            Page<TblExpenseProvision> page = new Page<>(pageNum, pageSize);

            com.baomidou.mybatisplus.core.metadata.IPage<TblExpenseProvision> resultPage =
                expenseProvisionMapper.selectExpenseProvisionPage(page,
                    (String) param.get("provisionCode"),
                    (String) param.get("applicantName"),
                    (String) param.get("provisionStatus"),
                    (String) param.get("provisionType"),
                    (String) param.get("startDate") != null ? java.time.LocalDate.parse((String) param.get("startDate")) : null,
                    (String) param.get("endDate") != null ? java.time.LocalDate.parse((String) param.get("endDate")) : null,
                    (String) param.get("applicantDeptId"));

            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("tlist", resultPage.getRecords());
            pageResult.put("totalRecord", (int) resultPage.getTotal());
            pageResult.put("pageNo", pageNum);
            pageResult.put("pageSize", pageSize);
            pageResult.put("totalPage", (int) resultPage.getPages());

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询费用预提单列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String provisionId) {
        try {
            if (!StringUtils.hasText(provisionId)) {
                return MyJsonBean.errorData("预提单ID不能为空");
            }

            log.info("查询费用预提单详情，provisionId={}", provisionId);

            TblExpenseProvision provision = expenseProvisionMapper.selectById(provisionId);
            if (provision == null) {
                return MyJsonBean.errorData("预提单不存在");
            }

            return MyJsonBean.successData("查询成功", provision);
        } catch (Exception e) {
            log.error("查询费用预提单详情失败，provisionId={}", provisionId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblExpenseProvision provision) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            log.info("保存或更新费用预提单，参数：{}", provision);

            LocalDateTime now = LocalDateTime.now();

            if (!StringUtils.hasText(provision.getProvisionId())) {
                // 新增
                provision.setProvisionId(UUID.randomUUID().toString().replace("-", ""));
                provision.setCreateTime(now);
                provision.setCreateUser(currentUser.getStaffid().toString());
                provision.setProvisionStatus("DRAFT");
                provision.setReverseStatus("UNREVERSED");

                // 生成预提单号
                provision.setProvisionCode("PROV" + System.currentTimeMillis());

                // 设置申请人信息
                provision.setApplicantId(currentUser.getStaffid().toString());
                provision.setApplicantName(currentUser.getUsername());
                provision.setApplicantDeptId(currentUser.getLinkDetp().getOrgid().toString());
                provision.setApplicantDeptName(currentUser.getLinkDetp().getOrgname());
            } else {
                // 更新
                provision.setUpdateTime(now);
                provision.setUpdateUser(currentUser.getStaffid().toString());
            }

            int result = StringUtils.hasText(provision.getProvisionId()) &&
                expenseProvisionMapper.selectById(provision.getProvisionId()) != null
                ? expenseProvisionMapper.updateById(provision)
                : expenseProvisionMapper.insert(provision);

            if (result > 0) {
                return MyJsonBean.successData("保存成功", provision.getProvisionId());
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存或更新费用预提单失败", e);
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String provisionId) {
        try {
            if (!StringUtils.hasText(provisionId)) {
                return MyJsonBean.errorData("预提单ID不能为空");
            }

            log.info("删除费用预提单，provisionId={}", provisionId);

            TblExpenseProvision provision = expenseProvisionMapper.selectById(provisionId);
            if (provision == null) {
                return MyJsonBean.errorData("预提单不存在");
            }

            // 只有草稿状态可以删除
            if (!"DRAFT".equals(provision.getProvisionStatus())) {
                return MyJsonBean.errorData("只有草稿状态的预提单可以删除");
            }

            int result = expenseProvisionMapper.deleteById(provisionId);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除费用预提单失败，provisionId={}", provisionId, e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDelete(List<String> provisionIds) {
        try {
            if (provisionIds == null || provisionIds.isEmpty()) {
                return MyJsonBean.errorData("预提单ID列表不能为空");
            }

            log.info("批量删除费用预提单，provisionIds={}", provisionIds);

            int count = 0;
            for (String provisionId : provisionIds) {
                TblExpenseProvision provision = expenseProvisionMapper.selectById(provisionId);
                if (provision != null && "DRAFT".equals(provision.getProvisionStatus())) {
                    count += expenseProvisionMapper.deleteById(provisionId);
                }
            }

            return MyJsonBean.successData("批量删除成功，共删除 " + count + " 条记录");
        } catch (Exception e) {
            log.error("批量删除费用预提单失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean submit(String provisionId) {
        try {
            if (!StringUtils.hasText(provisionId)) {
                return MyJsonBean.errorData("预提单ID不能为空");
            }

            log.info("提交费用预提单，provisionId={}", provisionId);

            TblExpenseProvision provision = expenseProvisionMapper.selectById(provisionId);
            if (provision == null) {
                return MyJsonBean.errorData("预提单不存在");
            }

            if (!"DRAFT".equals(provision.getProvisionStatus())) {
                return MyJsonBean.errorData("只有草稿状态的预提单可以提交");
            }

            provision.setProvisionStatus("SUBMITTED");

            int result = expenseProvisionMapper.updateById(provision);
            if (result > 0) {
                return MyJsonBean.successData("提交成功");
            } else {
                return MyJsonBean.errorData("提交失败");
            }
        } catch (Exception e) {
            log.error("提交费用预提单失败，provisionId={}", provisionId, e);
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean approve(String provisionId, String action, String opinion) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(provisionId)) {
                return MyJsonBean.errorData("预提单ID不能为空");
            }

            log.info("审批费用预提单，provisionId={}, action={}, opinion={}", provisionId, action, opinion);

            TblExpenseProvision provision = expenseProvisionMapper.selectById(provisionId);
            if (provision == null) {
                return MyJsonBean.errorData("预提单不存在");
            }

            if (!"SUBMITTED".equals(provision.getProvisionStatus()) && !"APPROVING".equals(provision.getProvisionStatus())) {
                return MyJsonBean.errorData("只有待审批状态的预提单可以审批");
            }

            LocalDateTime now = LocalDateTime.now();
            provision.setApproveTime(now);
            provision.setApproveOpinion(opinion);
            provision.setApproverId(currentUser.getStaffid().toString());
            provision.setApproverName(currentUser.getUsername());

            if ("APPROVE".equals(action)) {
                provision.setProvisionStatus("APPROVED");
            } else if ("REJECT".equals(action)) {
                provision.setProvisionStatus("REJECTED");
            } else {
                return MyJsonBean.errorData("无效的审批动作");
            }

            int result = expenseProvisionMapper.updateById(provision);
            if (result > 0) {
                return MyJsonBean.successData("审批成功");
            } else {
                return MyJsonBean.errorData("审批失败");
            }
        } catch (Exception e) {
            log.error("审批费用预提单失败，provisionId={}", provisionId, e);
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean withdraw(String provisionId) {
        try {
            if (!StringUtils.hasText(provisionId)) {
                return MyJsonBean.errorData("预提单ID不能为空");
            }

            log.info("撤回费用预提单，provisionId={}", provisionId);

            TblExpenseProvision provision = expenseProvisionMapper.selectById(provisionId);
            if (provision == null) {
                return MyJsonBean.errorData("预提单不存在");
            }

            if (!"SUBMITTED".equals(provision.getProvisionStatus()) && !"APPROVING".equals(provision.getProvisionStatus())) {
                return MyJsonBean.errorData("只有待审批状态的预提单可以撤回");
            }

            provision.setProvisionStatus("DRAFT");

            int result = expenseProvisionMapper.updateById(provision);
            if (result > 0) {
                return MyJsonBean.successData("撤回成功");
            } else {
                return MyJsonBean.errorData("撤回失败");
            }
        } catch (Exception e) {
            log.error("撤回费用预提单失败，provisionId={}", provisionId, e);
            return MyJsonBean.errorData("撤回失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean reverse(String provisionId, String voucherNo) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(provisionId)) {
                return MyJsonBean.errorData("预提单ID不能为空");
            }

            log.info("冲销费用预提单，provisionId={}, voucherNo={}", provisionId, voucherNo);

            TblExpenseProvision provision = expenseProvisionMapper.selectById(provisionId);
            if (provision == null) {
                return MyJsonBean.errorData("预提单不存在");
            }

            if (!"APPROVED".equals(provision.getProvisionStatus())) {
                return MyJsonBean.errorData("只有已审批状态的预提单可以冲销");
            }

            if ("REVERSED".equals(provision.getReverseStatus())) {
                return MyJsonBean.errorData("该预提单已冲销");
            }

            // 创建冲销记录
            TblExpenseProvisionReverse reverse = new TblExpenseProvisionReverse();
            reverse.setReverseId(UUID.randomUUID().toString().replace("-", ""));
            reverse.setProvisionId(provisionId);
            reverse.setReverseAmount(provision.getProvisionAmount());
            reverse.setReverseDate(java.time.LocalDate.now());
            reverse.setReverseVoucherNo(voucherNo);
            reverse.setCreateTime(LocalDateTime.now());
            reverse.setCreateUser(currentUser.getStaffid().toString());

            expenseProvisionReverseMapper.insert(reverse);

            // 更新预提单状态为已冲销
            provision.setReverseStatus("REVERSED");
            provision.setReverseTime(LocalDateTime.now());
            provision.setUpdateTime(LocalDateTime.now());
            provision.setUpdateUser(currentUser.getStaffid().toString());

            int result = expenseProvisionMapper.updateById(provision);
            if (result > 0) {
                return MyJsonBean.successData("冲销成功");
            } else {
                return MyJsonBean.errorData("冲销失败");
            }
        } catch (Exception e) {
            log.error("冲销费用预提单失败，provisionId={}", provisionId, e);
            return MyJsonBean.errorData("冲销失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getByProvisionCode(String provisionCode) {
        try {
            if (!StringUtils.hasText(provisionCode)) {
                return MyJsonBean.errorData("预提单号不能为空");
            }

            log.info("根据预提单号查询，provisionCode={}", provisionCode);

            TblExpenseProvision provision = expenseProvisionMapper.selectByProvisionCode(provisionCode);
            if (provision == null) {
                return MyJsonBean.errorData("预提单不存在");
            }

            return MyJsonBean.successData("查询成功", provision);
        } catch (Exception e) {
            log.error("根据预提单号查询失败，provisionCode={}", provisionCode, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean export(Map<String, Object> param) {
        log.warn("ExpenseProvisionService.export 暂未实现，param={}", param);
        return MyJsonBean.errorData("导出功能开发中");
    }

    @Override
    public MyJsonBean getStatistics(Map<String, Object> param) {
        log.warn("ExpenseProvisionService.getStatistics 暂未实现，param={}", param);
        return MyJsonBean.successData("查询成功", new HashMap<>());
    }
}
