package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 融资风险监控查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
public class FinancingRiskQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页数量 */
    private Integer pageSize = 10;

    /** 预警类型 */
    private String alertType;

    /** 预警级别 */
    private String alertLevel;

    /** 预警状态 */
    private String alertStatus;

    /** 公司ID */
    private Long companyId;

    /** 关联融资ID */
    private Long relatedFinancingId;

    /** 预警开始日期 */
    private Date alertStartDate;

    /** 预警结束日期 */
    private Date alertEndDate;

    /** 处理人ID */
    private Long handlerId;

    /** 关键字搜索(预警内容) */
    private String keyword;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getRelatedFinancingId() { return relatedFinancingId; }
    public void setRelatedFinancingId(Long relatedFinancingId) { this.relatedFinancingId = relatedFinancingId; }
    public Date getAlertStartDate() { return alertStartDate; }
    public void setAlertStartDate(Date alertStartDate) { this.alertStartDate = alertStartDate; }
    public Date getAlertEndDate() { return alertEndDate; }
    public void setAlertEndDate(Date alertEndDate) { this.alertEndDate = alertEndDate; }
    public Long getHandlerId() { return handlerId; }
    public void setHandlerId(Long handlerId) { this.handlerId = handlerId; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }


    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
