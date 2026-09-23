package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算数据实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_NCV_BUDGET_DATA")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BudgetData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID (主键)
     */
    @TableId(value = "DATA_ID", type = IdType.ASSIGN_UUID)
    private String dataId;

    /**
     * 预算任务ID
     */
    @TableField("TASK_ID")
    private String taskId;

    /**
     * 预算ID
     */
    @TableField("BUDGET_ID")
    private String budgetId;

    /**
     * 是否锁定
     */
    @TableField("IS_LOCKED")
     private Integer isLocked;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    /**
     * 预算期间
     */
    @TableField("BUDGET_PERIOD")
    private String budgetPeriod;

    /**
     * 组织ID
     */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /**
     * 部门ID
     */
    @TableField("DEPARTMENT_ID")
    private String departmentId;

    /**
     * 项目ID
     */
    @TableField("PROJECT_ID")
    private String projectId;

    /**
     * 产品ID
     */
    @TableField("PRODUCT_ID")
    private String productId;

    /**
     * 指标ID
     */
    @TableField("INDICATOR_ID")
    private String indicatorId;

    /**
     * 指标编码
     */
    @TableField("INDICATOR_CODE")
    private String indicatorCode;

    /**
     * 指标名称
     */
    @TableField("INDICATOR_NAME")
    private String indicatorName;

    /**
     * 预算值
     */
    @TableField("BUDGET_VALUE")
    private BigDecimal budgetValue;

    /**
     * 实际值
     */
    @TableField("ACTUAL_VALUE")
    private BigDecimal actualValue;

    /**
     * 差异值
     */
    @TableField("VARIANCE_VALUE")
    private BigDecimal varianceValue;

    /**
     * 差异率 (%)
     */
    @TableField("VARIANCE_RATE")
    private BigDecimal varianceRate;

    /**
     * 币种
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 数据来源
     * MANUAL: 手工录入
     * IMPORT: 导入
     * CALCULATION: 计算
     * SYSTEM: 系统生成
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 数据状态
     * DRAFT: 草稿
     * SUBMITTED: 已提交
     * APPROVED: 已审批
     * REJECTED: 已拒绝
     */
    @TableField("DATA_STATUS")
    private String dataStatus;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private String versionNo;

    /**
     * 场景ID
     */
    @TableField("SCENARIO_ID")
    private String scenarioId;

    /**
     * 维度值JSON
     */
    @TableField("DIMENSION_VALUES")
    private String dimensionValues;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标志
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;
}

