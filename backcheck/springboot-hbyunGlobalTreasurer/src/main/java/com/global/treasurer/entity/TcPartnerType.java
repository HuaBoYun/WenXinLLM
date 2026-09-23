package com.global.treasurer.entity;

import lombok.Data;
import java.util.Date;

/**
 * 合作伙伴类型实体类
 * 表名：TBL_TREASURY_PARTNER_TYPE
 */
@Data
public class TcPartnerType {
    
    /** 合作伙伴类型 ID */
    private Long partnerTypeId;
    
    /** 类型编码 */
    private String typeCode;
    
    /** 类型名称 */
    private String typeName;
    
    /** 描述 */
    private String description;
    
    /** 父类型 ID */
    private Long parentTypeId;
    
    /** 层级 */
    private Integer level;
    
    /** 排序 */
    private Integer sortOrder;
    
    /** 是否启用 (0-禁用，1-启用) */
    private Integer isEnabled;
    
    /** 是否允许银企直连 */
    private Integer allowBankDirectConnection;
    
    /** 是否允许付款 */
    private Integer allowPayment;
    
    /** 是否允许收款 */
    private Integer allowReceipt;
    
    /** 是否允许融资 */
    private Integer allowFinancing;
    
    /** 是否允许投资 */
    private Integer allowInvestment;
    
    /** 风险等级限制 */
    private String riskLevelLimit;
    
    /** 信用评级限制 */
    private String creditRatingLimit;
    
    /** 所需文档 */
    private String requiredDocuments;
    
    /** 业务范围 */
    private String businessScope;
    
    /** 分类 */
    private String category;
    
    /** 备注 */
    private String remarks;
    
    /** 组织 ID */
    private Long orgId;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 创建人 */
    private Long createUser;
    
    /** 更新时间 */
    private Date updateTime;

    /** 更新人 */
    private Long updateUser;

    /** 是否全局启用 */
    private Integer isEnabledGlobal;

    // Getter and Setter methods for Lombok compatibility
    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }
    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
