package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 伙伴关系实体类
 * 
 * @author AI Assistant
 * @date 2025-09-20
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除

@TableName("TC_PARTNER_RELATIONSHIP")
public class TcPartnerRelationship implements Serializable {
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
     * 关联伙伴ID
     */
    @TableField("RELATED_PARTNER_ID")
    private String relatedPartnerId;

    /**
     * 关系类型
     */
    @TableField("RELATIONSHIP_TYPE")
    private String relationshipType;

    /**
     * 关系描述
     */
    @TableField("RELATIONSHIP_DESC")
    private String relationshipDesc;

    /**
     * 关系层级
     */
    @TableField("RELATIONSHIP_LEVEL")
    private String relationshipLevel;

    /**
     * 持股比例
     */
    @TableField("SHAREHOLDING_RATIO")
    private BigDecimal shareholdingRatio;

    /**
     * 业务合作类型
     */
    @TableField("BUSINESS_COOPERATION_TYPE")
    private String businessCooperationType;

    /**
     * 合作历史
     */
    @TableField("COOPERATION_HISTORY")
    private String cooperationHistory;

    /**
     * 合作规模
     */
    @TableField("COOPERATION_SCALE")
    private BigDecimal cooperationScale;

    /**
     * 风险关联度
     */
    @TableField("RISK_CORRELATION")
    private String riskCorrelation;

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

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPartnerId() { return partnerId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    public String getRelatedPartnerId() { return relatedPartnerId; }
    public void setRelatedPartnerId(String relatedPartnerId) { this.relatedPartnerId = relatedPartnerId; }
    public String getRelationshipType() { return relationshipType; }
    public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }
    public String getRelationshipDesc() { return relationshipDesc; }
    public void setRelationshipDesc(String relationshipDesc) { this.relationshipDesc = relationshipDesc; }
    public String getRelationshipLevel() { return relationshipLevel; }
    public void setRelationshipLevel(String relationshipLevel) { this.relationshipLevel = relationshipLevel; }
    public BigDecimal getShareholdingRatio() { return shareholdingRatio; }
    public void setShareholdingRatio(BigDecimal shareholdingRatio) { this.shareholdingRatio = shareholdingRatio; }
    public String getBusinessCooperationType() { return businessCooperationType; }
    public void setBusinessCooperationType(String businessCooperationType) { this.businessCooperationType = businessCooperationType; }
    public String getCooperationHistory() { return cooperationHistory; }
    public void setCooperationHistory(String cooperationHistory) { this.cooperationHistory = cooperationHistory; }
    public BigDecimal getCooperationScale() { return cooperationScale; }
    public void setCooperationScale(BigDecimal cooperationScale) { this.cooperationScale = cooperationScale; }
    public String getRiskCorrelation() { return riskCorrelation; }
    public void setRiskCorrelation(String riskCorrelation) { this.riskCorrelation = riskCorrelation; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }
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

}
