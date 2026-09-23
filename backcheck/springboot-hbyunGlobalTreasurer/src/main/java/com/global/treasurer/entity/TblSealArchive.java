package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 印鉴档案管理实体类
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_SEAL_ARCHIVE_MANAGE")
public class TblSealArchive implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 印鉴ID
     */
    @TableId(value = "SEAL_ID", type = IdType.INPUT)
    private Long sealId;

    /**
     * 印鉴编码
     */
    @TableField("SEAL_CODE")
    private String sealCode;

    /**
     * 印鉴名称
     */
    @TableField("SEAL_NAME")
    private String sealName;

    /**
     * 印鉴类型ID
     */
    @TableField("SEAL_TYPE_ID")
    private Long sealTypeId;

    /**
     * 印鉴类型名称
     */
    @TableField("SEAL_TYPE_NAME")
    private String sealTypeName;

    /**
     * 持有人姓名
     */
    @TableField("OWNER_NAME")
    private String ownerName;

    /**
     * 持有人职位
     */
    @TableField("OWNER_POSITION")
    private String ownerPosition;

    /**
     * 持有人身份证号
     */
    @TableField("OWNER_ID_CARD")
    private String ownerIdCard;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRE_DATE")
    private LocalDate expireDate;

    /**
     * 印鉴图片URL
     */
    @TableField("SEAL_IMAGE_URL")
    private String sealImageUrl;

    /**
     * 是否启用:1启用,0禁用
     */
    @TableField("IS_ACTIVE")
    private Integer isActive;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
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


    public Long getSealId() { return sealId; }
    public void setSealId(Long sealId) { this.sealId = sealId; }
    public String getSealCode() { return sealCode; }
    public void setSealCode(String sealCode) { this.sealCode = sealCode; }
    public String getSealName() { return sealName; }
    public void setSealName(String sealName) { this.sealName = sealName; }
    public Long getSealTypeId() { return sealTypeId; }
    public void setSealTypeId(Long sealTypeId) { this.sealTypeId = sealTypeId; }
    public String getSealTypeName() { return sealTypeName; }
    public void setSealTypeName(String sealTypeName) { this.sealTypeName = sealTypeName; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerPosition() { return ownerPosition; }
    public void setOwnerPosition(String ownerPosition) { this.ownerPosition = ownerPosition; }
    public String getOwnerIdCard() { return ownerIdCard; }
    public void setOwnerIdCard(String ownerIdCard) { this.ownerIdCard = ownerIdCard; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public LocalDate getExpireDate() { return expireDate; }
    public void setExpireDate(LocalDate expireDate) { this.expireDate = expireDate; }
    public String getSealImageUrl() { return sealImageUrl; }
    public void setSealImageUrl(String sealImageUrl) { this.sealImageUrl = sealImageUrl; }
    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
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
