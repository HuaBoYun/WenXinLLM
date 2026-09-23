package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算场景实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_SCENARIO")
public class BudgetScenario implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 场景ID (主键)
     */
    @TableId(value = "SCENARIO_ID", type = IdType.ASSIGN_UUID)
    private String scenarioId;

    /**
     * 场景编码
     */
    @TableField("SCENARIO_CODE")
    private String scenarioCode;

    /**
     * 场景名称
     */
    @TableField("SCENARIO_NAME")
    private String scenarioName;

    /**
     * 场景类型
     * BASE: 基准场景
     * OPTIMISTIC: 乐观场景
     * PESSIMISTIC: 悲观场景
     * CUSTOM: 自定义场景
     */
    @TableField("SCENARIO_TYPE")
    private String scenarioType;

    /**
     * 假设条件 (JSON格式)
     */
    @TableField("ASSUMPTIONS")
    private String assumptions;

    /**
     * 参数配置 (JSON格式)
     */
    @TableField("PARAMETERS")
    private String parameters;

    /**
     * 是否默认场景
     */
    @TableField("IS_DEFAULT")
    private Boolean isDefault;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 场景描述
     */
    @TableField("SCENARIO_DESCRIPTION")
    private String scenarioDescription;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 删除标志
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;
}

