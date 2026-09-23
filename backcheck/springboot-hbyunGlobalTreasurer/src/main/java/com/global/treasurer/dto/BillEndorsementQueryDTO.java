package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据背书查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillEndorsementQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 背书编号 */
    private String endorsementNumber;

    /** 票据编号 */
    private String billNumber;

    /** 背书类型 */
    private String endorsementType;

    /** 背书人名称 */
    private String endorserName;

    /** 被背书人名称 */
    private String endorseeName;

    /** 背书状态 */
    private String endorsementStatus;

    /** 申请日期开始 */
    private Date applicationDateStart;

    /** 申请日期结束 */
    private Date applicationDateEnd;

    /** 金额下限 */
    private BigDecimal amountMin;

    /** 金额上限 */
    private BigDecimal amountMax;

    /** 公司ID */
    private Long companyId;

    /** 部门ID */
    private Long deptId;

    /** 当前页码 */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 10;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getEndorsementNumber() { return endorsementNumber; }
    public void setEndorsementNumber(String endorsementNumber) { this.endorsementNumber = endorsementNumber; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getEndorsementType() { return endorsementType; }
    public void setEndorsementType(String endorsementType) { this.endorsementType = endorsementType; }
    public String getEndorserName() { return endorserName; }
    public void setEndorserName(String endorserName) { this.endorserName = endorserName; }
    public String getEndorseeName() { return endorseeName; }
    public void setEndorseeName(String endorseeName) { this.endorseeName = endorseeName; }
    public String getEndorsementStatus() { return endorsementStatus; }
    public void setEndorsementStatus(String endorsementStatus) { this.endorsementStatus = endorsementStatus; }
    public Date getApplicationDateStart() { return applicationDateStart; }
    public void setApplicationDateStart(Date applicationDateStart) { this.applicationDateStart = applicationDateStart; }
    public Date getApplicationDateEnd() { return applicationDateEnd; }
    public void setApplicationDateEnd(Date applicationDateEnd) { this.applicationDateEnd = applicationDateEnd; }
    public BigDecimal getAmountMin() { return amountMin; }
    public void setAmountMin(BigDecimal amountMin) { this.amountMin = amountMin; }
    public BigDecimal getAmountMax() { return amountMax; }
    public void setAmountMax(BigDecimal amountMax) { this.amountMax = amountMax; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }


    public Integer getPageNum() { return pageNum != null ? pageNum : 1; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize != null ? pageSize : 10; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
