package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.math.BigDecimal;
import java.util.Date;

/**
 * 印章档案管理实体类
 *
 * @author system
 * @date 2024-12-09
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_SEAL_ARCHIVE_MANAGE")
public class TblSealArchiveManage {
    @TableId(type = IdType.AUTO)
    private Long sealId;

    /**
     * 印鉴编码
     */
    private String sealCode;

    /**
     * 印鉴名称
     */
    private String sealName;

    /**
     * 印鉴类型ID
     */
    private Long sealTypeId;

    /**
     * 持有人姓名
     */
    private String ownerName;

    /**
     * 持有人职位
     */
    private String ownerPosition;

    /**
     * 持有人身份证号
     */
    private String ownerIdCard;

    /**
     * 生效日期
     */
    private Date effectiveDate;

    /**
     * 失效日期
     */
    private Date expireDate;

    /**
     * 印鉴图片URL
     */
    private String sealImageUrl;

    /**
     * 印鉴图片存储路径
     */
    private String sealImagePath;

    /**
     * 是否启用(1-启用,0-停用)
     */
    private Integer isActive;

    /**
     * 安全级别(1-低,2-中,3-高)
     */
    private Integer securityLevel;

    /**
     * 登记号码
     */
    private String registrationNumber;

    /**
     * 发证机关
     */
    private String certificateAuthority;

    /**
     * 发证日期
     */
    private Date certificateDate;

    /**
     * 使用范围说明
     */
    private String usageScope;

    /**
     * 存放位置
     */
    private String storageLocation;

    /**
     * 责任人
     */
    private String responsiblePerson;

    /**
     * 上次检查日期
     */
    private Date lastInspectionDate;

    /**
     * 下次检查日期
     */
    private Date nextInspectionDate;

    /**
     * 检查间隔(天)
     */
    private Integer inspectionInterval;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新人
     */
    private Long updateUser;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getSealId() { return sealId; }
    public void setSealId(Long sealId) { this.sealId = sealId; }
    public String getSealCode() { return sealCode; }
    public void setSealCode(String sealCode) { this.sealCode = sealCode; }
    public String getSealName() { return sealName; }
    public void setSealName(String sealName) { this.sealName = sealName; }
    public Long getSealTypeId() { return sealTypeId; }
    public void setSealTypeId(Long sealTypeId) { this.sealTypeId = sealTypeId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerPosition() { return ownerPosition; }
    public void setOwnerPosition(String ownerPosition) { this.ownerPosition = ownerPosition; }
    public String getOwnerIdCard() { return ownerIdCard; }
    public void setOwnerIdCard(String ownerIdCard) { this.ownerIdCard = ownerIdCard; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }
    public String getSealImageUrl() { return sealImageUrl; }
    public void setSealImageUrl(String sealImageUrl) { this.sealImageUrl = sealImageUrl; }
    public String getSealImagePath() { return sealImagePath; }
    public void setSealImagePath(String sealImagePath) { this.sealImagePath = sealImagePath; }
    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
    public Integer getSecurityLevel() { return securityLevel; }
    public void setSecurityLevel(Integer securityLevel) { this.securityLevel = securityLevel; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getCertificateAuthority() { return certificateAuthority; }
    public void setCertificateAuthority(String certificateAuthority) { this.certificateAuthority = certificateAuthority; }
    public Date getCertificateDate() { return certificateDate; }
    public void setCertificateDate(Date certificateDate) { this.certificateDate = certificateDate; }
    public String getUsageScope() { return usageScope; }
    public void setUsageScope(String usageScope) { this.usageScope = usageScope; }
    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
    public String getResponsiblePerson() { return responsiblePerson; }
    public void setResponsiblePerson(String responsiblePerson) { this.responsiblePerson = responsiblePerson; }
    public Date getLastInspectionDate() { return lastInspectionDate; }
    public void setLastInspectionDate(Date lastInspectionDate) { this.lastInspectionDate = lastInspectionDate; }
    public Date getNextInspectionDate() { return nextInspectionDate; }
    public void setNextInspectionDate(Date nextInspectionDate) { this.nextInspectionDate = nextInspectionDate; }
    public Integer getInspectionInterval() { return inspectionInterval; }
    public void setInspectionInterval(Integer inspectionInterval) { this.inspectionInterval = inspectionInterval; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }

}
