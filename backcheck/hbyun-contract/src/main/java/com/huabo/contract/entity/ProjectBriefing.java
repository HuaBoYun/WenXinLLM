package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 项目交底表
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_briefing")
public class ProjectBriefing implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 交底编号
     */
    @TableField("briefing_no")
    private String briefingNo;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 任务ID
     */
    @TableField("task_id")
    private Long taskId;

    /**
     * 交底标题
     */
    @TableField("briefing_name")
    private String briefingTitle;

    /**
     * 交底类型(1:技术交底,2:安全交底,3:质量交底,4:进度交底,5:成本交底,6:其他交底)
     */
    @TableField("briefing_type")
    private Integer briefingType;

    /**
     * 交底状态(1:待交底,2:已交底,3:已确认)
     */
    @TableField("briefing_status")
    private Integer briefingStatus;

    /**
     * 交底内容
     */
    @TableField("briefing_content")
    private String briefingContent;

    /**
     * 质量要求
     */
    @TableField("quality_requirements")
    private String qualityRequirements;

    /**
     * 安全要求
     */
    @TableField("safety_requirements")
    private String safetyRequirements;

    /**
     * 技术标准
     */
    @TableField("technical_standards")
    private String technicalStandards;

    /**
     * 付款流程
     */
    @TableField("payment_process")
    private String paymentProcess;

    /**
     * 进度要求
     */
    @TableField("schedule_requirements")
    private String scheduleRequirements;

    /**
     * 交付标准
     */
    @TableField("deliverable_standards")
    private String deliverableStandards;

    /**
     * 交底人ID
     */
    @TableField(value = "briefer_id", fill = FieldFill.INSERT)
    private Long brieferId;

    /**
     * 交底时间
     */
    @TableField(value = "briefing_date", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date briefingTime;

    /**
     * 参与人员
     */
    @TableField("participants")
    private String participants;

    /**
     * 交底地点
     */
    @TableField("briefing_location")
    private String briefingLocation;

    /**
     * 附件路径
     */
    @TableField("attachment_path")
    private String attachmentPath;

    /**
     * 确认状态(0:未确认,1:已确认)
     */
    @TableField("confirmation_status")
    private Integer confirmationStatus;

    /**
     * 确认人ID
     */
    @TableField("confirmer_id")
    private Long confirmerId;

    /**
     * 确认时间
     */
    @TableField("confirmation_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date confirmationDate;




    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;




}
