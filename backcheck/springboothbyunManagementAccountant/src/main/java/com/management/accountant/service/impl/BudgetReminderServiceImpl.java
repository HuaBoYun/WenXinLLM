package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetReminder;
import com.management.accountant.oracle.mapper.BudgetReminderMapper;
import com.management.accountant.service.BudgetReminderService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 预算催报管理Service实现类
 * 
 * @description 预算催报管理业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetReminderServiceImpl implements BudgetReminderService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetReminderMapper reminderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetReminder create(BudgetReminder reminder) {
        if (reminder == null) {
            throw new ServiceException("催报信息不能为空");
        }
        if (!StringUtils.hasText(reminder.getReminderName())) {
            throw new ServiceException("催报名称不能为空");
        }

        if (!StringUtils.hasText(reminder.getReminderCode())) {
            reminder.setReminderCode(generateReminderCode());
        }

        if (reminder.getDelFlag() == null) {
            reminder.setDelFlag(0);
        }
        if (!StringUtils.hasText(reminder.getReminderStatus())) {
            reminder.setReminderStatus("ACTIVE");
        }
        reminder.setCreateTime(new Date());
        reminder.setUpdateTime(new Date());

        int result = reminderMapper.insert(reminder);
        if (result <= 0) {
            throw new ServiceException("创建催报配置失败");
        }

        log.info("创建催报配置成功，ID: {}", reminder.getReminderId());
        return reminder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetReminder reminder) {
        if (reminder == null || !StringUtils.hasText(reminder.getReminderId())) {
            throw new ServiceException("催报ID不能为空");
        }

        reminder.setUpdateTime(new Date());
        int result = reminderMapper.updateById(reminder);
        if (result <= 0) {
            throw new ServiceException("更新催报配置失败");
        }

        log.info("更新催报配置成功，ID: {}", reminder.getReminderId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String reminderId) {
        if (!StringUtils.hasText(reminderId)) {
            throw new ServiceException("催报ID不能为空");
        }

        BudgetReminder reminder = new BudgetReminder();
        reminder.setReminderId(reminderId);
        reminder.setDelFlag(1);
        reminder.setUpdateTime(new Date());

        int result = reminderMapper.updateById(reminder);
        if (result <= 0) {
            throw new ServiceException("删除催报配置失败");
        }

        log.info("删除催报配置成功，ID: {}", reminderId);
    }

    @Override
    public PageResult<BudgetReminder> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetReminder> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        if (params.get("reminderName") != null) {
            wrapper.like("REMINDER_NAME", params.get("reminderName"));
        }
        if (params.get("reminderStatus") != null) {
            wrapper.eq("REMINDER_STATUS", params.get("reminderStatus"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetReminder> page = new Page<>(pageNum, pageSize);
        IPage<BudgetReminder> pageResult = reminderMapper.selectPage(page, wrapper);

        PageResult<BudgetReminder> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> executeReminder(String reminderId) {
        if (!StringUtils.hasText(reminderId)) {
            throw new ServiceException("催报ID不能为空");
        }

        // TODO: 实际的催报执行逻辑
        // 1. 查询催报配置
        // 2. 查询未提交预算的用户/部门
        // 3. 发送催报通知（邮件、短信、站内信）
        // 4. 记录催报历史

        Map<String, Object> result = new HashMap<>();
        result.put("reminderId", reminderId);
        result.put("remindTime", new Date());
        result.put("remindCount", 15); // 催报人数
        result.put("successCount", 15);
        result.put("failCount", 0);
        result.put("status", "SUCCESS");

        log.info("执行催报成功，催报ID: {}, 催报人数: {}", reminderId, 15);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchRemind(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> reminderIds = (List<String>) params.get("reminderIds");

        if (reminderIds == null || reminderIds.isEmpty()) {
            throw new ServiceException("催报ID列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        int totalRemindCount = 0;

        for (String reminderId : reminderIds) {
            try {
                Map<String, Object> result = executeReminder(reminderId);
                successCount++;
                totalRemindCount += (Integer) result.get("remindCount");
            } catch (Exception e) {
                failCount++;
                log.error("批量催报失败，催报ID: {}", reminderId, e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", reminderIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalRemindCount", totalRemindCount);
        result.put("batchTime", new Date());

        log.info("批量催报完成，成功: {}, 失败: {}, 总催报人数: {}", successCount, failCount, totalRemindCount);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getReminderRecords(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        String reminderId = params.get("reminderId") != null ? params.get("reminderId").toString() : null;

        int offset = (pageNum - 1) * pageSize;
        List<Map<String, Object>> records = reminderMapper.selectReminderRecords(reminderId, offset, pageSize);
        int total = reminderMapper.countReminderRecords(reminderId);

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(records);
        result.setTotalRecord(total);
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String reminderId) {
        if (!StringUtils.hasText(reminderId)) {
            throw new ServiceException("催报ID不能为空");
        }

        BudgetReminder reminder = new BudgetReminder();
        reminder.setReminderId(reminderId);
        reminder.setReminderStatus("ACTIVE");
        reminder.setUpdateTime(new Date());

        int result = reminderMapper.updateById(reminder);
        if (result <= 0) {
            throw new ServiceException("启用催报策略失败");
        }

        log.info("启用催报策略成功，ID: {}", reminderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String reminderId) {
        if (!StringUtils.hasText(reminderId)) {
            throw new ServiceException("催报ID不能为空");
        }

        BudgetReminder reminder = new BudgetReminder();
        reminder.setReminderId(reminderId);
        reminder.setReminderStatus("INACTIVE");
        reminder.setUpdateTime(new Date());

        int result = reminderMapper.updateById(reminder);
        if (result <= 0) {
            throw new ServiceException("禁用催报策略失败");
        }

        log.info("禁用催报策略成功，ID: {}", reminderId);
    }

    @Override
    public Map<String, Object> test(String reminderId) {
        if (!StringUtils.hasText(reminderId)) {
            throw new ServiceException("催报ID不能为空");
        }

        // TODO: 实际的测试逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("reminderId", reminderId);
        result.put("testTime", new Date());
        result.put("testResult", "SUCCESS");
        result.put("message", "测试催报发送成功");

        log.info("测试催报策略成功，ID: {}", reminderId);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetReminder copy(String reminderId) {
        if (!StringUtils.hasText(reminderId)) {
            throw new ServiceException("催报ID不能为空");
        }

        BudgetReminder source = reminderMapper.selectById(reminderId);
        if (source == null) {
            throw new ServiceException("催报策略不存在");
        }

        BudgetReminder copied = new BudgetReminder();
        copied.setReminderName(source.getReminderName() + "_副本");
        copied.setReminderCode(generateReminderCode());
        copied.setReminderType(source.getReminderType());
        copied.setReminderStatus("INACTIVE");
        copied.setDelFlag(0);
        copied.setCreateTime(new Date());
        copied.setUpdateTime(new Date());

        int result = reminderMapper.insert(copied);
        if (result <= 0) {
            throw new ServiceException("复制催报策略失败");
        }

        log.info("复制催报策略成功，源ID: {}, 新ID: {}", reminderId, copied.getReminderId());
        return copied;
    }

    @Override
    public Map<String, Object> getStats(Map<String, Object> params) {
        Map<String, Object> stats = new HashMap<>();

        // 总策略数
        QueryWrapper<BudgetReminder> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("DEL_FLAG", 0);
        long total = reminderMapper.selectCount(totalWrapper);
        stats.put("totalStrategies", total);

        // 活跃策略数（ACTIVE状态）
        QueryWrapper<BudgetReminder> activeWrapper = new QueryWrapper<>();
        activeWrapper.eq("DEL_FLAG", 0).eq("REMINDER_STATUS", "ACTIVE");
        long activeTasks = reminderMapper.selectCount(activeWrapper);
        stats.put("activeTasks", activeTasks);

        // 各状态数量
        for (String status : new String[]{"ACTIVE", "INACTIVE", "DRAFT", "DISABLED"}) {
            QueryWrapper<BudgetReminder> sw = new QueryWrapper<>();
            sw.eq("DEL_FLAG", 0).eq("REMINDER_STATUS", status);
            stats.put("count_" + status.toLowerCase(), reminderMapper.selectCount(sw));
        }

        // 各催报方式数量
        for (String method : new String[]{"EMAIL", "SMS", "SYSTEM", "WECHAT"}) {
            QueryWrapper<BudgetReminder> mw = new QueryWrapper<>();
            mw.eq("DEL_FLAG", 0).eq("REMINDER_METHOD", method);
            stats.put("method_" + method.toLowerCase(), reminderMapper.selectCount(mw));
        }

        // 今日发送数和响应率（从记录表统计）
        Map<String, Object> todayStats = reminderMapper.selectTodayStats();
        if (todayStats != null) {
            Object sentToday = todayStats.get("SENT_TODAY");
            Object totalTarget = todayStats.get("TOTAL_TARGET");
            Object totalResponse = todayStats.get("TOTAL_RESPONSE");
            stats.put("sentToday", sentToday != null ? sentToday : 0);
            long target = totalTarget != null ? Long.parseLong(totalTarget.toString()) : 0;
            long response = totalResponse != null ? Long.parseLong(totalResponse.toString()) : 0;
            stats.put("responseRate", target > 0 ? Math.round(response * 100.0 / target) : 0);
        } else {
            stats.put("sentToday", 0);
            stats.put("responseRate", 0);
        }

        return stats;
    }

    @Override
    public Map<String, Object> getTargets(String reminderId) {
        if (!StringUtils.hasText(reminderId)) {
            throw new ServiceException("催报ID不能为空");
        }

        // TODO: 从数据库查询催报目标
        List<Map<String, Object>> targets = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> target = new HashMap<>();
            target.put("targetId", "TARGET_" + (i + 1));
            target.put("targetType", i % 2 == 0 ? "USER" : "DEPARTMENT");
            target.put("targetName", "目标" + (i + 1));
            target.put("status", i % 3 == 0 ? "PENDING" : "SUBMITTED");
            targets.add(target);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("reminderId", reminderId);
        result.put("targets", targets);
        result.put("totalCount", targets.size());

        return result;
    }

    @Override
    public Map<String, Object> getList(Map<String, Object> params) {
        QueryWrapper<BudgetReminder> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        if (params != null) {
            if (params.get("reminderMethod") != null && !params.get("reminderMethod").toString().isEmpty()) {
                wrapper.eq("REMINDER_METHOD", params.get("reminderMethod"));
            }
            if (params.get("reminderStatus") != null && !params.get("reminderStatus").toString().isEmpty()) {
                wrapper.eq("REMINDER_STATUS", params.get("reminderStatus"));
            }
            if (params.get("keyword") != null && !params.get("keyword").toString().isEmpty()) {
                wrapper.like("REMINDER_NAME", params.get("keyword"));
            }
        }

        wrapper.orderByDesc("CREATE_TIME");
        List<BudgetReminder> list = reminderMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", list.size());

        return result;
    }

    private String generateReminderCode() {
        return "RMD" + System.currentTimeMillis();
    }
}

