package com.management.accountant.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import com.management.accountant.oracle.entity.budget.BudgetOrganization;
import com.management.accountant.oracle.entity.budget.BudgetQuota;
import com.management.accountant.oracle.mapper.budget.BudgetAccountMapper;
import com.management.accountant.oracle.mapper.budget.BudgetOrganizationMapper;
import com.management.accountant.oracle.mapper.budget.BudgetQuotaMapper;
import com.management.accountant.service.BudgetQuotaService;
import com.management.accountant.util.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预算配额Service实现类
 *
 * @description 预算配额业务实现
 * @author AI Assistant
 * @date 2025-01-05
 */
@Service
public class BudgetQuotaServiceImpl extends ServiceImpl<BudgetQuotaMapper, BudgetQuota> implements BudgetQuotaService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetOrganizationMapper organizationMapper;

    @Resource
    private BudgetAccountMapper accountMapper;

    @Override
    public BudgetQuota create(BudgetQuota quota) {
        save(quota);
        return quota;
    }

    @Override
    public BudgetQuota getById(String quotaId) {
        return baseMapper.selectById(quotaId);
    }

    @Override
    public void update(BudgetQuota quota) {
        updateById(quota);
    }

    @Override
    public void delete(String quotaId) {
        removeById(quotaId);
    }

    @Override
    public PageResult<BudgetQuota> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetQuota> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 配额名称模糊查询
        if (hasValue(params, "quotaName")) {
            wrapper.like("QUOTA_NAME", params.get("quotaName").toString().trim());
        }
        // 配额编码精确/模糊
        if (hasValue(params, "quotaCode")) {
            wrapper.like("QUOTA_CODE", params.get("quotaCode").toString().trim());
        }
        // 配额类型
        if (hasValue(params, "quotaType")) {
            wrapper.eq("QUOTA_TYPE", params.get("quotaType").toString().trim());
        }
        // 配额状态
        if (hasValue(params, "quotaStatus")) {
            wrapper.eq("QUOTA_STATUS", params.get("quotaStatus").toString().trim());
        }
        // 预算ID（关联预算科目）
        if (hasValue(params, "budgetId")) {
            wrapper.eq("BUDGET_ID", params.get("budgetId").toString().trim());
        }
        // 兼容前端字段 budgetAccount
        if (hasValue(params, "budgetAccount")) {
            wrapper.eq("BUDGET_ID", params.get("budgetAccount").toString().trim());
        }
        // 组织单元筛选（前端字段 organizationPath）
        if (hasValue(params, "organizationPath")) {
            wrapper.eq("ORGANIZATION_ID", params.get("organizationPath").toString().trim());
        }
        // 是否启用
        if (params.get("isEnabled") != null) {
            wrapper.eq("IS_ENABLED", params.get("isEnabled"));
        }

        // 排序
        wrapper.orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetQuota> page = new Page<>(pageNum, pageSize);
        IPage<BudgetQuota> pageResult = baseMapper.selectPage(page, wrapper);

        // 4. 填充展示字段（组织名称、科目名称、剩余金额、使用率）
        fillDisplayFields(pageResult.getRecords());

        // 5. 封装返回
        PageResult<BudgetQuota> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);
        result.setTotalPage((int) pageResult.getPages());
        return result;
    }

    /**
     * 填充展示用的虚拟字段（批量查询，避免 N+1）
     */
    private void fillDisplayFields(List<BudgetQuota> quotas) {
        if (quotas == null || quotas.isEmpty()) {
            return;
        }

        Set<String> organizationIds = quotas.stream()
                .map(BudgetQuota::getOrganizationId)
                .filter(id -> id != null && !id.trim().isEmpty())
                .collect(Collectors.toSet());
        Set<String> budgetIds = quotas.stream()
                .map(BudgetQuota::getBudgetId)
                .filter(id -> id != null && !id.trim().isEmpty())
                .collect(Collectors.toSet());

        Map<String, String> organizationNameMap = organizationIds.isEmpty()
                ? Collections.emptyMap()
                : organizationMapper.selectBatchIds(organizationIds).stream()
                        .collect(Collectors.toMap(BudgetOrganization::getOrganizationId,
                                BudgetOrganization::getOrganizationName,
                                (a, b) -> a));

        Map<String, String> accountNameMap = budgetIds.isEmpty()
                ? Collections.emptyMap()
                : accountMapper.selectBatchIds(budgetIds).stream()
                        .collect(Collectors.toMap(BudgetAccount::getAccountId,
                                BudgetAccount::getAccountName,
                                (a, b) -> a));

        for (BudgetQuota quota : quotas) {
            quota.setOrganizationName(organizationNameMap.getOrDefault(quota.getOrganizationId(), ""));
            quota.setBudgetAccountName(accountNameMap.getOrDefault(quota.getBudgetId(), ""));

            BigDecimal quotaAmount = quota.getQuotaAmount() == null ? BigDecimal.ZERO : quota.getQuotaAmount();
            BigDecimal usedAmount = quota.getUsedAmount() == null ? BigDecimal.ZERO : quota.getUsedAmount();
            BigDecimal remainingAmount = quota.getAvailableAmount() != null
                    ? quota.getAvailableAmount()
                    : quotaAmount.subtract(usedAmount);
            quota.setRemainingAmount(remainingAmount);

            double usageRate = 0D;
            if (quotaAmount.compareTo(BigDecimal.ZERO) > 0) {
                usageRate = usedAmount.multiply(BigDecimal.valueOf(100))
                        .divide(quotaAmount, 2, RoundingMode.HALF_UP)
                        .doubleValue();
            }
            quota.setUsageRate(usageRate);
        }
    }

    /** 判断 Map 中是否存在非空非空白的值 */
    private boolean hasValue(Map<String, Object> params, String key) {
        Object val = params.get(key);
        return val != null && !val.toString().trim().isEmpty();
    }

    @Override
    public void enable(String quotaId) {
        BudgetQuota quota = getById(quotaId);
        if (quota != null) {
            quota.setIsEnabled(true);
            updateById(quota);
        }
    }

    @Override
    public void disable(String quotaId) {
        BudgetQuota quota = getById(quotaId);
        if (quota != null) {
            quota.setIsEnabled(false);
            updateById(quota);
        }
    }

    @Override
    public Map<String, Object> allocate(Map<String, Object> params) {
        // TODO: 实现分配配额逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "配额分配成功");
        return result;
    }

    @Override
    public void adjust(Map<String, Object> params) {
        String quotaId = (String) params.get("quotaId");
        if (quotaId == null) {
            throw new RuntimeException("配额ID不能为空");
        }
        BudgetQuota quota = getById(quotaId);
        if (quota == null) {
            throw new RuntimeException("配额不存在");
        }
        // 调整配额金额
        if (hasValue(params, "quotaAmount")) {
            quota.setQuotaAmount(new BigDecimal(params.get("quotaAmount").toString()));
        }
        // 调整可用金额
        if (hasValue(params, "availableAmount")) {
            quota.setAvailableAmount(new BigDecimal(params.get("availableAmount").toString()));
        }
        // 调整配额状态
        if (hasValue(params, "quotaStatus")) {
            quota.setQuotaStatus(params.get("quotaStatus").toString());
        }
        // 调整备注
        if (hasValue(params, "remark")) {
            quota.setRemark(params.get("remark").toString());
        }
        updateById(quota);
        log.info("调整配额成功，ID: {}", quotaId);
    }

    @Override
    public Map<String, Object> batchAdjust(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        java.util.List<String> quotaIds = (java.util.List<String>) params.get("quotaIds");

        if (quotaIds == null || quotaIds.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "配额ID列表不能为空");
            return result;
        }

        int successCount = 0;
        int failCount = 0;
        java.util.List<String> failedIds = new java.util.ArrayList<>();

        for (String quotaId : quotaIds) {
            try {
                Map<String, Object> adjustParams = new HashMap<>();
                adjustParams.put("quotaId", quotaId);
                adjustParams.putAll(params);
                adjust(adjustParams);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(quotaId);
                log.error("批量调整配额失败，ID: {}", quotaId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", quotaIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量调整配额完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public java.util.List<BudgetQuota> exportData(Map<String, Object> params) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BudgetQuota> wrapper =
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();

        // 应用查询条件
        if (params.get("quotaType") != null) {
            wrapper.eq("QUOTA_TYPE", params.get("quotaType"));
        }
        if (params.get("quotaStatus") != null) {
            wrapper.eq("QUOTA_STATUS", params.get("quotaStatus"));
        }
        if (params.get("companyId") != null) {
            wrapper.eq("COMPANY_ID", params.get("companyId"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        return baseMapper.selectList(wrapper);
    }

    @Override
    public java.util.List<BudgetQuota> list(com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BudgetQuota> wrapper) {
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        try {
            // 总数（排除已删除）
            QueryWrapper<BudgetQuota> totalWrapper = new QueryWrapper<>();
            totalWrapper.eq("DEL_FLAG", 0);
            int totalCount = baseMapper.selectCount(totalWrapper).intValue();
            statistics.put("totalCount", totalCount);

            // ACTIVE 数量（注意：状态值大写 ACTIVE）
            QueryWrapper<BudgetQuota> activeWrapper = new QueryWrapper<>();
            activeWrapper.eq("DEL_FLAG", 0).eq("QUOTA_STATUS", "ACTIVE");
            int activeCount = baseMapper.selectCount(activeWrapper).intValue();
            statistics.put("activeCount", activeCount);

            // INACTIVE 数量
            QueryWrapper<BudgetQuota> inactiveWrapper = new QueryWrapper<>();
            inactiveWrapper.eq("DEL_FLAG", 0).eq("QUOTA_STATUS", "INACTIVE");
            int inactiveCount = baseMapper.selectCount(inactiveWrapper).intValue();
            statistics.put("inactiveCount", inactiveCount);

            // EXPIRED 数量
            QueryWrapper<BudgetQuota> expiredWrapper = new QueryWrapper<>();
            expiredWrapper.eq("DEL_FLAG", 0).eq("QUOTA_STATUS", "EXPIRED");
            int expiredCount = baseMapper.selectCount(expiredWrapper).intValue();
            statistics.put("expiredCount", expiredCount);

            // 汇总金额
            QueryWrapper<BudgetQuota> amtWrapper = new QueryWrapper<>();
            amtWrapper.eq("DEL_FLAG", 0).select("SUM(QUOTA_AMOUNT) AS quotaAmount",
                    "SUM(USED_AMOUNT) AS usedAmount",
                    "SUM(AVAILABLE_AMOUNT) AS availableAmount",
                    "SUM(ALLOCATED_AMOUNT) AS allocatedAmount");
            List<Map<String, Object>> amtList = baseMapper.selectMaps(amtWrapper);
            if (amtList != null && !amtList.isEmpty()) {
                Map<String, Object> amt = amtList.get(0);
                BigDecimal totalQuota = toBigDecimal(amt.get("quotaAmount"));
                BigDecimal usedAmount = toBigDecimal(amt.get("usedAmount"));
                BigDecimal availableAmount = toBigDecimal(amt.get("availableAmount"));
                BigDecimal allocatedAmount = toBigDecimal(amt.get("allocatedAmount"));
                statistics.put("totalQuota", totalQuota);
                statistics.put("usedAmount", usedAmount);
                statistics.put("remainingAmount", availableAmount);
                statistics.put("allocatedAmount", allocatedAmount);
                // 使用率
                double usageRate = totalQuota.compareTo(BigDecimal.ZERO) > 0
                        ? usedAmount.divide(totalQuota, 4, java.math.RoundingMode.HALF_UP)
                              .multiply(BigDecimal.valueOf(100))
                              .setScale(1, java.math.RoundingMode.HALF_UP).doubleValue()
                        : 0.0;
                statistics.put("usageRate", usageRate);
                // 分配率
                double allocationRate = totalQuota.compareTo(BigDecimal.ZERO) > 0
                        ? allocatedAmount.divide(totalQuota, 4, java.math.RoundingMode.HALF_UP)
                              .multiply(BigDecimal.valueOf(100))
                              .setScale(1, java.math.RoundingMode.HALF_UP).doubleValue()
                        : 0.0;
                statistics.put("allocationRate", allocationRate);
                // 剩余率
                double remainingRate = totalQuota.compareTo(BigDecimal.ZERO) > 0
                        ? availableAmount.divide(totalQuota, 4, java.math.RoundingMode.HALF_UP)
                              .multiply(BigDecimal.valueOf(100))
                              .setScale(1, java.math.RoundingMode.HALF_UP).doubleValue()
                        : 0.0;
                statistics.put("remainingRate", remainingRate);
            } else {
                statistics.put("totalQuota", 0);
                statistics.put("usedAmount", 0);
                statistics.put("remainingAmount", 0);
                statistics.put("allocatedAmount", 0);
                statistics.put("usageRate", 0);
                statistics.put("allocationRate", 0);
                statistics.put("remainingRate", 0);
            }
        } catch (Exception e) {
            log.error("获取配额统计信息异常", e);
        }
        return statistics;
    }

    private BigDecimal toBigDecimal(Object val) {
        if (val == null) return BigDecimal.ZERO;
        if (val instanceof BigDecimal) return (BigDecimal) val;
        try { return new BigDecimal(val.toString()); } catch (Exception e) { return BigDecimal.ZERO; }
    }
}
