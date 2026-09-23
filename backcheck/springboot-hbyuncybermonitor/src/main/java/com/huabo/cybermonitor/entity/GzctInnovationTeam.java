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
@TableName("GZCT_INNOVATION_TEAM")
public class GzctInnovationTeam extends Model<GzctInnovationTeam> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("TEAM_NO")
    private String teamNo;

    @TableField("TEAM_NAME")
    private String teamName;

    @TableField("RESEARCH_DIRECTION")
    private String researchDirection;

    @TableField("LEADER")
    private String leader;

    @TableField("MEMBER_COUNT")
    private Integer memberCount;

    @TableField("SENIOR_COUNT")
    private Integer seniorCount;

    @TableField("AVERAGE_AGE")
    private Integer averageAge;

    @TableField("ESTABLISH_DATE")
    private LocalDate establishDate;

    @TableField("CURRENT_PROJECTS")
    private Integer currentProjects;

    @TableField("COMPLETED_PROJECTS")
    private Integer completedProjects;

    @TableField("TEAM_EFFICIENCY")
    private Integer teamEfficiency;

    @TableField("INNOVATION_SCORE")
    private Integer innovationScore;

    @TableField("STATUS")
    private String status;

    @TableField("BUDGET")
    private BigDecimal budget;

    @TableField("DESCRIPTION")
    private String description;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
