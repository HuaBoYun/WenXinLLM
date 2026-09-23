package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 产权登记台账实体 - 产权穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_PROPERTY_RIGHT")
public class TblPropertyRight {

    @TableId(value = "PROPERTY_ID", type = IdType.ASSIGN_UUID)
    private String propertyId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("PARENT_COMPANY_ID")
    private String parentCompanyId;

    /** 产权类型(SOLE国有独资/HOLDING国有控股/PARTICIPATING国有参股) */
    @TableField("RIGHT_TYPE")
    private String rightType;

    /** 持股比例(%) */
    @TableField("HOLDING_RATIO")
    private BigDecimal holdingRatio;

    /** 登记状态(REGISTERED已登记/PENDING待登记/CHANGING变更中) */
    @TableField("REGISTRATION_STATUS")
    private String registrationStatus;

    @TableField("EQUITY_RATIO")
    private BigDecimal equityRatio;

    @TableField("INVEST_AMOUNT")
    private BigDecimal investAmount;

    @TableField("REGISTERED_CAPITAL")
    private BigDecimal registeredCapital;

    @TableField("EQUITY_LEVEL")
    private Integer equityLevel;

    @TableField("INDUSTRY")
    private String industry;

    @TableField("REGION")
    private String region;

    @TableField("BUSINESS_STATUS")
    private String businessStatus;

    @TableField("REGISTRATION_NO")
    private String registrationNo;

    @TableField("PROPERTY_STATUS")
    private String propertyStatus;

    @TableField("LEGAL_REPRESENTATIVE")
    private String legalRepresentative;

    @TableField("UNIFIED_CREDIT_CODE")
    private String unifiedCreditCode;

    @TableField("ESTABLISH_DATE")
    private LocalDate establishDate;

    /** 上级企业名称（冗余字段，方便展示） */
    @TableField("PARENT_COMPANY_NAME")
    private String parentCompanyName;

    /** 工商比对结果(CONSISTENT一致/DIFFERENT差异) */
    @TableField("REGISTRATION_CONSISTENCY")
    private String registrationConsistency;

    /** 是否并表(Y/N) */
    @TableField("IS_CONSOLIDATED")
    private String isConsolidated;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}

