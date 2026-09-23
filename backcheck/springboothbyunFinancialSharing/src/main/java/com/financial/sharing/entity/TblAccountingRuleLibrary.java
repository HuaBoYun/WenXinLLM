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
 * 会计规则库表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_ACCOUNTING_RULE_LIBRARY")
@ApiModel(value = "TblAccountingRuleLibrary", description = "会计规则库表")
public class TblAccountingRuleLibrary implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LIBRARY_ID")
    @ApiModelProperty(value = "库ID")
    private String libraryId;

    @TableField("LIBRARY_NAME")
    @ApiModelProperty(value = "库名称")
    private String libraryName;

    @TableField("LIBRARY_CODE")
    @ApiModelProperty(value = "库编码")
    private String libraryCode;

    @TableField("LIBRARY_TYPE")
    @ApiModelProperty(value = "库类型")
    private String libraryType;

    @TableField("RULE_TEMPLATE")
    @ApiModelProperty(value = "规则模板(JSON格式)")
    private String ruleTemplate;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用(0-禁用,1-启用)")
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
