package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblFundPlan;
import com.global.treasurer.mapper.FundPlanMapper;
import com.global.treasurer.service.FundPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 资金计划服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Service
public class FundPlanServiceImpl implements FundPlanService {
    @Autowired
    private FundPlanMapper fundPlanMapper;

    @Override
    public Map<String, Object> getFundPlanPage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 兼容前端分页参数: pageNum 和 pageSize
        Integer page = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) :
                       (params.get("current") != null ? Integer.parseInt(params.get("current").toString()) : 1);
        Integer size = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                       (params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10);

        params.put("offset", (page - 1) * size);
        params.put("limit", size);

        List<TblFundPlan> list = fundPlanMapper.selectPlanPage(params);
        int total = fundPlanMapper.countPlanList(params);

        // 兼容前端数据格式: rows 和 total
        result.put("rows", list);
        result.put("total", total);

        return result;
    }

    @Override
    public TblFundPlan getFundPlanById(Long planId) {
        return fundPlanMapper.selectPlanById(planId);
    }

    @Override
    public int createFundPlan(TblFundPlan fundPlan) {
        fundPlan.setPlanNo(generatePlanNo());
        fundPlan.setPlanStatus("DRAFT");
        fundPlan.setDeleteFlag(0);
        fundPlan.setCreatedTime(new Date());
        return fundPlanMapper.insert(fundPlan);
    }

    @Override
    public int updateFundPlan(TblFundPlan fundPlan) {
        fundPlan.setUpdatedTime(new Date());
        return fundPlanMapper.updateById(fundPlan);
    }

    @Override
    public int deleteFundPlan(Long planId) {
        TblFundPlan plan = getFundPlanById(planId);
        if (plan == null) {
            return 0;
        }

        // 只能删除草稿状态的计划
        if (!"DRAFT".equals(plan.getPlanStatus()) && !"REJECTED".equals(plan.getPlanStatus())) {
            throw new RuntimeException("只能删除草稿或已拒绝状态的计划");
        }

        plan.setDeleteFlag(1);
        plan.setUpdatedTime(new Date());
        return fundPlanMapper.updateById(plan);
    }

    @Override
    public int submitFundPlan(Long planId, Long userId) {
        TblFundPlan plan = getFundPlanById(planId);
        if (plan == null) {
            return 0;
        }

        if (!"DRAFT".equals(plan.getPlanStatus())) {
            throw new RuntimeException("只能提交草稿状态的计划");
        }

        return fundPlanMapper.updatePlanStatus(planId, "PENDING_APPROVAL");
    }

    @Override
    public int approveFundPlan(Long planId, Long userId, Boolean approved, String opinion) {
        TblFundPlan plan = getFundPlanById(planId);
        if (plan == null) {
            return 0;
        }

        if (!"PENDING_APPROVAL".equals(plan.getPlanStatus())) {
            throw new RuntimeException("只能审批待审批状态的计划");
        }

        String newStatus = approved ? "APPROVED" : "REJECTED";
        return fundPlanMapper.updatePlanStatus(planId, newStatus);
    }

    @Override
    public int executeFundPlan(Long planId, Long userId) {
        TblFundPlan plan = getFundPlanById(planId);
        if (plan == null) {
            return 0;
        }

        if (!"APPROVED".equals(plan.getPlanStatus())) {
            throw new RuntimeException("只能执行已审批状态的计划");
        }

        return fundPlanMapper.updatePlanStatus(planId, "EXECUTING");
    }

    @Override
    public int completeFundPlan(Long planId, Long userId) {
        TblFundPlan plan = getFundPlanById(planId);
        if (plan == null) {
            return 0;
        }

        if (!"EXECUTING".equals(plan.getPlanStatus())) {
            throw new RuntimeException("只能完成执行中的计划");
        }

        return fundPlanMapper.updatePlanStatus(planId, "COMPLETED");
    }

    @Override
    public int cancelFundPlan(Long planId, Long userId) {
        TblFundPlan plan = getFundPlanById(planId);
        if (plan == null) {
            return 0;
        }

        if ("COMPLETED".equals(plan.getPlanStatus()) || "CANCELLED".equals(plan.getPlanStatus())) {
            throw new RuntimeException("已完成或已取消的计划不能取消");
        }

        return fundPlanMapper.updatePlanStatus(planId, "CANCELLED");
    }

    /**
     * 生成计划编号
     */
    private String generatePlanNo() {
        return "FUND-" + System.currentTimeMillis();
    }
}
