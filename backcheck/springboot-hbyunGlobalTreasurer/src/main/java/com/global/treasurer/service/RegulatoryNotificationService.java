package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryNotification;

import java.util.List;
import java.util.Map;

/**
 * 监管通知服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface RegulatoryNotificationService {

    /**
     * 分页查询监管通知列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageInfo<TblRegulatoryNotification> getNotificationList(Map<String, Object> params);

    /**
     * 根据ID查询监管通知详情
     *
     * @param notificationId 通知ID
     * @return 监管通知
     */
    TblRegulatoryNotification getNotificationById(String notificationId);

    /**
     * 保存监管通知（新增或更新）
     *
     * @param notification 监管通知
     * @return 保存后的监管通知
     */
    TblRegulatoryNotification saveNotification(TblRegulatoryNotification notification);

    /**
     * 删除监管通知
     *
     * @param notificationId 通知ID
     */
    void deleteNotification(String notificationId);

    /**
     * 批量删除监管通知
     *
     * @param notificationIds 通知ID列表
     */
    void batchDeleteNotifications(List<String> notificationIds);

    /**
     * 发送通知
     *
     * @param notificationId 通知ID
     * @return 发送后的通知
     */
    TblRegulatoryNotification sendNotification(String notificationId);

    /**
     * 确认通知
     *
     * @param notificationId 通知ID
     * @return 确认后的通知
     */
    TblRegulatoryNotification acknowledgeNotification(String notificationId);

    /**
     * 获取未读通知列表
     *
     * @return 未读通知列表
     */
    List<TblRegulatoryNotification> getUnreadNotifications();

    /**
     * 获取紧急通知列表
     *
     * @return 紧急通知列表
     */
    List<TblRegulatoryNotification> getUrgentNotifications();

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @return 标记后的通知
     */
    TblRegulatoryNotification markAsRead(String notificationId);

    /**
     * 批量标记通知为已读
     *
     * @param notificationIds 通知ID列表
     */
    void batchMarkAsRead(List<String> notificationIds);

    /**
     * 处理通知
     *
     * @param notificationId 通知ID
     * @param processResult 处理结果
     * @return 处理后的通知
     */
    TblRegulatoryNotification processNotification(String notificationId, String processResult);
}

