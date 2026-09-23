package com.financial.sharing.consolidationReport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对账数据实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_RECONCILIATION_DATA")
public class TblReconciliationData {

    /**
     * 对账数据ID
     */
    @TableId("RECONCILIATION_ID")
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
     * 甲方公司ID
     */
    @TableField("COMPANY_A_ID")
    private String companyAId;

    /**
     * 甲方公司名称
     */
    @TableField("COMPANY_A_NAME")
    private String companyAName;

    /**
     * 乙方公司ID
     */
    @TableField("COMPANY_B_ID")
    private String companyBId;

    /**
     * 乙方公司名称
     */
    @TableField("COMPANY_B_NAME")
    private String companyBName;

    /**
     * 交易类型：RECEIVABLE_PAYABLE(应收应付)/REVENUE_COST(收入成本)/
     * INVENTORY(存货)/INVESTMENT(投资)/OTHER(其他)
     */
    @TableField("TRANSACTION_TYPE")
    private String transactionType;

    /**
     * 甲方金额
     */
    @TableField("AMOUNT_A")
    private BigDecimal amountA;

    /**
     * 乙方金额
     */
    @TableField("AMOUNT_B")
    private BigDecimal amountB;

    /**
     * 差异金额(甲方金额-乙方金额)
     */
    @TableField("DIFF_AMOUNT")
    private BigDecimal diffAmount;

    /**
     * 科目编码
     */
    @TableField("ACCOUNT_CODE")
    private String accountCode;

    /**
     * 科目名称
     */
    @TableField("ACCOUNT_NAME")
    private String accountName;

    /**
     * 摘要
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 状态：PENDING(待对账)/MATCHED(已对账)/DIFF(有差异)
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

