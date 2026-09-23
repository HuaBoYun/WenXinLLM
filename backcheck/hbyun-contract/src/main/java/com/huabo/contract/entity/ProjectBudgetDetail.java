package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目预算明细表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_budget_detail")
public class ProjectBudgetDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 预算ID
     */
    @TableField("budget_id")
    private Long budgetId;

    /**
     * 预算编号
     */
    @TableField("budget_no")
    private String budgetNo;

    /**
     * 明细编号
     */
    @TableField("detail_no")
    private String detailNo;

    /**
     * 费用类别(1:人工费,2:材料费,3:设备费,4:管理费,5:其他费用,6:税费,7:利润)
     */
    @TableField("cost_category")
    private Integer costCategory;

    /**
     * 费用项目
     */
    @TableField("cost_item")
    private String costItem;

    /**
     * 费用名称
     */
    @TableField("cost_name")
    private String costName;

    /**
     * 费用描述
     */
    @TableField("cost_description")
    private String costDescription;

    /**
     * 计量单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 数量
     */
    @TableField("quantity")
    private BigDecimal quantity;

    /**
     * 单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 预算月份
     */
    @TableField("budget_month")
    private Integer budgetMonth;

    /**
     * 预算季度
     */
    @TableField("budget_quarter")
    private Integer budgetQuarter;

    /**
     * 已使用金额
     */
    @TableField("used_amount")
    private BigDecimal usedAmount;

    /**
     * 剩余金额
     */
    @TableField("remaining_amount")
    private BigDecimal remainingAmount;

    /**
     * 使用率(%)
     */
    @TableField("usage_rate")
    private BigDecimal usageRate;

    /**
     * 是否关键项目(0:否,1:是)
     */
    @TableField("is_key_item")
    private Integer isKeyItem;

    /**
     * 是否必需项目(0:否,1:是)
     */
    @TableField("is_required")
    private Integer isRequired;

    /**
     * 优先级(1:高,2:中,3:低)
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 负责人ID
     */
    @TableField("responsible_person_id")
    private Long responsiblePersonId;

    /**
     * 负责人姓名
     */
    @TableField("responsible_person_name")
    private String responsiblePersonName;

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
     * 计划使用时间
     */
    @TableField("planned_use_date")
    private Date plannedUseDate;

    /**
     * 实际使用时间
     */
    @TableField("actual_use_date")
    private Date actualUseDate;

    /**
     * 明细状态(1:待使用,2:使用中,3:已完成,4:已取消)
     */
    @TableField("detail_status")
    private Integer detailStatus;

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
     * 获取费用类别名称
     */
    public String getCostCategoryName() {
        if (costCategory == null) {
            return "";
        }
        switch (costCategory) {
            case 1:
                return "人工费";
            case 2:
                return "材料费";
            case 3:
                return "设备费";
            case 4:
                return "管理费";
            case 5:
                return "其他费用";
            case 6:
                return "税费";
            case 7:
                return "利润";
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
                return "高";
            case 2:
                return "中";
            case 3:
                return "低";
            default:
                return "未知";
        }
    }

    /**
     * 获取明细状态名称
     */
    public String getDetailStatusName() {
        if (detailStatus == null) {
            return "";
        }
        switch (detailStatus) {
            case 1:
                return "待使用";
            case 2:
                return "使用中";
            case 3:
                return "已完成";
            case 4:
                return "已取消";
            default:
                return "未知";
        }
    }

    /**
     * 获取费用类别颜色
     */
    public String getCostCategoryColor() {
        if (costCategory == null) {
            return "#909399";
        }
        switch (costCategory) {
            case 1:
                return "#409EFF"; // 蓝色
            case 2:
                return "#67C23A"; // 绿色
            case 3:
                return "#E6A23C"; // 橙色
            case 4:
                return "#F56C6C"; // 红色
            case 5:
                return "#909399"; // 灰色
            case 6:
                return "#F56C6C"; // 红色
            case 7:
                return "#67C23A"; // 绿色
            default:
                return "#909399";
        }
    }

    /**
     * 获取优先级颜色
     */
    public String getPriorityColor() {
        if (priority == null) {
            return "#909399";
        }
        switch (priority) {
            case 1:
                return "#F56C6C"; // 红色
            case 2:
                return "#E6A23C"; // 橙色
            case 3:
                return "#67C23A"; // 绿色
            default:
                return "#909399";
        }
    }

    /**
     * 获取明细状态颜色
     */
    public String getDetailStatusColor() {
        if (detailStatus == null) {
            return "#909399";
        }
        switch (detailStatus) {
            case 1:
                return "#909399"; // 灰色
            case 2:
                return "#E6A23C"; // 橙色
            case 3:
                return "#67C23A"; // 绿色
            case 4:
                return "#F56C6C"; // 红色
            default:
                return "#909399";
        }
    }

    /**
     * 判断是否为关键项目
     */
    public boolean isKeyItem() {
        return isKeyItem != null && isKeyItem == 1;
    }

    /**
     * 判断是否为必需项目
     */
    public boolean isRequired() {
        return isRequired != null && isRequired == 1;
    }

    /**
     * 判断是否为高优先级
     */
    public boolean isHighPriority() {
        return priority != null && priority == 1;
    }

    /**
     * 判断是否使用中
     */
    public boolean isInUse() {
        return detailStatus != null && detailStatus == 2;
    }

    /**
     * 判断是否已完成
     */
    public boolean isCompleted() {
        return detailStatus != null && detailStatus == 3;
    }

    /**
     * 判断是否已取消
     */
    public boolean isCancelled() {
        return detailStatus != null && detailStatus == 4;
    }

    /**
     * 获取金额显示文本
     */
    public String getAmountText() {
        if (amount == null) {
            return "0元";
        }
        if (amount.compareTo(new BigDecimal("10000")) >= 0) {
            return amount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return amount.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 获取已使用金额显示文本
     */
    public String getUsedAmountText() {
        if (usedAmount == null) {
            return "0元";
        }
        if (usedAmount.compareTo(new BigDecimal("10000")) >= 0) {
            return usedAmount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return usedAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 获取剩余金额显示文本
     */
    public String getRemainingAmountText() {
        if (remainingAmount == null) {
            return "未知";
        }
        if (remainingAmount.compareTo(new BigDecimal("10000")) >= 0) {
            return remainingAmount.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP) + "万元";
        } else {
            return remainingAmount.setScale(2, BigDecimal.ROUND_HALF_UP) + "元";
        }
    }

    /**
     * 获取使用率显示文本
     */
    public String getUsageRateText() {
        if (usageRate == null) {
            return "0%";
        }
        return usageRate.setScale(1, BigDecimal.ROUND_HALF_UP) + "%";
    }

    /**
     * 计算金额
     */
    public BigDecimal calculateAmount() {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(unitPrice);
    }

    /**
     * 计算剩余金额
     */
    public BigDecimal calculateRemainingAmount() {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        if (usedAmount == null) {
            return amount;
        }
        return amount.subtract(usedAmount);
    }

    /**
     * 计算使用率
     */
    public BigDecimal calculateUsageRate() {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (usedAmount == null) {
            return BigDecimal.ZERO;
        }
        return usedAmount.divide(amount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
    }

    /**
     * 判断是否超预算
     */
    public boolean isOverBudget() {
        if (amount == null || usedAmount == null) {
            return false;
        }
        return usedAmount.compareTo(amount) > 0;
    }

    /**
     * 判断是否预算紧张（使用率>=80%）
     */
    public boolean isBudgetTight() {
        BigDecimal rate = calculateUsageRate();
        return rate.compareTo(new BigDecimal("80")) >= 0;
    }

    /**
     * 更新使用情况
     */
    public void updateUsage() {
        this.amount = calculateAmount();
        this.remainingAmount = calculateRemainingAmount();
        this.usageRate = calculateUsageRate();
    }
}
