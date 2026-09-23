package com.financial.sharing.vo.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 专项成本结果类
 * 
 * @author system
 * @date 2024-12-19
 */
public class SpecialCostResult {

    // ==================== 项目成本 ====================

    @Data
    @ApiModel("项目成本列表项")
    public static class ProjectCost {
        @ApiModelProperty("项目ID")
        private Long id;

        @ApiModelProperty("项目编码")
        private String projectCode;

        @ApiModelProperty("项目名称")
        private String projectName;

        @ApiModelProperty("项目类型")
        private String projectType;

        @ApiModelProperty("项目状态")
        private String status;

        @ApiModelProperty("预算金额")
        private BigDecimal budgetAmount;

        @ApiModelProperty("实际成本")
        private BigDecimal actualAmount;

        @ApiModelProperty("预算差异")
        private BigDecimal variance;

        @ApiModelProperty("项目进度")
        private Integer progress;

        @ApiModelProperty("开始日期")
        private String startDate;

        @ApiModelProperty("结束日期")
        private String endDate;

        @ApiModelProperty("项目经理")
        private String projectManager;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;

        @ApiModelProperty("创建时间")
        private String createTime;
    }

    @Data
    @ApiModel("项目成本详情")
    public static class ProjectCostDetail extends ProjectCost {
        @ApiModelProperty("项目描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("成本构成明细")
        private List<CostBreakdownItem> costBreakdown;

        @ApiModelProperty("更新时间")
        private String updateTime;

        @ApiModelProperty("创建人")
        private String createBy;

        @ApiModelProperty("更新人")
        private String updateBy;
    }

    @Data
    @ApiModel("项目成本分析")
    public static class ProjectCostAnalysis {
        @ApiModelProperty("预算金额")
        private BigDecimal budgetAmount;

        @ApiModelProperty("实际成本")
        private BigDecimal actualAmount;

        @ApiModelProperty("预算差异")
        private BigDecimal variance;

        @ApiModelProperty("执行率")
        private BigDecimal executionRate;

        @ApiModelProperty("成本明细列表")
        private List<CostDetailItem> costDetailList;

        @ApiModelProperty("分析结论")
        private String conclusion;

        @ApiModelProperty("改进建议")
        private List<String> suggestions;
    }

    // ==================== 作业成本 ====================

    @Data
    @ApiModel("作业成本列表项")
    public static class ActivityCost {
        @ApiModelProperty("作业ID")
        private Long id;

        @ApiModelProperty("作业编码")
        private String activityCode;

        @ApiModelProperty("作业名称")
        private String activityName;

        @ApiModelProperty("作业类型")
        private String activityType;

        @ApiModelProperty("成本动因")
        private String costDriver;

        @ApiModelProperty("动因数量")
        private BigDecimal driverQuantity;

        @ApiModelProperty("总成本")
        private BigDecimal totalCost;

        @ApiModelProperty("单位成本")
        private BigDecimal unitCost;

        @ApiModelProperty("已分配成本")
        private BigDecimal allocatedCost;

        @ApiModelProperty("未分配成本")
        private BigDecimal unallocatedCost;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("创建时间")
        private String createTime;
    }

    @Data
    @ApiModel("作业成本详情")
    public static class ActivityCostDetail extends ActivityCost {
        @ApiModelProperty("成本中心名称")
        private String costCenterName;

        @ApiModelProperty("作业描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("成本构成明细")
        private List<CostItem> costItems;

        @ApiModelProperty("分配记录")
        private List<AllocationRecord> allocationRecords;

        @ApiModelProperty("更新时间")
        private String updateTime;

        @ApiModelProperty("创建人")
        private String createBy;

        @ApiModelProperty("更新人")
        private String updateBy;
    }

    // ==================== 质量成本 ====================

    @Data
    @ApiModel("质量成本列表项")
    public static class QualityCost {
        @ApiModelProperty("质量成本ID")
        private Long id;

        @ApiModelProperty("质量成本编码")
        private String qualityCode;

        @ApiModelProperty("质量成本名称")
        private String qualityName;

        @ApiModelProperty("质量成本类型")
        private String qualityType;

        @ApiModelProperty("成本金额")
        private BigDecimal costAmount;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;

        @ApiModelProperty("发生日期")
        private String occurDate;

        @ApiModelProperty("创建时间")
        private String createTime;
    }

    @Data
    @ApiModel("质量成本详情")
    public static class QualityCostDetail extends QualityCost {
        @ApiModelProperty("质量成本描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("更新时间")
        private String updateTime;

        @ApiModelProperty("创建人")
        private String createBy;

        @ApiModelProperty("更新人")
        private String updateBy;
    }

    // ==================== 环境成本 ====================

    @Data
    @ApiModel("环境成本列表项")
    public static class EnvironmentCost {
        @ApiModelProperty("环境成本ID")
        private Long id;

        @ApiModelProperty("环境成本编码")
        private String environmentCode;

        @ApiModelProperty("环境成本名称")
        private String environmentName;

        @ApiModelProperty("环境成本类型")
        private String environmentType;

        @ApiModelProperty("成本金额")
        private BigDecimal costAmount;

        @ApiModelProperty("环境效益")
        private BigDecimal environmentBenefit;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;

        @ApiModelProperty("发生日期")
        private String occurDate;

        @ApiModelProperty("创建时间")
        private String createTime;
    }

    @Data
    @ApiModel("环境成本详情")
    public static class EnvironmentCostDetail extends EnvironmentCost {
        @ApiModelProperty("环境成本描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("更新时间")
        private String updateTime;

        @ApiModelProperty("创建人")
        private String createBy;

        @ApiModelProperty("更新人")
        private String updateBy;
    }

    // ==================== 研发成本 ====================

    @Data
    @ApiModel("研发成本列表项")
    public static class RdCost {
        @ApiModelProperty("研发成本ID")
        private Long id;

        @ApiModelProperty("研发项目编码")
        private String rdProjectCode;

        @ApiModelProperty("研发项目名称")
        private String rdProjectName;

        @ApiModelProperty("研发类型")
        private String rdType;

        @ApiModelProperty("研发成本")
        private BigDecimal rdCost;

        @ApiModelProperty("预期收益")
        private BigDecimal expectedBenefit;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;

        @ApiModelProperty("研发开始日期")
        private String rdStartDate;

        @ApiModelProperty("研发结束日期")
        private String rdEndDate;

        @ApiModelProperty("创建时间")
        private String createTime;
    }

    @Data
    @ApiModel("研发成本详情")
    public static class RdCostDetail extends RdCost {
        @ApiModelProperty("研发描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("更新时间")
        private String updateTime;

        @ApiModelProperty("创建人")
        private String createBy;

        @ApiModelProperty("更新人")
        private String updateBy;
    }

    // ==================== 专项分析 ====================

    @Data
    @ApiModel("综合分析结果")
    public static class ComprehensiveAnalysis {
        @ApiModelProperty("总成本")
        private BigDecimal totalCost;

        @ApiModelProperty("项目成本")
        private BigDecimal projectCost;

        @ApiModelProperty("作业成本")
        private BigDecimal activityCost;

        @ApiModelProperty("质量成本")
        private BigDecimal qualityCost;

        @ApiModelProperty("环境成本")
        private BigDecimal environmentCost;

        @ApiModelProperty("研发成本")
        private BigDecimal rdCost;

        @ApiModelProperty("成本趋势数据")
        private List<TrendData> trendData;

        @ApiModelProperty("成本分布数据")
        private List<DistributionData> distributionData;

        @ApiModelProperty("分析结论")
        private String conclusion;

        @ApiModelProperty("建议措施")
        private List<String> recommendations;
    }

    @Data
    @ApiModel("对比分析结果")
    public static class CompareAnalysis {
        @ApiModelProperty("当期数据")
        private PeriodData currentPeriod;

        @ApiModelProperty("对比期数据")
        private PeriodData comparePeriod;

        @ApiModelProperty("变化分析")
        private List<ChangeAnalysis> changeAnalysis;

        @ApiModelProperty("对比结论")
        private String conclusion;
    }

    // ==================== 公共类 ====================

    @Data
    @ApiModel("成本构成明细")
    public static class CostBreakdownItem {
        @ApiModelProperty("成本类型")
        private String costType;

        @ApiModelProperty("预算金额")
        private BigDecimal budgetAmount;

        @ApiModelProperty("实际金额")
        private BigDecimal actualAmount;

        @ApiModelProperty("差异")
        private BigDecimal variance;

        @ApiModelProperty("差异率")
        private BigDecimal varianceRate;

        @ApiModelProperty("占比")
        private BigDecimal percentage;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("备注")
        private String remark;
    }

    @Data
    @ApiModel("成本明细项")
    public static class CostDetailItem {
        @ApiModelProperty("成本类型")
        private String costType;

        @ApiModelProperty("预算金额")
        private BigDecimal budgetAmount;

        @ApiModelProperty("实际成本")
        private BigDecimal actualAmount;

        @ApiModelProperty("差异")
        private BigDecimal variance;

        @ApiModelProperty("差异率")
        private BigDecimal varianceRate;

        @ApiModelProperty("占比")
        private BigDecimal percentage;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("备注")
        private String remark;
    }

    @Data
    @ApiModel("成本项")
    public static class CostItem {
        @ApiModelProperty("成本类型")
        private String costType;

        @ApiModelProperty("金额")
        private BigDecimal amount;

        @ApiModelProperty("备注")
        private String remark;
    }

    @Data
    @ApiModel("分配记录")
    public static class AllocationRecord {
        @ApiModelProperty("分配日期")
        private String allocationDate;

        @ApiModelProperty("分配对象")
        private String targetObject;

        @ApiModelProperty("分配数量")
        private BigDecimal allocatedQuantity;

        @ApiModelProperty("分配金额")
        private BigDecimal allocatedAmount;

        @ApiModelProperty("分配率")
        private BigDecimal allocationRate;

        @ApiModelProperty("操作人")
        private String operator;

        @ApiModelProperty("备注")
        private String remark;
    }

    @Data
    @ApiModel("趋势数据")
    public static class TrendData {
        @ApiModelProperty("日期")
        private String date;

        @ApiModelProperty("金额")
        private BigDecimal amount;

        @ApiModelProperty("类型")
        private String type;
    }

    @Data
    @ApiModel("分布数据")
    public static class DistributionData {
        @ApiModelProperty("名称")
        private String name;

        @ApiModelProperty("金额")
        private BigDecimal amount;

        @ApiModelProperty("占比")
        private BigDecimal percentage;
    }

    @Data
    @ApiModel("期间数据")
    public static class PeriodData {
        @ApiModelProperty("期间")
        private String period;

        @ApiModelProperty("总成本")
        private BigDecimal totalCost;

        @ApiModelProperty("项目成本")
        private BigDecimal projectCost;

        @ApiModelProperty("作业成本")
        private BigDecimal activityCost;

        @ApiModelProperty("质量成本")
        private BigDecimal qualityCost;

        @ApiModelProperty("环境成本")
        private BigDecimal environmentCost;

        @ApiModelProperty("研发成本")
        private BigDecimal rdCost;
    }

    @Data
    @ApiModel("变化分析")
    public static class ChangeAnalysis {
        @ApiModelProperty("指标名称")
        private String indicator;

        @ApiModelProperty("当期值")
        private BigDecimal currentValue;

        @ApiModelProperty("对比期值")
        private BigDecimal compareValue;

        @ApiModelProperty("变化金额")
        private BigDecimal changeAmount;

        @ApiModelProperty("变化率")
        private BigDecimal changeRate;

        @ApiModelProperty("变化趋势")
        private String changeTrend;
    }
}
