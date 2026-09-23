package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算提醒实体类
 *
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_REMINDER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetReminder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 提醒ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String reminderId;

    /**
     * 提醒名称
     */
    private String reminderName;

    /**
     * 提醒编码
     */
    private String reminderCode;

    /**
     * 提醒类型: THRESHOLD-阈值提醒, DEADLINE-截止日期提醒, VARIANCE-差异提醒
     */
    private String reminderType;

    /**
     * 关联预算ID
     */
    private String budgetId;

    /**
     * 提醒规则配置(JSON格式)
     */
    private String reminderRule;

    /**
     * 提醒阈值
     */
    private Double thresholdValue;

    /**
     * 提醒消息模板
     */
    private String messageTemplate;

    /**
     * 提醒方式: EMAIL-邮件, SMS-短信, SYSTEM-系统通知
     */
    private String reminderMethod;

    /**
     * 提醒频率: REALTIME-实时, DAILY-每日, WEEKLY-每周, MONTHLY-每月
     */
    private String reminderFrequency;

    /**
     * 提醒对象
     */
    private String targetUsers;

    /**
     * 是否启用: 0-禁用, 1-启用
     */
    private Integer isEnabled;

    /**
     * 提醒状态
     */
    private String reminderStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志: 0-正常, 1-已删除
     */
    private Integer delFlag;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getReminderCode() {
        return reminderCode;
    }

    public void setReminderCode(String reminderCode) {
        this.reminderCode = reminderCode;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getReminderRule() {
        return reminderRule;
    }

    public void setReminderRule(String reminderRule) {
        this.reminderRule = reminderRule;
    }

    public Double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getReminderMethod() {
        return reminderMethod;
    }

    public void setReminderMethod(String reminderMethod) {
        this.reminderMethod = reminderMethod;
    }

    public String getReminderFrequency() {
        return reminderFrequency;
    }

    public void setReminderFrequency(String reminderFrequency) {
        this.reminderFrequency = reminderFrequency;
    }

    public String getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(String targetUsers) {
        this.targetUsers = targetUsers;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getReminderStatus() {
        return reminderStatus;
    }

    public void setReminderStatus(String reminderStatus) {
        this.reminderStatus = reminderStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
