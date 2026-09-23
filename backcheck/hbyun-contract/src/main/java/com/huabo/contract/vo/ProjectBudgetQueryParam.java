package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目预算查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectBudgetQueryParam implements Serializable {

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
     * 预算编号
     */
    private String budgetNo;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 预算名称
     */
    private String budgetName;

    /**
     * 预算类型(1:初步预算,2:详细预算,3:执行预算,4:调整预算)
     */
    private Integer budgetType;

    /**
     * 预算类型列表
     */
    private List<Integer> budgetTypeList;

    /**
     * 预算状态
     */
    private Integer budgetStatus;

    /**
     * 编制人ID
     */
    private Long budgeterId;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 批准人ID
     */
    private Long approverId;

    /**
     * 最小总预算
     */
    private BigDecimal minTotalBudget;

    /**
     * 最大总预算
     */
    private BigDecimal maxTotalBudget;

    /**
     * 预算期间开始日期
     */
    private Date budgetPeriodStart;

    /**
     * 预算期间结束日期
     */
    private Date budgetPeriodEnd;

    /**
     * 审批开始日期
     */
    private Date approvalStartDate;

    /**
     * 审批结束日期
     */
    private Date approvalEndDate;

    /**
     * 创建开始时间
     */
    private Date createStartTime;

    /**
     * 创建结束时间
     */
    private Date createEndTime;

    /**
     * 预算版本
     */
    private String budgetVersion;

    /**
     * 预算年度
     */
    private Integer budgetYear;

    /**
     * 预算年度开始
     */
    private Integer budgetYearStart;

    /**
     * 预算年度结束
     */
    private Integer budgetYearEnd;

    /**
     * 预算总金额最小值
     */
    private BigDecimal totalAmountMin;

    /**
     * 预算总金额最大值
     */
    private BigDecimal totalAmountMax;

    /**
     * 预算状态列表
     */
    private List<Integer> budgetStatusList;

    /**
     * 编制人ID
     */
    private Long compilerId;

    /**
     * 编制人姓名
     */
    private String compilerName;

    /**
     * 编制时间开始
     */
    private Date compileTimeStart;

    /**
     * 编制时间结束
     */
    private Date compileTimeEnd;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 审核时间开始
     */
    private Date reviewTimeStart;

    /**
     * 审核时间结束
     */
    private Date reviewTimeEnd;

    /**
     * 批准人姓名
     */
    private String approverName;

    /**
     * 批准时间开始
     */
    private Date approveTimeStart;

    /**
     * 批准时间结束
     */
    private Date approveTimeEnd;

    /**
     * 使用率最小值
     */
    private BigDecimal usageRateMin;

    /**
     * 使用率最大值
     */
    private BigDecimal usageRateMax;

    /**
     * 是否超预算(0:否,1:是)
     */
    private Integer isOverBudget;

    /**
     * 是否预算紧张(0:否,1:是)
     */
    private Integer isBudgetTight;

    /**
     * 是否大额预算(0:否,1:是)
     */
    private Integer isLargeBudget;

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
}
