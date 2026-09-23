package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产信息实体类
 * 用于资产配置监管、资产质量评估、资产流向追踪、资产运营分析
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ASSET_INFO")
public class AssetInfo {

    /**
     * 资产ID（主键）
     */
    @TableId(value = "ASSET_ID", type = IdType.ASSIGN_UUID)
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
     * 资产原值
     */
    private BigDecimal originalValue;

    /**
     * 资产净值
     */
    private BigDecimal netValue;

    /**
     * 资产市值
     */
    private BigDecimal marketValue;

    /**
     * 累计折旧
     */
    private BigDecimal accumulatedDepreciation;

    /**
     * 减值准备
     */
    private BigDecimal impairmentProvision;

    /**
     * 资产账面价值
     */
    private BigDecimal bookValue;

    /**
     * 资产评估价值
     */
    private BigDecimal assessmentValue;

    /**
     * 评估基准日
     */
    private LocalDate assessmentDate;

    /**
     * 评估机构
     */
    private String assessmentInstitution;

    /**
     * 购置日期
     */
    private LocalDate acquisitionDate;

    /**
     * 购置价格
     */
    private BigDecimal acquisitionPrice;

    /**
     * 购置方式
     */
    private String acquisitionMethod;

    /**
     * 使用年限
     */
    private Integer usefulLife;

    /**
     * 已使用年限
     */
    private Integer usedYears;

    /**
     * 剩余使用年限
     */
    private Integer remainingLife;

    /**
     * 折旧方法
     */
    private String depreciationMethod;

    /**
     * 年折旧率
     */
    private BigDecimal annualDepreciationRate;

    /**
     * 月折旧额
     */
    private BigDecimal monthlyDepreciation;

    /**
     * 资产收益率
     */
    private BigDecimal assetReturnRate;

    /**
     * 资产周转率
     */
    private BigDecimal assetTurnoverRate;

    /**
     * 资产利用率
     */
    private BigDecimal assetUtilizationRate;

    /**
     * 资产产出效率
     */
    private BigDecimal assetOutputEfficiency;

    /**
     * 资产质量等级
     */
    private String assetQualityLevel;

    /**
     * 资产风险等级
     */
    private String assetRiskLevel;

    /**
     * 风险因素
     */
    private String riskFactors;

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
     * 抵押质押金额
     */
    private BigDecimal pledgeAmount;

    /**
     * 抵押质押机构
     */
    private String pledgeInstitution;

    /**
     * 抵押质押开始日期
     */
    private LocalDate pledgeStartDate;

    /**
     * 抵押质押结束日期
     */
    private LocalDate pledgeEndDate;

    /**
     * 是否需要监管关注
     */
    private Boolean needRegulatoryAttention;

    /**
     * 监管关注原因
     */
    private String regulatoryAttentionReason;

    /**
     * 资产管理人
     */
    private String assetManager;

    /**
     * 管理部门
     */
    private String managementDepartment;

    /**
     * 维护状况
     */
    private String maintenanceCondition;

    /**
     * 最后维护日期
     */
    private LocalDate lastMaintenanceDate;

    /**
     * 下次维护日期
     */
    private LocalDate nextMaintenanceDate;

    /**
     * 保险情况
     */
    private String insuranceStatus;

    /**
     * 保险金额
     */
    private BigDecimal insuranceAmount;

    /**
     * 保险公司
     */
    private String insuranceCompany;

    /**
     * 保险到期日期
     */
    private LocalDate insuranceExpiryDate;

    /**
     * 技术状态
     */
    private String technicalCondition;

    /**
     * 技术参数
     */
    private String technicalParameters;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 产品型号
     */
    private String productModel;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 数据更新时间
     */
    private LocalDateTime dataUpdateTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    // ==================== 常量定义 ====================

    /**
     * 资产类型常量
     */
    public static final String ASSET_TYPE_FIXED = "FIXED"; // 固定资产
    public static final String ASSET_TYPE_CURRENT = "CURRENT"; // 流动资产
    public static final String ASSET_TYPE_INTANGIBLE = "INTANGIBLE"; // 无形资产
    public static final String ASSET_TYPE_FINANCIAL = "FINANCIAL"; // 金融资产
    public static final String ASSET_TYPE_INVESTMENT = "INVESTMENT"; // 投资性资产
    public static final String ASSET_TYPE_BIOLOGICAL = "BIOLOGICAL"; // 生物资产

    /**
     * 资产分类常量
     */
    public static final String ASSET_CATEGORY_LAND = "LAND"; // 土地
    public static final String ASSET_CATEGORY_BUILDING = "BUILDING"; // 房屋建筑物
    public static final String ASSET_CATEGORY_EQUIPMENT = "EQUIPMENT"; // 机器设备
    public static final String ASSET_CATEGORY_VEHICLE = "VEHICLE"; // 运输工具
    public static final String ASSET_CATEGORY_ELECTRONIC = "ELECTRONIC"; // 电子设备
    public static final String ASSET_CATEGORY_FURNITURE = "FURNITURE"; // 办公家具
    public static final String ASSET_CATEGORY_PATENT = "PATENT"; // 专利权
    public static final String ASSET_CATEGORY_TRADEMARK = "TRADEMARK"; // 商标权
    public static final String ASSET_CATEGORY_COPYRIGHT = "COPYRIGHT"; // 著作权
    public static final String ASSET_CATEGORY_GOODWILL = "GOODWILL"; // 商誉

    /**
     * 资产性质常量
     */
    public static final String ASSET_NATURE_OWNED = "OWNED"; // 自有资产
    public static final String ASSET_NATURE_LEASED = "LEASED"; // 租赁资产
    public static final String ASSET_NATURE_SHARED = "SHARED"; // 共有资产
    public static final String ASSET_NATURE_MANAGED = "MANAGED"; // 代管资产
    public static final String ASSET_NATURE_TRUST = "TRUST"; // 信托资产

    /**
     * 资产状态常量
     */
    public static final String ASSET_STATUS_NORMAL = "NORMAL"; // 正常使用
    public static final String ASSET_STATUS_IDLE = "IDLE"; // 闲置
    public static final String ASSET_STATUS_MAINTENANCE = "MAINTENANCE"; // 维修中
    public static final String ASSET_STATUS_SCRAPPED = "SCRAPPED"; // 报废
    public static final String ASSET_STATUS_DISPOSED = "DISPOSED"; // 已处置
    public static final String ASSET_STATUS_PLEDGED = "PLEDGED"; // 抵押质押

    /**
     * 购置方式常量
     */
    public static final String ACQUISITION_METHOD_PURCHASE = "PURCHASE"; // 购买
    public static final String ACQUISITION_METHOD_CONSTRUCTION = "CONSTRUCTION"; // 自建
    public static final String ACQUISITION_METHOD_DONATION = "DONATION"; // 捐赠
    public static final String ACQUISITION_METHOD_TRANSFER = "TRANSFER"; // 划转
    public static final String ACQUISITION_METHOD_LEASE = "LEASE"; // 租赁
    public static final String ACQUISITION_METHOD_MERGER = "MERGER"; // 合并取得

    /**
     * 折旧方法常量
     */
    public static final String DEPRECIATION_METHOD_STRAIGHT_LINE = "STRAIGHT_LINE"; // 直线法
    public static final String DEPRECIATION_METHOD_DECLINING_BALANCE = "DECLINING_BALANCE"; // 余额递减法
    public static final String DEPRECIATION_METHOD_SUM_OF_YEARS = "SUM_OF_YEARS"; // 年数总和法
    public static final String DEPRECIATION_METHOD_UNITS_OF_PRODUCTION = "UNITS_OF_PRODUCTION"; // 工作量法

    /**
     * 资产质量等级常量
     */
    public static final String QUALITY_LEVEL_EXCELLENT = "EXCELLENT"; // 优秀
    public static final String QUALITY_LEVEL_GOOD = "GOOD"; // 良好
    public static final String QUALITY_LEVEL_AVERAGE = "AVERAGE"; // 一般
    public static final String QUALITY_LEVEL_POOR = "POOR"; // 较差
    public static final String QUALITY_LEVEL_BAD = "BAD"; // 很差

    /**
     * 资产风险等级常量
     */
    public static final String RISK_LEVEL_LOW = "LOW"; // 低风险
    public static final String RISK_LEVEL_MEDIUM = "MEDIUM"; // 中风险
    public static final String RISK_LEVEL_HIGH = "HIGH"; // 高风险
    public static final String RISK_LEVEL_CRITICAL = "CRITICAL"; // 极高风险

    /**
     * 技术状态常量
     */
    public static final String TECHNICAL_CONDITION_EXCELLENT = "EXCELLENT"; // 优秀
    public static final String TECHNICAL_CONDITION_GOOD = "GOOD"; // 良好
    public static final String TECHNICAL_CONDITION_AVERAGE = "AVERAGE"; // 一般
    public static final String TECHNICAL_CONDITION_POOR = "POOR"; // 较差
    public static final String TECHNICAL_CONDITION_OBSOLETE = "OBSOLETE"; // 过时

    /**
     * 维护状况常量
     */
    public static final String MAINTENANCE_CONDITION_EXCELLENT = "EXCELLENT"; // 优秀
    public static final String MAINTENANCE_CONDITION_GOOD = "GOOD"; // 良好
    public static final String MAINTENANCE_CONDITION_AVERAGE = "AVERAGE"; // 一般
    public static final String MAINTENANCE_CONDITION_POOR = "POOR"; // 较差
    public static final String MAINTENANCE_CONDITION_NEGLECTED = "NEGLECTED"; // 疏于维护

    /**
     * 保险状况常量
     */
    public static final String INSURANCE_STATUS_INSURED = "INSURED"; // 已投保
    public static final String INSURANCE_STATUS_UNINSURED = "UNINSURED"; // 未投保
    public static final String INSURANCE_STATUS_EXPIRED = "EXPIRED"; // 保险过期
    public static final String INSURANCE_STATUS_PARTIAL = "PARTIAL"; // 部分投保

}
