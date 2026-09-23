package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算执行分析实体类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
@TableName("TBL_BUDGET_EXECUTION")
public class TblBudgetExecution implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 执行ID
     */
    @TableId(value = "EXECUTION_ID", type = IdType.ASSIGN_ID)
    private String executionId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 业务组织ID（华东/华南分公司等业务维度）
     */
    @TableField("BIZ_ORG_ID")
    private String bizOrgId;

    /**
     * 组织编码
     */
    @TableField("ORG_CODE")
    private String orgCode;

    /**
     * 组织名称
     */
    @TableField("ORG_NAME")
    private String orgName;

    /**
     * 科目编码
     */
    @TableField("SUBJECT_CODE")
    private String subjectCode;

    /**
     * 科目名称
     */
    @TableField("SUBJECT_NAME")
    private String subjectName;

    /**
     * 期间
     */
    @TableField("PERIOD")
    private String period;

    /**
     * 预算年度
     */
    @TableField("BUDGET_YEAR")
    private String budgetYear;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private String versionNo;

    /**
     * 维度1编码
     */
    @TableField("DIMENSION1_CODE")
    private String dimension1Code;

    /**
     * 维度1名称
     */
    @TableField("DIMENSION1_NAME")
    private String dimension1Name;

    /**
     * 维度2编码
     */
    @TableField("DIMENSION2_CODE")
    private String dimension2Code;

    /**
     * 维度2名称
     */
    @TableField("DIMENSION2_NAME")
    private String dimension2Name;

    /**
     * 预算金额
     */
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    /**
     * 调整后预算
     */
    @TableField("ADJUSTED_AMOUNT")
    private BigDecimal adjustedAmount;

    /**
     * 实际发生额
     */
    @TableField("ACTUAL_AMOUNT")
    private BigDecimal actualAmount;

    /**
     * 承诺金额
     */
    @TableField("COMMITTED_AMOUNT")
    private BigDecimal committedAmount;

    /**
     * 占用金额
     */
    @TableField("OCCUPIED_AMOUNT")
    private BigDecimal occupiedAmount;

    /**
     * 可用金额
     */
    @TableField("AVAILABLE_AMOUNT")
    private BigDecimal availableAmount;

    /**
     * 差异金额
     */
    @TableField("VARIANCE_AMOUNT")
    private BigDecimal varianceAmount;

    /**
     * 执行率（%）
     */
    @TableField("EXECUTION_RATE")
    private BigDecimal executionRate;

    /**
     * 差异率（%）
     */
    @TableField("VARIANCE_RATE")
    private BigDecimal varianceRate;

    /**
     * 状态：NORMAL(正常)/WARNING(预警)/EXCEEDED(超支)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 预警级别：LOW(低)/MEDIUM(中)/HIGH(高)
     */
    @TableField("WARNING_LEVEL")
    private String warningLevel;

    /**
     * 最后更新时间
     */
    @TableField("LAST_UPDATE_TIME")
    private Date lastUpdateTime;

    /**
     * 组织ID（来自登录用户 orgid，即原 TENANT_ID 字段）
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 模型名称（非数据库字段）
     */
    @TableField(exist = false)
    private String modelName;
}

