package com.financial.sharing.consolidationReport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 合并报表实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_CONSOLIDATED_REPORT")
public class TblConsolidatedReport {

    /**
     * 合并报表ID
     */
    @TableId("REPORT_ID")
    private String reportId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 期间
     */
    @TableField("PERIOD")
    private String period;

    /**
     * 报表类型：BALANCE_SHEET(资产负债表)/INCOME_STATEMENT(利润表)/
     * CASH_FLOW(现金流量表)
     */
    @TableField("REPORT_TYPE")
    private String reportType;

    /**
     * 报表项目编码
     */
    @TableField("ITEM_CODE")
    private String itemCode;

    /**
     * 报表项目名称
     */
    @TableField("ITEM_NAME")
    private String itemName;

    /**
     * 项目层级(1为一级项目)
     */
    @TableField("ITEM_LEVEL")
    private Integer itemLevel;

    /**
     * 父项目编码
     */
    @TableField("PARENT_CODE")
    private String parentCode;

    /**
     * 母公司金额
     */
    @TableField("PARENT_AMOUNT")
    private BigDecimal parentAmount;

    /**
     * 子公司金额
     */
    @TableField("SUBSIDIARY_AMOUNT")
    private BigDecimal subsidiaryAmount;

    /**
     * 抵消金额
     */
    @TableField("ELIMINATION_AMOUNT")
    private BigDecimal eliminationAmount;

    /**
     * 合并金额
     */
    @TableField("CONSOLIDATED_AMOUNT")
    private BigDecimal consolidatedAmount;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 状态：DRAFT(草稿)/CONFIRMED(已确认)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

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

    // 非数据库字段
    /**
     * 模型名称(用于显示)
     */
    @TableField(exist = false)
    private String modelName;
}

