package com.management.accountant.oracle.service.advanced;

import com.management.accountant.oracle.entity.advanced.ReminderStrategy;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 催报管理Service接口
 * 
 * @description 催报管理业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-06
 */
public interface ReminderManagementService {

    /**
     * 获取统计数据
     * 
     * @param companyId 公司ID
     * @return 统计数据
     */
    Map<String, Object> getStats(String companyId);

    /**
     * 获取催报策略列表（分页）
     * 
     * @param keyword 关键词
     * @param status 状态
     * @param companyId 公司ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<ReminderStrategy> getStrategyList(String keyword, String status,
                                                 String companyId, Integer pageNo,
                                                 Integer pageSize);

    /**
     * 发送催报
     * 
     * @param strategyId 策略ID
     * @param targets 目标列表
     * @param message 消息内容
     * @param companyId 公司ID
     * @param userId 用户ID
     * @return 发送结果
     */
    Map<String, Object> sendReminder(String strategyId, List<String> targets,
                                     String message, String companyId, String userId);

    /**
     * 获取催报记录（分页）
     * 
     * @param strategyId 策略ID
     * @param companyId 公司ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<Map<String, Object>> getReminderRecords(String strategyId, String companyId,
                                                       Integer pageNo, Integer pageSize);

    /**
     * 获取催报目标列表
     * 
     * @param strategyId 策略ID
     * @return 目标列表
     */
    List<Map<String, Object>> getStrategyTargets(String strategyId);
}

