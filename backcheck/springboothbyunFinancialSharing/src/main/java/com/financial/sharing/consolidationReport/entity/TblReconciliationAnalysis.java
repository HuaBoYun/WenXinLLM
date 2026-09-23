package com.financial.sharing.consolidationReport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对账差异分析实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_RECONCILIATION_ANALYSIS")
public class TblReconciliationAnalysis {

    /**
     * 差异分析ID
     */
    @TableId("ANALYSIS_ID")
    private String analysisId;

    /**
     * 对账数据ID
     */
    @TableField("RECONCILIATION_ID")
    private String reconciliationId;

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
     * 差异原因：TIMING(时间性差异)/AMOUNT(金额差异)/
     * ACCOUNT(科目差异)/OTHER(其他)
     */
    @TableField("DIFF_REASON")
    private String diffReason;

    /**
     * 差异描述
     */
    @TableField("DIFF_DESCRIPTION")
    private String diffDescription;

    /**
     * 处理方案
     */
    @TableField("SOLUTION")
    private String solution;

    /**
     * 调整金额
     */
    @TableField("ADJUST_AMOUNT")
    private BigDecimal adjustAmount;

    /**
     * 状态：PENDING(待处理)/PROCESSING(处理中)/
     * PROCESSED(已处理)/CLOSED(已关闭)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 处理人
     */
    @TableField("HANDLER")
    private String handler;

    /**
     * 处理人姓名
     */
    @TableField("HANDLER_NAME")
    private String handlerName;

    /**
     * 处理时间
     */
    @TableField("HANDLE_TIME")
    private Date handleTime;

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

    /**
     * 甲方公司名称(用于显示)
     */
    @TableField(exist = false)
    private String companyAName;

    /**
     * 乙方公司名称(用于显示)
     */
    @TableField(exist = false)
    private String companyBName;

    /**
     * 差异金额(用于显示)
     */
    @TableField(exist = false)
    private BigDecimal diffAmount;

    /**
     * 交易类型(用于显示)
     */
    @TableField(exist = false)
    private String transactionType;
}

