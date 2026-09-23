package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.budget.BudgetAuditAlert;
import com.management.accountant.oracle.mapper.budget.BudgetAuditAlertMapper;
import com.management.accountant.service.BudgetAuditAlertService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class BudgetAuditAlertServiceImpl implements BudgetAuditAlertService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetAuditAlertMapper alertMapper;

    @Override
    public PageResult<BudgetAuditAlert> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetAuditAlert> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        if (params.get("alertType") != null && StringUtils.hasText(params.get("alertType").toString())) {
            wrapper.eq("ALERT_TYPE", params.get("alertType"));
        }
        if (params.get("alertLevel") != null && StringUtils.hasText(params.get("alertLevel").toString())) {
            wrapper.eq("ALERT_LEVEL", params.get("alertLevel"));
        }
        if (params.get("alertStatus") != null && StringUtils.hasText(params.get("alertStatus").toString())) {
            wrapper.eq("ALERT_STATUS", params.get("alertStatus"));
        }
        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetAuditAlert> page = new Page<>(pageNum, pageSize);
        IPage<BudgetAuditAlert> pageResult = alertMapper.selectPage(page, wrapper);

        PageResult<BudgetAuditAlert> result = new PageResult<>();
        result.setList(pageResult.getRecords());
        result.setTotal((int) pageResult.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public BudgetAuditAlert getById(String alertId) {
        if (!StringUtils.hasText(alertId)) return null;
        QueryWrapper<BudgetAuditAlert> wrapper = new QueryWrapper<>();
        wrapper.eq("ALERT_ID", alertId).eq("IS_DELETED", 0);
        return alertMapper.selectOne(wrapper);
    }

    @Override
    public void handleAlert(String alertId, String handlerId, String handlerName, String handleRemark, String alertStatus) {
        BudgetAuditAlert alert = alertMapper.selectById(alertId);
        if (alert != null) {
            alert.setAlertStatus(alertStatus);
            alert.setHandlerId(handlerId);
            alert.setHandlerName(handlerName);
            alert.setHandleRemark(handleRemark);
            alert.setHandleTime(new Date());
            alertMapper.updateById(alert);
        }
    }

    @Override
    public Map<String, Object> getAlertStats() {
        Map<String, Object> stats = new HashMap<>();
        QueryWrapper<BudgetAuditAlert> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("IS_DELETED", 0);
        stats.put("total", alertMapper.selectCount(totalWrapper));

        QueryWrapper<BudgetAuditAlert> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("IS_DELETED", 0).eq("ALERT_STATUS", "PENDING");
        stats.put("pending", alertMapper.selectCount(pendingWrapper));

        QueryWrapper<BudgetAuditAlert> processingWrapper = new QueryWrapper<>();
        processingWrapper.eq("IS_DELETED", 0).eq("ALERT_STATUS", "PROCESSING");
        stats.put("processing", alertMapper.selectCount(processingWrapper));

        QueryWrapper<BudgetAuditAlert> resolvedWrapper = new QueryWrapper<>();
        resolvedWrapper.eq("IS_DELETED", 0).eq("ALERT_STATUS", "RESOLVED");
        stats.put("resolved", alertMapper.selectCount(resolvedWrapper));

        QueryWrapper<BudgetAuditAlert> criticalWrapper = new QueryWrapper<>();
        criticalWrapper.eq("IS_DELETED", 0).eq("ALERT_LEVEL", "CRITICAL").eq("ALERT_STATUS", "PENDING");
        stats.put("criticalPending", alertMapper.selectCount(criticalWrapper));

        return stats;
    }

    @Override
    public List<BudgetAuditAlert> getPendingAlerts() {
        QueryWrapper<BudgetAuditAlert> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0).eq("ALERT_STATUS", "PENDING");
        wrapper.orderByDesc("CREATE_TIME");
        return alertMapper.selectList(wrapper);
    }
}
