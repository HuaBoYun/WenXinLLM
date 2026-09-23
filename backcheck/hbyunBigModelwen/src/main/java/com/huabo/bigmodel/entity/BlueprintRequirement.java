package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务蓝图-需求点（字段对齐现有需求行）
 */
@Data
@TableName("ai_blueprint_requirement")
@Schema(description = "业务蓝图需求点")
public class BlueprintRequirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "所属蓝图ID")
    private String blueprintId;

    @Schema(description = "序号")
    private Integer seqNo;

    @Schema(description = "问题状态 待处理/已完成")
    private String status;

    @Schema(description = "提交人")
    private String submitter;

    @Schema(description = "提交时间")
    private String submitTime;

    @Schema(description = "问题领域 需求调整/新增需求/BUG修复/功能优化")
    @JsonProperty("domain")
    private String reqDomain;

    @Schema(description = "问题模块")
    private String module;

    @Schema(description = "问题/功能点描述")
    private String description;

    @Schema(description = "问题截图base64 JSON数组")
    private String imageList;

    @Schema(description = "紧急程度 高/中/低")
    private String urgency;

    @Schema(description = "预计解决时间")
    private String resolveDate;

    @Schema(description = "处理人")
    private String handler;

    @Schema(description = "解决方案")
    private String solution;

    @Schema(description = "是否解决 是/否")
    private String resolved;

    @Schema(description = "创建时间")
    private Date createTime;
}
