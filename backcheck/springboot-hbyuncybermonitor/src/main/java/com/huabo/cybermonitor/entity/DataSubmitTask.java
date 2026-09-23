package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据报送任务实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("DATA_SUBMIT_TASK")
public class DataSubmitTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "TASK_ID", type = IdType.ASSIGN_UUID)
    private String taskId;

    /**
     * 任务名称
     */
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 任务类型
     */
    @TableField("TASK_TYPE")
    private String taskType;

    /**
     * 任务分类
     */
    @TableField("TASK_CATEGORY")
    private String taskCategory;

    /**
     * 任务描述
     */
    @TableField("TASK_DESCRIPTION")
    private String taskDescription;

    /**
     * 报送周期
     */
    @TableField("SUBMIT_CYCLE")
    private String submitCycle;

    /**
     * 开始日期
     */
    @TableField("START_DATE")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField("END_DATE")
    private LocalDate endDate;

    /**
     * 任务状态
     */
    @TableField("TASK_STATUS")
    private String taskStatus;

    /**
     * 数据模板
     */
    @TableField("DATA_TEMPLATE")
    private String dataTemplate;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    private LocalDateTime publishTime;

    // 任务类型常量
    public static final String TYPE_REGULAR = "REGULAR";     // 定期报送
    public static final String TYPE_SPECIAL = "SPECIAL";     // 专项报送
    public static final String TYPE_URGENT = "URGENT";       // 紧急报送

    // 任务状态常量
    public static final String STATUS_DRAFT = "DRAFT";       // 草稿
    public static final String STATUS_PUBLISHED = "PUBLISHED"; // 已发布
    public static final String STATUS_ACTIVE = "ACTIVE";     // 进行中
    public static final String STATUS_COMPLETED = "COMPLETED"; // 已完成
    public static final String STATUS_CANCELLED = "CANCELLED"; // 已取消

    // 报送周期常量
    public static final String CYCLE_DAILY = "DAILY";        // 日报
    public static final String CYCLE_WEEKLY = "WEEKLY";      // 周报
    public static final String CYCLE_MONTHLY = "MONTHLY";    // 月报
    public static final String CYCLE_QUARTERLY = "QUARTERLY"; // 季报
    public static final String CYCLE_YEARLY = "YEARLY";      // 年报
    public static final String CYCLE_ONCE = "ONCE";          // 一次性

    // 任务分类常量
    public static final String CATEGORY_FINANCIAL = "FINANCIAL";     // 财务数据
    public static final String CATEGORY_OPERATIONAL = "OPERATIONAL"; // 经营数据
    public static final String CATEGORY_GOVERNANCE = "GOVERNANCE";   // 治理数据
    public static final String CATEGORY_RISK = "RISK";               // 风险数据
    public static final String CATEGORY_COMPLIANCE = "COMPLIANCE";   // 合规数据
    public static final String CATEGORY_PERFORMANCE = "PERFORMANCE"; // 绩效数据
}
