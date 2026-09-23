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
 * 通用标准主表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_GENERAL_STANDARD")
@ApiModel(value = "TblGeneralStandard", description = "通用标准主表")
public class TblGeneralStandard implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "STANDARD_ID")
    @ApiModelProperty(value = "标准ID")
    private String standardId;

    @TableField("STANDARD_CODE")
    @ApiModelProperty(value = "标准编码")
    private String standardCode;

    @TableField("STANDARD_NAME")
    @ApiModelProperty(value = "标准名称")
    private String standardName;

    @TableField("STANDARD_TYPE")
    @ApiModelProperty(value = "标准类型")
    private String standardType;

    @TableField("LEVEL_COUNT")
    @ApiModelProperty(value = "级别数量")
    private Integer levelCount;

    @TableField("APPLICABLE_SCOPE")
    @ApiModelProperty(value = "适用范围")
    private String applicableScope;

    @TableField("EFFECTIVE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生效日期")
    private LocalDate effectiveDate;

    @TableField("EXPIRY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "失效日期")
    private LocalDate expiryDate;

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

    @TableField("DESCRIPTION")
    @ApiModelProperty(value = "标准描述")
    private String description;
}
