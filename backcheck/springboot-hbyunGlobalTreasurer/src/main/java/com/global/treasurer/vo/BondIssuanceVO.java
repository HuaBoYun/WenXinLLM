package com.global.treasurer.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 债券发行VO
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@Data
public class BondIssuanceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 发行ID */
    private Long issuanceId;

    /** 发行编号 */
    private String issuanceNo;

    /** 融资计划ID */
    private Long planId;

    /** 债券名称 */
    private String bondName;

    /** 债券代码 */
    private String bondCode;

    /** 债券类型 */
    private String bondType;

    /** 债券类型名称 */
    private String bondTypeName;

    /** 发行金额 */
    private BigDecimal issuanceAmount;

    /** 币种 */
    private String currencyCode;

    /** 币种名称 */
    private String currencyName;

    /** 面值 */
    private BigDecimal faceValue;

    /** 票面利率 */
    private BigDecimal couponRate;

    /** 债券期限 */
    private Integer bondTerm;

    /** 期限单位 */
    private String termUnit;

    /** 期限单位名称 */
    private String termUnitName;

    /** 付息频率 */
    private String paymentFrequency;

    /** 付息频率名称 */
    private String paymentFrequencyName;

    /** 主承销商 */
    private String underwriter;

    /** 受托管理人 */
    private String trustee;

    /** 评级机构 */
    private String ratingAgency;

    /** 信用评级 */
    private String creditRating;

    /** 上市交易所 */
    private String listingExchange;

    /** 发行状态 */
    private String issuanceStatus;

    /** 发行状态名称 */
    private String issuanceStatusName;

    /** 发行日期 */
    private Date issuanceDate;

    /** 到期日期 */
    private Date maturityDate;

    /** 付息日 */
    private Date interestPaymentDate;

    /** 兑付日 */
    private Date redemptionDate;

    /** 实际发行金额 */
    private BigDecimal actualIssuanceAmount;

    /** 发行成本 */
    private BigDecimal issueCost;

    /** 承销费用 */
    private BigDecimal underwritingFee;

    /** 公司ID */
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 创建人 */
    private Long createdBy;

    /** 创建人姓名 */
    private String createdByName;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private Long updatedBy;

    /** 更新人姓名 */
    private String updatedByName;

    /** 更新时间 */
    private Date updatedTime;

    /** 备注 */
    private String remark;
}

