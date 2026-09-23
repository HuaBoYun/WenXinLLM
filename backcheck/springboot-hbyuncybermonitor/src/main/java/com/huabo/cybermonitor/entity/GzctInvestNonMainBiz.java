package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_TZ_NON_MAIN_BIZ")
public class GzctInvestNonMainBiz extends Model<GzctInvestNonMainBiz> {
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;
    @TableField("COMPANY_ID")
    private String companyId;
    @TableField("COMPANY_NAME")
    private String companyName;
    @TableField("PROJECT_NAME")
    private String projectName;
    @TableField("INVEST_AMOUNT")
    private BigDecimal investAmount;
    @TableField("MAIN_BIZ_AMOUNT")
    private BigDecimal mainBizAmount;
    @TableField("NON_MAIN_AMOUNT")
    private BigDecimal nonMainAmount;
    @TableField("NON_MAIN_RATIO")
    private BigDecimal nonMainRatio;
    @TableField("RISK_LEVEL")
    private String riskLevel;
    @TableField("REPORT_YEAR")
    private String reportYear;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /** 组织ID */
    @TableField("ORG_ID")
    private String orgId;
}
