package com.financial.sharing.consolidationReport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 合并模型实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_CONSOLIDATION_MODEL")
public class TblConsolidationModel {

    /**
     * 模型ID
     */
    @TableId("MODEL_ID")
    private String modelId;

    /**
     * 模型编码
     */
    @TableField("MODEL_CODE")
    private String modelCode;

    /**
     * 模型名称
     */
    @TableField("MODEL_NAME")
    private String modelName;

    /**
     * 合并类型：FULL(完全合并)/PROPORTIONAL(比例合并)/EQUITY(权益法)
     */
    @TableField("CONSOLIDATION_TYPE")
    private String consolidationType;

    /**
     * 母公司ID
     */
    @TableField("PARENT_ORG_ID")
    private String parentOrgId;

    /**
     * 合并范围（JSON数组）
     */
    @TableField("CONSOLIDATION_SCOPE")
    private String consolidationScope;

    /**
     * 周期类型：YEAR/HALF_YEAR/QUARTER/MONTH
     */
    @TableField("PERIOD_TYPE")
    private String periodType;

    /**
     * 起始期间
     */
    @TableField("START_PERIOD")
    private String startPeriod;

    /**
     * 终止期间
     */
    @TableField("END_PERIOD")
    private String endPeriod;

    /**
     * 记账本位币
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /**
     * 汇率类型：SPOT(即期)/AVERAGE(平均)/FIXED(固定)
     */
    @TableField("EXCHANGE_RATE_TYPE")
    private String exchangeRateType;

    /**
     * 是否自动抵消：Y/N
     */
    @TableField("IS_AUTO_ELIMINATION")
    private String isAutoElimination;

    /**
     * 抵消规则（JSON格式）
     */
    @TableField("ELIMINATION_RULES")
    private String eliminationRules;

    /**
     * 状态：ACTIVE(启用)/INACTIVE(停用)
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
}

