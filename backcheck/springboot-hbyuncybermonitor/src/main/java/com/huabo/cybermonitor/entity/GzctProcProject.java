package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PROC_PROJECT")
public class GzctProcProject extends Model<GzctProcProject> {
    @TableId(value = "PROCUREMENT_ID", type = IdType.ASSIGN_UUID)
    private String procurementId;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("PROJECT_NAME")
    private String projectName;
    @TableField("PROCUREMENT_METHOD")
    private String procurementMethod;
    @TableField("PURCHASE_TYPE")
    private String purchaseType;
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;
    @TableField("WINNING_AMOUNT")
    private BigDecimal winningAmount;
    @TableField("WINNING_SUPPLIER")
    private String winningSupplier;
    @TableField("PROJECT_STATUS")
    private String projectStatus;
    @TableField("REMARK")
    private String remark;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
