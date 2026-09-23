package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 境外单位台账实体 - 境外单位穿透式监管
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_OVERSEAS_UNIT")
public class TblOverseasUnit {

    @TableId(value = "UNIT_ID", type = IdType.ASSIGN_UUID)
    private String unitId;

    @TableField("UNIT_NAME")
    private String unitName;

    @TableField("COUNTRY")
    private String country;

    @TableField("CITY")
    private String city;

    @TableField("REGISTERED_CAPITAL")
    private BigDecimal registeredCapital;

    @TableField("TOTAL_ASSETS")
    private BigDecimal totalAssets;

    @TableField("EMPLOYEE_COUNT")
    private Integer employeeCount;

    @TableField("BUSINESS_SCOPE")
    private String businessScope;

    @TableField("OPERATION_STATUS")
    private String operationStatus;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}

