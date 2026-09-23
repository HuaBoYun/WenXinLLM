package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应链合同管理实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_SUPPLY_CONTRACT")
public class GzctSupplyContract extends Model<GzctSupplyContract> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("CONTRACT_NO")
    private String contractNo;

    @TableField("CONTRACT_NAME")
    private String contractName;

    @TableField("SUPPLIER_ID")
    private String supplierId;

    @TableField("SUPPLIER_NAME")
    private String supplierName;

    @TableField("CONTRACT_TYPE")
    private String contractType;

    @TableField("CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    @TableField("SIGN_DATE")
    private LocalDate signDate;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("EXECUTION_PROGRESS")
    private Integer executionProgress;

    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

    @TableField("STATUS")
    private String status;

    @TableField("REMARK")
    private String remark;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
