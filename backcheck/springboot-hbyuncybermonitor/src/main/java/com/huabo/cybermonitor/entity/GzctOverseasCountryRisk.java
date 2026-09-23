package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.math.BigDecimal;
import java.time.LocalDateTime;;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_OVERSEAS_COUNTRY_RISK")
public class GzctOverseasCountryRisk extends Model<GzctOverseasCountryRisk> {
    @TableId(value = "RISK_ID", type = IdType.ASSIGN_UUID) private String riskId;
    @TableField("COUNTRY") private String country;
    @TableField("COUNTRY_CODE") private String countryCode;
    @TableField("RISK_SCORE") private BigDecimal riskScore;
    @TableField("RISK_LEVEL") private String riskLevel;
    @TableField("POLITICAL_RISK") private BigDecimal politicalRisk;
    @TableField("ECONOMIC_RISK") private BigDecimal economicRisk;
    @TableField("LEGAL_RISK") private BigDecimal legalRisk;
    @TableField("SECURITY_RISK") private BigDecimal securityRisk;
    @TableField("INVEST_UNIT_COUNT") private Integer investUnitCount;
    @TableField("INVEST_AMOUNT") private BigDecimal investAmount;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
