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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 差旅标准明细表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TRAVEL_STANDARD_DETAIL")
@ApiModel(value = "TblTravelStandardDetail", description = "差旅标准明细表")
public class TblTravelStandardDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "DETAIL_ID")
    @ApiModelProperty(value = "明细ID")
    private String detailId;

    @TableField("STANDARD_ID")
    @ApiModelProperty(value = "标准ID")
    private String standardId;

    @TableField("EXPENSE_TYPE")
    @ApiModelProperty(value = "费用类型")
    private String expenseType;

    @TableField("QUOTA_TYPE")
    @ApiModelProperty(value = "配额类型")
    private String quotaType;

    @TableField("QUOTA_VALUE")
    @ApiModelProperty(value = "配额值")
    private BigDecimal quotaValue;

    @TableField("UNIT")
    @ApiModelProperty(value = "单位")
    private String unit;

    @TableField("MAX_AMOUNT")
    @ApiModelProperty(value = "最高限额")
    private BigDecimal maxAmount;

    @TableField("DESCRIPTION")
    @ApiModelProperty(value = "描述")
    private String description;

    @TableField("SORT_ORDER")
    @ApiModelProperty(value = "排序号")
    private Integer sortOrder;

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

