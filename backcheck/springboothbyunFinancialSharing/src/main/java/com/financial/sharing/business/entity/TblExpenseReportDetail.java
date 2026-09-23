package com.financial.sharing.business.entity;

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
 * 报销单费用明细表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_EXPENSE_REPORT_DETAIL")
@ApiModel(value = "TblExpenseReportDetail", description = "报销单费用明细表")
public class TblExpenseReportDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "DETAIL_ID")
    @ApiModelProperty(value = "明细ID")
    private String detailId;

    @TableField("REPORT_ID")
    @ApiModelProperty(value = "报销单ID")
    private String reportId;

    @TableField("EXPENSE_ITEM_ID")
    @ApiModelProperty(value = "费用项目ID")
    private String expenseItemId;

    @TableField("EXPENSE_ITEM_NAME")
    @ApiModelProperty(value = "费用项目名称")
    private String expenseItemName;

    @TableField("EXPENSE_AMOUNT")
    @ApiModelProperty(value = "费用金额")
    private BigDecimal expenseAmount;

    @TableField("EXPENSE_DATE")
    @ApiModelProperty(value = "费用日期")
    private String expenseDate;

    @TableField("RECEIPT_COUNT")
    @ApiModelProperty(value = "发票数量")
    private Integer receiptCount;

    @TableField("DESCRIPTION")
    @ApiModelProperty(value = "费用说明")
    private String description;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
