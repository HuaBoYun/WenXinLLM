package com.financial.sharing.consolidationReport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 抵消凭证实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_ELIMINATION_VOUCHER")
public class TblEliminationVoucher {

    /**
     * 凭证ID
     */
    @TableId("VOUCHER_ID")
    private String voucherId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 模板ID
     */
    @TableField("TEMPLATE_ID")
    private String templateId;

    /**
     * 期间
     */
    @TableField("PERIOD")
    private String period;

    /**
     * 凭证号
     */
    @TableField("VOUCHER_NO")
    private String voucherNo;

    /**
     * 凭证日期
     */
    @TableField("VOUCHER_DATE")
    private Date voucherDate;

    /**
     * 分录类型：DEBIT(借方)/CREDIT(贷方)
     */
    @TableField("ENTRY_TYPE")
    private String entryType;

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
     * 金额
     */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 组织名称
     */
    @TableField("ORG_NAME")
    private String orgName;

    /**
     * 摘要
     */
    @TableField("DESCRIPTION")
    private String description;

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

    /**
     * 模板名称(用于显示)
     */
    @TableField(exist = false)
    private String templateName;
}

