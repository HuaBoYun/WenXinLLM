package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产信息查询参数VO
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AssetInfoQueryVO extends BaseVo {

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
     * 资产类型
     */
    private String assetType;

    /**
     * 资产分类
     */
    private String assetCategory;

    /**
     * 资产子类
     */
    private String assetSubcategory;

    /**
     * 资产性质
     */
    private String assetNature;

    /**
     * 资产状态
     */
    private String assetStatus;

    /**
     * 资产位置
     */
    private String assetLocation;

    /**
     * 资产地区
     */
    private String assetRegion;

    /**
     * 资产行业
     */
    private String assetIndustry;

    /**
     * 资产质量等级
     */
    private String assetQualityLevel;

    /**
     * 资产风险等级
     */
    private String assetRiskLevel;

    /**
     * 是否核心资产
     */
    private Boolean isCoreAsset;

    /**
     * 是否战略资产
     */
    private Boolean isStrategicAsset;

    /**
     * 是否闲置资产
     */
    private Boolean isIdleAsset;

    /**
     * 是否抵押质押
     */
    private Boolean isPledged;

    /**
     * 是否需要监管关注
     */
    private Boolean needRegulatoryAttention;

    /**
     * 资产管理人
     */
    private String assetManager;

    /**
     * 管理部门
     */
    private String managementDepartment;

    /**
     * 技术状态
     */
    private String technicalCondition;

    /**
     * 维护状况
     */
    private String maintenanceCondition;

    /**
     * 保险情况
     */
    private String insuranceStatus;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 产品型号
     */
    private String productModel;

    /**
     * 最小原值
     */
    private BigDecimal minOriginalValue;

    /**
     * 最大原值
     */
    private BigDecimal maxOriginalValue;

    /**
     * 最小净值
     */
    private BigDecimal minNetValue;

    /**
     * 最大净值
     */
    private BigDecimal maxNetValue;

    /**
     * 最小市值
     */
    private BigDecimal minMarketValue;

    /**
     * 最大市值
     */
    private BigDecimal maxMarketValue;

    /**
     * 最小收益率
     */
    private BigDecimal minReturnRate;

    /**
     * 最大收益率
     */
    private BigDecimal maxReturnRate;

    /**
     * 最小周转率
     */
    private BigDecimal minTurnoverRate;

    /**
     * 最大周转率
     */
    private BigDecimal maxTurnoverRate;

    /**
     * 最小利用率
     */
    private BigDecimal minUtilizationRate;

    /**
     * 最大利用率
     */
    private BigDecimal maxUtilizationRate;

    /**
     * 购置开始日期
     */
    private String acquisitionStartDate;

    /**
     * 购置结束日期
     */
    private String acquisitionEndDate;

    /**
     * 评估开始日期
     */
    private String assessmentStartDate;

    /**
     * 评估结束日期
     */
    private String assessmentEndDate;

    /**
     * 最后维护开始日期
     */
    private String lastMaintenanceStartDate;

    /**
     * 最后维护结束日期
     */
    private String lastMaintenanceEndDate;

    /**
     * 保险到期开始日期
     */
    private String insuranceExpiryStartDate;

    /**
     * 保险到期结束日期
     */
    private String insuranceExpiryEndDate;

}
