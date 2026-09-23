package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 信用证查询DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class LetterOfCreditQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 信用证编号 */
    private String lcNumber;

    /** 信用证类型 */
    private String lcType;

    /** 信用证状态 */
    private String lcStatus;

    /** 申请人 */
    private String applicant;

    /** 受益人 */
    private String beneficiary;

    /** 开证银行 */
    private String issuingBank;

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


    public String getLcNumber() { return lcNumber; }
    public void setLcNumber(String lcNumber) { this.lcNumber = lcNumber; }
    public String getLcType() { return lcType; }
    public void setLcType(String lcType) { this.lcType = lcType; }
    public String getLcStatus() { return lcStatus; }
    public void setLcStatus(String lcStatus) { this.lcStatus = lcStatus; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getBeneficiary() { return beneficiary; }
    public void setBeneficiary(String beneficiary) { this.beneficiary = beneficiary; }
    public String getIssuingBank() { return issuingBank; }
    public void setIssuingBank(String issuingBank) { this.issuingBank = issuingBank; }
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
