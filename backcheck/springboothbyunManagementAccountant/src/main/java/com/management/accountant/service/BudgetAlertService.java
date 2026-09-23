package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetAlert;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算警报Service接口
 * 
 * @description 预算警报业务接口，提供警报的创建、查询、确认、升级等功能
 * @author AI Agent
 * @date 2026-02-09
 */
public interface BudgetAlertService {

    /**
     * 创建预算警报
     * 
     * @param alert 警报对象
     * @return 创建后的警报对象
     */
    BudgetAlert create(BudgetAlert alert);

    /**
     * 根据ID查询警报
     * 
     * @param alertId 警报ID
     * @return 警报对象
     */
    BudgetAlert getById(String alertId);

    /**
     * 更新预算警报
     * 
     * @param alert 警报对象
     */
    void update(BudgetAlert alert);

    /**
     * 删除预算警报
     * 
     * @param alertId 警报ID
     */
    void delete(String alertId);

    /**
     * 分页查询警报列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetAlert> getPage(Map<String, Object> params);

    /**
     * 确认警报
     * 
     * @param alertId 警报ID
     * @param acknowledgedBy 确认人ID
     * @param note 确认备注
     */
    void acknowledge(String alertId, String acknowledgedBy, String note);

    /**
     * 批量确认警报
     * 
     * @param alertIds 警报ID列表
     * @param acknowledgedBy 确认人ID
     * @param note 确认备注
     * @return 批量确认结果
     */
    Map<String, Object> batchAcknowledge(List<String> alertIds, String acknowledgedBy, String note);

    /**
     * 升级警报
     * 
     * @param alertId 警报ID
     * @param escalatedBy 升级人ID
     * @param escalatedTo 升级目标
     * @param reason 升级原因
     */
    void escalate(String alertId, String escalatedBy, String escalatedTo, String reason);

    /**
     * 解决警报
     * 
     * @param alertId 警报ID
     * @param resolvedBy 处理人ID
     * @param note 处理备注
     */
    void resolve(String alertId, String resolvedBy, String note);

    /**
     * 关闭警报
     * 
     * @param alertId 警报ID
     * @param closedBy 关闭人ID
     * @param note 关闭备注
     */
    void close(String alertId, String closedBy, String note);

    /**
     * 获取警报统计信息
     * 
     * @param params 查询参数
     * @return 统计信息
     */
    Map<String, Object> getStatistics(Map<String, Object> params);

    /**
     * 获取待处理警报数量
     * 
     * @param companyId 公司ID
     * @return 待处理数量
     */
    int getPendingCount(String companyId);

    /**
     * 获取用户相关警报
     * 
     * @param userId 用户ID
     * @param params 查询参数
     * @return 警报列表
     */
    List<BudgetAlert> getByUser(String userId, Map<String, Object> params);

    /**
     * 发送警报通知
     * 
     * @param alertId 警报ID
     * @return 发送结果
     */
    boolean sendNotification(String alertId);

    /**
     * 批量发送警报通知
     * 
     * @param alertIds 警报ID列表
     * @return 发送结果
     */
    Map<String, Object> batchSendNotification(List<String> alertIds);

    /**
     * 自动关闭过期警报
     * 
     * @return 关闭数量
     */
    int autoCloseExpiredAlerts();

    /**
     * 导出警报数据
     * 
     * @param params 查询参数
     * @return 导出数据列表
     */
    List<BudgetAlert> exportData(Map<String, Object> params);
}

