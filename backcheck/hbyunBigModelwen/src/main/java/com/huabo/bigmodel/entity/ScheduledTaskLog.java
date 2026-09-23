package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;

/**
 * AI办公定时任务执行日志实体
 */
@Data
@TableName("ai_scheduled_task_log")
@Schema(description = "AI办公定时任务执行日志")
public class ScheduledTaskLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID(雪花)")
    private String id;

    @Schema(description = "任务ID")
    private String taskId;

    @Schema(description = "任务描述快照")
    private String taskDesc;

    @Schema(description = "触发时间")
    private Date fireTime;

    @Schema(description = "状态 sent已发送/responded已回复/failed发送失败/error回复异常")
    private String status;

    @Schema(description = "AI回复摘要")
    private String responseExcerpt;

    @Schema(description = "AI完整回复(不截断)")
    @TableField(jdbcType = JdbcType.CLOB)
    private String responseContent;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "创建时间")
    private Date createTime;
}
