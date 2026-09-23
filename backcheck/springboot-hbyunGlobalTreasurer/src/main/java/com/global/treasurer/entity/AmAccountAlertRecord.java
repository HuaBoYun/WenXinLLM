package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 账户异常预警记录实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_account_alert_record")
public class AmAccountAlertRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账户ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 预警类型：BALANCE-余额预警，TRANSACTION-交易预警，SECURITY-安全预警，COMPLIANCE-合规预警
     */
    @TableField("alert_type")
    private String alertType;

    /**
     * 预警级别：LOW-低，MEDIUM-中，HIGH-高，CRITICAL-严重
     */
    @TableField("alert_level")
    private String alertLevel;

    /**
     * 预警标题
     */
    @TableField("alert_title")
    private String alertTitle;

    /**
     * 预警内容
     */
    @TableField("alert_content")
    private String alertContent;

    /**
     * 预警数据（JSON格式）
     */
    @TableField("alert_data")
    private String alertData;

    /**
     * 触发条件
     */
    @TableField("trigger_condition")
    private String triggerCondition;

    /**
     * 预警时间
     */
    @TableField("alert_time")
    private LocalDateTime alertTime;

    /**
     * 预警状态：ACTIVE-活跃，HANDLED-已处理，IGNORED-已忽略，RESOLVED-已解决
     */
    @TableField("alert_status")
    private String alertStatus;

    /**
     * 处理时间
     */
    @TableField("handle_time")
    private LocalDateTime handleTime;

    /**
     * 处理人
     */
    @TableField("handle_user")
    private Long handleUser;

    /**
     * 处理结果
     */
    @TableField("handle_result")
    private String handleResult;

    /**
     * 是否已发送通知：1-已发送，0-未发送
     */
    @TableField("notification_sent")
    private String notificationSent;

    /**
     * 通知发送时间
     */
    @TableField("notification_time")
    private LocalDateTime notificationTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField("update_user")
    private Long updateUser;

    /**
     * 版本号
     */
    @TableField("version_no")
    private Long versionNo;

    /**
     * 客户端IP
     */
    @TableField("client_ip")
    private String clientIp;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getAlertTitle() { return alertTitle; }
    public void setAlertTitle(String alertTitle) { this.alertTitle = alertTitle; }
    public String getAlertContent() { return alertContent; }
    public void setAlertContent(String alertContent) { this.alertContent = alertContent; }
    public String getAlertData() { return alertData; }
    public void setAlertData(String alertData) { this.alertData = alertData; }
    public String getTriggerCondition() { return triggerCondition; }
    public void setTriggerCondition(String triggerCondition) { this.triggerCondition = triggerCondition; }
    public LocalDateTime getAlertTime() { return alertTime; }
    public void setAlertTime(LocalDateTime alertTime) { this.alertTime = alertTime; }
    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }
    public LocalDateTime getHandleTime() { return handleTime; }
    public void setHandleTime(LocalDateTime handleTime) { this.handleTime = handleTime; }
    public Long getHandleUser() { return handleUser; }
    public void setHandleUser(Long handleUser) { this.handleUser = handleUser; }
    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
    public String getNotificationSent() { return notificationSent; }
    public void setNotificationSent(String notificationSent) { this.notificationSent = notificationSent; }
    public LocalDateTime getNotificationTime() { return notificationTime; }
    public void setNotificationTime(LocalDateTime notificationTime) { this.notificationTime = notificationTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public Long getVersionNo() { return versionNo; }
    public void setVersionNo(Long versionNo) { this.versionNo = versionNo; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

}
