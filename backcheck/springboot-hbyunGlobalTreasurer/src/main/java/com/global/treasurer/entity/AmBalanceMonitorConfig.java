package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 账户余额监控配置实体类
 * 
 * @author system
 * @since 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("t_am_balance_monitor_config")
public class AmBalanceMonitorConfig implements Serializable {
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
     * 监控类型：MIN_BALANCE-最低余额，MAX_BALANCE-最高余额，BALANCE_CHANGE-余额变动
     */
    @TableField("monitor_type")
    private String monitorType;

    /**
     * 阈值
     */
    @TableField("threshold_value")
    private BigDecimal thresholdValue;

    /**
     * 阈值类型：AMOUNT-金额，PERCENTAGE-百分比
     */
    @TableField("threshold_type")
    private String thresholdType;

    /**
     * 比较操作符：GT-大于，LT-小于，EQ-等于，GTE-大于等于，LTE-小于等于
     */
    @TableField("comparison_operator")
    private String comparisonOperator;

    /**
     * 预警级别：LOW-低，MEDIUM-中，HIGH-高，CRITICAL-严重
     */
    @TableField("warning_level")
    private String warningLevel;

    /**
     * 是否启用：1-启用，0-禁用
     */
    @TableField("is_enabled")
    private String isEnabled;

    /**
     * 通知方式（JSON数组）：EMAIL-邮件，SMS-短信，SYSTEM-系统通知
     */
    @TableField("notification_methods")
    private String notificationMethods;

    /**
     * 通知接收人（JSON数组）
     */
    @TableField("notification_recipients")
    private String notificationRecipients;

    /**
     * 检查频率（分钟）
     */
    @TableField("check_frequency")
    private Integer checkFrequency;

    /**
     * 生效时间开始
     */
    @TableField("effective_time_start")
    private LocalTime effectiveTimeStart;

    /**
     * 生效时间结束
     */
    @TableField("effective_time_end")
    private LocalTime effectiveTimeEnd;

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
    public String getMonitorType() { return monitorType; }
    public void setMonitorType(String monitorType) { this.monitorType = monitorType; }
    public BigDecimal getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
    public String getThresholdType() { return thresholdType; }
    public void setThresholdType(String thresholdType) { this.thresholdType = thresholdType; }
    public String getComparisonOperator() { return comparisonOperator; }
    public void setComparisonOperator(String comparisonOperator) { this.comparisonOperator = comparisonOperator; }
    public String getWarningLevel() { return warningLevel; }
    public void setWarningLevel(String warningLevel) { this.warningLevel = warningLevel; }
    public String getIsEnabled() { return isEnabled; }
    public void setIsEnabled(String isEnabled) { this.isEnabled = isEnabled; }
    public String getNotificationMethods() { return notificationMethods; }
    public void setNotificationMethods(String notificationMethods) { this.notificationMethods = notificationMethods; }
    public String getNotificationRecipients() { return notificationRecipients; }
    public void setNotificationRecipients(String notificationRecipients) { this.notificationRecipients = notificationRecipients; }
    public Integer getCheckFrequency() { return checkFrequency; }
    public void setCheckFrequency(Integer checkFrequency) { this.checkFrequency = checkFrequency; }
    public LocalTime getEffectiveTimeStart() { return effectiveTimeStart; }
    public void setEffectiveTimeStart(LocalTime effectiveTimeStart) { this.effectiveTimeStart = effectiveTimeStart; }
    public LocalTime getEffectiveTimeEnd() { return effectiveTimeEnd; }
    public void setEffectiveTimeEnd(LocalTime effectiveTimeEnd) { this.effectiveTimeEnd = effectiveTimeEnd; }
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
