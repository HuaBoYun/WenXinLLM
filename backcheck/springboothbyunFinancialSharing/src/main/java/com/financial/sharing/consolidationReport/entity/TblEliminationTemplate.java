package com.financial.sharing.consolidationReport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 抵消凭证模板实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_ELIMINATION_TEMPLATE")
public class TblEliminationTemplate {

    /**
     * 模板ID
     */
    @TableId("TEMPLATE_ID")
    private String templateId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 模板编码
     */
    @TableField("TEMPLATE_CODE")
    private String templateCode;

    /**
     * 模板名称
     */
    @TableField("TEMPLATE_NAME")
    private String templateName;

    /**
     * 抵消类型：INTERNAL_TRANSACTION(内部交易)/INTERNAL_DEBT(内部债权债务)/
     * UNREALIZED_PROFIT(未实现利润)/INVESTMENT_ELIMINATION(长期股权投资)/
     * EQUITY_ELIMINATION(所有者权益)/OTHER(其他)
     */
    @TableField("ELIMINATION_TYPE")
    private String eliminationType;

    /**
     * 借方科目(多个科目用逗号分隔)
     */
    @TableField("DEBIT_ACCOUNT")
    private String debitAccount;

    /**
     * 贷方科目(多个科目用逗号分隔)
     */
    @TableField("CREDIT_ACCOUNT")
    private String creditAccount;

    /**
     * 计算规则(JSON格式)
     */
    @TableField("CALCULATION_RULE")
    private String calculationRule;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 是否启用：Y/N
     */
    @TableField("IS_ACTIVE")
    private String isActive;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

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

