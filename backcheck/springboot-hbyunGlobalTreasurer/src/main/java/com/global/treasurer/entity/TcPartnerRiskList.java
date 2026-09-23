package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 伙伴黑白灰名单实体类
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除

@TableName("TC_PARTNER_RISK_LIST")
public class TcPartnerRiskList implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private String id;

    /**
     * 伙伴ID
     */
    @TableField("PARTNER_ID")
    private String partnerId;

    /**
     * 名单类型(WHITE-白名单,BLACK-黑名单,GRAY-灰名单)
     */
    @TableField("LIST_TYPE")
    private String listType;

    /**
     * 名单原因
     */
    @TableField("LIST_REASON")
    private String listReason;

    /**
     * 风险等级
     */
    @TableField("RISK_LEVEL")
    private String riskLevel;

    /**
     * 业务范围
     */
    @TableField("BUSINESS_SCOPE")
    private String businessScope;

    /**
     * 限制详情
     */
    @TableField("RESTRICTION_DETAILS")
    private String restrictionDetails;

    /**
     * 证据材料
     */
    @TableField("EVIDENCE_MATERIALS")
    private String evidenceMaterials;

    /**
     * 审查周期(天)
     */
    @TableField("REVIEW_PERIOD")
    private Integer reviewPeriod;

    /**
     * 下次审查日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("NEXT_REVIEW_DATE")
    private Date nextReviewDate;

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    /**
     * 审批人
     */
    @TableField("APPROVAL_USER")
    private String approvalUser;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("APPROVAL_TIME")
    private Date approvalTime;

    /**
     * 移除原因
     */
    @TableField("REMOVAL_REASON")
    private String removalReason;

    /**
     * 移除人
     */
    @TableField("REMOVAL_USER")
    private String removalUser;

    /**
     * 移除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("REMOVAL_TIME")
    private Date removalTime;

    /**
     * 状态(1-启用,0-停用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private Integer versionNo;

    // ========== 以下字段非数据库字段,用于关联查询显示 ==========

    /**
     * 伙伴编码(关联查询TC_PARTNER_ARCHIVE)
     */
    @TableField(exist = false)
    private String partnerCode;

    /**
     * 伙伴名称(关联查询TC_PARTNER_ARCHIVE)
     */
    @TableField(exist = false)
    private String partnerName;

    /**
     * 伙伴类型(关联查询TC_PARTNER_ARCHIVE)
     */
    @TableField(exist = false)
    private String partnerType;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPartnerId() { return partnerId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    public String getListType() { return listType; }
    public void setListType(String listType) { this.listType = listType; }
    public String getListReason() { return listReason; }
    public void setListReason(String listReason) { this.listReason = listReason; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getBusinessScope() { return businessScope; }
    public void setBusinessScope(String businessScope) { this.businessScope = businessScope; }
    public String getRestrictionDetails() { return restrictionDetails; }
    public void setRestrictionDetails(String restrictionDetails) { this.restrictionDetails = restrictionDetails; }
    public String getEvidenceMaterials() { return evidenceMaterials; }
    public void setEvidenceMaterials(String evidenceMaterials) { this.evidenceMaterials = evidenceMaterials; }
    public Integer getReviewPeriod() { return reviewPeriod; }
    public void setReviewPeriod(Integer reviewPeriod) { this.reviewPeriod = reviewPeriod; }
    public Date getNextReviewDate() { return nextReviewDate; }
    public void setNextReviewDate(Date nextReviewDate) { this.nextReviewDate = nextReviewDate; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }
    public String getApprovalUser() { return approvalUser; }
    public void setApprovalUser(String approvalUser) { this.approvalUser = approvalUser; }
    public Date getApprovalTime() { return approvalTime; }
    public void setApprovalTime(Date approvalTime) { this.approvalTime = approvalTime; }
    public String getRemovalReason() { return removalReason; }
    public void setRemovalReason(String removalReason) { this.removalReason = removalReason; }
    public String getRemovalUser() { return removalUser; }
    public void setRemovalUser(String removalUser) { this.removalUser = removalUser; }
    public Date getRemovalTime() { return removalTime; }
    public void setRemovalTime(Date removalTime) { this.removalTime = removalTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }
    public String getPartnerCode() { return partnerCode; }
    public void setPartnerCode(String partnerCode) { this.partnerCode = partnerCode; }
    public String getPartnerName() { return partnerName; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    public String getPartnerType() { return partnerType; }
    public void setPartnerType(String partnerType) { this.partnerType = partnerType; }
}
