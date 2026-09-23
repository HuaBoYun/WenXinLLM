package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算提醒实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("settlement_reminder")
@Schema(name="SettlementReminder", description="结算提醒")
public class SettlementReminder {

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "项目ID")
    @TableField("project_id")
    private Long projectId;

    @Schema(name = "结算项目名称")
    @TableField("settlement_project")
    private String settlementProject;

    @Schema(name = "合同编号")
    @TableField("contract_number")
    private String contractNumber;

    @Schema(name = "合同金额")
    @TableField("contract_amount")
    private BigDecimal contractAmount;

    @Schema(name = "已结算金额")
    @TableField("settled_amount")
    private BigDecimal settledAmount;

    @Schema(name = "待结算金额")
    @TableField("pending_amount")
    private BigDecimal pendingAmount;

    @Schema(name = "提醒类型(1:到期提醒,2:逾期提醒,3:手动提醒)")
    @TableField("reminder_type")
    private Short reminderType;

    @Schema(name = "提醒状态(1:待处理,2:已处理,3:已忽略)")
    @TableField("reminder_status")
    private Short reminderStatus;

    @Schema(name = "优先级(1:低,2:中,3:高)")
    @TableField("priority")
    private Short priority;

    @Schema(name = "计划结算日期")
    @TableField("planned_settlement_date")
    private Date plannedSettlementDate;

    @Schema(name = "实际结算日期")
    @TableField("actual_settlement_date")
    private Date actualSettlementDate;

    @Schema(name = "负责人ID")
    @TableField("responsible_person_id")
    private Long responsiblePersonId;

    @Schema(name = "负责人姓名")
    @TableField("responsible_person_name")
    private String responsiblePersonName;

    @Schema(name = "提醒内容")
    @TableField("reminder_content")
    private String reminderContent;

    @Schema(name = "处理备注")
    @TableField("process_notes")
    private String processNotes;

    @Schema(name = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(name = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(name = "创建人ID")
    @TableField("create_by")
    private Long createBy;

    @Schema(name = "更新人ID")
    @TableField("update_by")
    private Long updateBy;

    // 扩展字段（非数据库字段）
    @Schema(name = "项目名称")
    @TableField(exist = false)
    private String projectName;

    @Schema(name = "创建人姓名")
    @TableField(exist = false)
    private String createByName;

    @Schema(name = "更新人姓名")
    @TableField(exist = false)
    private String updateByName;

    // 便捷方法
    public boolean isPending() {
        return reminderStatus != null && reminderStatus == 1;
    }

    public boolean isProcessed() {
        return reminderStatus != null && reminderStatus == 2;
    }

    public boolean isIgnored() {
        return reminderStatus != null && reminderStatus == 3;
    }

    public boolean isHighPriority() {
        return priority != null && priority == 3;
    }

    public boolean isOverdue() {
        return plannedSettlementDate != null && 
               plannedSettlementDate.before(new Date()) && 
               isPending();
    }
}
