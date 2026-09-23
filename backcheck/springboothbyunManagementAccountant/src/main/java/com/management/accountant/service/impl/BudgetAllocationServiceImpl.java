package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAllocation;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.mapper.budget.BudgetAllocationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.service.BudgetAllocationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class BudgetAllocationServiceImpl implements BudgetAllocationService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetAllocationMapper allocationMapper;

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetAllocation create(BudgetAllocation allocation) {
        if (allocation == null) throw new ServiceException("分配信息不能为空");
        // 生成分配编号
        if (!StringUtils.hasText(allocation.getAllocationCode())) {
            allocation.setAllocationCode("ALLOC" + System.currentTimeMillis());
        }
        if (allocation.getDelFlag() == null) allocation.setDelFlag(0);
        // 兜底：如果前端没传 allocatedAmount，默认为 0；remainingAmount = totalAmount - allocatedAmount
        if (allocation.getAllocatedAmount() == null) {
            allocation.setAllocatedAmount(java.math.BigDecimal.ZERO);
        }
        if (allocation.getTotalAmount() == null) {
            allocation.setTotalAmount(java.math.BigDecimal.ZERO);
        }
        if (allocation.getRemainingAmount() == null) {
            allocation.setRemainingAmount(
                allocation.getTotalAmount().subtract(allocation.getAllocatedAmount())
            );
        }
        allocation.setCreateTime(new Date());
        allocation.setUpdateTime(new Date());
        allocationMapper.insert(allocation);
        return allocation;
    }

    @Override
    public BudgetAllocation getById(String allocationId) {
        QueryWrapper<BudgetAllocation> w = new QueryWrapper<>();
        w.eq("ALLOCATION_ID", allocationId).eq("DEL_FLAG", 0L);
        return allocationMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetAllocation allocation) {
        if (allocation == null || !StringUtils.hasText(allocation.getAllocationId())) throw new ServiceException("分配ID不能为空");
        // 如果传了 totalAmount 和 allocatedAmount，自动更新 remainingAmount
        if (allocation.getTotalAmount() != null && allocation.getAllocatedAmount() != null) {
            allocation.setRemainingAmount(
                allocation.getTotalAmount().subtract(allocation.getAllocatedAmount())
            );
        }
        allocation.setUpdateTime(new Date());
        allocationMapper.updateById(allocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String allocationId) {
        BudgetAllocation u = new BudgetAllocation();
        u.setAllocationId(allocationId);
        u.setDelFlag(1);
        u.setUpdateTime(new Date());
        allocationMapper.updateById(u);
    }

    @Override
    public Map<String, Object> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        QueryWrapper<BudgetAllocation> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L);
        if (hasValue(params.get("allocationName"))) w.like("ALLOCATION_NAME", params.get("allocationName"));
        if (hasValue(params.get("allocationType"))) w.eq("ALLOCATION_TYPE", params.get("allocationType"));
        if (hasValue(params.get("allocationStatus"))) w.eq("ALLOCATION_STATUS", params.get("allocationStatus"));
        w.orderByDesc("CREATE_TIME");
        IPage<BudgetAllocation> pageResult = allocationMapper.selectPage(new Page<>(pageNum, pageSize), w);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        // 查询所有未删除记录，汇总各项数据
        QueryWrapper<BudgetAllocation> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0L);
        List<BudgetAllocation> allRecords = allocationMapper.selectList(w);

        java.math.BigDecimal totalBudget = java.math.BigDecimal.ZERO;
        java.math.BigDecimal allocatedAmount = java.math.BigDecimal.ZERO;
        java.math.BigDecimal remainingAmount = java.math.BigDecimal.ZERO;
        long allocationUnits = allRecords.size();

        for (BudgetAllocation a : allRecords) {
            if (a.getTotalAmount() != null) totalBudget = totalBudget.add(a.getTotalAmount());
            if (a.getAllocatedAmount() != null) allocatedAmount = allocatedAmount.add(a.getAllocatedAmount());
            if (a.getRemainingAmount() != null) remainingAmount = remainingAmount.add(a.getRemainingAmount());
        }

        // 分配率（百分比整数）
        int allocationRate = 0;
        int remainingRate = 0;
        if (totalBudget.compareTo(java.math.BigDecimal.ZERO) > 0) {
            allocationRate = allocatedAmount.multiply(java.math.BigDecimal.valueOf(100))
                    .divide(totalBudget, 0, java.math.RoundingMode.HALF_UP).intValue();
            allocationRate = Math.min(allocationRate, 100);
            remainingRate = 100 - allocationRate;
        }

        // 完成率：已确认数 / 总数
        QueryWrapper<BudgetAllocation> confirmedW = new QueryWrapper<>();
        confirmedW.eq("DEL_FLAG", 0L).eq("ALLOCATION_STATUS", "CONFIRMED");
        long confirmedCount = allocationMapper.selectCount(confirmedW);
        int completionRate = allocationUnits > 0
                ? (int) Math.min(confirmedCount * 100 / allocationUnits, 100)
                : 0;

        stats.put("totalBudget", totalBudget);
        stats.put("allocatedAmount", allocatedAmount);
        stats.put("remainingAmount", remainingAmount);
        stats.put("allocationRate", allocationRate);
        stats.put("remainingRate", remainingRate);
        stats.put("allocationUnits", allocationUnits);
        stats.put("completionRate", completionRate);
        // 兼容旧字段
        stats.put("total", allocationUnits);
        stats.put("confirmed", confirmedCount);
        return stats;
    }

    @Override
    public List<Map<String, Object>> getDimensions() {
        List<Map<String, Object>> list = new ArrayList<>();
        String[][] dims = {
            {"ORGANIZATION", "组织机构"},
            {"PROJECT",      "项目"},
            {"PRODUCT",      "产品"},
            {"CUSTOMER",     "客户"},
            {"REGION",       "地区"},
            {"COST_CENTER",  "成本中心"}
        };
        for (String[] d : dims) {
            Map<String, Object> m = new HashMap<>();
            m.put("value", d[0]);
            m.put("label", d[1]);
            list.add(m);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getTargets() {
        return buildTargetList("ORGANIZATION");
    }

    @Override
    public List<Map<String, Object>> getTargetsByDimension(String dimension) {
        return buildTargetList(dimension);
    }

    /** 根据维度类型构建分配对象下拉列表 */
    private List<Map<String, Object>> buildTargetList(String dimension) {
        List<Map<String, Object>> list = new ArrayList<>();
        if ("ORGANIZATION".equals(dimension)) {
            // 从预算组织表查
            QueryWrapper<BudgetOrganization> w = new QueryWrapper<>();
            w.eq("DEL_FLAG", 0).orderByAsc("SORT_ORDER");
            List<BudgetOrganization> orgs = organizationMapper.selectList(w);
            for (BudgetOrganization org : orgs) {
                Map<String, Object> m = new HashMap<>();
                m.put("id",   org.getOrganizationId());
                m.put("name", org.getOrganizationName());
                m.put("code", org.getOrganizationCode());
                list.add(m);
            }
        } else {
            // 其他维度：返回通用占位数据，后续可扩展
            String[][] defaults = {
                {"T001", "目标对象1"},
                {"T002", "目标对象2"},
                {"T003", "目标对象3"}
            };
            for (String[] d : defaults) {
                Map<String, Object> m = new HashMap<>();
                m.put("id",   d[0]);
                m.put("name", d[1]);
                list.add(m);
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(String allocationId) {
        BudgetAllocation u = new BudgetAllocation();
        u.setAllocationId(allocationId);
        u.setAllocationStatus("CONFIRMED");
        u.setUpdateTime(new Date());
        allocationMapper.updateById(u);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchConfirm(List<String> ids) { for (String id : ids) confirm(id); }

    @Override
    public void recalculate() { log.info("重新计算分配"); }

    private boolean hasValue(Object val) {
        if (val == null) return false;
        if (val instanceof String) return !((String) val).trim().isEmpty();
        if (val instanceof java.util.Collection) return !((java.util.Collection<?>) val).isEmpty();
        return true;
    }
}

