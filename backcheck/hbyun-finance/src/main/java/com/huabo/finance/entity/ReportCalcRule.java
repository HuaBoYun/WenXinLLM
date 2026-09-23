package com.huabo.finance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报表计算规则实体类
 * 用于存储财务报表计算规则
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_REPORT_CALC_RULE")
@Schema(name = "ReportCalcRule对象", description = "报表计算规则")
public class ReportCalcRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name =  "规则ID(主键)")
    @TableId("RULE_ID")
    private String ruleId;

    @Schema(name =  "报表类型(BALANCE_SHEET-资产负债表,PROFIT_LOSS-利润表)")
    @TableField("REPORT_TYPE")
    private String reportType;

    @Schema(name =  "报表项目编码")
    @TableField("ITEM_CODE")
    private String itemCode;

    @Schema(name =  "报表项目名称")
    @TableField("ITEM_NAME")
    private String itemName;

    @Schema(name =  "父级项目编码")
    @TableField("PARENT_CODE")
    private String parentCode;

    @Schema(name =  "项目层级")
    @TableField("ITEM_LEVEL")
    private Integer itemLevel;

    @Schema(name =  "计算公式(JSON格式)")
    @TableField("CALC_FORMULA")
    private String calcFormula;

    @Schema(name =  "关联科目编码(逗号分隔)")
    @TableField("ACCOUNT_CODES")
    private String accountCodes;

    @Schema(name =  "计算类型(SUM-求和,SUBTRACT-相减,FORMULA-公式)")
    @TableField("CALC_TYPE")
    private String calcType;

    @Schema(name =  "排序序号")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @Schema(name =  "是否启用(1-启用,0-禁用)")
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    @Schema(name =  "所属公司")
    @TableField("LINK_ORG_ID")
    private BigDecimal linkOrgId;

    @Schema(name =  "所属部门")
    @TableField("LINK_DEPT_ID")
    private BigDecimal linkDeptId;

    @Schema(name =  "创建人")
    @TableField("CREATOR")
    private BigDecimal creator;

    @Schema(name =  "创建时间")
    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(name =  "修改人")
    @TableField("MODIFIER")
    private BigDecimal modifier;

    @Schema(name =  "修改时间")
    @TableField("MODIFY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @Schema(name =  "备注")
    @TableField("REMARK")
    private String remark;
}

