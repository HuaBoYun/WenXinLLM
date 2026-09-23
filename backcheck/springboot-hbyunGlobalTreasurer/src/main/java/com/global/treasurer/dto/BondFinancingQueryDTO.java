package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 债券融资查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BondFinancingQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 发行ID */
    private Long issuanceId;

    /** 融资计划ID */
    private Long planId;

    /** 债券名称（模糊查询） */
    private String bondName;

    /** 债券代码 */
    private String bondCode;

    /** 债券类型 */
    private String bondType;

    /** 信用评级 */
    private String creditRating;

    /** 发行状态 */
    private String issuanceStatus;

    /** 公司ID */
    private Long companyId;

    /** 发行日期开始 */
    private Date issuanceDateStart;

    /** 发行日期结束 */
    private Date issuanceDateEnd;

    /** 到期日期开始 */
    private Date maturityDateStart;

    /** 到期日期结束 */
    private Date maturityDateEnd;

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 10;

    /** 排序字段 */
    private String orderBy;

    /** 排序方式 ASC/DESC */
    private String orderDirection = "DESC";

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getIssuanceId() { return issuanceId; }
    public void setIssuanceId(Long issuanceId) { this.issuanceId = issuanceId; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getBondName() { return bondName; }
    public void setBondName(String bondName) { this.bondName = bondName; }
    public String getBondCode() { return bondCode; }
    public void setBondCode(String bondCode) { this.bondCode = bondCode; }
    public String getBondType() { return bondType; }
    public void setBondType(String bondType) { this.bondType = bondType; }
    public String getCreditRating() { return creditRating; }
    public void setCreditRating(String creditRating) { this.creditRating = creditRating; }
    public String getIssuanceStatus() { return issuanceStatus; }
    public void setIssuanceStatus(String issuanceStatus) { this.issuanceStatus = issuanceStatus; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Date getIssuanceDateStart() { return issuanceDateStart; }
    public void setIssuanceDateStart(Date issuanceDateStart) { this.issuanceDateStart = issuanceDateStart; }
    public Date getIssuanceDateEnd() { return issuanceDateEnd; }
    public void setIssuanceDateEnd(Date issuanceDateEnd) { this.issuanceDateEnd = issuanceDateEnd; }
    public Date getMaturityDateStart() { return maturityDateStart; }
    public void setMaturityDateStart(Date maturityDateStart) { this.maturityDateStart = maturityDateStart; }
    public Date getMaturityDateEnd() { return maturityDateEnd; }
    public void setMaturityDateEnd(Date maturityDateEnd) { this.maturityDateEnd = maturityDateEnd; }
    public String getOrderBy() { return orderBy; }
    public void setOrderBy(String orderBy) { this.orderBy = orderBy; }

}
