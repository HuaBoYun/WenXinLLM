package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingPlanDTO;
import com.global.treasurer.dto.FinancingPlanQueryDTO;
import com.global.treasurer.entity.TblFinancingPlan;
import com.global.treasurer.mapper.FinancingPlanMapper;
import com.global.treasurer.service.FinancingPlanService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 融资计划服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Service
public class FinancingPlanServiceImpl implements FinancingPlanService {
    @Autowired
    private FinancingPlanMapper financingPlanMapper;

    @Override
    public PageInfo<TblFinancingPlan> getPlanList(FinancingPlanQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("planYear", queryDTO.getPlanYear());
        params.put("planType", queryDTO.getPlanType());
        params.put("companyId", queryDTO.getCompanyId());
        params.put("planStatus", queryDTO.getPlanStatus());
        params.put("currencyCode", queryDTO.getCurrencyCode());
        List<TblFinancingPlan> list = financingPlanMapper.selectPlanList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblFinancingPlan getPlanById(Long planId) {
        TblFinancingPlan plan = financingPlanMapper.selectPlanById(planId);
        if (plan == null) {
            throw new ServiceException(404, "融资计划不存在");
        }
        return plan;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancingPlan savePlan(FinancingPlanDTO dto) {
        TblFinancingPlan plan = new TblFinancingPlan();
        BeanUtils.copyProperties(dto, plan);
        
        if (dto.getPlanId() == null) {
            // 新增
            plan.setPlanNo(generatePlanNo());
            plan.setPlanStatus("DRAFT");
            plan.setDeleteFlag(0);
            plan.setCreatedTime(new Date());
            financingPlanMapper.insert(plan);
        } else {
            // 更新
            plan.setUpdatedTime(new Date());
            financingPlanMapper.updateById(plan);
        }
        return plan;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long planId) {
        TblFinancingPlan plan = getPlanById(planId);
        if (!"DRAFT".equals(plan.getPlanStatus())) {
            throw new ServiceException(400, "只能删除草稿状态的融资计划");
        }
        plan.setDeleteFlag(1);
        plan.setUpdatedTime(new Date());
        financingPlanMapper.updateById(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeletePlans(List<Long> planIds) {
        financingPlanMapper.batchDeleteByIds(planIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForApproval(Long planId) {
        TblFinancingPlan plan = getPlanById(planId);
        if (!"DRAFT".equals(plan.getPlanStatus())) {
            throw new ServiceException(400, "只能提交草稿状态的融资计划");
        }
        financingPlanMapper.updatePlanStatus(planId, "PENDING");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long planId, String comments) {
        TblFinancingPlan plan = getPlanById(planId);
        if (!"PENDING".equals(plan.getPlanStatus())) {
            throw new ServiceException(400, "只能审批待审批状态的融资计划");
        }
        plan.setPlanStatus("APPROVED");
        plan.setApprovalDate(new Date());
        plan.setApprovalComments(comments);
        plan.setUpdatedTime(new Date());
        financingPlanMapper.updateById(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long planId, String comments) {
        TblFinancingPlan plan = getPlanById(planId);
        if (!"PENDING".equals(plan.getPlanStatus())) {
            throw new ServiceException(400, "只能审批待审批状态的融资计划");
        }
        plan.setPlanStatus("REJECTED");
        plan.setApprovalDate(new Date());
        plan.setApprovalComments(comments);
        plan.setUpdatedTime(new Date());
        financingPlanMapper.updateById(plan);
    }

    @Override
    public Map<String, Object> getYearSummary(Integer planYear, Long companyId) {
        return financingPlanMapper.selectYearSummary(planYear, companyId);
    }

    private String generatePlanNo() {
        return "FP" + System.currentTimeMillis();
    }
}

