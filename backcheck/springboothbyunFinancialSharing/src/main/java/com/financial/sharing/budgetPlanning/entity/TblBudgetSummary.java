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
 * 预算数据汇总实体
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
@TableName("TBL_BUDGET_SUMMARY")
public class TblBudgetSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 汇总ID
     */
    @TableId(value = "SUMMARY_ID", type = IdType.ASSIGN_ID)
    private String summaryId;

    /**
     * 预算模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 预算期间
     */
    @TableField("PERIOD")
    private String period;

    /**
     * 预算版本
     */
    @TableField("VERSION")
    private String version;

    /**
     * 汇总类型(SUBJECT-科目/ORGANIZATION-组织/PERIOD-期间/CUSTOM-自定义)
     */
    @TableField("SUMMARY_TYPE")
    private String summaryType;

    /**
     * 维度编码
     */
    @TableField("DIMENSION_CODE")
    private String dimensionCode;

    /**
     * 维度名称
     */
    @TableField("DIMENSION_NAME")
    private String dimensionName;

    /**
     * 汇总值
     */
    @TableField("SUMMARY_VALUE")
    private BigDecimal summaryValue;

    /**
     * 汇总方法(SUM-求和/AVG-平均/MAX-最大/MIN-最小/COUNT-计数)
     */
    @TableField("SUMMARY_METHOD")
    private String summaryMethod;

    /**
     * 数据条数
     */
    @TableField("DATA_COUNT")
    private Integer dataCount;

    /**
     * 状态(PROCESSING-处理中/COMPLETED-已完成/FAILED-失败)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 错误信息
     */
    @TableField("ERROR_MESSAGE")
    private String errorMessage;

    /**
     * 租户ID
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
     * 预算模型名称(非数据库字段)
     */
    @TableField(exist = false)
    private String modelName;
}

