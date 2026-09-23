package com.financial.sharing.budgetControl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 控制规则表实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_CONTROL_RULE")
@ApiModel(value = "TblControlRule对象", description = "控制规则表")
public class TblControlRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "规则ID")
    @TableId(value = "RULE_ID", type = IdType.ASSIGN_ID)
    private String ruleId;

    @ApiModelProperty(value = "规则编码")
    @TableField("RULE_CODE")
    private String ruleCode;

    @ApiModelProperty(value = "规则名称")
    @TableField("RULE_NAME")
    private String ruleName;

    @ApiModelProperty(value = "控制类型：RIGID(刚性)/FLEXIBLE(弹性)/WARNING(预警)")
    @TableField("CONTROL_TYPE")
    private String controlType;

    @ApiModelProperty(value = "控制级别：SUBJECT(科目)/ORG(组织)/PROJECT(项目)/CUSTOM(自定义)")
    @TableField("CONTROL_LEVEL")
    private String controlLevel;

    @ApiModelProperty(value = "控制维度（JSON格式）")
    @TableField("CONTROL_DIMENSION")
    private String controlDimension;

    @ApiModelProperty(value = "控制期间：MONTH(月度)/QUARTER(季度)/YEAR(年度)")
    @TableField("CONTROL_PERIOD")
    private String controlPeriod;

    @ApiModelProperty(value = "阈值类型：AMOUNT(金额)/RATIO(比例)")
    @TableField("THRESHOLD_TYPE")
    private String thresholdType;

    @ApiModelProperty(value = "阈值")
    @TableField("THRESHOLD_VALUE")
    private BigDecimal thresholdValue;

    @ApiModelProperty(value = "预警比例（%）")
    @TableField("WARNING_RATIO")
    private BigDecimal warningRatio;

    @ApiModelProperty(value = "控制动作：BLOCK(阻止)/WARN(警告)/APPROVE(审批)")
    @TableField("CONTROL_ACTION")
    private String controlAction;

    @ApiModelProperty(value = "审批流程ID")
    @TableField("APPROVAL_WORKFLOW_ID")
    private String approvalWorkflowId;

    @ApiModelProperty(value = "例外用户（JSON数组）")
    @TableField("EXCEPTION_USERS")
    private String exceptionUsers;

    @ApiModelProperty(value = "是否启用：Y/N")
    @TableField("IS_ENABLED")
    private String isEnabled;

    @ApiModelProperty(value = "生效开始日期")
    @TableField("EFFECTIVE_START_DATE")
    private Date effectiveStartDate;

    @ApiModelProperty(value = "生效结束日期")
    @TableField("EFFECTIVE_END_DATE")
    private Date effectiveEndDate;

    @ApiModelProperty(value = "优先级")
    @TableField("PRIORITY")
    private Integer priority;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORG_ID")
    private String orgId;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @ApiModelProperty(value = "修改时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;
}

