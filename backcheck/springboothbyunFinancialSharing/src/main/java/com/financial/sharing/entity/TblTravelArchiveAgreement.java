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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合作协议表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TRAVEL_ARCHIVE_AGREEMENT")
@ApiModel(value = "TblTravelArchiveAgreement", description = "合作协议表")
public class TblTravelArchiveAgreement implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "AGREEMENT_ID")
    @ApiModelProperty(value = "协议ID")
    private String agreementId;

    @TableField("ARCHIVE_ID")
    @ApiModelProperty(value = "档案ID")
    private String archiveId;

    @TableField("AGREEMENT_NAME")
    @ApiModelProperty(value = "协议名称")
    private String agreementName;

    // ========== 原有文件相关字段 ==========
    @TableField("FILE_NAME")
    @ApiModelProperty(value = "文件名")
    private String fileName;

    @TableField("FILE_PATH")
    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @TableField("FILE_SIZE")
    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @TableField("CONTENT_TYPE")
    @ApiModelProperty(value = "文件类型")
    private String contentType;

    @TableField("UPLOAD_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "上传日期")
    private LocalDate uploadDate;

    // ========== 新增协议详情字段 ==========
    @TableField("AGREEMENT_TYPE")
    @ApiModelProperty(value = "协议类型")
    private String agreementType;

    @TableField("SIGN_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "签订日期")
    private LocalDate signDate;

    @TableField("EFFECTIVE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生效日期")
    private LocalDate effectiveDate;

    @TableField("EXPIRY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "失效日期")
    private LocalDate expiryDate;

    @TableField("AGREEMENT_AMOUNT")
    @ApiModelProperty(value = "协议金额")
    private java.math.BigDecimal agreementAmount;

    @TableField("PAYMENT_TERMS")
    @ApiModelProperty(value = "付款条款")
    private String paymentTerms;

    @TableField("AGREEMENT_CONTENT")
    @ApiModelProperty(value = "协议内容")
    private String agreementContent;

    @TableField("ATTACHMENT_URL")
    @ApiModelProperty(value = "附件地址")
    private String attachmentUrl;

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
