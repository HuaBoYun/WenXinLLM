package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PROC_BIDDING")
public class GzctProcBidding extends Model<GzctProcBidding> {
    @TableId(value = "BIDDING_ID", type = IdType.ASSIGN_UUID)
    private String biddingId;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("PROJECT_NAME")
    private String projectName;
    @TableField("BIDDING_TYPE")
    private String biddingType;
    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;
    @TableField("WIN_AMOUNT")
    private BigDecimal winAmount;
    @TableField("WIN_SUPPLIER")
    private String winSupplier;
    @TableField("IS_COMPLIANT")
    private String isCompliant;
    @TableField("VIOLATION_DESC")
    private String violationDesc;
    @TableField("BIDDING_DATE")
    private LocalDate biddingDate;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    /** 组织路径 */
    @TableField("ORG_PATH")
    private String orgPath;
}
