package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 融资监控查询DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@ApiModel(value = "FinancingMonitoringQueryDTO", description = "融资监控查询对象")
@Data
public class FinancingMonitoringQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("预警类型")
    private String alertType;

    @ApiModelProperty("预警级别")
    private String alertLevel;

    @ApiModelProperty("预警状态")
    private String alertStatus;

    @ApiModelProperty("关联融资ID")
    private Long relatedFinancingId;

    @ApiModelProperty("公司ID")
    private Long companyId;

    @ApiModelProperty("开始日期")
    private Date startDate;

    @ApiModelProperty("结束日期")
    private Date endDate;

    @ApiModelProperty("关键词(预警内容、公司名称等)")
    private String keyword;

    // Getter和Setter
    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public Long getRelatedFinancingId() {
        return relatedFinancingId;
    }

    public void setRelatedFinancingId(Long relatedFinancingId) {
        this.relatedFinancingId = relatedFinancingId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
