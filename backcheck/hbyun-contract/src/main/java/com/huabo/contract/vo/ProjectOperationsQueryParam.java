package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目经营管理查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectOperationsQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 经营编号
     */
    private String operationsNo;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 经营类型(1:收入管理,2:成本管理,3:利润分析,4:现金流管理,5:风险控制,6:绩效评估)
     */
    private Integer operationsType;

    /**
     * 经营类型列表
     */
    private List<Integer> operationsTypeList;

    /**
     * 经营期间
     */
    private String operationsPeriod;

    /**
     * 合同金额最小值
     */
    private BigDecimal contractAmountMin;

    /**
     * 合同金额最大值
     */
    private BigDecimal contractAmountMax;

    /**
     * 预算收入最小值
     */
    private BigDecimal budgetRevenueMin;

    /**
     * 预算收入最大值
     */
    private BigDecimal budgetRevenueMax;

    /**
     * 实际收入最小值
     */
    private BigDecimal actualRevenueMin;

    /**
     * 实际收入最大值
     */
    private BigDecimal actualRevenueMax;

    /**
     * 预算成本最小值
     */
    private BigDecimal budgetCostMin;

    /**
     * 预算成本最大值
     */
    private BigDecimal budgetCostMax;

    /**
     * 实际成本最小值
     */
    private BigDecimal actualCostMin;

    /**
     * 实际成本最大值
     */
    private BigDecimal actualCostMax;

    /**
     * 利润率最小值
     */
    private BigDecimal profitRateMin;

    /**
     * 利润率最大值
     */
    private BigDecimal profitRateMax;

    /**
     * 风险等级(1:低,2:中,3:高,4:极高)
     */
    private Integer riskLevel;

    /**
     * 风险等级列表
     */
    private List<Integer> riskLevelList;

    /**
     * 经营状态(1:正常,2:预警,3:异常,4:停止)
     */
    private Integer operationsStatus;

    /**
     * 经营状态列表
     */
    private List<Integer> operationsStatusList;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 负责人姓名
     */
    private String managerName;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 创建时间开始
     */
    private Date createTimeStart;

    /**
     * 创建时间结束
     */
    private Date createTimeEnd;

    /**
     * 更新时间开始
     */
    private Date updateTimeStart;

    /**
     * 更新时间结束
     */
    private Date updateTimeEnd;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向(ASC/DESC)
     */
    private String orderDirection;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 组织ID
     */
    private Long organizationId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 数据权限用户ID
     */
    private Long dataPermissionUserId;

    /**
     * 数据权限部门ID列表
     */
    private List<Long> dataPermissionDeptIds;

    /**
     * 是否包含子部门
     */
    private Boolean includeSubDept;

    /**
     * 统计开始时间
     */
    private Date statisticsStartTime;

    /**
     * 统计结束时间
     */
    private Date statisticsEndTime;

    /**
     * 统计类型(1:按月,2:按季度,3:按年)
     */
    private Integer statisticsType;

    /**
     * 分组字段
     */
    private String groupBy;

    /**
     * 是否需要统计
     */
    private Boolean needStatistics;

    /**
     * 导出类型(1:Excel,2:PDF,3:Word)
     */
    private Integer exportType;

    /**
     * 导出字段列表
     */
    private List<String> exportFields;

    /**
     * 是否导出明细
     */
    private Boolean exportDetail;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 自定义查询条件
     */
    private String customCondition;

    /**
     * 扩展参数
     */
    private String extendParams;

    /**
     * 毛利率最小值
     */
    private BigDecimal grossProfitRateMin;

    /**
     * 毛利率最大值
     */
    private BigDecimal grossProfitRateMax;

    /**
     * 净利率最小值
     */
    private BigDecimal netProfitRateMin;

    /**
     * 净利率最大值
     */
    private BigDecimal netProfitRateMax;

    /**
     * 投资回报率最小值
     */
    private BigDecimal roiMin;

    /**
     * 投资回报率最大值
     */
    private BigDecimal roiMax;

    /**
     * 资产负债率最小值
     */
    private BigDecimal debtRatioMin;

    /**
     * 资产负债率最大值
     */
    private BigDecimal debtRatioMax;

    /**
     * 现金流最小值
     */
    private BigDecimal netCashFlowMin;

    /**
     * 现金流最大值
     */
    private BigDecimal netCashFlowMax;

    /**
     * 风险评分最小值
     */
    private BigDecimal riskScoreMin;

    /**
     * 风险评分最大值
     */
    private BigDecimal riskScoreMax;

    /**
     * 绩效评分最小值
     */
    private BigDecimal performanceScoreMin;

    /**
     * 绩效评分最大值
     */
    private BigDecimal performanceScoreMax;

    /**
     * 是否盈利(0:否,1:是)
     */
    private Integer isProfitable;

    /**
     * 是否亏损(0:否,1:是)
     */
    private Integer isLoss;

    /**
     * 是否高风险(0:否,1:是)
     */
    private Integer isHighRisk;

    /**
     * 是否需要预警(0:否,1:是)
     */
    private Integer needsWarning;

    /**
     * 分析报告
     */
    private String analysisReport;

    /**
     * 改进建议
     */
    private String improvementSuggestions;

    /**
     * 风险提示
     */
    private String riskWarnings;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 删除标志(0:未删除,1:已删除)
     */
    private Integer delFlag;
}
