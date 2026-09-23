package com.financial.sharing.consolidationReport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 股权结构实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_EQUITY_STRUCTURE")
public class TblEquityStructure {

    /**
     * 股权结构ID
     */
    @TableId("STRUCTURE_ID")
    private String structureId;

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
     * 组织名称
     */
    @TableField("ORG_NAME")
    private String orgName;

    /**
     * 父组织ID
     */
    @TableField("PARENT_ORG_ID")
    private String parentOrgId;

    /**
     * 直接持股比例(%)
     */
    @TableField("DIRECT_HOLDING_RATIO")
    private BigDecimal directHoldingRatio;

    /**
     * 间接持股比例(%)
     */
    @TableField("INDIRECT_HOLDING_RATIO")
    private BigDecimal indirectHoldingRatio;

    /**
     * 综合持股比例(%)
     */
    @TableField("TOTAL_HOLDING_RATIO")
    private BigDecimal totalHoldingRatio;

    /**
     * 层级(1为顶层母公司)
     */
    @TableField("LEVEL")
    private Integer level;

    /**
     * 股权路径(用于追溯)
     */
    @TableField("PATH")
    private String path;

    /**
     * 计算日期
     */
    @TableField("CALCULATION_DATE")
    private Date calculationDate;

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
     * 子节点列表(用于树形结构)
     */
    @TableField(exist = false)
    private List<TblEquityStructure> children;

    /**
     * 模型名称(用于显示)
     */
    @TableField(exist = false)
    private String modelName;
}

