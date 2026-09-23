package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业管理驾驶舱实体类
 * 
 * @author huabo
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("GZCT_ENTERPRISE_DASHBOARD")
public class EnterpriseDashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 驾驶舱ID
     */
    @TableId(value = "DASHBOARD_ID", type = IdType.ASSIGN_UUID)
    private String dashboardId;

    /**
     * 企业ID
     */
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    /**
     * 企业名称
     */
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    /**
     * 统计期间
     */
    @TableField("STATISTICS_PERIOD")
    private LocalDate statisticsPeriod;

    /**
     * 营业收入
     */
    @TableField("OPERATING_REVENUE")
    private BigDecimal operatingRevenue;

    /**
     * 净利润
     */
    @TableField("NET_PROFIT")
    private BigDecimal netProfit;

    /**
     * 总资产
     */
    @TableField("TOTAL_ASSETS")
    private BigDecimal totalAssets;

    /**
     * 净资产
     */
    @TableField("NET_ASSETS")
    private BigDecimal netAssets;

    /**
     * 资产负债率
     */
    @TableField("ASSET_LIABILITY_RATIO")
    private BigDecimal assetLiabilityRatio;

    /**
     * 净资产收益率
     */
    @TableField("ROE")
    private BigDecimal roe;

    /**
     * 总资产收益率
     */
    @TableField("ROA")
    private BigDecimal roa;

    /**
     * 营收增长率
     */
    @TableField("REVENUE_GROWTH_RATE")
    private BigDecimal revenueGrowthRate;

    /**
     * 利润增长率
     */
    @TableField("PROFIT_GROWTH_RATE")
    private BigDecimal profitGrowthRate;

    /**
     * 员工总数
     */
    @TableField("EMPLOYEE_COUNT")
    private Integer employeeCount;

    /**
     * 子公司数量
     */
    @TableField("SUBSIDIARY_COUNT")
    private Integer subsidiaryCount;

    /**
     * 业务板块数量
     */
    @TableField("BUSINESS_SEGMENT_COUNT")
    private Integer businessSegmentCount;

    /**
     * 风险等级
     */
    @TableField("RISK_LEVEL")
    private String riskLevel;

    /**
     * 风险评分
     */
    @TableField("RISK_SCORE")
    private BigDecimal riskScore;

    /**
     * 预警数量
     */
    @TableField("WARNING_COUNT")
    private Integer warningCount;

    /**
     * 合规评分
     */
    @TableField("COMPLIANCE_SCORE")
    private BigDecimal complianceScore;

    /**
     * 数据质量评分
     */
    @TableField("DATA_QUALITY_SCORE")
    private BigDecimal dataQualityScore;

    /**
     * 报送完成率
     */
    @TableField("SUBMISSION_COMPLETION_RATE")
    private BigDecimal submissionCompletionRate;

    /**
     * 经营状态
     */
    @TableField("OPERATING_STATUS")
    private String operatingStatus;

    /**
     * 发展趋势
     */
    @TableField("DEVELOPMENT_TREND")
    private String developmentTrend;

    /**
     * 主要业务描述
     */
    @TableField("MAIN_BUSINESS_DESC")
    private String mainBusinessDesc;

    /**
     * 地域分布
     */
    @TableField("GEOGRAPHICAL_DISTRIBUTION")
    private String geographicalDistribution;

    /**
     * 行业分类
     */
    @TableField("INDUSTRY_CLASSIFICATION")
    private String industryClassification;

    /**
     * 企业规模
     */
    @TableField("ENTERPRISE_SCALE")
    private String enterpriseScale;

    /**
     * 上市状态
     */
    @TableField("LISTING_STATUS")
    private String listingStatus;

    /**
     * 最后更新时间
     */
    @TableField("LAST_UPDATE_TIME")
    private LocalDateTime lastUpdateTime;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 数据状态
     */
    @TableField("DATA_STATUS")
    private String dataStatus;

    /**
     * 备注
     */
    @TableField("REMARKS")
    private String remarks;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 删除标志
     */
    @TableField("DEL_FLAG")
    private String delFlag;
}
