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
 * 执行记录表实体类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@TableName("TBL_EXECUTION_RECORD")
@ApiModel(value = "TblExecutionRecord对象", description = "执行记录表")
public class TblExecutionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录ID")
    @TableId(value = "RECORD_ID", type = IdType.ASSIGN_ID)
    private String recordId;

    @ApiModelProperty(value = "规则ID")
    @TableField("RULE_ID")
    private String ruleId;

    @ApiModelProperty(value = "来源系统")
    @TableField("SOURCE_SYSTEM")
    private String sourceSystem;

    @ApiModelProperty(value = "来源单据类型")
    @TableField("SOURCE_DOC_TYPE")
    private String sourceDocType;

    @ApiModelProperty(value = "来源单据ID")
    @TableField("SOURCE_DOC_ID")
    private String sourceDocId;

    @ApiModelProperty(value = "来源单据编号")
    @TableField("SOURCE_DOC_CODE")
    private String sourceDocCode;

    @ApiModelProperty(value = "业务组织ID")
    @TableField("BIZ_ORG_ID")
    private String bizOrgId;

    @ApiModelProperty(value = "科目编码")
    @TableField("SUBJECT_CODE")
    private String subjectCode;

    @ApiModelProperty(value = "期间")
    @TableField("PERIOD")
    private String period;

    @ApiModelProperty(value = "维度值（JSON格式）")
    @TableField("DIMENSION_VALUES")
    private String dimensionValues;

    @ApiModelProperty(value = "申请金额")
    @TableField("APPLY_AMOUNT")
    private BigDecimal applyAmount;

    @ApiModelProperty(value = "预算金额")
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "已用金额")
    @TableField("USED_AMOUNT")
    private BigDecimal usedAmount;

    @ApiModelProperty(value = "可用金额")
    @TableField("AVAILABLE_AMOUNT")
    private BigDecimal availableAmount;

    @ApiModelProperty(value = "控制结果：PASS(通过)/BLOCK(阻止)/WARN(警告)/APPROVE(待审批)")
    @TableField("CONTROL_RESULT")
    private String controlResult;

    @ApiModelProperty(value = "控制消息")
    @TableField("CONTROL_MESSAGE")
    private String controlMessage;

    @ApiModelProperty(value = "审批状态：PENDING(待审批)/APPROVED(已审批)/REJECTED(已拒绝)")
    @TableField("APPROVAL_STATUS")
    private String approvalStatus;

    @ApiModelProperty(value = "审批人")
    @TableField("APPROVER")
    private String approver;

    @ApiModelProperty(value = "审批时间")
    @TableField("APPROVAL_TIME")
    private Date approvalTime;

    @ApiModelProperty(value = "审批意见")
    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    @ApiModelProperty(value = "执行时间")
    @TableField("EXECUTE_TIME")
    private Date executeTime;

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

    /**
     * 业务组织名称（非数据库字段, 通过 LEFT JOIN TBL_ORGANIZATION 获取）
     */
    @TableField(exist = false)
    private String bizOrgName;

    /**
     * 科目名称（非数据库字段, 通过 LEFT JOIN TBL_ACCOUNT_SUBJECT 获取）
     */
    @TableField(exist = false)
    private String subjectName;

    /**
     * 操作人姓名（非数据库字段, 通过 LEFT JOIN TBL_STAFF 获取）
     */
    @TableField(exist = false)
    private String createUserName;
}

