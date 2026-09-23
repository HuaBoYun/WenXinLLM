package com.huabo.fxgl.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 企业信息展示对象
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@Schema(name="EnterpriseInfoVO", description="企业信息展示对象")
public class EnterpriseInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="企业基本信息")
    private EnterpriseBasicInfo enterpriseInfo;

    @Schema(name="关键指标")
    private KeyMetrics keyMetrics;

    @Schema(name="系统状态")
    private SystemStatus systemStatus;

    @Data
    @Schema(name="EnterpriseBasicInfo", description="企业基本信息")
    public static class EnterpriseBasicInfo implements Serializable {

        @Schema(name="企业ID")
        private String enterpriseId;

        @Schema(name="企业名称")
        private String enterpriseName;

        @Schema(name="企业编码")
        private String enterpriseCode;

        @Schema(name="统一社会信用代码")
        private String creditCode;

        @Schema(name="法定代表人")
        private String legalRepresentative;

        @Schema(name="注册资本(万元)")
        private BigDecimal registeredCapital;

        @Schema(name="成立日期")
        private Date establishmentDate;

        @Schema(name="行业类型")
        private String industryType;

        @Schema(name="行业类型名称")
        private String industryTypeName;

        @Schema(name="企业类型名称")
        private String enterpriseTypeName;

        @Schema(name="企业规模")
        private String enterpriseScale;

        @Schema(name="企业规模名称")
        private String enterpriseScaleName;

        @Schema(name="状态")
        private String status;

        @Schema(name="状态名称")
        private String statusName;

        @Schema(name="风险等级(1-低,2-中,3-高)")
        private String riskLevel;

        @Schema(name="员工总数")
        private Integer employeeCount;

        @Schema(name="子公司数量")
        private Integer subsidiaryCount;

        @Schema(name="总资产(万元)")
        private BigDecimal totalAssets;
    }

    @Data
    @Schema(name="KeyMetrics", description="关键指标")
    public static class KeyMetrics implements Serializable {
        
        @Schema(name="总资产(万元)")
        private BigDecimal totalAssets;

        @Schema(name="年营业收入(万元)")
        private BigDecimal annualRevenue;

        @Schema(name="净利润(万元)")
        private BigDecimal netProfit;

        @Schema(name="员工总数")
        private Integer employeeCount;

        @Schema(name="组织层级数")
        private Integer organizationLevels;
    }

    @Data
    @Schema(name="SystemStatus", description="系统状态")
    public static class SystemStatus implements Serializable {
        
        @Schema(name="最后更新时间")
        private String lastUpdateTime;

        @Schema(name="数据状态")
        private String dataStatus;

        @Schema(name="刷新间隔(秒)")
        private Integer refreshInterval;
    }
}
