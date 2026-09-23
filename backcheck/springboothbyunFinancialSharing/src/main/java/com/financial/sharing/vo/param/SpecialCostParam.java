package com.financial.sharing.vo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 专项成本参数类
 * 
 * @author system
 * @date 2024-12-19
 */
public class SpecialCostParam {

    // ==================== 项目成本 ====================

    @Data
    @ApiModel("项目成本查询参数")
    public static class ProjectCostQuery {
        @ApiModelProperty("项目名称")
        private String projectName;

        @ApiModelProperty("项目类型")
        private String projectType;

        @ApiModelProperty("项目状态")
        private String status;

        @ApiModelProperty("开始日期")
        private String startDate;

        @ApiModelProperty("结束日期")
        private String endDate;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;
    }

    @Data
    @ApiModel("项目成本保存参数")
    public static class ProjectCostSave {
        @ApiModelProperty("项目ID")
        private Long id;

        @ApiModelProperty(value = "项目编码", required = true)
        @NotBlank(message = "项目编码不能为空")
        private String projectCode;

        @ApiModelProperty(value = "项目名称", required = true)
        @NotBlank(message = "项目名称不能为空")
        private String projectName;

        @ApiModelProperty(value = "项目类型", required = true)
        @NotBlank(message = "项目类型不能为空")
        private String projectType;

        @ApiModelProperty(value = "项目状态", required = true)
        @NotBlank(message = "项目状态不能为空")
        private String status;

        @ApiModelProperty(value = "预算金额", required = true)
        @NotNull(message = "预算金额不能为空")
        private BigDecimal budgetAmount;

        @ApiModelProperty("实际成本")
        private BigDecimal actualAmount;

        @ApiModelProperty("开始日期")
        private String startDate;

        @ApiModelProperty("结束日期")
        private String endDate;

        @ApiModelProperty("项目经理")
        private String projectManager;

        @ApiModelProperty("项目进度")
        private Integer progress;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;

        @ApiModelProperty("项目描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("成本构成明细")
        private List<CostBreakdownItem> costBreakdown;
    }

    // ==================== 作业成本 ====================

    @Data
    @ApiModel("作业成本查询参数")
    public static class ActivityCostQuery {
        @ApiModelProperty("作业名称")
        private String activityName;

        @ApiModelProperty("作业类型")
        private String activityType;

        @ApiModelProperty("成本动因")
        private String costDriver;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("开始日期")
        private String startDate;

        @ApiModelProperty("结束日期")
        private String endDate;
    }

    @Data
    @ApiModel("作业成本保存参数")
    public static class ActivityCostSave {
        @ApiModelProperty("作业ID")
        private Long id;

        @ApiModelProperty(value = "作业编码", required = true)
        @NotBlank(message = "作业编码不能为空")
        private String activityCode;

        @ApiModelProperty(value = "作业名称", required = true)
        @NotBlank(message = "作业名称不能为空")
        private String activityName;

        @ApiModelProperty(value = "作业类型", required = true)
        @NotBlank(message = "作业类型不能为空")
        private String activityType;

        @ApiModelProperty(value = "成本动因", required = true)
        @NotBlank(message = "成本动因不能为空")
        private String costDriver;

        @ApiModelProperty(value = "动因数量", required = true)
        @NotNull(message = "动因数量不能为空")
        private BigDecimal driverQuantity;

        @ApiModelProperty(value = "总成本", required = true)
        @NotNull(message = "总成本不能为空")
        private BigDecimal totalCost;

        @ApiModelProperty("单位成本")
        private BigDecimal unitCost;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("作业描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("成本构成明细")
        private List<CostItem> costItems;
    }

    @Data
    @ApiModel("作业成本分配参数")
    public static class ActivityAllocation {
        @ApiModelProperty("作业ID列表")
        private List<Long> activities;

        @ApiModelProperty("分配方式")
        private String allocationType;

        @ApiModelProperty("分配日期")
        private String allocationDate;

        @ApiModelProperty("分配明细")
        private List<AllocationItem> allocationItems;
    }

    // ==================== 质量成本 ====================

    @Data
    @ApiModel("质量成本查询参数")
    public static class QualityCostQuery {
        @ApiModelProperty("质量成本名称")
        private String qualityName;

        @ApiModelProperty("质量成本类型")
        private String qualityType;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("开始日期")
        private String startDate;

        @ApiModelProperty("结束日期")
        private String endDate;
    }

    @Data
    @ApiModel("质量成本保存参数")
    public static class QualityCostSave {
        @ApiModelProperty("质量成本ID")
        private Long id;

        @ApiModelProperty(value = "质量成本编码", required = true)
        @NotBlank(message = "质量成本编码不能为空")
        private String qualityCode;

        @ApiModelProperty(value = "质量成本名称", required = true)
        @NotBlank(message = "质量成本名称不能为空")
        private String qualityName;

        @ApiModelProperty(value = "质量成本类型", required = true)
        @NotBlank(message = "质量成本类型不能为空")
        private String qualityType;

        @ApiModelProperty(value = "成本金额", required = true)
        @NotNull(message = "成本金额不能为空")
        private BigDecimal costAmount;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;

        @ApiModelProperty("发生日期")
        private String occurDate;

        @ApiModelProperty("质量成本描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;
    }

    // ==================== 环境成本 ====================

    @Data
    @ApiModel("环境成本查询参数")
    public static class EnvironmentCostQuery {
        @ApiModelProperty("环境成本名称")
        private String environmentName;

        @ApiModelProperty("环境成本类型")
        private String environmentType;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("开始日期")
        private String startDate;

        @ApiModelProperty("结束日期")
        private String endDate;
    }

    @Data
    @ApiModel("环境成本保存参数")
    public static class EnvironmentCostSave {
        @ApiModelProperty("环境成本ID")
        private Long id;

        @ApiModelProperty(value = "环境成本编码", required = true)
        @NotBlank(message = "环境成本编码不能为空")
        private String environmentCode;

        @ApiModelProperty(value = "环境成本名称", required = true)
        @NotBlank(message = "环境成本名称不能为空")
        private String environmentName;

        @ApiModelProperty(value = "环境成本类型", required = true)
        @NotBlank(message = "环境成本类型不能为空")
        private String environmentType;

        @ApiModelProperty(value = "成本金额", required = true)
        @NotNull(message = "成本金额不能为空")
        private BigDecimal costAmount;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;

        @ApiModelProperty("发生日期")
        private String occurDate;

        @ApiModelProperty("环境效益")
        private BigDecimal environmentBenefit;

        @ApiModelProperty("环境成本描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;
    }

    // ==================== 研发成本 ====================

    @Data
    @ApiModel("研发成本查询参数")
    public static class RdCostQuery {
        @ApiModelProperty("研发项目名称")
        private String rdProjectName;

        @ApiModelProperty("研发类型")
        private String rdType;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("开始日期")
        private String startDate;

        @ApiModelProperty("结束日期")
        private String endDate;
    }

    @Data
    @ApiModel("研发成本保存参数")
    public static class RdCostSave {
        @ApiModelProperty("研发成本ID")
        private Long id;

        @ApiModelProperty(value = "研发项目编码", required = true)
        @NotBlank(message = "研发项目编码不能为空")
        private String rdProjectCode;

        @ApiModelProperty(value = "研发项目名称", required = true)
        @NotBlank(message = "研发项目名称不能为空")
        private String rdProjectName;

        @ApiModelProperty(value = "研发类型", required = true)
        @NotBlank(message = "研发类型不能为空")
        private String rdType;

        @ApiModelProperty(value = "研发成本", required = true)
        @NotNull(message = "研发成本不能为空")
        private BigDecimal rdCost;

        @ApiModelProperty("所属部门")
        private String department;

        @ApiModelProperty("成本中心")
        private String costCenter;

        @ApiModelProperty("研发开始日期")
        private String rdStartDate;

        @ApiModelProperty("研发结束日期")
        private String rdEndDate;

        @ApiModelProperty("预期收益")
        private BigDecimal expectedBenefit;

        @ApiModelProperty("研发描述")
        private String description;

        @ApiModelProperty("备注")
        private String remark;
    }

    // ==================== 专项分析 ====================

    @Data
    @ApiModel("专项分析查询参数")
    public static class AnalysisQuery {
        @ApiModelProperty("分析类型")
        private String analysisType;

        @ApiModelProperty("开始日期")
        private String startDate;

        @ApiModelProperty("结束日期")
        private String endDate;

        @ApiModelProperty("分析维度")
        private List<String> dimensions;

        @ApiModelProperty("成本类型")
        private List<String> costTypes;
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
    @ApiModel("分配项")
    public static class AllocationItem {
        @ApiModelProperty("分配对象")
        private String targetObject;

        @ApiModelProperty("分配数量")
        private BigDecimal allocatedQuantity;

        @ApiModelProperty("分配比例")
        private BigDecimal allocationRate;

        @ApiModelProperty("分配金额")
        private BigDecimal allocatedAmount;

        @ApiModelProperty("备注")
        private String remark;
    }
}
