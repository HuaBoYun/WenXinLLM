package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * AI办公定时任务实体（后端调度，到点直连 wenxinclaw 网关发送）
 */
@Data
@TableName("ai_scheduled_task")
@Schema(description = "AI办公定时任务")
public class ScheduledTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID(雪花)")
    private String id;

    @Schema(description = "任务描述(到点发送的消息内容)")
    private String taskName;

    @Schema(description = "执行方式 daily/once/interval")
    private String mode;

    @Schema(description = "daily模式执行时间 HH:mm")
    private String execTime;

    @Schema(description = "once模式执行时间点")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date execDatetime;

    @Schema(description = "interval模式间隔分钟数")
    private Integer intervalMinutes;

    @Schema(description = "创建时选中的模块名称")
    private String moduleName;

    @Schema(description = "创建时选中的模块唯一标识")
    private String moduleIdentification;

    @Schema(description = "创建人ID")
    private String userId;

    @Schema(description = "创建人登录账号(触发时经 hbyunSystemSetting /getzhToken 换取最新 token)")
    private String userName;

    @Schema(description = "创建人token快照(旧任务回退用：userName 缺失或换票失败时使用)")
    private String userToken;

    @Schema(description = "状态 1启用 0停用")
    private Integer status;

    @Schema(description = "once模式是否已执行 0否 1是")
    private Integer done;

    @Schema(description = "上次触发时间")
    private Date lastFireTime;

    @Schema(description = "daily模式上次触发日期 yyyy-M-d")
    private String lastFireDate;

    @Schema(description = "创建人姓名")
    private String creatorName;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
