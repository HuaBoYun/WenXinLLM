package com.global.treasurer.financialProductDefinition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 产品交易事件实体类
 * 对应数据库表：TBL_PRODUCT_TRANSACTION_EVENT
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("REDACTED.TBL_PRODUCT_TRANSACTION_EVENT")
public class TblProductTransactionEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "EVENT_ID", type = IdType.INPUT)
    private Long eventId;

    @TableField("EVENT_CODE")
    private String eventCode;

    @TableField("EVENT_NAME")
    private String eventName;

    @TableField("EVENT_TYPE")
    private String eventType;

    @TableField("PRODUCT_TYPE")
    private String productType;

    @TableField("TRIGGER_CONDITION")
    private String triggerCondition;

    @TableField("ACCOUNTING_SUBJECT")
    private String accountingSubject;

    @TableField("EVENT_ACTION")
    private String eventAction;

    @TableField("IMPACT_DIRECTION")
    private String impactDirection;

    @TableField("EVENT_STATUS")
    private String eventStatus;

    @TableField("PRIORITY")
    private Integer priority;

    @TableField("NOTIFICATION_REQUIRED")
    private Integer notificationRequired;

    @TableField("APPROVAL_REQUIRED")
    private Integer approvalRequired;

    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("ORG_ID")
    private Long orgId;

    /** 交易类型名称（非数据库字段） */
    @TableField(exist = false)
    private String transactionTypeName;


    // 以下方法由Lombok生成,手动添加以解决编译问题

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventCode() { return eventCode; }
    public void setEventCode(String eventCode) { this.eventCode = eventCode; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getTriggerCondition() { return triggerCondition; }
    public void setTriggerCondition(String triggerCondition) { this.triggerCondition = triggerCondition; }

    public String getAccountingSubject() { return accountingSubject; }
    public void setAccountingSubject(String accountingSubject) { this.accountingSubject = accountingSubject; }

    public String getEventAction() { return eventAction; }
    public void setEventAction(String eventAction) { this.eventAction = eventAction; }

    public String getImpactDirection() { return impactDirection; }
    public void setImpactDirection(String impactDirection) { this.impactDirection = impactDirection; }

    public String getEventStatus() { return eventStatus; }
    public void setEventStatus(String eventStatus) { this.eventStatus = eventStatus; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Integer getNotificationRequired() { return notificationRequired; }
    public void setNotificationRequired(Integer notificationRequired) { this.notificationRequired = notificationRequired; }

    public Integer getApprovalRequired() { return approvalRequired; }
    public void setApprovalRequired(Integer approvalRequired) { this.approvalRequired = approvalRequired; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }

    public String getTransactionTypeName() { return transactionTypeName; }
    public void setTransactionTypeName(String transactionTypeName) { this.transactionTypeName = transactionTypeName; }
}
