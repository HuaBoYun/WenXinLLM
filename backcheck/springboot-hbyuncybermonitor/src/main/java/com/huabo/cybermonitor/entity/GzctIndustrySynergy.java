package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_INDUSTRY_SYNERGY")
public class GzctIndustrySynergy extends Model<GzctIndustrySynergy> {
    @TableId(value = "SYNERGY_ID", type = IdType.ASSIGN_UUID)
    private String synergyId;
    @TableField("SOURCE_COMPANY")
    private String sourceCompany;
    @TableField("TARGET_COMPANY")
    private String targetCompany;
    @TableField("INDUSTRY_CHAIN")
    private String industryChain;
    @TableField("SYNERGY_TYPE")
    private String synergyType;
    @TableField("SYNERGY_AMOUNT")
    private BigDecimal synergyAmount;
    @TableField("SYNERGY_SCORE")
    private BigDecimal synergyScore;
    @TableField("REPORT_YEAR")
    private String reportYear;
    @TableField("FROM_INDUSTRY")
    private String fromIndustry;
    @TableField("TO_INDUSTRY")
    private String toIndustry;
    @TableField("ENTERPRISE_COUNT")
    private Integer enterpriseCount;
    @TableField("ANNUAL_VALUE")
    private BigDecimal annualValue;
    @TableField("STRENGTH")
    private String strength;
    @TableField("MATURITY")
    private String maturity;
    @TableField("DESCRIPTION")
    private String description;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
