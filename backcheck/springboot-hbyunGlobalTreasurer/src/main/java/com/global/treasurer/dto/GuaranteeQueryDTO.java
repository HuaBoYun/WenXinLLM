package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 保函查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class GuaranteeQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 保函编号 */
    private String guaranteeNumber;

    /** 保函类型 */
    private String guaranteeType;

    /** 保函状态 */
    private String guaranteeStatus;

    /** 申请人 */
    private String applicant;

    /** 受益人 */
    private String beneficiary;

    /** 担保银行 */
    private String guaranteeBank;

    /** 公司ID */
    private String companyId;

    /** 开始日期 */
    private Date startDate;

    /** 结束日期 */
    private Date endDate;

    /** 页码 */
    private Integer pageNum;

    /** 每页数量 */
    private Integer pageSize;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getGuaranteeNumber() { return guaranteeNumber; }
    public void setGuaranteeNumber(String guaranteeNumber) { this.guaranteeNumber = guaranteeNumber; }
    public String getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(String guaranteeType) { this.guaranteeType = guaranteeType; }
    public String getGuaranteeStatus() { return guaranteeStatus; }
    public void setGuaranteeStatus(String guaranteeStatus) { this.guaranteeStatus = guaranteeStatus; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getBeneficiary() { return beneficiary; }
    public void setBeneficiary(String beneficiary) { this.beneficiary = beneficiary; }
    public String getGuaranteeBank() { return guaranteeBank; }
    public void setGuaranteeBank(String guaranteeBank) { this.guaranteeBank = guaranteeBank; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}
