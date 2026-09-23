package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 监管通知实体类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_REGULATORY_NOTIFICATION")
public class TblRegulatoryNotification implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 通知ID */
    @TableId(type = IdType.ASSIGN_UUID)
    private String notificationId;

    /** 通知代码 */
    private String notificationCode;

    /** 通知标题 */
    private String notificationTitle;

    /** 通知类型 */
    private String notificationType;

    /** 监管机构ID */
    private String authorityId;

    /** 通知内容 */
    private String notificationContent;

    /** 优先级(HIGH/NORMAL/LOW) */
    private String priorityLevel;

    /** 发送状态(DRAFT/SENT/ACKNOWLEDGED) */
    private String sendStatus;

    /** 发送时间 */
    private Date sendTime;

    /** 确认时间 */
    private Date acknowledgeTime;

    /** 接收人 */
    private String recipients;

    /** 公司ID */
    private String companyId;

    /** 删除标志 */
    private Integer deleteFlag;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }
    public String getNotificationCode() { return notificationCode; }
    public void setNotificationCode(String notificationCode) { this.notificationCode = notificationCode; }
    public String getNotificationTitle() { return notificationTitle; }
    public void setNotificationTitle(String notificationTitle) { this.notificationTitle = notificationTitle; }
    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    public String getAuthorityId() { return authorityId; }
    public void setAuthorityId(String authorityId) { this.authorityId = authorityId; }
    public String getNotificationContent() { return notificationContent; }
    public void setNotificationContent(String notificationContent) { this.notificationContent = notificationContent; }
    public String getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }
    public String getSendStatus() { return sendStatus; }
    public void setSendStatus(String sendStatus) { this.sendStatus = sendStatus; }
    public Date getSendTime() { return sendTime; }
    public void setSendTime(Date sendTime) { this.sendTime = sendTime; }
    public Date getAcknowledgeTime() { return acknowledgeTime; }
    public void setAcknowledgeTime(Date acknowledgeTime) { this.acknowledgeTime = acknowledgeTime; }
    public String getRecipients() { return recipients; }
    public void setRecipients(String recipients) { this.recipients = recipients; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
