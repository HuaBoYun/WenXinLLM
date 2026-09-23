package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 人员配置表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("personnel_allocation")
public class PersonnelAllocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目策划ID
     */
    @TableField("planning_id")
    private Long planningId;

    /**
     * 配置编号
     */
    @TableField("allocation_no")
    private String allocationNo;

    /**
     * 岗位名称
     */
    @TableField("position_name")
    private String positionName;

    /**
     * 岗位类型(1:项目经理,2:技术负责人,3:质量负责人,4:安全负责人,5:普通员工,6:其他)
     */
    @TableField("position_type")
    private Integer positionType;

    /**
     * 需求人数
     */
    @TableField("required_count")
    private Integer requiredCount;

    /**
     * 已分配人数
     */
    @TableField("allocated_count")
    private Integer allocatedCount;

    /**
     * 技能要求
     */
    @TableField("skill_requirements")
    private String skillRequirements;

    /**
     * 经验要求
     */
    @TableField("experience_requirements")
    private String experienceRequirements;

    /**
     * 学历要求
     */
    @TableField("education_requirements")
    private String educationRequirements;

    /**
     * 证书要求
     */
    @TableField("certificate_requirements")
    private String certificateRequirements;

    /**
     * 预算工时
     */
    @TableField("budgeted_hours")
    private BigDecimal budgetedHours;

    /**
     * 单价（元/小时）
     */
    @TableField("hourly_rate")
    private BigDecimal hourlyRate;

    /**
     * 预算成本
     */
    @TableField("budgeted_cost")
    private BigDecimal budgetedCost;

    /**
     * 实际工时
     */
    @TableField("actual_hours")
    private BigDecimal actualHours;

    /**
     * 实际成本
     */
    @TableField("actual_cost")
    private BigDecimal actualCost;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 配置状态(1:待分配,2:已分配,3:执行中,4:已完成,5:已取消)
     */
    @TableField("allocation_status")
    private Integer allocationStatus;

    /**
     * 优先级(1:低,2:中,3:高,4:紧急)
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 负责人ID
     */
    @TableField("manager_id")
    private Long managerId;

    /**
     * 负责人姓名
     */
    @TableField("manager_name")
    private String managerName;

    /**
     * 分配人员列表（JSON格式）
     */
    @TableField("allocated_personnel")
    private String allocatedPersonnel;

    /**
     * 工作地点
     */
    @TableField("work_location")
    private String workLocation;

    /**
     * 工作内容
     */
    @TableField("work_content")
    private String workContent;

    /**
     * 绩效考核标准
     */
    @TableField("performance_criteria")
    private String performanceCriteria;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 获取岗位类型名称
     */
    public String getPositionTypeName() {
        if (positionType == null) {
            return "";
        }
        switch (positionType) {
            case 1:
                return "项目经理";
            case 2:
                return "技术负责人";
            case 3:
                return "质量负责人";
            case 4:
                return "安全负责人";
            case 5:
                return "普通员工";
            case 6:
                return "其他";
            default:
                return "未知";
        }
    }

    /**
     * 获取配置状态名称
     */
    public String getAllocationStatusName() {
        if (allocationStatus == null) {
            return "";
        }
        switch (allocationStatus) {
            case 1:
                return "待分配";
            case 2:
                return "已分配";
            case 3:
                return "执行中";
            case 4:
                return "已完成";
            case 5:
                return "已取消";
            default:
                return "未知";
        }
    }

    /**
     * 获取优先级名称
     */
    public String getPriorityName() {
        if (priority == null) {
            return "";
        }
        switch (priority) {
            case 1:
                return "低";
            case 2:
                return "中";
            case 3:
                return "高";
            case 4:
                return "紧急";
            default:
                return "未知";
        }
    }

    /**
     * 计算分配完成率
     */
    public BigDecimal getAllocationRate() {
        if (requiredCount == null || requiredCount == 0) {
            return BigDecimal.ZERO;
        }
        if (allocatedCount == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(allocatedCount)
                .divide(BigDecimal.valueOf(requiredCount), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 计算成本差异
     */
    public BigDecimal getCostVariance() {
        if (budgetedCost == null) {
            return BigDecimal.ZERO;
        }
        if (actualCost == null) {
            return budgetedCost.negate();
        }
        return budgetedCost.subtract(actualCost);
    }

    /**
     * 判断是否超预算
     */
    public boolean isOverBudget() {
        if (budgetedCost == null || actualCost == null) {
            return false;
        }
        return actualCost.compareTo(budgetedCost) > 0;
    }
}
