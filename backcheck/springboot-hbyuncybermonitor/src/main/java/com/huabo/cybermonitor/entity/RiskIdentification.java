package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("GZCT_RISK_IDENTIFICATION")
public class RiskIdentification {
    @TableId(value = "RISK_IDENTIFICATION_ID", type = IdType.ASSIGN_UUID)
    private String riskIdentificationId;
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;
    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;
    @TableField("RISK_CODE")
    private String riskCode;
    @TableField("RISK_NAME")
    private String riskName;
    @TableField("RISK_TYPE")
    private String riskType;
    @TableField("RISK_LEVEL")
    private String riskLevel;
    @TableField("RISK_CATEGORY")
    private String riskCategory;
    @TableField("IDENTIFICATION_METHOD")
    private String identificationMethod;
    @TableField("IDENTIFIED_BY")
    private String identifiedBy;
    @TableField("IDENTIFICATION_DATE")
    private LocalDate identificationDate;
    @TableField("PROBABILITY")
    private BigDecimal probability;
    @TableField("IMPACT")
    private String impact;
    @TableField("IMPACT_SCORE")
    private BigDecimal impactScore;
    @TableField("RISK_SCORE")
    private BigDecimal riskScore;
    @TableField("RISK_DESC")
    private String riskDesc;
    @TableField("STATUS")
    private String status;
    @TableField("RISK_SOURCE")
    private String riskSource;
    @TableField("RISK_INDICATOR")
    private String riskIndicator;
    @TableField("RESPONSE_STRATEGY")
    private String responseStrategy;
    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;
    @TableField("RESPONSIBLE_DEPARTMENT")
    private String responsibleDepartment;
    @TableField("REMARKS")
    private String remarks;
    @TableField("CREATE_BY")
    private String createBy;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_BY")
    private String updateBy;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    @TableField("DELETED")
    @TableLogic
    private Boolean deleted;
    @TableField("VERSION")
    @Version
    private Long version;
}
