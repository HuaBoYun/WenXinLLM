package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商旅档案主表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TRAVEL_ARCHIVE")
@ApiModel(value = "TblTravelArchive", description = "商旅档案主表")
public class TblTravelArchive implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ARCHIVE_ID")
    @ApiModelProperty(value = "档案ID")
    private String archiveId;

    @TableField("ARCHIVE_CODE")
    @ApiModelProperty(value = "档案编码")
    private String archiveCode;

    @TableField("ARCHIVE_NAME")
    @ApiModelProperty(value = "档案名称")
    private String archiveName;

    @TableField("ARCHIVE_TYPE")
    @ApiModelProperty(value = "档案类型")
    private String archiveType;

    @TableField("PROVIDER_NAME")
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    @TableField("PROVIDER_CODE")
    @ApiModelProperty(value = "供应商编码")
    private String providerCode;

    @TableField("CONTACT_PERSON")
    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @TableField("CONTACT_PHONE")
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @TableField("CONTACT_EMAIL")
    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    @TableField("ADDRESS")
    @ApiModelProperty(value = "地址")
    private String address;

    @TableField("STAR_LEVEL")
    @ApiModelProperty(value = "星级评定")
    private Integer starLevel;

    @TableField("BUSINESS_LICENSE")
    @ApiModelProperty(value = "营业执照号")
    private String businessLicense;

    @TableField("TAX_NUMBER")
    @ApiModelProperty(value = "税号")
    private String taxNumber;

    @TableField("BANK_ACCOUNT")
    @ApiModelProperty(value = "银行账户")
    private String bankAccount;

    @TableField("BANK_NAME")
    @ApiModelProperty(value = "开户银行")
    private String bankName;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
