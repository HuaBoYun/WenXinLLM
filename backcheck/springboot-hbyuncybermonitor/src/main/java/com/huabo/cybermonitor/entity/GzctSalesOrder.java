package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SALES_ORDER")
public class GzctSalesOrder extends Model<GzctSalesOrder> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("ORDER_NO")
    private String orderNo;

    @TableField("CUSTOMER_NAME")
    private String customerName;

    @TableField("PRODUCT_NAME")
    private String productName;

    @TableField("QUANTITY")
    private BigDecimal quantity;

    @TableField("UNIT_PRICE")
    private BigDecimal unitPrice;

    @TableField("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @TableField("ORDER_DATE")
    private String orderDate;

    @TableField("DELIVERY_DATE")
    private String deliveryDate;

    @TableField("STATUS")
    private String status;

    @TableField("SALESPERSON")
    private String salesperson;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
