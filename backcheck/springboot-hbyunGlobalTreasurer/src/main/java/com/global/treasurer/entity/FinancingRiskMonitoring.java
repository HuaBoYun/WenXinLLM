package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资风险监控实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCING_RISK_MONITORING")
public class FinancingRiskMonitoring implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 监控ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 监控编号 */
    private String monitoringNo;

    /** 风险类型 */
    private String riskType;

    /** 风险等级(HIGH-高,MEDIUM-中,LOW-低) */
    private String riskLevel;

    /** 风险描述 */
    private String riskDescription;

    /** 关联融资ID */
    private Long relatedFinancingId;

    /** 融资类型 */
    private String financingType;

    /** 融资金额 */
    private BigDecimal financingAmount;

    /** 风险指标值 */
    private BigDecimal riskIndicatorValue;

    /** 阈值 */
    private BigDecimal threshold;

    /** 监控状态(ACTIVE-活跃,HANDLED-已处理,CLOSED-已关闭) */
    private String status;

    /** 监控日期 */
    private Date monitoringDate;

    /** 预警时间 */
    private Date alertTime;

    /** 处理人ID */
    private Long handlerId;

    /** 处理人姓名 */
    private String handlerName;

    /** 处理日期 */
    private Date handleDate;

    /** 采取措施 */
    private String actionTaken;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 删除标志(0-正常,1-删除) */
    private Integer deleteFlag;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人姓名 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMonitoringNo() { return monitoringNo; }
    public void setMonitoringNo(String monitoringNo) { this.monitoringNo = monitoringNo; }
    public String getRiskType() { return riskType; }
    public void setRiskType(String riskType) { this.riskType = riskType; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getRiskDescription() { return riskDescription; }
    public void setRiskDescription(String riskDescription) { this.riskDescription = riskDescription; }
    public Long getRelatedFinancingId() { return relatedFinancingId; }
    public void setRelatedFinancingId(Long relatedFinancingId) { this.relatedFinancingId = relatedFinancingId; }
    public String getFinancingType() { return financingType; }
    public void setFinancingType(String financingType) { this.financingType = financingType; }
    public BigDecimal getFinancingAmount() { return financingAmount; }
    public void setFinancingAmount(BigDecimal financingAmount) { this.financingAmount = financingAmount; }
    public BigDecimal getRiskIndicatorValue() { return riskIndicatorValue; }
    public void setRiskIndicatorValue(BigDecimal riskIndicatorValue) { this.riskIndicatorValue = riskIndicatorValue; }
    public BigDecimal getThreshold() { return threshold; }
    public void setThreshold(BigDecimal threshold) { this.threshold = threshold; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getMonitoringDate() { return monitoringDate; }
    public void setMonitoringDate(Date monitoringDate) { this.monitoringDate = monitoringDate; }
    public Date getAlertTime() { return alertTime; }
    public void setAlertTime(Date alertTime) { this.alertTime = alertTime; }
    public Long getHandlerId() { return handlerId; }
    public void setHandlerId(Long handlerId) { this.handlerId = handlerId; }
    public String getHandlerName() { return handlerName; }
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }
    public Date getHandleDate() { return handleDate; }
    public void setHandleDate(Date handleDate) { this.handleDate = handleDate; }
    public String getActionTaken() { return actionTaken; }
    public void setActionTaken(String actionTaken) { this.actionTaken = actionTaken; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
    public String getUpdatedByName() { return updatedByName; }
    public void setUpdatedByName(String updatedByName) { this.updatedByName = updatedByName; }
    public Date getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(Date updatedTime) { this.updatedTime = updatedTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

}
