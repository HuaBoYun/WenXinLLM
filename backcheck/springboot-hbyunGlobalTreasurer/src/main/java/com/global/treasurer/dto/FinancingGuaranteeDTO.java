package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 融资担保管理DTO
 *
 * @author HuaBo Cloud
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@ApiModel(description = "融资担保管理DTO")
public class FinancingGuaranteeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "担保ID")
    private Long guaranteeId;

    @ApiModelProperty(value = "担保合同编号")
    private String contractNo;

    @ApiModelProperty(value = "担保方式（1-保证 2-抵押 3-质押 4-留置 5-定金）")
    private Integer guaranteeType;

    @ApiModelProperty(value = "担保方式名称")
    private String guaranteeTypeName;

    @ApiModelProperty(value = "担保人")
    private String guarantor;

    @ApiModelProperty(value = "担保人组织ID")
    private Long guarantorOrgId;

    @ApiModelProperty(value = "担保人组织名称")
    private String guarantorOrgName;

    @ApiModelProperty(value = "被担保人/借款人")
    private String borrower;

    @ApiModelProperty(value = "被担保人组织ID")
    private Long borrowerOrgId;

    @ApiModelProperty(value = "被担保人组织名称")
    private String borrowerOrgName;

    @ApiModelProperty(value = "担保金额")
    private BigDecimal guaranteeAmount;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "担保开始日期")
    private Date startDate;

    @ApiModelProperty(value = "担保结束日期")
    private Date endDate;

    @ApiModelProperty(value = "担保期限（月）")
    private Integer guaranteePeriod;

    @ApiModelProperty(value = "担保用途")
    private String guaranteePurpose;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "附件ID")
    private Long attachmentId;

    @ApiModelProperty(value = "状态（0-草稿 1-生效中 2-已解除 3-已失效）")
    private Integer status;

    @ApiModelProperty(value = "状态名称")
    private String statusName;

    @ApiModelProperty(value = "审批状态（0-待审批 1-已审批 2-审批拒绝）")
    private Integer approvalStatus;

    @ApiModelProperty(value = "审批状态名称")
    private String approvalStatusName;

    @ApiModelProperty(value = "审批人")
    private String approvalBy;

    @ApiModelProperty(value = "审批时间")
    private Date approvalTime;

    @ApiModelProperty(value = "审批意见")
    private String approvalOpinion;

    @ApiModelProperty(value = "解除日期")
    private Date releaseDate;

    @ApiModelProperty(value = "解除原因")
    private String releaseReason;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "组织ID")
    private Long orgId;

    @ApiModelProperty(value = "员工ID")
    private Long staffId;

    @ApiModelProperty(value = "数据来源")
    private String datasource;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getGuaranteeId() { return guaranteeId; }
    public void setGuaranteeId(Long guaranteeId) { this.guaranteeId = guaranteeId; }
    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public Integer getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(Integer guaranteeType) { this.guaranteeType = guaranteeType; }
    public String getGuaranteeTypeName() { return guaranteeTypeName; }
    public void setGuaranteeTypeName(String guaranteeTypeName) { this.guaranteeTypeName = guaranteeTypeName; }
    public String getGuarantor() { return guarantor; }
    public void setGuarantor(String guarantor) { this.guarantor = guarantor; }
    public Long getGuarantorOrgId() { return guarantorOrgId; }
    public void setGuarantorOrgId(Long guarantorOrgId) { this.guarantorOrgId = guarantorOrgId; }
    public String getGuarantorOrgName() { return guarantorOrgName; }
    public void setGuarantorOrgName(String guarantorOrgName) { this.guarantorOrgName = guarantorOrgName; }
    public String getBorrower() { return borrower; }
    public void setBorrower(String borrower) { this.borrower = borrower; }
    public Long getBorrowerOrgId() { return borrowerOrgId; }
    public void setBorrowerOrgId(Long borrowerOrgId) { this.borrowerOrgId = borrowerOrgId; }
    public String getBorrowerOrgName() { return borrowerOrgName; }
    public void setBorrowerOrgName(String borrowerOrgName) { this.borrowerOrgName = borrowerOrgName; }
    public BigDecimal getGuaranteeAmount() { return guaranteeAmount; }
    public void setGuaranteeAmount(BigDecimal guaranteeAmount) { this.guaranteeAmount = guaranteeAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public Integer getGuaranteePeriod() { return guaranteePeriod; }
    public void setGuaranteePeriod(Integer guaranteePeriod) { this.guaranteePeriod = guaranteePeriod; }
    public String getGuaranteePurpose() { return guaranteePurpose; }
    public void setGuaranteePurpose(String guaranteePurpose) { this.guaranteePurpose = guaranteePurpose; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Long getAttachmentId() { return attachmentId; }
    public void setAttachmentId(Long attachmentId) { this.attachmentId = attachmentId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
    public Integer getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(Integer approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getApprovalStatusName() { return approvalStatusName; }
    public void setApprovalStatusName(String approvalStatusName) { this.approvalStatusName = approvalStatusName; }
    public String getApprovalBy() { return approvalBy; }
    public void setApprovalBy(String approvalBy) { this.approvalBy = approvalBy; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public String getApprovalOpinion() { return approvalOpinion; }
    public void setApprovalOpinion(String approvalOpinion) { this.approvalOpinion = approvalOpinion; }
    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }
    public String getReleaseReason() { return releaseReason; }
    public void setReleaseReason(String releaseReason) { this.releaseReason = releaseReason; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getDatasource() { return datasource; }
    public void setDatasource(String datasource) { this.datasource = datasource; }

}
