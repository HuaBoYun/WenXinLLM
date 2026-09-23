package com.management.accountant.entity.ss;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 成本分摊实体类
 * 
 * @author AI Assistant
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ss_cost_allocation")
public class SsCostAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分摊ID
     */
    @TableId(value = "allocation_id", type = IdType.AUTO)
    private Long allocationId;

    /**
     * 分摊编码
     */
    @TableField("allocation_code")
    private String allocationCode;

    /**
     * 分摊名称
     */
    @TableField("allocation_name")
    private String allocationName;

    /**
     * 分摊描述
     */
    @TableField("allocation_description")
    private String allocationDescription;

    /**
     * 分摊类型：DIRECT-直接分摊, INDIRECT-间接分摊, STEP-阶梯分摊, RECIPROCAL-交互分摊, ACTIVITY-作业分摊, VALUE-价值分摊
     */
    @TableField("allocation_type")
    private String allocationType;

    /**
     * 分摊状态：DRAFT-草稿, ACTIVE-活跃, CALCULATING-计算中, COMPLETED-已完成, SUSPENDED-已暂停, CANCELLED-已取消
     */
    @TableField("allocation_status")
    private String allocationStatus;

    /**
     * 分摊优先级
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 成本中心ID
     */
    @TableField("cost_center_id")
    private Long costCenterId;

    /**
     * 成本中心编码
     */
    @TableField("cost_center_code")
    private String costCenterCode;

    /**
     * 成本中心名称
     */
    @TableField("cost_center_name")
    private String costCenterName;

    /**
     * 分摊基础：AMOUNT-金额基础, QUANTITY-数量基础, RATIO-比例基础, DRIVER-动因基础, WEIGHT-权重基础, FORMULA-公式基础
     */
    @TableField("allocation_basis")
    private String allocationBasis;

    /**
     * 分摊方法：EQUAL-平均分摊, WEIGHTED-加权分摊, PROPORTIONAL-比例分摊, ACTIVITY_BASED-作业成本分摊, STANDARD-标准分摊, ACTUAL-实际分摊
     */
    @TableField("allocation_method")
    private String allocationMethod;

    /**
     * 分摊规则
     */
    @TableField("allocation_rules")
    private String allocationRules;

    /**
     * 分摊公式
     */
    @TableField("allocation_formula")
    private String allocationFormula;

    /**
     * 分摊周期：DAILY-日, WEEKLY-周, MONTHLY-月, QUARTERLY-季, YEARLY-年, CUSTOM-自定义
     */
    @TableField("allocation_period")
    private String allocationPeriod;

    /**
     * 分摊开始时间
     */
    @TableField("allocation_start_time")
    private LocalDateTime allocationStartTime;

    /**
     * 分摊结束时间
     */
    @TableField("allocation_end_time")
    private LocalDateTime allocationEndTime;

    /**
     * 总成本金额
     */
    @TableField("total_cost_amount")
    private BigDecimal totalCostAmount;

    /**
     * 已分摊金额
     */
    @TableField("allocated_amount")
    private BigDecimal allocatedAmount;

    /**
     * 未分摊金额
     */
    @TableField("unallocated_amount")
    private BigDecimal unallocatedAmount;

    /**
     * 分摊比例
     */
    @TableField("allocation_percentage")
    private BigDecimal allocationPercentage;

    /**
     * 分摊权重
     */
    @TableField("allocation_weight")
    private BigDecimal allocationWeight;

    /**
     * 分摊驱动因子
     */
    @TableField("allocation_driver")
    private String allocationDriver;

    /**
     * 驱动因子数量
     */
    @TableField("driver_quantity")
    private BigDecimal driverQuantity;

    /**
     * 驱动因子单价
     */
    @TableField("driver_unit_cost")
    private BigDecimal driverUnitCost;

    /**
     * 分摊目标数量
     */
    @TableField("target_count")
    private Integer targetCount;

    /**
     * 分摊完成数量
     */
    @TableField("completed_count")
    private Integer completedCount;

    /**
     * 分摊成功率
     */
    @TableField("success_rate")
    private BigDecimal successRate;

    /**
     * 分摊精度
     */
    @TableField("allocation_precision")
    private Integer allocationPrecision;

    /**
     * 舍入规则：ROUND_UP-向上舍入, ROUND_DOWN-向下舍入, ROUND_HALF_UP-四舍五入, ROUND_HALF_DOWN-五舍六入
     */
    @TableField("rounding_rule")
    private String roundingRule;

    /**
     * 分摊维度
     */
    @TableField("allocation_dimensions")
    private String allocationDimensions;

    /**
     * 分摊层级
     */
    @TableField("allocation_level")
    private Integer allocationLevel;

    /**
     * 父分摊ID
     */
    @TableField("parent_allocation_id")
    private Long parentAllocationId;

    /**
     * 分摊路径
     */
    @TableField("allocation_path")
    private String allocationPath;

    /**
     * 分摊深度
     */
    @TableField("allocation_depth")
    private Integer allocationDepth;

    /**
     * 是否自动分摊
     */
    @TableField("is_auto_allocation")
    private Boolean isAutoAllocation;

    /**
     * 是否实时分摊
     */
    @TableField("is_realtime_allocation")
    private Boolean isRealtimeAllocation;

    /**
     * 是否启用审批
     */
    @TableField("is_approval_enabled")
    private Boolean isApprovalEnabled;

    /**
     * 审批状态：PENDING-待审批, APPROVED-已审批, REJECTED-已拒绝, CANCELLED-已取消
     */
    @TableField("approval_status")
    private String approvalStatus;

    /**
     * 审批人ID
     */
    @TableField("approver_id")
    private Long approverId;

    /**
     * 审批人姓名
     */
    @TableField("approver_name")
    private String approverName;

    /**
     * 审批时间
     */
    @TableField("approval_time")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @TableField("approval_comments")
    private String approvalComments;

    /**
     * 计算开始时间
     */
    @TableField("calculation_start_time")
    private LocalDateTime calculationStartTime;

    /**
     * 计算结束时间
     */
    @TableField("calculation_end_time")
    private LocalDateTime calculationEndTime;

    /**
     * 计算耗时(秒)
     */
    @TableField("calculation_duration")
    private Long calculationDuration;

    /**
     * 计算状态：PENDING-待计算, RUNNING-计算中, COMPLETED-已完成, FAILED-计算失败, CANCELLED-已取消
     */
    @TableField("calculation_status")
    private String calculationStatus;

    /**
     * 计算结果
     */
    @TableField("calculation_result")
    private String calculationResult;

    /**
     * 计算错误信息
     */
    @TableField("calculation_error")
    private String calculationError;

    /**
     * 分摊结果摘要
     */
    @TableField("allocation_summary")
    private String allocationSummary;

    /**
     * 分摊详细结果
     */
    @TableField("allocation_details")
    private String allocationDetails;

    /**
     * 分摊报告路径
     */
    @TableField("report_path")
    private String reportPath;

    /**
     * 分摊日志
     */
    @TableField("allocation_log")
    private String allocationLog;

    /**
     * 数据来源：MANUAL-手工录入, IMPORT-导入, INTERFACE-接口, CALCULATION-计算生成
     */
    @TableField("data_source")
    private String dataSource;

    /**
     * 数据版本
     */
    @TableField("data_version")
    private String dataVersion;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 扩展字段1
     */
    @TableField("ext_field1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("ext_field2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("ext_field3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("ext_field4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("ext_field5")
    private String extField5;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @TableField(value = "created_name", fill = FieldFill.INSERT)
    private String createdName;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 更新人姓名
     */
    @TableField(value = "updated_name", fill = FieldFill.INSERT_UPDATE)
    private String updatedName;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
}
