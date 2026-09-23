package com.financial.sharing.consolidationReport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 合并范围配置实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_CONSOLIDATION_SCOPE")
public class TblConsolidationScope {

    /**
     * 范围配置ID
     */
    @TableId("SCOPE_ID")
    private String scopeId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 组织名称(冗余字段,便于查询)
     */
    @TableField("ORG_NAME")
    private String orgName;

    /**
     * 合并方法：FULL(完全合并)/PROPORTIONAL(比例合并)/EQUITY(权益法)
     */
    @TableField("CONSOLIDATION_METHOD")
    private String consolidationMethod;

    /**
     * 生效起始期间
     */
    @TableField("EFFECTIVE_START_PERIOD")
    private String effectiveStartPeriod;

    /**
     * 生效终止期间
     */
    @TableField("EFFECTIVE_END_PERIOD")
    private String effectiveEndPeriod;

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
     * 备注
     */
    @TableField("REMARK")
    private String remark;

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

