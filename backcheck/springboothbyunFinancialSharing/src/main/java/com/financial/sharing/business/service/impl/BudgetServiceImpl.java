package com.financial.sharing.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblBudget;
import com.financial.sharing.business.mapper.BudgetMapper;
import com.financial.sharing.business.service.BudgetService;
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
import java.time.LocalDateTime;
import java.util.*;

/**
 * 预算服务实现类
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Slf4j
@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetMapper budgetMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public MyJsonBean getList(Map<String, Object> param) {
        try {
            log.info("查询预算列表，参数：{}", param);

            int pageNum = param.get("pageNum") != null ? (Integer) param.get("pageNum") : 1;
            int pageSize = param.get("pageSize") != null ? (Integer) param.get("pageSize") : 10;

            Page<TblBudget> page = new Page<>(pageNum, pageSize);

            com.baomidou.mybatisplus.core.metadata.IPage<TblBudget> resultPage =
                budgetMapper.selectBudgetPage(page,
                    (String) param.get("budgetName"),
                    (String) param.get("budgetType"),
                    (String) param.get("status"),
                    (String) param.get("departmentId"),
                    (String) param.get("budgetYear"),
                    (String) param.get("startDate") != null ? java.time.LocalDate.parse((String) param.get("startDate")) : null,
                    (String) param.get("endDate") != null ? java.time.LocalDate.parse((String) param.get("endDate")) : null);

            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("tlist", resultPage.getRecords());
            pageResult.put("totalRecord", (int) resultPage.getTotal());
            pageResult.put("pageNo", pageNum);
            pageResult.put("pageSize", pageSize);
            pageResult.put("totalPage", (int) resultPage.getPages());

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询预算列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String budgetId) {
        try {
            if (!StringUtils.hasText(budgetId)) {
                return MyJsonBean.errorData("预算ID不能为空");
            }

            log.info("查询预算详情，budgetId={}", budgetId);

            TblBudget budget = budgetMapper.selectById(budgetId);
            if (budget == null) {
                return MyJsonBean.errorData("预算不存在");
            }

            // 计算执行率
            if (budget.getTotalAmount() != null && budget.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal executionRate = budget.getExecutedAmount() != null
                    ? budget.getExecutedAmount().multiply(new BigDecimal("100")).divide(budget.getTotalAmount(), 2, BigDecimal.ROUND_HALF_UP)
                    : BigDecimal.ZERO;
                budget.setExecutedAmount(budget.getExecutedAmount());
            }

            return MyJsonBean.successData("查询成功", budget);
        } catch (Exception e) {
            log.error("查询预算详情失败，budgetId={}", budgetId, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveOrUpdate(TblBudget budget) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            log.info("保存或更新预算，参数：{}", budget);

            LocalDateTime now = LocalDateTime.now();

            if (!StringUtils.hasText(budget.getBudgetId())) {
                // 新增
                budget.setBudgetId(UUID.randomUUID().toString().replace("-", ""));
                budget.setCreateTime(now);
                budget.setCreateUser(currentUser.getStaffid().toString());
                budget.setBudgetStatus("DRAFT");
                budget.setExecutedAmount(BigDecimal.ZERO);
                budget.setRemainingAmount(budget.getTotalAmount());

                // 生成预算编号
                budget.setBudgetCode("BUD" + System.currentTimeMillis());

                // 设置申请人信息
                budget.setApplicantId(currentUser.getStaffid().toString());
                budget.setApplicantName(currentUser.getUsername());
            } else {
                // 更新
                budget.setUpdateTime(now);
                budget.setUpdateUser(currentUser.getStaffid().toString());
            }

            int result = StringUtils.hasText(budget.getBudgetId()) &&
                budgetMapper.selectById(budget.getBudgetId()) != null
                ? budgetMapper.updateById(budget)
                : budgetMapper.insert(budget);

            if (result > 0) {
                return MyJsonBean.successData("保存成功", budget.getBudgetId());
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存或更新预算失败", e);
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean delete(String budgetId) {
        try {
            if (!StringUtils.hasText(budgetId)) {
                return MyJsonBean.errorData("预算ID不能为空");
            }

            log.info("删除预算，budgetId={}", budgetId);

            TblBudget budget = budgetMapper.selectById(budgetId);
            if (budget == null) {
                return MyJsonBean.errorData("预算不存在");
            }

            // 只有草稿状态可以删除
            if (!"DRAFT".equals(budget.getBudgetStatus())) {
                return MyJsonBean.errorData("只有草稿状态的预算可以删除");
            }

            int result = budgetMapper.deleteById(budgetId);
            if (result > 0) {
                return MyJsonBean.successData("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算失败，budgetId={}", budgetId, e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean batchDelete(List<String> budgetIds) {
        try {
            if (budgetIds == null || budgetIds.isEmpty()) {
                return MyJsonBean.errorData("预算ID列表不能为空");
            }

            log.info("批量删除预算，budgetIds={}", budgetIds);

            int count = 0;
            for (String budgetId : budgetIds) {
                TblBudget budget = budgetMapper.selectById(budgetId);
                if (budget != null && "DRAFT".equals(budget.getBudgetStatus())) {
                    count += budgetMapper.deleteById(budgetId);
                }
            }

            return MyJsonBean.successData("批量删除成功，共删除 " + count + " 条记录");
        } catch (Exception e) {
            log.error("批量删除预算失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean submit(String budgetId) {
        try {
            if (!StringUtils.hasText(budgetId)) {
                return MyJsonBean.errorData("预算ID不能为空");
            }

            log.info("提交预算，budgetId={}", budgetId);

            TblBudget budget = budgetMapper.selectById(budgetId);
            if (budget == null) {
                return MyJsonBean.errorData("预算不存在");
            }

            if (!"DRAFT".equals(budget.getBudgetStatus())) {
                return MyJsonBean.errorData("只有草稿状态的预算可以提交");
            }

            budget.setBudgetStatus("SUBMITTED");

            int result = budgetMapper.updateById(budget);
            if (result > 0) {
                return MyJsonBean.successData("提交成功");
            } else {
                return MyJsonBean.errorData("提交失败");
            }
        } catch (Exception e) {
            log.error("提交预算失败，budgetId={}", budgetId, e);
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean approve(String budgetId, String action, String opinion) {
        try {
            // 获取当前用户
            TblStaffUtil currentUser = userProvider.get();
            if (currentUser == null || currentUser.getLinkDetp() == null || currentUser.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            if (!StringUtils.hasText(budgetId)) {
                return MyJsonBean.errorData("预算ID不能为空");
            }

            log.info("审批预算，budgetId={}, action={}, opinion={}", budgetId, action, opinion);

            TblBudget budget = budgetMapper.selectById(budgetId);
            if (budget == null) {
                return MyJsonBean.errorData("预算不存在");
            }

            if (!"SUBMITTED".equals(budget.getBudgetStatus()) && !"APPROVING".equals(budget.getBudgetStatus())) {
                return MyJsonBean.errorData("只有待审批状态的预算可以审批");
            }

            LocalDateTime now = LocalDateTime.now();
            budget.setApproveTime(now);
            budget.setApproveOpinion(opinion);
            budget.setApproverId(currentUser.getStaffid().toString());
            budget.setApproverName(currentUser.getUsername());

            if ("APPROVE".equals(action)) {
                budget.setBudgetStatus("APPROVED");
            } else if ("REJECT".equals(action)) {
                budget.setBudgetStatus("REJECTED");
            } else {
                return MyJsonBean.errorData("无效的审批动作");
            }

            int result = budgetMapper.updateById(budget);
            if (result > 0) {
                return MyJsonBean.successData("审批成功");
            } else {
                return MyJsonBean.errorData("审批失败");
            }
        } catch (Exception e) {
            log.error("审批预算失败，budgetId={}", budgetId, e);
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean checkBudget(String budgetId, BigDecimal amount) {
        try {
            if (!StringUtils.hasText(budgetId)) {
                return MyJsonBean.errorData("预算ID不能为空");
            }

            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return MyJsonBean.errorData("金额必须大于0");
            }

            log.info("预算控制，budgetId={}, amount={}", budgetId, amount);

            TblBudget budget = budgetMapper.selectById(budgetId);
            if (budget == null) {
                return MyJsonBean.errorData("预算不存在");
            }

            if (!"APPROVED".equals(budget.getBudgetStatus()) && !"EXECUTING".equals(budget.getBudgetStatus())) {
                return MyJsonBean.errorData("预算未审批或未开始执行");
            }

            boolean hasBalance = budgetMapper.checkBudgetBalance(budgetId, amount);

            if (hasBalance) {
                // 更新预算执行金额
                BigDecimal newExecutedAmount = budget.getExecutedAmount().add(amount);
                BigDecimal newRemainingAmount = budget.getRemainingAmount().subtract(amount);
                budget.setExecutedAmount(newExecutedAmount);
                budget.setRemainingAmount(newRemainingAmount);
                budget.setBudgetStatus("EXECUTING");

                budgetMapper.updateById(budget);

                return MyJsonBean.successData("预算余额充足，可以执行");
            } else {
                return MyJsonBean.errorData("预算余额不足，无法执行");
            }
        } catch (Exception e) {
            log.error("预算控制失败，budgetId={}", budgetId, e);
            return MyJsonBean.errorData("预算控制失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getByBudgetCode(String budgetCode) {
        try {
            if (!StringUtils.hasText(budgetCode)) {
                return MyJsonBean.errorData("预算编号不能为空");
            }

            log.info("根据预算编号查询，budgetCode={}", budgetCode);

            TblBudget budget = budgetMapper.selectByBudgetCode(budgetCode);
            if (budget == null) {
                return MyJsonBean.errorData("预算不存在");
            }

            return MyJsonBean.successData("查询成功", budget);
        } catch (Exception e) {
            log.error("根据预算编号查询失败，budgetCode={}", budgetCode, e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> checkBudgetControl(String businessType, String businessId,
                                                  String departmentId, String projectId,
                                                  BigDecimal amount, String currency) {
        log.warn("checkBudgetControl 暂未实现，返回默认放行结果，businessType={}, businessId={}", businessType, businessId);
        Map<String, Object> result = new HashMap<>();
        result.put("passed", true);
        result.put("warning", false);
        result.put("message", "预算控制检查功能开发中，默认放行");
        result.put("availableAmount", BigDecimal.ZERO);
        return result;
    }

    @Override
    public MyJsonBean occupy(String businessType, String businessId, String departmentId,
                             String projectId, BigDecimal amount, String currency) {
        log.warn("occupy 暂未实现，businessType={}, businessId={}, amount={}", businessType, businessId, amount);
        return MyJsonBean.errorData("预算占用功能开发中");
    }

    @Override
    public MyJsonBean release(String businessType, String businessId) {
        log.warn("release 暂未实现，businessType={}, businessId={}", businessType, businessId);
        return MyJsonBean.errorData("预算释放功能开发中");
    }

    @Override
    public MyJsonBean getStatistics(Map<String, Object> param) {
        log.warn("BudgetService.getStatistics 暂未实现，param={}", param);
        return MyJsonBean.successData("查询成功", new HashMap<>());
    }

    @Override
    public MyJsonBean export(Map<String, Object> param) {
        log.warn("BudgetService.export 暂未实现，param={}", param);
        return MyJsonBean.errorData("导出功能开发中");
    }
}
