package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.budget.BudgetAuditTrail;
import com.management.accountant.oracle.mapper.budget.BudgetAuditTrailMapper;
import com.management.accountant.service.BudgetAuditTrailService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class BudgetAuditTrailServiceImpl implements BudgetAuditTrailService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetAuditTrailMapper auditTrailMapper;

    @Override
    public PageResult<BudgetAuditTrail> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetAuditTrail> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        // 操作类型
        if (params.get("operationType") != null && StringUtils.hasText(params.get("operationType").toString())) {
            wrapper.eq("OPERATION_TYPE", params.get("operationType"));
        }
        // 操作模块 - frontend sends "module", maps to MODULE_NAME
        if (params.get("module") != null && StringUtils.hasText(params.get("module").toString())) {
            wrapper.eq("MODULE_NAME", params.get("module"));
        }
        // 操作用户 - frontend sends "operator", maps to OPERATOR_NAME
        if (params.get("operator") != null && StringUtils.hasText(params.get("operator").toString())) {
            wrapper.like("OPERATOR_NAME", params.get("operator"));
        }
        // IP地址
        if (params.get("ipAddress") != null && StringUtils.hasText(params.get("ipAddress").toString())) {
            wrapper.like("IP_ADDRESS", params.get("ipAddress"));
        }
        // 风险级别
        if (params.get("riskLevel") != null && StringUtils.hasText(params.get("riskLevel").toString())) {
            wrapper.eq("RISK_LEVEL", params.get("riskLevel"));
        }
        // 操作时间范围 - frontend sends operationTime as array [startTime, endTime]
        Object operationTime = params.get("operationTime");
        if (operationTime instanceof List) {
            List<?> timeRange = (List<?>) operationTime;
            if (timeRange.size() >= 2 && timeRange.get(0) != null && timeRange.get(1) != null) {
                String startTime = timeRange.get(0).toString();
                String endTime = timeRange.get(1).toString();
                if (StringUtils.hasText(startTime) && StringUtils.hasText(endTime)) {
                    wrapper.apply("OPERATION_TIME >= TO_TIMESTAMP('" + startTime + "', 'YYYY-MM-DD HH24:MI:SS')");
                    wrapper.apply("OPERATION_TIME <= TO_TIMESTAMP('" + endTime + "', 'YYYY-MM-DD HH24:MI:SS')");
                }
            }
        }

        wrapper.orderByDesc("OPERATION_TIME");

        Page<BudgetAuditTrail> page = new Page<>(pageNum, pageSize);
        IPage<BudgetAuditTrail> pageResult = auditTrailMapper.selectPage(page, wrapper);

        PageResult<BudgetAuditTrail> result = new PageResult<>();
        result.setList(pageResult.getRecords());
        result.setTotal((int) pageResult.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public BudgetAuditTrail getById(String auditId) {
        if (!StringUtils.hasText(auditId)) {
            return null;
        }
        QueryWrapper<BudgetAuditTrail> wrapper = new QueryWrapper<>();
        wrapper.eq("AUDIT_ID", auditId).eq("IS_DELETED", 0);
        return auditTrailMapper.selectOne(wrapper);
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        // 总日志数
        QueryWrapper<BudgetAuditTrail> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("IS_DELETED", 0);
        stats.put("totalLogs", auditTrailMapper.selectCount(totalWrapper));

        // 今日日志数
        QueryWrapper<BudgetAuditTrail> todayWrapper = new QueryWrapper<>();
        todayWrapper.eq("IS_DELETED", 0);
        todayWrapper.apply("TRUNC(OPERATION_TIME) = TRUNC(SYSDATE)");
        stats.put("todayLogs", auditTrailMapper.selectCount(todayWrapper));

        // 今日活跃用户数 - count distinct operators today
        try {
            List<BudgetAuditTrail> users = auditTrailMapper.selectList(
                new QueryWrapper<BudgetAuditTrail>()
                    .eq("IS_DELETED", 0)
                    .apply("TRUNC(OPERATION_TIME) = TRUNC(SYSDATE)")
                    .select("DISTINCT OPERATOR_ID")
            );
            stats.put("activeUsers", users != null ? users.size() : 0);
        } catch (Exception e) {
            log.warn("查询活跃用户数异常", e);
            stats.put("activeUsers", 0);
        }

        // 风险事件数 - HIGH or CRITICAL risk level
        QueryWrapper<BudgetAuditTrail> riskWrapper = new QueryWrapper<>();
        riskWrapper.eq("IS_DELETED", 0);
        riskWrapper.in("RISK_LEVEL", "HIGH", "CRITICAL");
        stats.put("riskEvents", auditTrailMapper.selectCount(riskWrapper));

        return stats;
    }

    @Override
    public List<BudgetAuditTrail> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetAuditTrail> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        if (params.get("operationType") != null && StringUtils.hasText(params.get("operationType").toString())) {
            wrapper.eq("OPERATION_TYPE", params.get("operationType"));
        }
        if (params.get("module") != null && StringUtils.hasText(params.get("module").toString())) {
            wrapper.eq("MODULE_NAME", params.get("module"));
        }
        if (params.get("riskLevel") != null && StringUtils.hasText(params.get("riskLevel").toString())) {
            wrapper.eq("RISK_LEVEL", params.get("riskLevel"));
        }
        wrapper.orderByDesc("OPERATION_TIME");
        return auditTrailMapper.selectList(wrapper);
    }

    @Override
    public int cleanupLogs(int retentionDays) {
        QueryWrapper<BudgetAuditTrail> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.apply("OPERATION_TIME < SYSDATE - " + retentionDays);

        BudgetAuditTrail update = new BudgetAuditTrail();
        update.setIsDeleted(1);
        return auditTrailMapper.update(update, wrapper);
    }

    @Override
    public Map<String, Object> getLogAnalysis(Map<String, Object> params) {
        Map<String, Object> analysis = new HashMap<>();

        // 按操作类型统计
        List<Map<String, Object>> typeStats = new ArrayList<>();
        String[] types = {"LOGIN", "LOGOUT", "QUERY", "CREATE", "UPDATE", "DELETE", "IMPORT", "EXPORT", "APPROVE", "REJECT"};
        for (String type : types) {
            QueryWrapper<BudgetAuditTrail> tw = new QueryWrapper<>();
            tw.eq("IS_DELETED", 0).eq("OPERATION_TYPE", type);
            long count = auditTrailMapper.selectCount(tw);
            if (count > 0) {
                Map<String, Object> item = new HashMap<>();
                item.put("type", type);
                item.put("count", count);
                typeStats.add(item);
            }
        }
        analysis.put("operationTypeStats", typeStats);

        // 按模块统计
        List<Map<String, Object>> moduleStats = new ArrayList<>();
        String[] modules = {"BUDGET_PREPARATION", "BUDGET_ANALYSIS", "BUDGET_CONTROL", "BUDGET_SYSTEM", "USER_MANAGEMENT", "PERMISSION_MANAGEMENT", "SYSTEM_CONFIG"};
        for (String mod : modules) {
            QueryWrapper<BudgetAuditTrail> mw = new QueryWrapper<>();
            mw.eq("IS_DELETED", 0).eq("MODULE_NAME", mod);
            long count = auditTrailMapper.selectCount(mw);
            if (count > 0) {
                Map<String, Object> item = new HashMap<>();
                item.put("module", mod);
                item.put("count", count);
                moduleStats.add(item);
            }
        }
        analysis.put("moduleStats", moduleStats);

        // 按风险级别统计
        List<Map<String, Object>> riskStats = new ArrayList<>();
        String[] risks = {"LOW", "MEDIUM", "HIGH", "CRITICAL"};
        for (String risk : risks) {
            QueryWrapper<BudgetAuditTrail> rw = new QueryWrapper<>();
            rw.eq("IS_DELETED", 0).eq("RISK_LEVEL", risk);
            long count = auditTrailMapper.selectCount(rw);
            Map<String, Object> item = new HashMap<>();
            item.put("level", risk);
            item.put("count", count);
            riskStats.add(item);
        }
        analysis.put("riskLevelStats", riskStats);

        // 按结果统计
        QueryWrapper<BudgetAuditTrail> successWrapper = new QueryWrapper<>();
        successWrapper.eq("IS_DELETED", 0).eq("RESULT", "SUCCESS");
        long successCount = auditTrailMapper.selectCount(successWrapper);

        QueryWrapper<BudgetAuditTrail> failWrapper = new QueryWrapper<>();
        failWrapper.eq("IS_DELETED", 0).eq("RESULT", "FAILURE");
        long failCount = auditTrailMapper.selectCount(failWrapper);

        Map<String, Object> resultStats = new HashMap<>();
        resultStats.put("success", successCount);
        resultStats.put("failure", failCount);
        analysis.put("resultStats", resultStats);

        return analysis;
    }
}

