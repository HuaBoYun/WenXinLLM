package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 通知阅读记录实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_NOTIFICATION_READ")
public class NotificationRead {

    /**
     * 阅读记录ID
     */
    @TableId(value = "READ_ID", type = IdType.ASSIGN_UUID)
    private String readId;

    /**
     * 通知ID
     */
    private String notificationId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 阅读状态
     */
    private String readStatus;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 阅读IP
     */
    private String readIp;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 阅读设备
     */
    private String readDevice;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    // 阅读状态常量
    public static final String READ_STATUS_UNREAD = "UNREAD";
    public static final String READ_STATUS_READ = "READ";
    public static final String READ_STATUS_STARRED = "STARRED";
    public static final String READ_STATUS_ARCHIVED = "ARCHIVED";
    public static final String READ_STATUS_DELETED = "DELETED";
}
