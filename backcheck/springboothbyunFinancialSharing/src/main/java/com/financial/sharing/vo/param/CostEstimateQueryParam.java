package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 成本估算查询参数
 * 
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CostEstimateQueryParam", description = "成本估算查询参数")
public class CostEstimateQueryParam extends PageableParam {

    @ApiModelProperty(value = "估算ID")
    private Long estimateId;

    @ApiModelProperty(value = "估算编号")
    private String estimateNo;

    @ApiModelProperty(value = "估算编号模糊查询")
    private String estimateNoLike;

    @ApiModelProperty(value = "产品ID")
    private Long productId;

    @ApiModelProperty(value = "产品ID列表")
    private List<Long> productIds;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品名称模糊查询")
    private String productNameLike;

    @ApiModelProperty(value = "产品编码")
    private String productCode;

    @ApiModelProperty(value = "产品编码模糊查询")
    private String productCodeLike;

    @ApiModelProperty(value = "估算期间")
    private String estimatePeriod;

    @ApiModelProperty(value = "估算期间开始")
    private String estimatePeriodStart;

    @ApiModelProperty(value = "估算期间结束")
    private String estimatePeriodEnd;

    @ApiModelProperty(value = "估算期间列表")
    private List<String> estimatePeriods;

    @ApiModelProperty(value = "估算数量最小值")
    private BigDecimal estimateQuantityMin;

    @ApiModelProperty(value = "估算数量最大值")
    private BigDecimal estimateQuantityMax;

    @ApiModelProperty(value = "总估算成本最小值")
    private BigDecimal totalEstimatedCostMin;

    @ApiModelProperty(value = "总估算成本最大值")
    private BigDecimal totalEstimatedCostMax;

    @ApiModelProperty(value = "单位估算成本最小值")
    private BigDecimal unitEstimatedCostMin;

    @ApiModelProperty(value = "单位估算成本最大值")
    private BigDecimal unitEstimatedCostMax;

    @ApiModelProperty(value = "估算状态")
    private Integer estimateStatus;

    @ApiModelProperty(value = "估算状态列表")
    private List<Integer> estimateStatusList;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "账簿ID列表")
    private List<Long> bookIds;

    @ApiModelProperty(value = "账簿名称")
    private String bookName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "版本号列表")
    private List<Integer> versions;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "更新时间开始")
    private LocalDateTime updateTimeStart;

    @ApiModelProperty(value = "更新时间结束")
    private LocalDateTime updateTimeEnd;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建人列表")
    private List<Long> creators;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    @ApiModelProperty(value = "更新人列表")
    private List<Long> updaters;

    @ApiModelProperty(value = "更新人姓名")
    private String updaterName;

    // 扩展查询条件

    @ApiModelProperty(value = "估算方法")
    private String estimateMethod;

    @ApiModelProperty(value = "估算方法列表")
    private List<String> estimateMethods;

    @ApiModelProperty(value = "估算模型ID")
    private Long modelId;

    @ApiModelProperty(value = "估算模型ID列表")
    private List<Long> modelIds;

    @ApiModelProperty(value = "估算模型名称")
    private String modelName;

    @ApiModelProperty(value = "估算精度最小值")
    private BigDecimal accuracyMin;

    @ApiModelProperty(value = "估算精度最大值")
    private BigDecimal accuracyMax;

    @ApiModelProperty(value = "置信度最小值")
    private BigDecimal confidenceMin;

    @ApiModelProperty(value = "置信度最大值")
    private BigDecimal confidenceMax;

    @ApiModelProperty(value = "风险等级")
    private String riskLevel;

    @ApiModelProperty(value = "风险等级列表")
    private List<String> riskLevels;

    @ApiModelProperty(value = "审批状态")
    private Integer approvalStatus;

    @ApiModelProperty(value = "审批状态列表")
    private List<Integer> approvalStatusList;

    @ApiModelProperty(value = "审批人")
    private Long approver;

    @ApiModelProperty(value = "审批人列表")
    private List<Long> approvers;

    @ApiModelProperty(value = "审批人姓名")
    private String approverName;

    @ApiModelProperty(value = "审批时间开始")
    private LocalDateTime approvalTimeStart;

    @ApiModelProperty(value = "审批时间结束")
    private LocalDateTime approvalTimeEnd;

    @ApiModelProperty(value = "是否基准版本")
    private Boolean isBaseline;

    @ApiModelProperty(value = "基准版本ID")
    private Long baselineId;

    @ApiModelProperty(value = "基准版本ID列表")
    private List<Long> baselineIds;

    @ApiModelProperty(value = "父版本ID")
    private Long parentId;

    @ApiModelProperty(value = "父版本ID列表")
    private List<Long> parentIds;

    @ApiModelProperty(value = "估算场景")
    private String scenario;

    @ApiModelProperty(value = "估算场景列表")
    private List<String> scenarios;

    @ApiModelProperty(value = "数据来源")
    private String dataSource;

    @ApiModelProperty(value = "数据来源列表")
    private List<String> dataSources;

    @ApiModelProperty(value = "有效期开始")
    private LocalDateTime validFrom;

    @ApiModelProperty(value = "有效期结束")
    private LocalDateTime validTo;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "币种列表")
    private List<String> currencies;

    @ApiModelProperty(value = "成本中心ID")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心ID列表")
    private List<Long> costCenterIds;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目ID列表")
    private List<Long> projectIds;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "部门ID")
    private Long departmentId;

    @ApiModelProperty(value = "部门ID列表")
    private List<Long> departmentIds;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "业务类型列表")
    private List<String> businessTypes;

    @ApiModelProperty(value = "优先级")
    private Integer priority;

    @ApiModelProperty(value = "优先级列表")
    private List<Integer> priorities;

    @ApiModelProperty(value = "标签")
    private String tags;

    @ApiModelProperty(value = "标签列表")
    private List<String> tagsList;

    // 统计分析相关参数

    @ApiModelProperty(value = "分组字段")
    private String groupBy;

    @ApiModelProperty(value = "分组字段列表")
    private List<String> groupByFields;

    @ApiModelProperty(value = "排序字段")
    private String orderBy;

    @ApiModelProperty(value = "排序方向")
    private String orderDirection;

    @ApiModelProperty(value = "是否包含明细")
    private Boolean includeDetails;

    @ApiModelProperty(value = "是否包含历史版本")
    private Boolean includeHistory;

    @ApiModelProperty(value = "是否包含关联数据")
    private Boolean includeRelated;

    @ApiModelProperty(value = "导出格式")
    private String exportFormat;

    @ApiModelProperty(value = "导出字段")
    private List<String> exportFields;

    @ApiModelProperty(value = "查询类型")
    private String queryType;

    @ApiModelProperty(value = "自定义条件")
    private String customCondition;

    @ApiModelProperty(value = "扩展参数")
    private String extendParams;

    // 差异分析相关参数

    @ApiModelProperty(value = "对比基准ID")
    private Long compareBaselineId;

    @ApiModelProperty(value = "对比类型")
    private String compareType;

    @ApiModelProperty(value = "差异阈值")
    private BigDecimal varianceThreshold;

    @ApiModelProperty(value = "差异类型")
    private String varianceType;

    // 模拟分析相关参数

    @ApiModelProperty(value = "模拟场景")
    private String simulationScenario;

    @ApiModelProperty(value = "模拟参数")
    private String simulationParameters;

    @ApiModelProperty(value = "模拟次数")
    private Integer simulationCount;

    @ApiModelProperty(value = "置信区间")
    private BigDecimal confidenceInterval;

    // 预算对比相关参数

    @ApiModelProperty(value = "预算年度")
    private String budgetYear;

    @ApiModelProperty(value = "预算类型")
    private String budgetType;

    @ApiModelProperty(value = "预算版本")
    private String budgetVersion;

    @ApiModelProperty(value = "对比维度")
    private String comparisonDimension;
}
