package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备资源表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("equipment_resource")
public class EquipmentResource implements Serializable {

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
     * 设备编号
     */
    @TableField("equipment_no")
    private String equipmentNo;

    /**
     * 设备名称
     */
    @TableField("equipment_name")
    private String equipmentName;

    /**
     * 设备类型(1:施工设备,2:检测设备,3:办公设备,4:运输设备,5:其他)
     */
    @TableField("equipment_type")
    private Integer equipmentType;

    /**
     * 设备规格
     */
    @TableField("equipment_spec")
    private String equipmentSpec;

    /**
     * 设备型号
     */
    @TableField("equipment_model")
    private String equipmentModel;

    /**
     * 制造商
     */
    @TableField("manufacturer")
    private String manufacturer;

    /**
     * 需求数量
     */
    @TableField("required_quantity")
    private Integer requiredQuantity;

    /**
     * 已分配数量
     */
    @TableField("allocated_quantity")
    private Integer allocatedQuantity;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /**
     * 预算成本
     */
    @TableField("budgeted_cost")
    private BigDecimal budgetedCost;

    /**
     * 实际成本
     */
    @TableField("actual_cost")
    private BigDecimal actualCost;

    /**
     * 租赁方式(1:购买,2:租赁,3:自有,4:借用)
     */
    @TableField("lease_type")
    private Integer leaseType;

    /**
     * 租赁期限（天）
     */
    @TableField("lease_duration")
    private Integer leaseDuration;

    /**
     * 开始使用时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束使用时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 供应商ID
     */
    @TableField("supplier_id")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @TableField("supplier_name")
    private String supplierName;

    /**
     * 供应商联系人
     */
    @TableField("supplier_contact")
    private String supplierContact;

    /**
     * 供应商电话
     */
    @TableField("supplier_phone")
    private String supplierPhone;

    /**
     * 分配状态(1:待分配,2:已分配,3:使用中,4:已归还,5:已损坏,6:已报废)
     */
    @TableField("allocation_status")
    private Integer allocationStatus;

    /**
     * 设备状态(1:正常,2:维修中,3:故障,4:报废)
     */
    @TableField("equipment_status")
    private Integer equipmentStatus;

    /**
     * 使用地点
     */
    @TableField("usage_location")
    private String usageLocation;

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
     * 操作人员
     */
    @TableField("operators")
    private String operators;

    /**
     * 维护要求
     */
    @TableField("maintenance_requirements")
    private String maintenanceRequirements;

    /**
     * 安全要求
     */
    @TableField("safety_requirements")
    private String safetyRequirements;

    /**
     * 技术参数
     */
    @TableField("technical_parameters")
    private String technicalParameters;

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
     * 获取设备类型名称
     */
    public String getEquipmentTypeName() {
        if (equipmentType == null) {
            return "";
        }
        switch (equipmentType) {
            case 1:
                return "施工设备";
            case 2:
                return "检测设备";
            case 3:
                return "办公设备";
            case 4:
                return "运输设备";
            case 5:
                return "其他";
            default:
                return "未知";
        }
    }

    /**
     * 获取租赁方式名称
     */
    public String getLeaseTypeName() {
        if (leaseType == null) {
            return "";
        }
        switch (leaseType) {
            case 1:
                return "购买";
            case 2:
                return "租赁";
            case 3:
                return "自有";
            case 4:
                return "借用";
            default:
                return "未知";
        }
    }

    /**
     * 获取分配状态名称
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
                return "使用中";
            case 4:
                return "已归还";
            case 5:
                return "已损坏";
            case 6:
                return "已报废";
            default:
                return "未知";
        }
    }

    /**
     * 获取设备状态名称
     */
    public String getEquipmentStatusName() {
        if (equipmentStatus == null) {
            return "";
        }
        switch (equipmentStatus) {
            case 1:
                return "正常";
            case 2:
                return "维修中";
            case 3:
                return "故障";
            case 4:
                return "报废";
            default:
                return "未知";
        }
    }

    /**
     * 计算分配完成率
     */
    public BigDecimal getAllocationRate() {
        if (requiredQuantity == null || requiredQuantity == 0) {
            return BigDecimal.ZERO;
        }
        if (allocatedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(allocatedQuantity)
                .divide(BigDecimal.valueOf(requiredQuantity), 4, BigDecimal.ROUND_HALF_UP)
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
