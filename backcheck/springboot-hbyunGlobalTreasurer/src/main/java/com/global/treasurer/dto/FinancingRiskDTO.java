package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 融资风险监控DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingRiskDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 监控ID */
    private Long monitoringId;

    /** 预警类型 */
    @NotBlank(message = "预警类型不能为空")
    private String alertType;

    /** 预警级别(HIGH-高,MEDIUM-中,LOW-低) */
    @NotBlank(message = "预警级别不能为空")
    private String alertLevel;

    /** 预警内容 */
    @NotBlank(message = "预警内容不能为空")
    private String alertMessage;

    /** 关联融资ID */
    @NotNull(message = "关联融资ID不能为空")
    private Long relatedFinancingId;

    /** 预警状态(PENDING-未处理,HANDLED-已处理,CLOSED-已关闭) */
    private String alertStatus;

    /** 预警日期 */
    private Date alertDate;

    /** 处理人ID */
    private Long handlerId;

    /** 处理人姓名 */
    private String handlerName;

    /** 处理日期 */
    private Date handleDate;

    /** 处理意见 */
    private String handleOpinion;

    /** 公司ID */
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getMonitoringId() { return monitoringId; }
    public void setMonitoringId(Long monitoringId) { this.monitoringId = monitoringId; }
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
    public Long getRelatedFinancingId() { return relatedFinancingId; }
    public void setRelatedFinancingId(Long relatedFinancingId) { this.relatedFinancingId = relatedFinancingId; }
    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }
    public Date getAlertDate() { return alertDate; }
    public void setAlertDate(Date alertDate) { this.alertDate = alertDate; }
    public Long getHandlerId() { return handlerId; }
    public void setHandlerId(Long handlerId) { this.handlerId = handlerId; }
    public String getHandlerName() { return handlerName; }
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }
    public Date getHandleDate() { return handleDate; }
    public void setHandleDate(Date handleDate) { this.handleDate = handleDate; }
    public String getHandleOpinion() { return handleOpinion; }
    public void setHandleOpinion(String handleOpinion) { this.handleOpinion = handleOpinion; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
