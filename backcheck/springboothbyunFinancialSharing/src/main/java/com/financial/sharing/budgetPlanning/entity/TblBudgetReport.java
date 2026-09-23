package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算报表配置实体类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
@TableName("TBL_BUDGET_REPORT")
public class TblBudgetReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报表ID
     */
    @TableId(value = "REPORT_ID", type = IdType.ASSIGN_ID)
    private String reportId;

    /**
     * 报表编码
     */
    @TableField("REPORT_CODE")
    private String reportCode;

    /**
     * 报表名称
     */
    @TableField("REPORT_NAME")
    private String reportName;

    /**
     * 报表类型：DETAIL(明细表)/SUMMARY(汇总表)/COMPARE(对比表)/TREND(趋势表)
     */
    @TableField("REPORT_TYPE")
    private String reportType;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 查询条件（JSON格式）
     */
    @TableField("QUERY_CONDITION")
    private String queryCondition;

    /**
     * 显示列配置（JSON格式）
     */
    @TableField("DISPLAY_COLUMNS")
    private String displayColumns;

    /**
     * 排序配置（JSON格式）
     */
    @TableField("SORT_CONFIG")
    private String sortConfig;

    /**
     * 筛选配置（JSON格式）
     */
    @TableField("FILTER_CONFIG")
    private String filterConfig;

    /**
     * 是否模板：Y/N
     */
    @TableField("IS_TEMPLATE")
    private String isTemplate;

    /**
     * 是否公开：Y/N
     */
    @TableField("IS_PUBLIC")
    private String isPublic;

    /**
     * 报表说明
     */
    @TableField("DESCRIPTION")
    private String description;

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
     * 模型名称（非数据库字段）
     */
    @TableField(exist = false)
    private String modelName;
}

