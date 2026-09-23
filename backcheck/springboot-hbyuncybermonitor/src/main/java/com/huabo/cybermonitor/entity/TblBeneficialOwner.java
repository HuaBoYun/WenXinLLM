package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 受益所有人实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BENEFICIAL_OWNER")
public class TblBeneficialOwner {

    @TableId(value = "OWNER_ID", type = IdType.ASSIGN_UUID)
    private String ownerId;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("OWNER_NAME")
    private String ownerName;

    @TableField("OWNER_TYPE")
    private String ownerType;

    @TableField("ID_TYPE")
    private String idType;

    @TableField("ID_NUMBER")
    private String idNumber;

    @TableField("NATIONALITY")
    private String nationality;

    @TableField("BENEFICIAL_RATIO")
    private BigDecimal beneficialRatio;

    @TableField("CONTROL_METHOD")
    private String controlMethod;

    @TableField("IDENTIFICATION_PATH")
    private String identificationPath;

    @TableField("PENETRATION_LEVEL")
    private Integer penetrationLevel;

    @TableField("IS_PEP")
    private String isPep;

    @TableField("RISK_LEVEL")
    private String riskLevel;

    @TableField("VERIFICATION_STATUS")
    private String verificationStatus;

    @TableField("VERIFICATION_DATE")
    private LocalDate verificationDate;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
