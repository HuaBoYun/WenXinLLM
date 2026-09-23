package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 报告定时生成任务实体
 */
@Data
@TableName("TBL_BUDGET_REPORT_SCHEDULE")
public class BudgetReportSchedule {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    /** 报告ID（关联已有报告，可为空表示新建） */
    private String reportId;

    /** 报告名称 */
    private String reportName;

    /** 任务名称 */
    private String scheduleName;

    /** 报告类型 */
    private String reportType;

    /** 模板ID */
    private String templateId;

    /** 模板名称 */
    private String templateName;

    /** 调度频率: ONCE-单次, DAILY-每天, WEEKLY-每周, MONTHLY-每月 */
    private String frequency;

    /** CRON 表达式（高级） */
    private String cronExpression;

    /** 下次执行时间 */
    private Date nextRunTime;

    /** 上次执行时间 */
    private Date lastRunTime;

    /** 状态: 0-已停止, 1-运行中, 2-已暂停 */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 删除标志: 0-正常, 1-已删除 */
    private Integer delFlag;
}
