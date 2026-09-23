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
@TableName("GZCT_PROC_PURCHASE_RECORD")
public class GzctProcPurchaseRecord extends Model<GzctProcPurchaseRecord> {
    @TableId(value = "RECORD_ID", type = IdType.ASSIGN_UUID)
    private String recordId;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("PROJECT_NAME")
    private String projectName;
    @TableField("PURCHASE_TYPE")
    private String purchaseType;
    @TableField("CATEGORY")
    private String category;
    @TableField("AMOUNT")
    private BigDecimal amount;
    @TableField("SUPPLIER_NAME")
    private String supplierName;
    @TableField("CONTRACT_NO")
    private String contractNo;
    @TableField("PURCHASE_DATE")
    private LocalDate purchaseDate;
    @TableField("STATUS")
    private String status;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
