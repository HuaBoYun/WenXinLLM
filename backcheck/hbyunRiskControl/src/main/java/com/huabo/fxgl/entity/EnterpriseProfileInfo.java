package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业基本信息实体类
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@ToString
@TableName("TBL_ENTERPRISE_INFO")
@Schema(name="EnterpriseProfileInfo", description="企业基本信息")
public class EnterpriseProfileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="企业ID")
    @TableId(value = "ENTERPRISE_ID", type = IdType.ASSIGN_ID)
    private String enterpriseId;

    @Schema(name="企业名称")
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @Schema(name="企业编码")
    @TableField("ENTERPRISE_CODE")
    private String enterpriseCode;

    @Schema(name="公司简介")
    @TableField("COMPANY_PROFILE")
    private String companyProfile;

    @Schema(name="法定代表人")
    @TableField("LEGAL_REPRESENTATIVE")
    private String legalRepresentative;

    @Schema(name="注册资本(万元)")
    @TableField("REGISTERED_CAPITAL")
    private BigDecimal registeredCapital;

    @Schema(name="成立日期")
    @TableField("ESTABLISHMENT_DATE")
    private Date establishmentDate;

    @Schema(name="经营范围")
    @TableField("BUSINESS_SCOPE")
    private String businessScope;

    @Schema(name="联系地址")
    @TableField("CONTACT_ADDRESS")
    private String contactAddress;

    @Schema(name="联系电话")
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    @Schema(name="行业类型")
    @TableField("INDUSTRY_TYPE")
    private String industryType;

    @Schema(name="企业规模")
    @TableField("ENTERPRISE_SCALE")
    private String enterpriseScale;

    @Schema(name="状态(ACTIVE/INACTIVE)")
    @TableField("STATUS")
    private String status;

    @Schema(name="创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name="创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @Schema(name="更新人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @Schema(name="更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    // 扩展字段 - 不映射到数据库
    @Schema(name="行业类型名称")
    @TableField(exist = false)
    private String industryTypeName;

    @Schema(name="企业规模名称")
    @TableField(exist = false)
    private String enterpriseScaleName;

    @Schema(name="状态名称")
    @TableField(exist = false)
    private String statusName;

    @Schema(name="总资产(万元)")
    @TableField(exist = false)
    private BigDecimal totalAssets;

    @Schema(name="年营业收入(万元)")
    @TableField(exist = false)
    private BigDecimal annualRevenue;

    @Schema(name="净利润(万元)")
    @TableField(exist = false)
    private BigDecimal netProfit;

    @Schema(name="员工总数")
    @TableField(exist = false)
    private Integer employeeCount;

    @Schema(name="组织层级数")
    @TableField(exist = false)
    private Integer organizationLevels;
}
