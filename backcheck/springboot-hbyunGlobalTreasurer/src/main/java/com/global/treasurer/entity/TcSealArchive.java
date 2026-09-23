package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 财资公共模块 - 印鉴档案表
 * 对应数据库表: TC_SEAL_ARCHIVE
 *
 * @author HuaBo Cloud
 * @since 2026-03-26
 */
@ApiModel(value = "TcSealArchive", description = "印鉴档案管理")
@TableName("TC_SEAL_ARCHIVE")
public class TcSealArchive implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    @ApiModelProperty(value = "主键ID")
    private String id;

    @TableField("SEAL_CODE")
    @ApiModelProperty(value = "印鉴编码", required = true)
    private String sealCode;

    @TableField("SEAL_NAME")
    @ApiModelProperty(value = "印鉴名称", required = true)
    private String sealName;

    @TableField("SEAL_TYPE_ID")
    @ApiModelProperty(value = "印鉴类型ID")
    private String sealTypeId;

    @TableField(exist = false)
    @ApiModelProperty(value = "印鉴类型名称（非DB字段，前端展示用）")
    private String sealTypeName;

    @TableField("OWNER_NAME")
    @ApiModelProperty(value = "持有人姓名", required = true)
    private String ownerName;

    @TableField("OWNER_POSITION")
    @ApiModelProperty(value = "持有人职位")
    private String ownerPosition;

    @TableField("OWNER_ID_CARD")
    @ApiModelProperty(value = "持有人身份证号")
    private String ownerIdCard;

    @TableField("SEAL_IMAGE_URL")
    @ApiModelProperty(value = "印鉴图片URL")
    private String sealImageUrl;

    @TableField("EFFECTIVE_DATE")
    @ApiModelProperty(value = "生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    @TableField("EXPIRE_DATE")
    @ApiModelProperty(value = "失效日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUser;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateUser;

    // Getter / Setter

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSealCode() { return sealCode; }
    public void setSealCode(String sealCode) { this.sealCode = sealCode; }

    public String getSealName() { return sealName; }
    public void setSealName(String sealName) { this.sealName = sealName; }

    public String getSealTypeId() { return sealTypeId; }
    public void setSealTypeId(String sealTypeId) { this.sealTypeId = sealTypeId; }

    public String getSealTypeName() { return sealTypeName; }
    public void setSealTypeName(String sealTypeName) { this.sealTypeName = sealTypeName; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getOwnerPosition() { return ownerPosition; }
    public void setOwnerPosition(String ownerPosition) { this.ownerPosition = ownerPosition; }

    public String getOwnerIdCard() { return ownerIdCard; }
    public void setOwnerIdCard(String ownerIdCard) { this.ownerIdCard = ownerIdCard; }

    public String getSealImageUrl() { return sealImageUrl; }
    public void setSealImageUrl(String sealImageUrl) { this.sealImageUrl = sealImageUrl; }

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
}
