package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预算明细表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("budget_detail")
public class BudgetDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目预算ID
     */
    @TableField("budget_id")
    private Long budgetId;

    /**
     * 费用项目
     */
    @TableField("cost_item")
    private String costItem;

    /**
     * 费用描述
     */
    @TableField("cost_description")
    private String costDescription;

    /**
     * 费用类别(1:人工费,2:材料费,3:设备费,4:其他费用)
     */
    @TableField("cost_category")
    private Integer costCategory;

    /**
     * 单位
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
     * 小计
     */
    @TableField("subtotal")
    private BigDecimal subtotal;

    /**
     * 费用依据
     */
    @TableField("cost_basis")
    private String costBasis;

    /**
     * 计算方法
     */
    @TableField("calculation_method")
    private String calculationMethod;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 获取成本类别名称
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
                return "其他费用";
            default:
                return "未知";
        }
    }
}
