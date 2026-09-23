package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 股权结构查询参数VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EquityStructureQueryVO extends BaseVo {

    /**
     * 股权结构ID
     */
    private String equityId;

    /**
     * 被投资企业ID
     */
    private String investeeEnterpriseId;

    /**
     * 被投资企业名称
     */
    private String investeeEnterpriseName;

    /**
     * 投资方企业ID
     */
    private String investorEnterpriseId;

    /**
     * 投资方企业名称
     */
    private String investorEnterpriseName;

    /**
     * 投资方类型
     */
    private String investorType;

    /**
     * 最小持股比例（%）
     */
    private BigDecimal minShareholdingRatio;

    /**
     * 最大持股比例（%）
     */
    private BigDecimal maxShareholdingRatio;

    /**
     * 投资方式
     */
    private String investmentMethod;

    /**
     * 股权性质
     */
    private String equityNature;

    /**
     * 是否控股
     */
    private Boolean isControlling;

    /**
     * 是否实际控制人
     */
    private Boolean isActualController;

    /**
     * 最小投资层级
     */
    private Integer minInvestmentLevel;

    /**
     * 最大投资层级
     */
    private Integer maxInvestmentLevel;

    /**
     * 上级投资方ID
     */
    private String parentInvestorId;

    /**
     * 股权状态
     */
    private String equityStatus;

    /**
     * 股权来源
     */
    private String equitySource;

    /**
     * 是否质押
     */
    private Boolean isPledged;

    /**
     * 最小质押比例（%）
     */
    private BigDecimal minPledgeRatio;

    /**
     * 最大质押比例（%）
     */
    private BigDecimal maxPledgeRatio;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向
     */
    private String sortDirection;
}
