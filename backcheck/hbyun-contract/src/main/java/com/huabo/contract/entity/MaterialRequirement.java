package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 材料需求表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("material_requirement")
public class MaterialRequirement implements Serializable {

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
     * 材料编号
     */
    @TableField("material_no")
    private String materialNo;

    /**
     * 材料名称
     */
    @TableField("material_name")
    private String materialName;

    /**
     * 材料类别
     */
    @TableField("material_category")
    private String materialCategory;

    /**
     * 材料规格
     */
    @TableField("material_specification")
    private String materialSpecification;

    /**
     * 材料型号
     */
    @TableField("material_model")
    private String materialModel;

    /**
     * 品牌
     */
    @TableField("brand")
    private String brand;

    /**
     * 需求数量
     */
    @TableField("required_quantity")
    private BigDecimal requiredQuantity;

    /**
     * 已采购数量
     */
    @TableField("purchased_quantity")
    private BigDecimal purchasedQuantity;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 质量等级
     */
    @TableField("quality_grade")
    private String qualityGrade;

    /**
     * 技术标准
     */
    @TableField("technical_standards")
    private String technicalStandards;

    /**
     * 计划使用日期
     */
    @TableField("planned_usage_date")
    private Date plannedUsageDate;

    /**
     * 采购方式(1:集中采购,2:分散采购,3:委托采购)
     */
    @TableField("procurement_method")
    private Integer procurementMethod;

    /**
     * 预估单价
     */
    @TableField("estimated_unit_price")
    private BigDecimal estimatedUnitPrice;

    /**
     * 预估总费用
     */
    @TableField("estimated_total_cost")
    private BigDecimal estimatedTotalCost;

    /**
     * 供应商要求
     */
    @TableField("supplier_requirements")
    private String supplierRequirements;

    /**
     * 交付地点
     */
    @TableField("delivery_location")
    private String deliveryLocation;

    /**
     * 储存要求
     */
    @TableField("storage_requirements")
    private String storageRequirements;

    /**
     * 使用要求
     */
    @TableField("usage_requirements")
    private String usageRequirements;

    /**
     * 采购状态(1:计划中,2:询价中,3:已采购,4:已到货,5:已验收)
     */
    @TableField("procurement_status")
    private Integer procurementStatus;

    /**
     * 实际数量
     */
    @TableField("actual_quantity")
    private BigDecimal actualQuantity;

    /**
     * 实际单价
     */
    @TableField("actual_unit_price")
    private BigDecimal actualUnitPrice;

    /**
     * 实际总费用
     */
    @TableField("actual_total_cost")
    private BigDecimal actualTotalCost;

    /**
     * 负责人ID
     */
    @TableField("responsible_person_id")
    private Long responsiblePersonId;



    /**
     * 采购时间
     */
    @TableField("purchase_time")
    private Date purchaseTime;

    /**
     * 到货时间
     */
    @TableField("delivery_time")
    private Date deliveryTime;

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
     * 质量要求
     */
    @TableField("quality_requirements")
    private String qualityRequirements;

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
     * 采购人员
     */
    @TableField("purchaser")
    private String purchaser;

    /**
     * 验收人员
     */
    @TableField("inspector")
    private String inspector;

    /**
     * 验收结果
     */
    @TableField("inspection_result")
    private String inspectionResult;

    /**
     * 优先级(1:低,2:中,3:高,4:紧急)
     */
    @TableField("priority")
    private Integer priority;

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
     * 获取材料类别名称
     */
    public String getMaterialCategoryName() {
        if (materialCategory == null || materialCategory.isEmpty()) {
            return "未分类";
        }
        return materialCategory;
    }

    /**
     * 获取采购状态名称
     */
    public String getProcurementStatusName() {
        if (procurementStatus == null) {
            return "";
        }
        switch (procurementStatus) {
            case 1:
                return "待采购";
            case 2:
                return "采购中";
            case 3:
                return "已到货";
            case 4:
                return "已验收";
            case 5:
                return "已入库";
            case 6:
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
     * 计算采购完成率
     */
    public BigDecimal getProcurementRate() {
        if (requiredQuantity == null || requiredQuantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (purchasedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return purchasedQuantity
                .divide(requiredQuantity, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 计算成本差异
     */
    public BigDecimal getCostVariance() {
        if (estimatedTotalCost == null) {
            return BigDecimal.ZERO;
        }
        if (actualTotalCost == null) {
            return estimatedTotalCost.negate();
        }
        return estimatedTotalCost.subtract(actualTotalCost);
    }

    /**
     * 判断是否超预算
     */
    public boolean isOverBudget() {
        if (estimatedTotalCost == null || actualTotalCost == null) {
            return false;
        }
        return actualTotalCost.compareTo(estimatedTotalCost) > 0;
    }

    /**
     * 判断是否需求紧急
     */
    public boolean isUrgent() {
        if (plannedUsageDate == null) {
            return false;
        }
        long diffTime = plannedUsageDate.getTime() - System.currentTimeMillis();
        long diffDays = diffTime / (24 * 60 * 60 * 1000);
        return diffDays <= 3 && diffDays >= 0;
    }
}
