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
@TableName("GZCT_PATENT")
public class GzctPatent extends Model<GzctPatent> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("PATENT_NO")
    private String patentNo;

    @TableField("PATENT_NAME")
    private String patentName;

    @TableField("PATENT_TYPE")
    private String patentType;

    @TableField("INVENTOR")
    private String inventor;

    @TableField("APPLICANT")
    private String applicant;

    @TableField("APPLICATION_DATE")
    private LocalDate applicationDate;

    @TableField("PUBLICATION_DATE")
    private LocalDate publicationDate;

    @TableField("AUTHORIZATION_DATE")
    private LocalDate authorizationDate;

    @TableField("VALIDITY_PERIOD")
    private Integer validityPeriod;

    @TableField("STATUS")
    private String status;

    @TableField("MAINTENANCE_FEE")
    private BigDecimal maintenanceFee;

    @TableField("COMMERCIAL_VALUE")
    private Integer commercialValue;

    @TableField("DESCRIPTION")
    private String description;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
