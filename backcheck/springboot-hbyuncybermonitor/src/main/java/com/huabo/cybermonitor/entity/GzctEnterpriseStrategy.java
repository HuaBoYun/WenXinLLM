package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_ENTERPRISE_STRATEGY")
public class GzctEnterpriseStrategy extends Model<GzctEnterpriseStrategy> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("STRATEGY_NAME")
    private String strategyName;

    @TableField("STRATEGY_TYPE")
    private String strategyType;

    @TableField("OBJECTIVE")
    private String objective;

    @TableField("KEY_MEASURES")
    private String keyMeasures;

    @TableField("START_YEAR")
    private String startYear;

    @TableField("END_YEAR")
    private String endYear;

    @TableField("PROGRESS_RATE")
    private BigDecimal progressRate;

    @TableField("STATUS")
    private String status;

    @TableField("RESPONSIBLE_PERSON")
    private String responsiblePerson;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
