package com.financial.sharing.mysql.entity;

import java.util.Date;

/**
 * 成本控制预警表实体类
 * @author AI Assistant
 * @date 2025-12-23
 */
public class CostControlAlertEntity {

    private String alertId;           // 预警ID
    private String controlId;         // 控制规则ID
    private String centerId;          // 成本中心ID
    private String alertType;         // 预警类型（WARNING:预警 OVER_BUDGET:超支）
    private String alertStatus;       // 处理状态（PENDING:待处理 HANDLED:已处理）
    private String alertMessage;      // 预警消息
    private String handleResult;      // 处理结果
    private String handlerId;         // 处理人ID
    private Date handleTime;          // 处理时间
    private Long bookId;              // 账簿ID
    private Long tenantId;            // 租户ID
    private Date createTime;          // 创建时间
    private String updater;           // 更新人
    private Date updateTime;          // 更新时间
    private Integer isDeleted;        // 删除标识（0:未删除 1:已删除）
    private Long version;             // 版本号（乐观锁）

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
