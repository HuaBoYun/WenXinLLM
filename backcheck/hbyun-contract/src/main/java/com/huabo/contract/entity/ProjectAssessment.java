package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目考核记录实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_assessment")
@Schema(name="ProjectAssessment对象", description="项目考核记录表")
public class ProjectAssessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "项目ID")
    private String projectId;

    @Schema(name = "考核期间")
    private String assessmentPeriod;

    @Schema(name = "考核类型：1-月度，2-季度，3-年度，4-项目完成")
    private Integer assessmentType;

    @Schema(name = "总分")
    @TableField("total_score")
    private BigDecimal totalScore;

    @Schema(name = "考核等级")
    private String assessmentLevel = "待评定";

    @Schema(name = "考核状态：1-进行中，2-已完成，3-已审核")
    private Integer assessmentStatus;

    @Schema(name = "考核人ID")
    private Long assessorId;

    @Schema(name = "审核人ID")
    private Long reviewerId;

    @Schema(name = "考核日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assessmentDate;

    @Schema(name = "审核日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reviewDate;

    @Schema(name = "审核结果：approved-通过，rejected-驳回")
    private String reviewResult;

    @Schema(name = "审核意见")
    private String reviewComments;

    @Schema(name = "备注")
    private String remarks;

    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(name = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(name = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy = 1L;

    @Schema(name = "更新人ID")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy = 1L;

    // 非数据库字段
    @Schema(name = "项目名称")
    @TableField(exist = false)
    private String projectName;

    @Schema(name = "考核人姓名")
    @TableField(exist = false)
    private String assessorName;

    @Schema(name = "审核人姓名")
    @TableField(exist = false)
    private String reviewerName;

    @Schema(name = "考核明细列表")
    @TableField(exist = false)
    private List<AssessmentDetail> details;

    @Schema(name = "考核类型名称")
    @TableField(exist = false)
    private String assessmentTypeName;

    @Schema(name = "考核状态名称")
    @TableField(exist = false)
    private String assessmentStatusName;
}
