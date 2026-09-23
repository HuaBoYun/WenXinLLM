package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务蓝图-主表
 */
@Data
@TableName("ai_blueprint")
@Schema(description = "业务蓝图主表")
public class Blueprint implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "来源需求ID")
    private String sourceTemplateId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "合同编号")
    private String contractNo;

    @Schema(description = "提出单位")
    private String department;

    @Schema(description = "提出人")
    private String proposer;

    @Schema(description = "实施方案")
    private String plan;

    @Schema(description = "预估人天")
    private Integer estimatedDays;

    @Schema(description = "优先级 紧急/高/中/低")
    private String priority;

    @Schema(description = "创建人ID")
    private String creatorId;

    @Schema(description = "创建人姓名")
    private String creatorName;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
