package com.financial.sharing.consolidationReport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 股权信息实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_EQUITY_INFO")
public class TblEquityInfo {

    /**
     * 股权信息ID
     */
    @TableId("EQUITY_ID")
    private String equityId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 母公司ID
     */
    @TableField("PARENT_ORG_ID")
    private String parentOrgId;

    /**
     * 母公司名称(冗余字段,便于查询)
     */
    @TableField("PARENT_ORG_NAME")
    private String parentOrgName;

    /**
     * 子公司ID
     */
    @TableField("SUBSIDIARY_ORG_ID")
    private String subsidiaryOrgId;

    /**
     * 子公司名称(冗余字段,便于查询)
     */
    @TableField("SUBSIDIARY_ORG_NAME")
    private String subsidiaryOrgName;

    /**
     * 股权类型：DIRECT(直接持股)/INDIRECT(间接持股)
     */
    @TableField("EQUITY_TYPE")
    private String equityType;

    /**
     * 持股比例(%)
     */
    @TableField("HOLDING_RATIO")
    private BigDecimal holdingRatio;

    /**
     * 投资日期
     */
    @TableField("INVESTMENT_DATE")
    private Date investmentDate;

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

