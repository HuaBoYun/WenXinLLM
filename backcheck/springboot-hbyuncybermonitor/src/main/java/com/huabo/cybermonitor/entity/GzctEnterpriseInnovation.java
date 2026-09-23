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
@TableName("GZCT_ENTERPRISE_INNOVATION")
public class GzctEnterpriseInnovation extends Model<GzctEnterpriseInnovation> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("PROJECT_NAME")
    private String projectName;

    @TableField("INNOVATION_TYPE")
    private String innovationType;

    @TableField("RD_INVESTMENT")
    private BigDecimal rdInvestment;

    @TableField("PATENT_COUNT")
    private Integer patentCount;

    @TableField("ACHIEVEMENT_DESC")
    private String achievementDesc;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("STATUS")
    private String status;

    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
