package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务蓝图-Bug列表
 */
@Data
@TableName("ai_blueprint_bug")
@Schema(description = "业务蓝图Bug")
public class BlueprintBug implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "所属蓝图ID")
    private String blueprintId;

    @Schema(description = "序号")
    private Integer seqNo;

    @Schema(description = "状态 待处理/已完成")
    private String status;

    @Schema(description = "提交人")
    private String submitter;

    @Schema(description = "提交时间")
    private String submitTime;

    @Schema(description = "所属模块")
    private String module;

    @Schema(description = "问题描述")
    private String description;

    @Schema(description = "截图base64 JSON数组")
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
