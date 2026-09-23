package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 融资监控DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@ApiModel(value = "FinancingMonitoringDTO", description = "融资监控传输对象")
@Data
public class FinancingMonitoringDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("监控ID")
    private Long monitoringId;

    @ApiModelProperty("预警类型")
    private String alertType;

    @ApiModelProperty("预警级别(HIGH-高,MEDIUM-中,LOW-低)")
    private String alertLevel;

    @ApiModelProperty("预警内容")
    private String alertMessage;

    @ApiModelProperty("关联融资ID")
    private Long relatedFinancingId;

    @ApiModelProperty("预警状态(PENDING-未处理,HANDLED-已处理,CLOSED-已关闭)")
    private String alertStatus;

    @ApiModelProperty("预警日期")
    private Date alertDate;

    @ApiModelProperty("处理人ID")
    private Long handlerId;

    @ApiModelProperty("处理人姓名")
    private String handlerName;

    @ApiModelProperty("处理日期")
    private Date handleDate;

    @ApiModelProperty("处理意见")
    private String handleOpinion;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("页码")
    private Integer page;

    @ApiModelProperty("每页大小")
    private Integer limit;

    @ApiModelProperty("天数")
    private Integer days;

    // Getter和Setter
    public Long getMonitoringId() {
        return monitoringId;
    }

    public void setMonitoringId(Long monitoringId) {
        this.monitoringId = monitoringId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public Long getRelatedFinancingId() {
        return relatedFinancingId;
    }

    public void setRelatedFinancingId(Long relatedFinancingId) {
        this.relatedFinancingId = relatedFinancingId;
    }

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public Long getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(Long handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public Date getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }

    public String getHandleOpinion() {
        return handleOpinion;
    }

    public void setHandleOpinion(String handleOpinion) {
        this.handleOpinion = handleOpinion;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
