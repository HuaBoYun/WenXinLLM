package com.huabo.fxgl.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 企业详细信息VO
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@Schema(name="EnterpriseDetailInfoVO", description="企业详细信息VO")
public class EnterpriseDetailInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="企业ID")
    private String enterpriseId;

    @Schema(name="企业名称")
    private String enterpriseName;

    @Schema(name="企业编码")
    private String enterpriseCode;

    @Schema(name="统一社会信用代码")
    private String unifiedSocialCreditCode;

    @Schema(name="法定代表人")
    private String legalRepresentative;

    @Schema(name="注册资本")
    private BigDecimal registeredCapital;

    @Schema(name="成立日期")
    private String establishmentDate;

    @Schema(name="企业状态")
    private String status;

    @Schema(name="企业状态名称")
    private String statusName;

    @Schema(name="经营范围")
    private String businessScope;

    @Schema(name="行业类型")
    private String industryType;

    @Schema(name="行业类型名称")
    private String industryTypeName;

    @Schema(name="企业规模")
    private String enterpriseScale;

    @Schema(name="企业规模名称")
    private String enterpriseScaleName;

    @Schema(name="注册地址")
    private String registeredAddress;

    @Schema(name="联系电话")
    private String contactPhone;

    @Schema(name="电子邮箱")
    private String email;

    @Schema(name="官方网站")
    private String website;

    @Schema(name="总资产")
    private BigDecimal totalAssets;

    @Schema(name="总资产文本")
    private String totalAssetsText;

    @Schema(name="营业收入")
    private BigDecimal totalRevenue;

    @Schema(name="营业收入文本")
    private String totalRevenueText;

    @Schema(name="上市状态")
    private String listingStatus;

    @Schema(name="上市交易所")
    private String listingExchange;

    @Schema(name="股票代码")
    private String stockCode;
}
