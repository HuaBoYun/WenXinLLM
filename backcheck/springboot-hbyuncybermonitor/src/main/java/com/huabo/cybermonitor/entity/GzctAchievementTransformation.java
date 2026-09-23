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
@TableName("GZCT_ACHIEVEMENT_TRANSFORMATION")
public class GzctAchievementTransformation extends Model<GzctAchievementTransformation> {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @TableField("ENTERPRISE_NAME")
    private String enterpriseName;

    @TableField("ACHIEVEMENT_NO")
    private String achievementNo;

    @TableField("ACHIEVEMENT_NAME")
    private String achievementName;

    @TableField("ACHIEVEMENT_TYPE")
    private String achievementType;

    @TableField("RESEARCH_TEAM")
    private String researchTeam;

    @TableField("MANAGER")
    private String manager;

    @TableField("TECHNOLOGY_LEVEL")
    private Integer technologyLevel;

    @TableField("MARKET_POTENTIAL")
    private String marketPotential;

    @TableField("INVESTMENT_AMOUNT")
    private BigDecimal investmentAmount;

    @TableField("EXPECTED_REVENUE")
    private BigDecimal expectedRevenue;

    @TableField("TRANSFORMATION_PROGRESS")
    private Integer transformationProgress;

    @TableField("COMPLETION_DATE")
    private LocalDate completionDate;

    @TableField("STATUS")
    private String status;

    @TableField("ROI")
    private BigDecimal roi;

    @TableField("DESCRIPTION")
    private String description;

    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
