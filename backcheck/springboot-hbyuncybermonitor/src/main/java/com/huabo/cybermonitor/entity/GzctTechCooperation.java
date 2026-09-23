package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_TECH_COOPERATION")
public class GzctTechCooperation extends Model<GzctTechCooperation> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("COOPERATION_NO")
    private String cooperationNo;

    @TableField("COOPERATION_NAME")
    private String cooperationName;

    @TableField("PARTNER")
    private String partner;

    @TableField("PARTNER_TYPE")
    private String partnerType;

    @TableField("COOPERATION_TYPE")
    private String cooperationType;

    @TableField("MANAGER")
    private String manager;

    @TableField("INVESTMENT_AMOUNT")
    private BigDecimal investmentAmount;

    @TableField("EXPECTED_RETURN")
    private BigDecimal expectedReturn;

    @TableField("COOPERATION_PERIOD")
    private Integer cooperationPeriod;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("PROGRESS")
    private Integer progress;

    @TableField("STATUS")
    private String status;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("DESCRIPTION")
    private String description;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
