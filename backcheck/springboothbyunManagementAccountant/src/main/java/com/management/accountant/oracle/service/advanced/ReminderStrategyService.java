package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.ReminderStrategy;

import java.util.List;
import java.util.Map;

/**
 * 催报策略Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface ReminderStrategyService {

    /**
     * 查询催报策略列表
     */
    List<ReminderStrategy> selectList(Map<String, Object> params);

    /**
     * 分页查询催报策略列表
     */
    Page<ReminderStrategy> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询催报策略
     */
    ReminderStrategy selectById(String strategyId);

    /**
     * 新增催报策略
     */
    boolean insert(ReminderStrategy strategy);

    /**
     * 修改催报策略
     */
    boolean update(ReminderStrategy strategy);

    /**
     * 删除催报策略
     */
    boolean deleteById(String strategyId);

    /**
     * 执行催报
     */
    boolean executeStrategy(String strategyId);

    /**
     * 启用策略
     */
    boolean enableStrategy(String strategyId);

    /**
     * 禁用策略
     */
    boolean disableStrategy(String strategyId);

    /**
     * 根据催报类型查询策略
     */
    List<ReminderStrategy> selectByReminderType(String reminderType);
}

