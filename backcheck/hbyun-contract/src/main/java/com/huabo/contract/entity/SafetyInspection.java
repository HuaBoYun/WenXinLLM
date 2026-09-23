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
 * 安全检查管理实体类
 * 根据达梦数据库 safety_inspection 表结构定义
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("safety_inspection")
@Schema(name="SafetyInspection对象", description="安全检查管理")
public class SafetyInspection {

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "项目ID")
    @TableField("project_id")
    private Long projectId;

    @Schema(name = "检查编号")
    @TableField("inspection_no")
    private String inspectionNo;

    @Schema(name = "检查名称")
    @TableField("inspection_name")
    private String inspectionName;

    @Schema(name = "检查类型(1:日常检查,2:专项检查,3:综合检查)")
    @TableField("inspection_type")
    private Short inspectionType;

    @Schema(name = "检查日期")
    @TableField("inspection_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date inspectionDate;

    @Schema(name = "检查人ID")
    @TableField("inspector_id")
    private Long inspectorId;

    @Schema(name = "检查地点")
    @TableField("inspection_location")
    private String inspectionLocation;

    @Schema(name = "检查范围")
    @TableField("inspection_scope")
    private String inspectionScope;

    @Schema(name = "安全标准")
    @TableField("safety_standards")
    private String safetyStandards;

    @Schema(name = "检查发现")
    @TableField("inspection_findings")
    private String inspectionFindings;

    @Schema(name = "隐患识别")
    @TableField("hazard_identification")
    private String hazardIdentification;

    @Schema(name = "隐患等级(1:一般,2:较大,3:重大,4:特别重大)")
    @TableField("hazard_level")
    private Short hazardLevel;

    @Schema(name = "整改措施")
    @TableField("rectification_measures")
    private String rectificationMeasures;

    @Schema(name = "整改期限")
    @TableField("rectification_deadline")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date rectificationDeadline;

    @Schema(name = "整改负责人ID")
    @TableField("rectification_person_id")
    private Long rectificationPersonId;

    @Schema(name = "紧急程度(1:一般,2:紧急,3:特急)")
    @TableField("emergency_level")
    private Short emergencyLevel;

    @Schema(name = "整改状态(1:待整改,2:整改中,3:已整改,4:已验收)")
    @TableField("rectification_status")
    private Short rectificationStatus;

    @Schema(name = "整改完成日期")
    @TableField("rectification_completion_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date rectificationCompletionDate;

    @Schema(name = "验证人ID")
    @TableField("verification_person_id")
    private Long verificationPersonId;

    @Schema(name = "验证日期")
    @TableField("verification_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date verificationDate;

    @Schema(name = "验证结果(1:合格,2:不合格)")
    @TableField("verification_result")
    private Short verificationResult;

    @Schema(name = "验证意见")
    @TableField("verification_comments")
    private String verificationComments;

    @Schema(name = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "更新时间")
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;



    /**
     * 判断是否为日常检查
     */
    public boolean isDailyInspection() {
        return inspectionType != null && inspectionType == 1;
    }

    /**
     * 判断是否为专项检查
     */
    public boolean isSpecialInspection() {
        return inspectionType != null && inspectionType == 2;
    }

    /**
     * 判断是否为综合检查
     */
    public boolean isComprehensiveInspection() {
        return inspectionType != null && inspectionType == 3;
    }

    /**
     * 判断隐患等级是否为一般
     */
    public boolean isGeneralHazard() {
        return hazardLevel != null && hazardLevel == 1;
    }

    /**
     * 判断隐患等级是否为较大
     */
    public boolean isMajorHazard() {
        return hazardLevel != null && hazardLevel == 2;
    }

    /**
     * 判断隐患等级是否为重大
     */
    public boolean isSeriousHazard() {
        return hazardLevel != null && hazardLevel == 3;
    }

    /**
     * 判断隐患等级是否为特别重大
     */
    public boolean isExtremelyHazard() {
        return hazardLevel != null && hazardLevel == 4;
    }

    /**
     * 判断紧急程度是否为一般
     */
    public boolean isGeneralEmergency() {
        return emergencyLevel != null && emergencyLevel == 1;
    }

    /**
     * 判断紧急程度是否为紧急
     */
    public boolean isUrgentEmergency() {
        return emergencyLevel != null && emergencyLevel == 2;
    }

    /**
     * 判断紧急程度是否为特急
     */
    public boolean isCriticalEmergency() {
        return emergencyLevel != null && emergencyLevel == 3;
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
        return rectificationStatus != null && rectificationStatus == 3;
    }

    /**
     * 判断是否已验收
     */
    public boolean isVerified() {
        return rectificationStatus != null && rectificationStatus == 4;
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
     * 判断验证是否合格
     */
    public boolean isVerificationPassed() {
        return verificationResult != null && verificationResult == 1;
    }

    /**
     * 判断验证是否不合格
     */
    public boolean isVerificationFailed() {
        return verificationResult != null && verificationResult == 2;
    }

    /**
     * 判断是否存在安全隐患
     */
    public boolean hasHazards() {
        return hazardIdentification != null && !hazardIdentification.trim().isEmpty();
    }

    /**
     * 判断是否有检查发现
     */
    public boolean hasFindings() {
        return inspectionFindings != null && !inspectionFindings.trim().isEmpty();
    }
}
