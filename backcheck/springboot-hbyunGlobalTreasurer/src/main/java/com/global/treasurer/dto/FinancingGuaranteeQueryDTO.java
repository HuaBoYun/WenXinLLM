package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 融资担保查询条件DTO
 *
 * @author HuaBo Cloud
 * @since 2025-01-13
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@ApiModel(description = "融资担保查询条件DTO")
public class FinancingGuaranteeQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "担保合同编号")
    private String contractNo;

    @ApiModelProperty(value = "担保方式（1-保证 2-抵押 3-质押 4-留置 5-定金）")
    private Integer guaranteeType;

    @ApiModelProperty(value = "担保人")
    private String guarantor;

    @ApiModelProperty(value = "被担保人/借款人")
    private String borrower;

    @ApiModelProperty(value = "担保开始日期-起")
    private Date startDateBegin;

    @ApiModelProperty(value = "担保开始日期-止")
    private Date startDateEnd;

    @ApiModelProperty(value = "担保结束日期-起")
    private Date endDateBegin;

    @ApiModelProperty(value = "担保结束日期-止")
    private Date endDateEnd;

    @ApiModelProperty(value = "状态（0-草稿 1-生效中 2-已解除 3-已失效）")
    private Integer status;

    @ApiModelProperty(value = "审批状态（0-待审批 1-已审批 2-审批拒绝）")
    private Integer approvalStatus;

    @ApiModelProperty(value = "组织ID")
    private Long orgId;

    @ApiModelProperty(value = "页码")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = 10;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getContractNo() { return contractNo; }
    public void setContractNo(String contractNo) { this.contractNo = contractNo; }
    public Integer getGuaranteeType() { return guaranteeType; }
    public void setGuaranteeType(Integer guaranteeType) { this.guaranteeType = guaranteeType; }
    public String getGuarantor() { return guarantor; }
    public void setGuarantor(String guarantor) { this.guarantor = guarantor; }
    public String getBorrower() { return borrower; }
    public void setBorrower(String borrower) { this.borrower = borrower; }
    public Date getStartDateBegin() { return startDateBegin; }
    public void setStartDateBegin(Date startDateBegin) { this.startDateBegin = startDateBegin; }
    public Date getStartDateEnd() { return startDateEnd; }
    public void setStartDateEnd(Date startDateEnd) { this.startDateEnd = startDateEnd; }
    public Date getEndDateBegin() { return endDateBegin; }
    public void setEndDateBegin(Date endDateBegin) { this.endDateBegin = endDateBegin; }
    public Date getEndDateEnd() { return endDateEnd; }
    public void setEndDateEnd(Date endDateEnd) { this.endDateEnd = endDateEnd; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(Integer approvalStatus) { this.approvalStatus = approvalStatus; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
}
