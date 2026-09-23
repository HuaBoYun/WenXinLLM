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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 进度管理实体类
 * 基于达梦数据库 project_progress 表结构
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("project_progress")
@Schema(name="Progress对象", description="进度管理")
public class Progress {

    /**
     * 默认构造函数，设置默认值
     */
    public Progress() {
        this.reportDate = new Date(); // 设置默认汇报日期为当前时间
        this.milestoneType = 2; // 默认为关键节点
        this.progressStatus = 1; // 默认为未开始
        this.plannedProgress = BigDecimal.ZERO;
        this.actualProgress = BigDecimal.ZERO;
        this.delayDays = 0;
    }

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "项目ID")
    @TableField("project_id")
    private Long projectId;

    @Schema(name = "任务ID")
    @TableField("task_id")
    private Long taskId;

    @Schema(name = "里程碑名称")
    @TableField("milestone_name")
    private String milestoneName;

    @Schema(name = "里程碑类型：1-项目开始，2-关键节点，3-项目完成")
    @TableField("milestone_type")
    private Integer milestoneType;

    @Schema(name = "计划开始日期")
    @TableField("planned_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date plannedStartDate;

    @Schema(name = "计划结束日期")
    @TableField("planned_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date plannedEndDate;

    @Schema(name = "实际开始日期")
    @TableField("actual_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date actualStartDate;

    @Schema(name = "实际结束日期")
    @TableField("actual_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date actualEndDate;

    @Schema(name = "计划进度(%)")
    @TableField("planned_progress")
    private BigDecimal plannedProgress;

    @Schema(name = "实际进度(%)")
    @TableField("actual_progress")
    private BigDecimal actualProgress;

    @Schema(name = "进度状态：1-未开始，2-进行中，3-已完成，4-延期")
    @TableField("progress_status")
    private Integer progressStatus;

    @Schema(name = "延期天数")
    @TableField("delay_days")
    private Integer delayDays;

    @Schema(name = "延期原因")
    @TableField("delay_reason")
    private String delayReason;

    @Schema(name = "纠正措施")
    @TableField("corrective_measures")
    private String correctiveMeasures;

    @Schema(name = "负责人ID")
    @TableField("responsible_person_id")
    private Long responsiblePersonId;

    @Schema(name = "汇报人ID")
    @TableField("reporter_id")
    private Long reporterId;

    @Schema(name = "汇报日期")
    @TableField("report_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date reportDate;

    @Schema(name = "进度描述")
    @TableField("progress_description")
    private String progressDescription;

    @Schema(name = "下期计划")
    @TableField("next_plan")
    private String nextPlan;

    @Schema(name = "问题风险")
    @TableField("issues_risks")
    private String issuesRisks;

    @Schema(name = "需要支持")
    @TableField("support_needed")
    private String supportNeeded;

    @Schema(name = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "更新时间")
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 判断是否未开始
     */
    public boolean isNotStarted() {
        return progressStatus != null && progressStatus == 1;
    }

    /**
     * 判断是否进行中
     */
    public boolean isInProgress() {
        return progressStatus != null && progressStatus == 2;
    }

    /**
     * 判断是否已完成
     */
    public boolean isCompleted() {
        return progressStatus != null && progressStatus == 3;
    }

    /**
     * 判断是否延期
     */
    public boolean isDelayed() {
        return progressStatus != null && progressStatus == 4;
    }

    /**
     * 判断是否有延期天数
     */
    public boolean hasDelayDays() {
        return delayDays != null && delayDays > 0;
    }

    /**
     * 判断是否提前完成
     */
    public boolean isEarlyCompletion() {
        if (actualEndDate == null || plannedEndDate == null) {
            return false;
        }
        return actualEndDate.before(plannedEndDate) && isCompleted();
    }

    /**
     * 计算进度偏差
     */
    public BigDecimal getProgressDeviation() {
        if (plannedProgress == null || actualProgress == null) {
            return BigDecimal.ZERO;
        }
        return actualProgress.subtract(plannedProgress);
    }
}
