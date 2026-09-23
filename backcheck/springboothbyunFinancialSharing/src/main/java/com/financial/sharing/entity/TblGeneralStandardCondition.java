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
 * 适用条件表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_GENERAL_STANDARD_CONDITION")
@ApiModel(value = "TblGeneralStandardCondition", description = "适用条件表")
public class TblGeneralStandardCondition implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "CONDITION_ID")
    @ApiModelProperty(value = "条件ID")
    private String conditionId;

    @TableField("STANDARD_ID")
    @ApiModelProperty(value = "标准ID")
    private String standardId;

    @TableField("CONDITION_EXPRESSION")
    @ApiModelProperty(value = "条件表达式")
    private String conditionExpression;

    @TableField("CONDITION_DESC")
    @ApiModelProperty(value = "条件描述")
    private String conditionDesc;

    @TableField("PRIORITY")
    @ApiModelProperty(value = "优先级")
    private Integer priority;

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
