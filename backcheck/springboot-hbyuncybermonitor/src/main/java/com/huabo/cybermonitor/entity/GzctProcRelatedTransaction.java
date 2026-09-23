package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_PROC_RELATED_TRANSACTION")
public class GzctProcRelatedTransaction extends Model<GzctProcRelatedTransaction> {
    @TableId(value = "TRANSACTION_ID", type = IdType.ASSIGN_UUID)
    private String transactionId;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("COUNTERPARTY")
    private String counterparty;
    @TableField("RELATION_TYPE")
    private String relationType;
    @TableField("TRANSACTION_TYPE")
    private String transactionType;
    @TableField("AMOUNT")
    private BigDecimal amount;
    @TableField("RATIO")
    private BigDecimal ratio;
    @TableField("IS_DISCLOSED")
    private String isDisclosed;
    @TableField("RISK_LEVEL")
    private String riskLevel;
    @TableField("REPORT_YEAR")
    private String reportYear;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    /** 组织路径 */
    @TableField("ORG_PATH")
    private String orgPath;
}
