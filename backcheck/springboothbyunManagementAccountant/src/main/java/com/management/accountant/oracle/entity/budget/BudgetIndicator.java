package com.management.accountant.oracle.entity.budget;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算指标实体类
 *
 * @description 预算指标管理实体,支持多种指标类型和计算方式
 * @author AI Assistant
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "TBL_BUDGET_INDICATOR", schema = "REDACTED")
@ApiModel(value = "BudgetIndicator对象", description = "预算指标")
public class BudgetIndicator implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "INDICATOR_ID", type = IdType.ASSIGN_UUID)
    @ExcelProperty(value = "指标ID", index = 0)
    private String indicatorId;

    @ApiModelProperty(value = "指标编码")
    @TableField("INDICATOR_CODE")
    @ExcelProperty(value = "指标编码", index = 1)
    private String indicatorCode;

    @ApiModelProperty(value = "指标名称")
    @TableField("INDICATOR_NAME")
    @ExcelProperty(value = "指标名称", index = 2)
    private String indicatorName;

    @ApiModelProperty(value = "指标类型:revenue-收入,cost-成本,expense-费用,profit-利润,asset-资产,liability-负债,cashflow-现金流,custom-自定义")
    @TableField("INDICATOR_TYPE")
    @ExcelProperty(value = "指标类型", index = 3)
    private String indicatorType;

    @ApiModelProperty(value = "数据类型:number-数值,percentage-百分比,amount-金额,text-文本")
    @TableField("DATA_TYPE")
    @ExcelProperty(value = "数据类型", index = 4)
    private String dataType;

    @ApiModelProperty(value = "单位")
    @TableField("UNIT")
    @ExcelProperty(value = "单位", index = 5)
    private String unit;

    @ApiModelProperty(value = "小数位数(0-6)")
    @TableField("DECIMAL_PLACES")
    @ExcelProperty(value = "小数位数", index = 6)
    private Integer decimalPlaces;

    @ApiModelProperty(value = "计算公式")
    @TableField("FORMULA")
    @ExcelProperty(value = "计算公式", index = 7)
    private String formula;

    @ApiModelProperty(value = "汇总方式:sum-求和,avg-平均,max-最大,min-最小,count-计数")
    @TableField("SUMMARY_METHOD")
    @ExcelProperty(value = "汇总方式", index = 8)
    private String summaryMethod;

    @ApiModelProperty(value = "默认值")
    @TableField("DEFAULT_VALUE")
    @ExcelProperty(value = "默认值", index = 9)
    private BigDecimal defaultValue;

    @ApiModelProperty(value = "最小值")
    @TableField("MIN_VALUE")
    @ExcelProperty(value = "最小值", index = 10)
    private BigDecimal minValue;

    @ApiModelProperty(value = "最大值")
    @TableField("MAX_VALUE")
    @ExcelProperty(value = "最大值", index = 11)
    private BigDecimal maxValue;

    @ApiModelProperty(value = "是否必填:0-否,1-是")
    @TableField("IS_REQUIRED")
    @ExcelProperty(value = "是否必填", index = 12)
    private Integer isRequired;

    @ApiModelProperty(value = "是否计算指标:0-否,1-是")
    @TableField("IS_CALCULATED")
    @ExcelProperty(value = "是否计算指标", index = 13)
    private Integer isCalculated;

    @ApiModelProperty(value = "是否启用:0-否,1-是")
    @TableField("IS_ENABLED")
    @ExcelProperty(value = "是否启用", index = 14)
    private Integer isEnabled;

    @ApiModelProperty(value = "指标描述")
    @TableField("DESCRIPTION")
    @ExcelProperty(value = "指标描述", index = 15)
    private String description;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    @ExcelProperty(value = "备注", index = 16)
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    @TableField("CREATOR_ID")
    @ExcelProperty(value = "创建人ID", index = 17)
    private String creatorId;

    @ApiModelProperty(value = "创建人姓名")
    @TableField("CREATOR_NAME")
    @ExcelProperty(value = "创建人姓名", index = 18)
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    @ExcelProperty(value = "创建时间", index = 19)
    private Date createTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField("UPDATER_ID")
    @ExcelProperty(value = "更新人ID", index = 20)
    private String updaterId;

    @ApiModelProperty(value = "更新人姓名")
    @TableField("UPDATER_NAME")
    @ExcelProperty(value = "更新人姓名", index = 21)
    private String updaterName;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    @ExcelProperty(value = "更新时间", index = 22)
    private Date updateTime;

    @ApiModelProperty(value = "删除标记:0-未删除,1-已删除")
    @TableField("IS_DELETED")
    @ExcelProperty(value = "删除标记", index = 23)
    private Integer isDeleted;

    @ApiModelProperty(value = "公司ID")
    @TableField("COMPANY_ID")
    @ExcelProperty(value = "公司ID", index = 24)
    private String companyId;

    @ApiModelProperty(value = "公司名称")
    @TableField("COMPANY_NAME")
    @ExcelProperty(value = "公司名称", index = 25)
    private String companyName;
}

