package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 票据贴现查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BillDiscountQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 贴现编号 */
    private String discountNumber;

    /** 票据编号 */
    private String billNumber;

    /** 贴现银行 */
    private String discountBank;

    /** 贴现状态 */
    private String discountStatus;

    /** 贴现类型 */
    private String discountType;

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


    public String getDiscountNumber() { return discountNumber; }
    public void setDiscountNumber(String discountNumber) { this.discountNumber = discountNumber; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public String getDiscountBank() { return discountBank; }
    public void setDiscountBank(String discountBank) { this.discountBank = discountBank; }
    public String getDiscountStatus() { return discountStatus; }
    public void setDiscountStatus(String discountStatus) { this.discountStatus = discountStatus; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
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
