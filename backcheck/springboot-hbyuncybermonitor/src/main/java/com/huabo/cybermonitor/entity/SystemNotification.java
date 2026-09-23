package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统通知公告实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_SYSTEM_NOTIFICATION")
public class SystemNotification {

    /**
     * 通知ID
     */
    @TableId(value = "NOTIFICATION_ID", type = IdType.ASSIGN_UUID)
    private String notificationId;

    /**
     * 通知标题
     */
    private String notificationTitle;

    /**
     * 通知内容
     */
    private String notificationContent;

    /**
     * 通知类型
     */
    private String notificationType;

    /**
     * 通知级别
     */
    private String notificationLevel;

    /**
     * 通知状态
     */
    private String notificationStatus;

    /**
     * 发布范围
     */
    private String publishScope;

    /**
     * 目标用户
     */
    private String targetUsers;

    /**
     * 目标角色
     */
    private String targetRoles;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 是否弹窗
     */
    private Boolean isPopup;

    /**
     * 是否发送邮件
     */
    private Boolean sendEmail;

    /**
     * 是否发送短信
     */
    private Boolean sendSms;

    /**
     * 附件路径
     */
    private String attachmentPath;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 阅读次数
     */
    private Integer readCount;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // 通知类型常量
    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_BUSINESS = "BUSINESS";
    public static final String TYPE_SECURITY = "SECURITY";
    public static final String TYPE_MAINTENANCE = "MAINTENANCE";
    public static final String TYPE_UPDATE = "UPDATE";
    public static final String TYPE_ALERT = "ALERT";
    public static final String TYPE_ANNOUNCEMENT = "ANNOUNCEMENT";
    public static final String TYPE_REMINDER = "REMINDER";

    // 通知级别常量
    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_WARNING = "WARNING";
    public static final String LEVEL_ERROR = "ERROR";
    public static final String LEVEL_CRITICAL = "CRITICAL";
    public static final String LEVEL_SUCCESS = "SUCCESS";

    // 通知状态常量
    public static final String STATUS_DRAFT = "DRAFT";
    public static final String STATUS_PUBLISHED = "PUBLISHED";
    public static final String STATUS_EXPIRED = "EXPIRED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_SCHEDULED = "SCHEDULED";

    // 发布范围常量
    public static final String SCOPE_ALL = "ALL";
    public static final String SCOPE_USERS = "USERS";
    public static final String SCOPE_ROLES = "ROLES";
    public static final String SCOPE_DEPARTMENTS = "DEPARTMENTS";
    public static final String SCOPE_CUSTOM = "CUSTOM";

    // Manual getter for Lombok compatibility
    public String getNotificationId() {
        return this.notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}
