package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 成本归集规则实体类
 * 对应表：TBL_COST_COLLECTION_RULE
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
@Data
@TableName("TBL_COST_COLLECTION_RULE")
public class CostCollectionRuleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(value = "RULE_ID", type = IdType.ASSIGN_ID)
    private String ruleId;

    /**
     * 规则名称
     */
    @TableField("RULE_NAME")
    private String ruleName;

    /**
     * 成本中心ID
     */
    @TableField("COST_CENTER_ID")
    private String costCenterId;

    /**
     * 源科目编码
     */
    @TableField("SOURCE_ACCOUNT")
    private String sourceAccount;

    /**
     * 归集方法（DIRECT-直接归集, ALLOCATION-分摊归集）
     */
    @TableField("COLLECTION_METHOD")
    private String collectionMethod;

    /**
     * 归集类型（MATERIAL-材料成本, LABOR-人工成本, OVERHEAD-制造费用）
     */
    @TableField("COLLECTION_TYPE")
    private String collectionType;

    /**
     * 归集规则描述
     */
    @TableField("RULE_DESCRIPTION")
    private String ruleDescription;

    /**
     * 是否启用（1-启用, 0-停用）
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 优先级
     */
    @TableField("PRIORITY")
    private Integer priority;

    /**
     * 账套ID
     */
    @TableField("BOOK_ID")
    private String bookId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;
}

