package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 风险预警实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("risk_warning")
@Schema(name="RiskWarning对象", description="风险预警")
public class RiskWarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "项目ID")
    private Long projectId;

    @Schema(name = "预警类型：1-进度预警，2-成本预警，3-质量预警，4-安全预警，5-合规预警")
    private Integer warningType;

    @Schema(name = "预警级别：1-低级，2-中级，3-高级，4-紧急")
    private Integer warningLevel;

    @Schema(name = "预警标题")
    private String warningTitle;

    @Schema(name = "预警内容")
    private String warningContent;

    @Schema(name = "预警来源")
    private String warningSource;

    @Schema(name = "触发条件")
    private String triggerCondition;

    @Schema(name = "预警状态：1-活跃，2-已解决，3-已忽略")
    private Integer warningStatus;

    @Schema(name = "处理状态：1-待处理，2-处理中，3-已处理")
    private Integer handleStatus;

    @Schema(name = "处理人")
    private String handlePerson;

    @Schema(name = "处理时间")
    private LocalDateTime handleTime;

    @Schema(name = "处理结果")
    private String handleResult;

    @Schema(name = "预警时间")
    private LocalDateTime warningTime;

    @Schema(name = "预期解决时间")
    private LocalDateTime expectedResolveTime;

    @Schema(name = "实际解决时间")
    private LocalDateTime actualResolveTime;

    @Schema(name = "通知发送状态：0-未发送，1-已发送")
    private Integer notificationSent;

    @Schema(name = "通知发送时间")
    private LocalDateTime notificationTime;

    @Schema(name = "创建人")
    private String createBy;

    @Schema(name = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    private String updateBy;

    @Schema(name = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "删除标志：0-未删除，1-已删除")
    private Integer deleted;

    @Schema(name = "备注")
    private String remarks;
}
