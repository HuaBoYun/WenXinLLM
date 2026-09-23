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
 * 会计规则版本表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_ACCOUNTING_RULE_VERSION")
@ApiModel(value = "TblAccountingRuleVersion", description = "会计规则版本表")
public class TblAccountingRuleVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "VERSION_ID")
    @ApiModelProperty(value = "版本ID")
    private String versionId;

    @TableField("RULE_ID")
    @ApiModelProperty(value = "规则ID")
    private String ruleId;

    @TableField("VERSION_NUMBER")
    @ApiModelProperty(value = "版本号")
    private String versionNumber;

    @TableField("VERSION_DATA")
    @ApiModelProperty(value = "版本数据(JSON格式)")
    private String versionData;

    @TableField("CHANGE_DESCRIPTION")
    @ApiModelProperty(value = "变更说明")
    private String changeDescription;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
