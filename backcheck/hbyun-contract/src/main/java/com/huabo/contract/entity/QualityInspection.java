package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 质量检查管理实体类
 * 根据达梦数据库 quality_inspection 表结构定义
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quality_inspection")
@Schema(name="QualityInspection对象", description="质量检查管理")
public class QualityInspection {

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "项目ID")
    @TableField("project_id")
    private Long projectId;

    @Schema(name = "任务ID")
    @TableField("task_id")
    private Long taskId;

    @Schema(name = "检查编号")
    @TableField("inspection_no")
    private String inspectionNo;

    @Schema(name = "检查名称")
    @TableField("inspection_name")
    private String inspectionName;

    @Schema(name = "检查类型(1:自检,2:互检,3:专检,4:验收)")
    @TableField("inspection_type")
    private Short inspectionType;

    @Schema(name = "检查日期")
    @TableField("inspection_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date inspectionDate;

    @Schema(name = "检查人ID")
    @TableField("inspector_id")
    private Long inspectorId;

    @Schema(name = "检查范围")
    @TableField("inspection_scope")
    private String inspectionScope;

    @Schema(name = "检查标准")
    @TableField("inspection_standards")
    private String inspectionStandards;

    @Schema(name = "检查方法")
    @TableField("inspection_methods")
    private String inspectionMethods;

    @Schema(name = "检查结果")
    @TableField("inspection_results")
    private String inspectionResults;

    @Schema(name = "检查结论(1:合格,2:不合格,3:待整改)")
    @TableField("check_result")
    private Short checkResult;

    @Schema(name = "发现问题")
    @TableField("identified_issues")
    private String identifiedIssues;

    @Schema(name = "整改要求")
    @TableField("rectification_requirements")
    private String rectificationRequirements;

    @Schema(name = "整改期限")
    @TableField("rectification_deadline")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date rectificationDeadline;

    @Schema(name = "整改负责人ID")
    @TableField("rectification_person_id")
    private Long rectificationPersonId;

    @Schema(name = "整改状态(1:待整改,2:整改中,3:已整改,4:已验收)")
    @TableField("rectification_status")
    private Short rectificationStatus;

    @Schema(name = "整改完成日期")
    @TableField("rectification_completion_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date rectificationCompletionDate;

    @Schema(name = "整改说明")
    @TableField("rectification_description")
    private String rectificationDescription;

    @Schema(name = "复查人ID")
    @TableField("recheck_person_id")
    private Long recheckPersonId;

    @Schema(name = "复查日期")
    @TableField("recheck_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date recheckDate;

    @Schema(name = "复查结果(1:合格,2:不合格)")
    @TableField("recheck_result")
    private Short recheckResult;

    @Schema(name = "复查意见")
    @TableField("recheck_comments")
    private String recheckComments;

    @Schema(name = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "更新时间")
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 判断检查结果是否合格
     */
    public boolean isQualified() {
        return checkResult != null && checkResult == 1;
    }

    /**
     * 判断检查结果是否不合格
     */
    public boolean isUnqualified() {
        return checkResult != null && checkResult == 2;
    }

    /**
     * 判断是否待整改
     */
    public boolean isPendingRectification() {
        return checkResult != null && checkResult == 3;
    }

    /**
     * 判断是否需要整改
     */
    public boolean needsRectification() {
        return rectificationStatus == null || rectificationStatus == 1;
    }

    /**
     * 判断是否正在整改
     */
    public boolean isRectifying() {
        return rectificationStatus != null && rectificationStatus == 2;
    }

    /**
     * 判断是否已完成整改
     */
    public boolean isRectificationCompleted() {
        return rectificationStatus != null && rectificationStatus >= 3;
    }

    /**
     * 判断整改是否逾期
     */
    public boolean isRectificationOverdue() {
        if (rectificationDeadline == null || isRectificationCompleted()) {
            return false;
        }
        return new Date().after(rectificationDeadline);
    }

    /**
     * 判断是否为自检
     */
    public boolean isSelfInspection() {
        return inspectionType != null && inspectionType == 1;
    }

    /**
     * 判断是否为互检
     */
    public boolean isMutualInspection() {
        return inspectionType != null && inspectionType == 2;
    }

    /**
     * 判断是否为专检
     */
    public boolean isSpecialInspection() {
        return inspectionType != null && inspectionType == 3;
    }

    /**
     * 判断是否为验收检查
     */
    public boolean isAcceptanceInspection() {
        return inspectionType != null && inspectionType == 4;
    }

    /**
     * 判断复查是否合格
     */
    public boolean isRecheckPassed() {
        return recheckResult != null && recheckResult == 1;
    }

    /**
     * 判断复查是否不合格
     */
    public boolean isRecheckFailed() {
        return recheckResult != null && recheckResult == 2;
    }
}
