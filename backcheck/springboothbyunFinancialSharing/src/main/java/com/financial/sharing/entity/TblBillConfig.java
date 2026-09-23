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
 * 单据配置表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_BILL_CONFIG")
@ApiModel(value = "TblBillConfig", description = "单据配置表")
public class TblBillConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "CONFIG_ID")
    @ApiModelProperty(value = "配置ID")
    private String configId;

    @TableField("CONFIG_CODE")
    @ApiModelProperty(value = "配置编码")
    private String configCode;

    @TableField("CONFIG_NAME")
    @ApiModelProperty(value = "配置名称")
    private String configName;

    @TableField("BILL_TYPE")
    @ApiModelProperty(value = "账单类型")
    private String billType;

    @TableField("OCR_PROVIDER")
    @ApiModelProperty(value = "OCR提供商")
    private String ocrProvider;

    @TableField("OCR_ENABLED")
    @ApiModelProperty(value = "是否启用OCR识别")
    private Integer ocrEnabled;

    @TableField("AUDIT_ENABLED")
    @ApiModelProperty(value = "是否启用智能稽核")
    private Integer auditEnabled;

    @TableField("AUTO_MATCH_ENABLED")
    @ApiModelProperty(value = "是否启用自动匹配")
    private Integer autoMatchEnabled;

    @TableField("DUPLICATE_CHECK_ENABLED")
    @ApiModelProperty(value = "是否启用重复检查")
    private Integer duplicateCheckEnabled;

    @TableField("ORG_ID")
    @ApiModelProperty(value = "所属组织ID")
    private String orgId;

    @TableField("ORG_NAME")
    @ApiModelProperty(value = "所属组织名称")
    private String orgName;

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

    @TableField("DESCRIPTION")
    @ApiModelProperty(value = "描述")
    private String description;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
