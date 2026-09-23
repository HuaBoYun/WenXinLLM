package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author zkl
 * @InterfaceName ProjectEvaluationItemEntity
 * @Description
 * @DATE 2024/04/13
 */
@Data
@TableName("TBL_PROJECT_EVALUATION_ITEM")
@Schema(name="项目人员积分")
@Accessors(chain = true)
public class ProjectEvaluationItemEntity {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;

    @TableField(value="USER_ID")
    @Schema(name = "人员ID")
    private BigDecimal userId;

    @TableField(exist = false)
    @Schema(name = "人员ID")
    private String userName;

    @TableField(value="BASE_SCORE")
    @Schema(name="得分")
    private Integer baseScore;

    @TableField(value="INCREASE_SCORE")
    @Schema(name="增分数值")
    private Integer increaseScore;

    @TableField(value="INCREASE_SCORE_REASON")
    @Schema(name="增分原因")
    private String increaseScoreReason;

    @TableField(value="PROJECT_ROLE")
    @Schema(name="项目角色")
    private String projectRole;

    @TableField(value="PROJECT_EVALUATION_ID")
    @Schema(name="项目评分主表ID")
    private String projectEvaluationId;

}
