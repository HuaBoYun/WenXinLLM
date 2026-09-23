package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryNotification;
import com.global.treasurer.mapper.RegulatoryNotificationMapper;
import com.global.treasurer.service.RegulatoryNotificationService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 监管通知服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class RegulatoryNotificationServiceImpl implements RegulatoryNotificationService {
    @Autowired
    private RegulatoryNotificationMapper notificationMapper;

    @Override
    public PageInfo<TblRegulatoryNotification> getNotificationList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? (Integer) params.get("pageNum") : 1;
        int pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<TblRegulatoryNotification> list = notificationMapper.selectNotificationList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblRegulatoryNotification getNotificationById(String notificationId) {
        TblRegulatoryNotification notification = notificationMapper.selectNotificationById(notificationId);
        if (notification == null) {
            throw new ServiceException(404, "监管通知不存在");
        }
        return notification;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryNotification saveNotification(TblRegulatoryNotification notification) {
        if (notification.getNotificationId() == null || notification.getNotificationId().isEmpty()) {
            notification.setNotificationCode(generateNotificationCode());
            notification.setSendStatus("DRAFT");
            notification.setDeleteFlag(0);
            notification.setCreatedTime(new Date());
            notificationMapper.insert(notification);
        } else {
            notification.setUpdatedTime(new Date());
            notificationMapper.updateById(notification);
        }
        return notification;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotification(String notificationId) {
        TblRegulatoryNotification notification = getNotificationById(notificationId);
        notification.setDeleteFlag(1);
        notification.setUpdatedTime(new Date());
        notificationMapper.updateById(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteNotifications(List<String> notificationIds) {
        notificationMapper.batchDeleteByIds(notificationIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryNotification sendNotification(String notificationId) {
        TblRegulatoryNotification notification = getNotificationById(notificationId);
        notification.setSendStatus("SENT");
        notification.setSendTime(new Date());
        notification.setUpdatedTime(new Date());
        notificationMapper.updateById(notification);
        return notification;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryNotification acknowledgeNotification(String notificationId) {
        TblRegulatoryNotification notification = getNotificationById(notificationId);
        notification.setSendStatus("ACKNOWLEDGED");
        notification.setAcknowledgeTime(new Date());
        notification.setUpdatedTime(new Date());
        notificationMapper.updateById(notification);
        return notification;
    }

    @Override
    public List<TblRegulatoryNotification> getUnreadNotifications() {
        return notificationMapper.selectList(
            new QueryWrapper<TblRegulatoryNotification>()
                .eq("IS_READ", 0)
                .orderByDesc("CREATED_TIME")
        );
    }

    @Override
    public List<TblRegulatoryNotification> getUrgentNotifications() {
        return notificationMapper.selectList(
            new QueryWrapper<TblRegulatoryNotification>()
                .eq("PRIORITY_LEVEL", "URGENT")
                .eq("SEND_STATUS", "PENDING")
                .orderByDesc("CREATED_TIME")
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryNotification markAsRead(String notificationId) {
        TblRegulatoryNotification notification = getNotificationById(notificationId);
        notification.setUpdatedTime(new Date());
        notificationMapper.updateById(notification);
        return notification;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchMarkAsRead(List<String> notificationIds) {
        if (notificationIds != null && !notificationIds.isEmpty()) {
            for (String notificationId : notificationIds) {
                markAsRead(notificationId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryNotification processNotification(String notificationId, String processResult) {
        TblRegulatoryNotification notification = getNotificationById(notificationId);
        notification.setSendStatus("PROCESSED");
        notification.setUpdatedTime(new Date());
        notificationMapper.updateById(notification);
        return notification;
    }

    private String generateNotificationCode() {
        return "NTF" + System.currentTimeMillis();
    }
}

