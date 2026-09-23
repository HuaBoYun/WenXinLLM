package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产变动记录查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AssetChangeRecordQueryVO extends BaseVo {

    /**
     * 资产ID
     */
    private String assetId;

    /**
     * 资产编码
     */
    private String assetCode;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 所属企业ID
     */
    private String enterpriseId;

    /**
     * 所属企业名称
     */
    private String enterpriseName;

    /**
     * 变动类型
     */
    private String changeType;

    /**
     * 变动原因
     */
    private String changeReason;

    /**
     * 转让方式
     */
    private String transferMethod;

    /**
     * 处置方式
     */
    private String disposalMethod;

    /**
     * 审批状态
     */
    private String approvalStatus;

    /**
     * 预警级别
     */
    private String warningLevel;

    /**
     * 是否重大变动
     */
    private Boolean isMajorChange;

    /**
     * 是否需要预警
     */
    private Boolean needWarning;

    /**
     * 转出方
     */
    private String transferor;

    /**
     * 转入方
     */
    private String transferee;

    /**
     * 购置供应商
     */
    private String supplier;

    /**
     * 变动经办人
     */
    private String changeOperator;

    /**
     * 变动审核人
     */
    private String changeReviewer;

    /**
     * 变动批准人
     */
    private String changeApprover;

    /**
     * 审批机构
     */
    private String approvalAuthority;

    /**
     * 最小变动金额
     */
    private BigDecimal minChangeAmount;

    /**
     * 最大变动金额
     */
    private BigDecimal maxChangeAmount;

    /**
     * 最小转让价格
     */
    private BigDecimal minTransferPrice;

    /**
     * 最大转让价格
     */
    private BigDecimal maxTransferPrice;

    /**
     * 最小处置价格
     */
    private BigDecimal minDisposalPrice;

    /**
     * 最大处置价格
     */
    private BigDecimal maxDisposalPrice;

    /**
     * 变动开始日期
     */
    private String changeStartDate;

    /**
     * 变动结束日期
     */
    private String changeEndDate;

    /**
     * 审批开始日期
     */
    private String approvalStartDate;

    /**
     * 审批结束日期
     */
    private String approvalEndDate;

    /**
     * 生效开始日期
     */
    private String effectiveStartDate;

    /**
     * 生效结束日期
     */
    private String effectiveEndDate;

    /**
     * 登记开始日期
     */
    private String registrationStartDate;

    /**
     * 登记结束日期
     */
    private String registrationEndDate;

}
