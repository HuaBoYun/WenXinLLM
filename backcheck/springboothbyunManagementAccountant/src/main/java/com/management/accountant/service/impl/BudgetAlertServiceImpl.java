package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAlert;
import com.management.accountant.oracle.mapper.budget.BudgetAlertMapper;
import com.management.accountant.service.BudgetAlertService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 预算警报Service实现类
 * 
 * @description 预算警报业务实现
 * @author AI Agent
 * @date 2026-02-09
 */
@Service
@Slf4j
public class BudgetAlertServiceImpl implements BudgetAlertService {
    @Resource
    private BudgetAlertMapper alertMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetAlert create(BudgetAlert alert) {
        // 1. 参数校验
        if (alert == null) {
            throw new ServiceException("警报信息不能为空");
        }
        if (!StringUtils.hasText(alert.getAlertName())) {
            throw new ServiceException("警报名称不能为空");
        }
        if (!StringUtils.hasText(alert.getAlertType())) {
            throw new ServiceException("警报类型不能为空");
        }

        // 2. 生成警报编码
        if (!StringUtils.hasText(alert.getAlertCode())) {
            alert.setAlertCode(generateAlertCode());
        }

        // 3. 检查编码唯一性
        QueryWrapper<BudgetAlert> checkWrapper = new QueryWrapper<>();
        checkWrapper.eq("ALERT_CODE", alert.getAlertCode());
        if (alertMapper.selectCount(checkWrapper) > 0) {
            throw new ServiceException("警报编码已存在");
        }

        // 4. 设置默认值
        if (alert.getDelFlag() == null) {
            alert.setDelFlag(0);
        }
        if (!StringUtils.hasText(alert.getAlertStatus())) {
            alert.setAlertStatus("PENDING");
        }
        if (!StringUtils.hasText(alert.getAlertLevel())) {
            alert.setAlertLevel("MEDIUM");
        }
        if (alert.getPriority() == null) {
            alert.setPriority(5);
        }
        if (alert.getIsNotified() == null) {
            alert.setIsNotified(0);
        }
        if (alert.getAutoClose() == null) {
            alert.setAutoClose(0);
        }
        if (alert.getTriggerTime() == null) {
            alert.setTriggerTime(new Date());
        }
        alert.setCreateTime(new Date());
        alert.setUpdateTime(new Date());

        // 5. 插入数据库
        int result = alertMapper.insert(alert);
        if (result <= 0) {
            throw new ServiceException("创建预算警报失败");
        }

        log.info("创建预算警报成功，ID: {}", alert.getAlertId());
        return alert;
    }

    @Override
    public BudgetAlert getById(String alertId) {
        if (!StringUtils.hasText(alertId)) {
            throw new ServiceException("警报ID不能为空");
        }
        
        QueryWrapper<BudgetAlert> wrapper = new QueryWrapper<>();
        wrapper.eq("ALERT_ID", alertId);
        
        return alertMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetAlert alert) {
        if (alert == null || !StringUtils.hasText(alert.getAlertId())) {
            throw new ServiceException("警报ID不能为空");
        }

        BudgetAlert existing = getById(alert.getAlertId());
        if (existing == null) {
            throw new ServiceException("预算警报不存在");
        }

        // 如果修改了编码，检查唯一性
        if (StringUtils.hasText(alert.getAlertCode()) && !alert.getAlertCode().equals(existing.getAlertCode())) {
            QueryWrapper<BudgetAlert> checkWrapper = new QueryWrapper<>();
            checkWrapper.eq("ALERT_CODE", alert.getAlertCode())
                       .ne("ALERT_ID", alert.getAlertId());
            if (alertMapper.selectCount(checkWrapper) > 0) {
                throw new ServiceException("警报编码已存在");
            }
        }

        alert.setUpdateTime(new Date());
        int result = alertMapper.updateById(alert);
        if (result <= 0) {
            throw new ServiceException("更新预算警报失败");
        }

        log.info("更新预算警报成功，ID: {}", alert.getAlertId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String alertId) {
        if (!StringUtils.hasText(alertId)) {
            throw new ServiceException("警报ID不能为空");
        }

        BudgetAlert alert = getById(alertId);
        if (alert == null) {
            throw new ServiceException("预算警报不存在");
        }

        // 使用 deleteById 让 @TableLogic 自动处理逻辑删除
        int result = alertMapper.deleteById(alertId);
        if (result <= 0) {
            throw new ServiceException("删除预算警报失败");
        }

        log.info("删除预算警报成功，ID: {}", alertId);
    }

    @Override
    public PageResult<BudgetAlert> getPage(Map<String, Object> params) {
        // 1. 获取分页参数
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        // 2. 构建查询条件
        QueryWrapper<BudgetAlert> wrapper = new QueryWrapper<>();
        if (params.get("alertCode") != null && StringUtils.hasText(params.get("alertCode").toString())) {
            wrapper.like("ALERT_CODE", params.get("alertCode"));
        }

        // 警报名称
        if (params.get("alertName") != null && StringUtils.hasText(params.get("alertName").toString())) {
            wrapper.like("ALERT_NAME", params.get("alertName"));
        }

        // 警报类型
        if (params.get("alertType") != null && StringUtils.hasText(params.get("alertType").toString())) {
            wrapper.eq("ALERT_TYPE", params.get("alertType"));
        }

        // 警报级别
        if (params.get("alertLevel") != null && StringUtils.hasText(params.get("alertLevel").toString())) {
            wrapper.eq("ALERT_LEVEL", params.get("alertLevel"));
        }

        // 警报状态
        if (params.get("alertStatus") != null && StringUtils.hasText(params.get("alertStatus").toString())) {
            wrapper.eq("ALERT_STATUS", params.get("alertStatus"));
        }

        // 预算ID
        if (params.get("budgetId") != null && StringUtils.hasText(params.get("budgetId").toString())) {
            wrapper.eq("BUDGET_ID", params.get("budgetId"));
        }

        // 组织ID
        if (params.get("organizationId") != null && StringUtils.hasText(params.get("organizationId").toString())) {
            wrapper.eq("ORGANIZATION_ID", params.get("organizationId"));
        }

        // 公司ID
        if (params.get("companyId") != null && StringUtils.hasText(params.get("companyId").toString())) {
            wrapper.eq("COMPANY_ID", params.get("companyId"));
        }

        // 排序
        wrapper.orderByDesc("PRIORITY").orderByDesc("CREATE_TIME");

        // 3. 执行分页查询
        Page<BudgetAlert> page = new Page<>(pageNum, pageSize);
        IPage<BudgetAlert> pageResult = alertMapper.selectPage(page, wrapper);

        // 4. 封装返回结果
        PageResult<BudgetAlert> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acknowledge(String alertId, String acknowledgedBy, String note) {
        if (!StringUtils.hasText(alertId)) {
            throw new ServiceException("警报ID不能为空");
        }

        BudgetAlert alert = getById(alertId);
        if (alert == null) {
            throw new ServiceException("预算警报不存在");
        }

        if ("ACKNOWLEDGED".equals(alert.getAlertStatus()) || "RESOLVED".equals(alert.getAlertStatus())) {
            throw new ServiceException("警报已确认或已解决");
        }

        BudgetAlert update = new BudgetAlert();
        update.setAlertId(alertId);
        update.setAlertStatus("ACKNOWLEDGED");
        update.setAcknowledgedBy(acknowledgedBy);
        update.setAcknowledgedTime(new Date());
        update.setAcknowledgedNote(note);
        update.setUpdateTime(new Date());

        int result = alertMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("确认警报失败");
        }

        log.info("确认警报成功，ID: {}", alertId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchAcknowledge(List<String> alertIds, String acknowledgedBy, String note) {
        if (alertIds == null || alertIds.isEmpty()) {
            throw new ServiceException("警报ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String alertId : alertIds) {
            try {
                acknowledge(alertId, acknowledgedBy, note);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(alertId);
                log.error("批量确认警报失败，ID: {}", alertId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", alertIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量确认警报完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void escalate(String alertId, String escalatedBy, String escalatedTo, String reason) {
        if (!StringUtils.hasText(alertId)) {
            throw new ServiceException("警报ID不能为空");
        }

        BudgetAlert alert = getById(alertId);
        if (alert == null) {
            throw new ServiceException("预算警报不存在");
        }

        if ("ESCALATED".equals(alert.getAlertStatus())) {
            throw new ServiceException("警报已升级");
        }

        BudgetAlert update = new BudgetAlert();
        update.setAlertId(alertId);
        update.setAlertStatus("ESCALATED");
        update.setEscalatedBy(escalatedBy);
        update.setEscalatedTo(escalatedTo);
        update.setEscalatedReason(reason);
        update.setEscalatedTime(new Date());
        update.setUpdateTime(new Date());

        int result = alertMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("升级警报失败");
        }

        log.info("升级警报成功，ID: {}", alertId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resolve(String alertId, String resolvedBy, String note) {
        if (!StringUtils.hasText(alertId)) {
            throw new ServiceException("警报ID不能为空");
        }

        BudgetAlert alert = getById(alertId);
        if (alert == null) {
            throw new ServiceException("预算警报不存在");
        }

        if ("RESOLVED".equals(alert.getAlertStatus()) || "CLOSED".equals(alert.getAlertStatus())) {
            throw new ServiceException("警报已解决或已关闭");
        }

        BudgetAlert update = new BudgetAlert();
        update.setAlertId(alertId);
        update.setAlertStatus("RESOLVED");
        update.setResolvedBy(resolvedBy);
        update.setResolvedTime(new Date());
        update.setResolvedNote(note);
        update.setUpdateTime(new Date());

        int result = alertMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("解决警报失败");
        }

        log.info("解决警报成功，ID: {}", alertId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void close(String alertId, String closedBy, String note) {
        if (!StringUtils.hasText(alertId)) {
            throw new ServiceException("警报ID不能为空");
        }

        BudgetAlert alert = getById(alertId);
        if (alert == null) {
            throw new ServiceException("预算警报不存在");
        }

        if ("CLOSED".equals(alert.getAlertStatus())) {
            throw new ServiceException("警报已关闭");
        }

        BudgetAlert update = new BudgetAlert();
        update.setAlertId(alertId);
        update.setAlertStatus("CLOSED");
        update.setResolvedBy(closedBy);
        update.setResolvedTime(new Date());
        update.setResolvedNote(note);
        update.setUpdateTime(new Date());

        int result = alertMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("关闭警报失败");
        }

        log.info("关闭警报成功，ID: {}", alertId);
    }

    @Override
    public Map<String, Object> getStatistics(Map<String, Object> params) {
        Map<String, Object> statistics = new HashMap<>();

        String companyId = params.get("companyId") != null ? params.get("companyId").toString() : null;

        // 总数
        QueryWrapper<BudgetAlert> totalWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(companyId)) {
            totalWrapper.eq("COMPANY_ID", companyId);
        }
        long totalCount = alertMapper.selectCount(totalWrapper);
        statistics.put("totalCount", totalCount);

        // 活跃数量（非CLOSED、非RESOLVED）
        QueryWrapper<BudgetAlert> activeWrapper = new QueryWrapper<>();
        activeWrapper.notIn("ALERT_STATUS", Arrays.asList("CLOSED", "RESOLVED"));
        if (StringUtils.hasText(companyId)) {
            activeWrapper.eq("COMPANY_ID", companyId);
        }
        long activeCount = alertMapper.selectCount(activeWrapper);
        statistics.put("activeCount", activeCount);

        // 今日警报
        QueryWrapper<BudgetAlert> todayWrapper = new QueryWrapper<>();
        todayWrapper.apply("TRUNC(TRIGGER_TIME) = TRUNC(CURRENT_TIMESTAMP)");
        if (StringUtils.hasText(companyId)) {
            todayWrapper.eq("COMPANY_ID", companyId);
        }
        long todayCount = alertMapper.selectCount(todayWrapper);
        statistics.put("todayCount", todayCount);

        // 未读（待处理）
        QueryWrapper<BudgetAlert> unreadWrapper = new QueryWrapper<>();
        unreadWrapper.eq("ALERT_STATUS", "PENDING");
        if (StringUtils.hasText(companyId)) {
            unreadWrapper.eq("COMPANY_ID", companyId);
        }
        long unreadCount = alertMapper.selectCount(unreadWrapper);
        statistics.put("unreadCount", unreadCount);

        // 待处理
        statistics.put("pendingCount", unreadCount);

        // 已确认
        QueryWrapper<BudgetAlert> acknowledgedWrapper = new QueryWrapper<>();
        acknowledgedWrapper.eq("ALERT_STATUS", "ACKNOWLEDGED");
        if (StringUtils.hasText(companyId)) {
            acknowledgedWrapper.eq("COMPANY_ID", companyId);
        }
        statistics.put("acknowledgedCount", alertMapper.selectCount(acknowledgedWrapper));

        // 已解决
        QueryWrapper<BudgetAlert> resolvedWrapper = new QueryWrapper<>();
        resolvedWrapper.eq("ALERT_STATUS", "RESOLVED");
        if (StringUtils.hasText(companyId)) {
            resolvedWrapper.eq("COMPANY_ID", companyId);
        }
        statistics.put("resolvedCount", alertMapper.selectCount(resolvedWrapper));

        // 已关闭
        QueryWrapper<BudgetAlert> closedWrapper = new QueryWrapper<>();
        closedWrapper.eq("ALERT_STATUS", "CLOSED");
        if (StringUtils.hasText(companyId)) {
            closedWrapper.eq("COMPANY_ID", companyId);
        }
        statistics.put("closedCount", alertMapper.selectCount(closedWrapper));

        // 按级别统计
        Map<String, Long> levelStats = new HashMap<>();
        for (String level : Arrays.asList("CRITICAL", "HIGH", "MEDIUM", "LOW", "INFO")) {
            QueryWrapper<BudgetAlert> levelWrapper = new QueryWrapper<>();
            levelWrapper.eq("ALERT_LEVEL", level);
            if (StringUtils.hasText(companyId)) {
                levelWrapper.eq("COMPANY_ID", companyId);
            }
            levelStats.put(level, alertMapper.selectCount(levelWrapper));
        }
        statistics.put("levelStats", levelStats);

        return statistics;
    }

    @Override
    public int getPendingCount(String companyId) {
        QueryWrapper<BudgetAlert> wrapper = new QueryWrapper<>();
        wrapper.eq("ALERT_STATUS", "PENDING");
        if (StringUtils.hasText(companyId)) {
            wrapper.eq("COMPANY_ID", companyId);
        }
        return Math.toIntExact(alertMapper.selectCount(wrapper));
    }

    @Override
    public List<BudgetAlert> getByUser(String userId, Map<String, Object> params) {
        QueryWrapper<BudgetAlert> wrapper = new QueryWrapper<>();

        // 查询用户相关的警报（创建人、确认人、处理人、升级目标）
        wrapper.and(w -> w.eq("CREATE_BY", userId)
                         .or().eq("ACKNOWLEDGED_BY", userId)
                         .or().eq("RESOLVED_BY", userId)
                         .or().eq("ESCALATED_TO", userId));

        // 状态过滤
        if (params.get("alertStatus") != null && StringUtils.hasText(params.get("alertStatus").toString())) {
            wrapper.eq("ALERT_STATUS", params.get("alertStatus"));
        }

        wrapper.orderByDesc("PRIORITY").orderByDesc("CREATE_TIME");

        return alertMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendNotification(String alertId) {
        if (!StringUtils.hasText(alertId)) {
            throw new ServiceException("警报ID不能为空");
        }

        BudgetAlert alert = getById(alertId);
        if (alert == null) {
            throw new ServiceException("预算警报不存在");
        }

        // TODO: 实际的通知发送逻辑（系统通知、邮件、短信等）

        BudgetAlert update = new BudgetAlert();
        update.setAlertId(alertId);
        update.setIsNotified(1);
        update.setNotificationTime(new Date());
        update.setUpdateTime(new Date());

        int result = alertMapper.updateById(update);
        if (result <= 0) {
            throw new ServiceException("更新通知状态失败");
        }

        log.info("发送警报通知成功，ID: {}", alertId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchSendNotification(List<String> alertIds) {
        if (alertIds == null || alertIds.isEmpty()) {
            throw new ServiceException("警报ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<String> failedIds = new ArrayList<>();

        for (String alertId : alertIds) {
            try {
                sendNotification(alertId);
                successCount++;
            } catch (Exception e) {
                failCount++;
                failedIds.add(alertId);
                log.error("批量发送通知失败，ID: {}", alertId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", alertIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failedIds", failedIds);

        log.info("批量发送通知完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoCloseExpiredAlerts() {
        // 查询需要自动关闭的警报
        QueryWrapper<BudgetAlert> wrapper = new QueryWrapper<>();
        wrapper.eq("AUTO_CLOSE", 1)
               .ne("ALERT_STATUS", "CLOSED")
               .lt("EXPIRE_TIME", new Date());

        List<BudgetAlert> expiredAlerts = alertMapper.selectList(wrapper);

        int closedCount = 0;
        for (BudgetAlert alert : expiredAlerts) {
            try {
                close(alert.getAlertId(), "SYSTEM", "自动关闭：已过期");
                closedCount++;
            } catch (Exception e) {
                log.error("自动关闭警报失败，ID: {}", alert.getAlertId(), e);
            }
        }

        log.info("自动关闭过期警报完成，关闭数量: {}", closedCount);
        return closedCount;
    }

    @Override
    public List<BudgetAlert> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetAlert> wrapper = new QueryWrapper<>();

        // 应用查询条件
        if (params.get("alertType") != null && StringUtils.hasText(params.get("alertType").toString())) {
            wrapper.eq("ALERT_TYPE", params.get("alertType"));
        }
        if (params.get("alertLevel") != null && StringUtils.hasText(params.get("alertLevel").toString())) {
            wrapper.eq("ALERT_LEVEL", params.get("alertLevel"));
        }
        if (params.get("alertStatus") != null && StringUtils.hasText(params.get("alertStatus").toString())) {
            wrapper.eq("ALERT_STATUS", params.get("alertStatus"));
        }
        if (params.get("companyId") != null && StringUtils.hasText(params.get("companyId").toString())) {
            wrapper.eq("COMPANY_ID", params.get("companyId"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        return alertMapper.selectList(wrapper);
    }

    /**
     * 生成警报编码
     */
    private String generateAlertCode() {
        return "ALT" + System.currentTimeMillis();
    }
}

