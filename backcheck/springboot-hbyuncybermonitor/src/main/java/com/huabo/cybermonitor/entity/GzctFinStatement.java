package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_FIN_STATEMENT")
public class GzctFinStatement {

    @TableId(value = "STATEMENT_ID", type = IdType.ASSIGN_UUID)
    private String statementId;

    @TableField("COMPANY_ID")
    private String companyId;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("PERIOD")
    private String period;

    @TableField("STATEMENT_TYPE")
    private String statementType;

    @TableField("TOTAL_ASSETS")
    private BigDecimal totalAssets;

    @TableField("TOTAL_LIABILITIES")
    private BigDecimal totalLiabilities;

    @TableField("NET_ASSETS")
    private BigDecimal netAssets;

    @TableField("REVENUE")
    private BigDecimal revenue;

    @TableField("NET_PROFIT")
    private BigDecimal netProfit;

    @TableField("OPERATING_CASHFLOW")
    private BigDecimal operatingCashflow;

    @TableField("CURRENT_ASSETS")
    private BigDecimal currentAssets;

    @TableField("CURRENT_LIABILITIES")
    private BigDecimal currentLiabilities;

    @TableField("AUDIT_STATUS")
    private String auditStatus;

    @TableField("INDUSTRY")
    private String industry;

    @TableField("FINANCIAL_STATUS")
    private String financialStatus;

    @TableField("ANALYSIS_NOTES")
    private String analysisNotes;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织路径(物化路径),格式:/根ID/子ID/孙ID/,用于穿透式查询 */
    @TableField("ORG_PATH")
    private String orgPath;
}

