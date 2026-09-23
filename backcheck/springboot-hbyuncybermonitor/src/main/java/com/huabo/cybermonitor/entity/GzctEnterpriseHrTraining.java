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
@TableName("GZCT_ENTERPRISE_HR_TRAINING")
public class GzctEnterpriseHrTraining extends Model<GzctEnterpriseHrTraining> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("TRAINING_NO")
    private String trainingNo;

    @TableField("TRAINING_NAME")
    private String trainingName;

    @TableField("TRAINING_TYPE")
    private String trainingType;

    @TableField("START_DATE")
    private LocalDate startDate;

    @TableField("END_DATE")
    private LocalDate endDate;

    @TableField("TRAINER")
    private String trainer;

    @TableField("PARTICIPANT_COUNT")
    private Integer participantCount;

    @TableField("MAX_PARTICIPANTS")
    private Integer maxParticipants;

    @TableField("CURRENT_PARTICIPANTS")
    private Integer currentParticipants;

    @TableField("COMPLETION_RATE")
    private BigDecimal completionRate;

    @TableField("BUDGET_AMOUNT")
    private BigDecimal budgetAmount;

    @TableField("ACTUAL_AMOUNT")
    private BigDecimal actualAmount;

    @TableField("STATUS")
    private String status;

    @TableField("SCORE")
    private BigDecimal score;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
