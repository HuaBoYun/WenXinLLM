package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.InvestmentPlanDTO;
import com.global.treasurer.dto.InvestmentPlanQueryDTO;
import com.global.treasurer.entity.TblInvestmentPlan;
import com.global.treasurer.mapper.InvestmentPlanMapper;
import com.global.treasurer.service.InvestmentPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资计划服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Service
public class InvestmentPlanServiceImpl implements InvestmentPlanService {

    private static final Logger log = LoggerFactory.getLogger(InvestmentPlanServiceImpl.class);

    @Resource
    private InvestmentPlanMapper investmentPlanMapper;

    @Override
    public PageInfo<TblInvestmentPlan> getPlanList(InvestmentPlanQueryDTO queryDTO) {
        try {
            PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

            Map<String, Object> params = new HashMap<>();
            if (StringUtils.hasText(queryDTO.getPlanNo())) {
                params.put("planNo", queryDTO.getPlanNo());
            }
            if (StringUtils.hasText(queryDTO.getPlanName())) {
                params.put("planName", queryDTO.getPlanName());
            }
            if (StringUtils.hasText(queryDTO.getPlanType())) {
                params.put("planType", queryDTO.getPlanType());
            }
            if (StringUtils.hasText(queryDTO.getInvestmentType())) {
                params.put("investmentType", queryDTO.getInvestmentType());
            }
            if (StringUtils.hasText(queryDTO.getRiskLevel())) {
                params.put("riskLevel", queryDTO.getRiskLevel());
            }
            if (StringUtils.hasText(queryDTO.getPlanStatus())) {
                params.put("planStatus", queryDTO.getPlanStatus());
            }
            if (queryDTO.getMinPlanAmount() != null) {
                params.put("minPlanAmount", queryDTO.getMinPlanAmount());
            }
            if (queryDTO.getMaxPlanAmount() != null) {
                params.put("maxPlanAmount", queryDTO.getMaxPlanAmount());
            }
            if (queryDTO.getPlanStartDateFrom() != null) {
                params.put("planStartDateFrom", queryDTO.getPlanStartDateFrom());
            }
            if (queryDTO.getPlanStartDateTo() != null) {
                params.put("planStartDateTo", queryDTO.getPlanStartDateTo());
            }
            if (queryDTO.getPlanEndDateFrom() != null) {
                params.put("planEndDateFrom", queryDTO.getPlanEndDateFrom());
            }
            if (queryDTO.getPlanEndDateTo() != null) {
                params.put("planEndDateTo", queryDTO.getPlanEndDateTo());
            }
            if (queryDTO.getCompanyId() != null) {
                params.put("companyId", queryDTO.getCompanyId());
            }

            List<TblInvestmentPlan> list = investmentPlanMapper.selectPlanList(params);
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("查询投资计划列表失败", e);
            throw new RuntimeException("查询投资计划列表失败: " + e.getMessage());
        }
    }

    @Override
    public TblInvestmentPlan getPlanById(Long planId) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到计划信息");
            }
            return plan;
        } catch (Exception e) {
            log.error("获取投资计划详情失败, planId: {}", planId, e);
            throw new RuntimeException("获取投资计划详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblInvestmentPlan savePlan(InvestmentPlanDTO dto) {
        try {
            TblInvestmentPlan plan = new TblInvestmentPlan();
            BeanUtils.copyProperties(dto, plan);

            if (dto.getPlanId() != null) {
                TblInvestmentPlan existingPlan = investmentPlanMapper.selectPlanById(dto.getPlanId());
                if (existingPlan == null) {
                    throw new RuntimeException("未找到要更新的计划信息");
                }
                plan.setUpdatedBy(1L);
                plan.setUpdatedByName("系统管理员");
                plan.setUpdatedTime(new Date());
                investmentPlanMapper.updateById(plan);
                log.info("更新投资计划成功, planId: {}", dto.getPlanId());
            } else {
                plan.setPlanStatus("DRAFT");
                plan.setDeleteFlag(0);
                plan.setCreatedBy(1L);
                plan.setCreatedByName("系统管理员");
                plan.setCreatedTime(new Date());
                investmentPlanMapper.insert(plan);
                log.info("新增投资计划成功, planId: {}", plan.getPlanId());
            }
            return plan;
        } catch (Exception e) {
            log.error("保存投资计划失败", e);
            throw new RuntimeException("保存投资计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long planId) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到要删除的计划信息");
            }
            plan.setDeleteFlag(1);
            plan.setUpdatedBy(1L);
            plan.setUpdatedByName("系统管理员");
            plan.setUpdatedTime(new Date());
            investmentPlanMapper.updateById(plan);
            log.info("删除投资计划成功, planId: {}", planId);
        } catch (Exception e) {
            log.error("删除投资计划失败, planId: {}", planId, e);
            throw new RuntimeException("删除投资计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeletePlans(List<Long> planIds) {
        try {
            if (planIds == null || planIds.isEmpty()) {
                throw new IllegalArgumentException("计划ID列表不能为空");
            }
            investmentPlanMapper.batchDeleteByIds(planIds);
            log.info("批量删除投资计划成功, count: {}", planIds.size());
        } catch (Exception e) {
            log.error("批量删除投资计划失败", e);
            throw new RuntimeException("批量删除投资计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitPlan(Long planId) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到计划信息");
            }
            if (!"DRAFT".equals(plan.getPlanStatus())) {
                throw new RuntimeException("只能提交草稿状态的计划");
            }
            investmentPlanMapper.updatePlanStatus(planId, "SUBMITTED");
            plan.setUpdatedBy(1L);
            plan.setUpdatedByName("系统管理员");
            plan.setUpdatedTime(new Date());
            investmentPlanMapper.updateById(plan);
            log.info("提交投资计划成功, planId: {}", planId);
        } catch (Exception e) {
            log.error("提交投资计划失败, planId: {}", planId, e);
            throw new RuntimeException("提交投资计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approvePlan(Long planId, String approvalComments) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到计划信息");
            }
            if (!"SUBMITTED".equals(plan.getPlanStatus())) {
                throw new RuntimeException("只能审批已提交状态的计划");
            }
            investmentPlanMapper.updatePlanStatus(planId, "APPROVED");
            plan.setApprovalComments(approvalComments);
            plan.setUpdatedBy(1L);
            plan.setUpdatedByName("系统管理员");
            plan.setUpdatedTime(new Date());
            investmentPlanMapper.updateById(plan);
            log.info("审批投资计划成功, planId: {}", planId);
        } catch (Exception e) {
            log.error("审批投资计划失败, planId: {}", planId, e);
            throw new RuntimeException("审批投资计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectPlan(Long planId, String rejectionReason) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到计划信息");
            }
            if (!"SUBMITTED".equals(plan.getPlanStatus())) {
                throw new RuntimeException("只能驳回已提交状态的计划");
            }
            investmentPlanMapper.updatePlanStatus(planId, "DRAFT");
            plan.setRejectionReason(rejectionReason);
            plan.setUpdatedBy(1L);
            plan.setUpdatedByName("系统管理员");
            plan.setUpdatedTime(new Date());
            investmentPlanMapper.updateById(plan);
            log.info("驳回投资计划成功, planId: {}", planId);
        } catch (Exception e) {
            log.error("驳回投资计划失败, planId: {}", planId, e);
            throw new RuntimeException("驳回投资计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executePlan(Long planId) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到计划信息");
            }
            if (!"APPROVED".equals(plan.getPlanStatus())) {
                throw new RuntimeException("只能执行已审批状态的计划");
            }
            investmentPlanMapper.updatePlanStatus(planId, "EXECUTING");
            plan.setUpdatedBy(1L);
            plan.setUpdatedByName("系统管理员");
            plan.setUpdatedTime(new Date());
            investmentPlanMapper.updateById(plan);
            log.info("执行投资计划成功, planId: {}", planId);
        } catch (Exception e) {
            log.error("执行投资计划失败, planId: {}", planId, e);
            throw new RuntimeException("执行投资计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completePlan(Long planId, String completionNotes) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到计划信息");
            }
            if (!"EXECUTING".equals(plan.getPlanStatus())) {
                throw new RuntimeException("只能完成执行中状态的计划");
            }
            investmentPlanMapper.updatePlanStatus(planId, "COMPLETED");
            plan.setCompletionNotes(completionNotes);
            plan.setUpdatedBy(1L);
            plan.setUpdatedByName("系统管理员");
            plan.setUpdatedTime(new Date());
            investmentPlanMapper.updateById(plan);
            log.info("完成投资计划成功, planId: {}", planId);
        } catch (Exception e) {
            log.error("完成投资计划失败, planId: {}", planId, e);
            throw new RuntimeException("完成投资计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPlan(Long planId, String cancelReason) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到计划信息");
            }
            if ("COMPLETED".equals(plan.getPlanStatus()) || "CANCELLED".equals(plan.getPlanStatus())) {
                throw new RuntimeException("不能取消已完成或已取消的计划");
            }
            investmentPlanMapper.updatePlanStatus(planId, "CANCELLED");
            plan.setCancelReason(cancelReason);
            plan.setUpdatedBy(1L);
            plan.setUpdatedByName("系统管理员");
            plan.setUpdatedTime(new Date());
            investmentPlanMapper.updateById(plan);
            log.info("取消投资计划成功, planId: {}", planId);
        } catch (Exception e) {
            log.error("取消投资计划失败, planId: {}", planId, e);
            throw new RuntimeException("取消投资计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInvestedAmount(Long planId, BigDecimal investedAmount) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            if (investedAmount == null || investedAmount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("已投资金额必须大于等于0");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到计划信息");
            }
            investmentPlanMapper.updateInvestedAmount(planId, investedAmount);
            BigDecimal remainingAmount = plan.getPlanAmount().subtract(investedAmount);
            plan.setRemainingAmount(remainingAmount);
            plan.setUpdatedBy(1L);
            plan.setUpdatedByName("系统管理员");
            plan.setUpdatedTime(new Date());
            investmentPlanMapper.updateById(plan);
            log.info("更新投资计划已投资金额成功, planId: {}, investedAmount: {}", planId, investedAmount);
        } catch (Exception e) {
            log.error("更新投资计划已投资金额失败, planId: {}, investedAmount: {}", planId, investedAmount, e);
            throw new RuntimeException("更新投资计划已投资金额失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateActualReturnRate(Long planId, BigDecimal actualReturnRate) {
        try {
            if (planId == null) {
                throw new IllegalArgumentException("计划ID不能为空");
            }
            if (actualReturnRate == null) {
                throw new IllegalArgumentException("实际收益率不能为空");
            }
            TblInvestmentPlan plan = investmentPlanMapper.selectPlanById(planId);
            if (plan == null) {
                throw new RuntimeException("未找到计划信息");
            }
            investmentPlanMapper.updateActualReturnRate(planId, actualReturnRate);
            plan.setUpdatedBy(1L);
            plan.setUpdatedByName("系统管理员");
            plan.setUpdatedTime(new Date());
            investmentPlanMapper.updateById(plan);
            log.info("更新投资计划实际收益率成功, planId: {}, actualReturnRate: {}", planId, actualReturnRate);
        } catch (Exception e) {
            log.error("更新投资计划实际收益率失败, planId: {}, actualReturnRate: {}", planId, actualReturnRate, e);
            throw new RuntimeException("更新投资计划实际收益率失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getPlanStatistics() {
        try {
            Map<String, Object> statistics = investmentPlanMapper.selectPlanStatistics();
            if (statistics == null) {
                statistics = new HashMap<>();
            }
            return statistics;
        } catch (Exception e) {
            log.error("获取计划统计信息失败", e);
            throw new RuntimeException("获取计划统计信息失败: " + e.getMessage());
        }
    }

    @Override
    public List<TblInvestmentPlan> getNearExpiryPlans(Integer days) {
        try {
            if (days == null || days <= 0) {
                days = 7;
            }
            return investmentPlanMapper.selectNearExpiryPlans(days);
        } catch (Exception e) {
            log.error("获取即将到期计划失败", e);
            throw new RuntimeException("获取即将到期计划失败: " + e.getMessage());
        }
    }

    @Override
    public List<TblInvestmentPlan> getOverduePlans() {
        try {
            return investmentPlanMapper.selectOverduePlans();
        } catch (Exception e) {
            log.error("获取逾期计划失败", e);
            throw new RuntimeException("获取逾期计划失败: " + e.getMessage());
        }
    }

    @Override
    public List<TblInvestmentPlan> exportPlans(InvestmentPlanQueryDTO queryDTO) {
        try {
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.hasText(queryDTO.getPlanNo())) {
                params.put("planNo", queryDTO.getPlanNo());
            }
            if (StringUtils.hasText(queryDTO.getPlanName())) {
                params.put("planName", queryDTO.getPlanName());
            }
            if (StringUtils.hasText(queryDTO.getPlanType())) {
                params.put("planType", queryDTO.getPlanType());
            }
            if (StringUtils.hasText(queryDTO.getInvestmentType())) {
                params.put("investmentType", queryDTO.getInvestmentType());
            }
            if (StringUtils.hasText(queryDTO.getRiskLevel())) {
                params.put("riskLevel", queryDTO.getRiskLevel());
            }
            if (StringUtils.hasText(queryDTO.getPlanStatus())) {
                params.put("planStatus", queryDTO.getPlanStatus());
            }
            if (queryDTO.getMinPlanAmount() != null) {
                params.put("minPlanAmount", queryDTO.getMinPlanAmount());
            }
            if (queryDTO.getMaxPlanAmount() != null) {
                params.put("maxPlanAmount", queryDTO.getMaxPlanAmount());
            }
            if (queryDTO.getPlanStartDateFrom() != null) {
                params.put("planStartDateFrom", queryDTO.getPlanStartDateFrom());
            }
            if (queryDTO.getPlanStartDateTo() != null) {
                params.put("planStartDateTo", queryDTO.getPlanStartDateTo());
            }
            if (queryDTO.getPlanEndDateFrom() != null) {
                params.put("planEndDateFrom", queryDTO.getPlanEndDateFrom());
            }
            if (queryDTO.getPlanEndDateTo() != null) {
                params.put("planEndDateTo", queryDTO.getPlanEndDateTo());
            }
            if (queryDTO.getCompanyId() != null) {
                params.put("companyId", queryDTO.getCompanyId());
            }
            return investmentPlanMapper.selectPlanList(params);
        } catch (Exception e) {
            log.error("导出投资计划数据失败", e);
            throw new RuntimeException("导出投资计划数据失败: " + e.getMessage());
        }
    }
}
