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
 * 参数选项表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_EXPENSE_PARAMETER_OPTION")
@ApiModel(value = "TblExpenseParameterOption", description = "参数选项表")
public class TblExpenseParameterOption implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "OPTION_ID")
    @ApiModelProperty(value = "选项ID")
    private String optionId;

    @TableField("PARAMETER_ID")
    @ApiModelProperty(value = "参数ID")
    private String parameterId;

    @TableField("OPTION_VALUE")
    @ApiModelProperty(value = "选项值")
    private String optionValue;

    @TableField("OPTION_NAME")
    @ApiModelProperty(value = "选项名称")
    private String optionName;

    @TableField("SORT_ORDER")
    @ApiModelProperty(value = "排序号")
    private Integer sortOrder;

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

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
