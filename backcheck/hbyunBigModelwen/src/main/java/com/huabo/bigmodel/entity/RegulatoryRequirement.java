package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 监管模型需求-主表
 */
@Data
@TableName("ai_regulatory_requirement")
@Schema(description = "监管模型需求主表")
public class RegulatoryRequirement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "需求编号 REQ-yyyyMMdd-xxxx")
    private String reqNo;

    // 【2026-08-25 新增】与业务梳理需求(ai_business_template)一对一关联：
    // 需求管理页"需求评审"按该字段加载/关联对应蓝图需求
    @Schema(description = "关联的业务梳理需求模板ID(ai_business_template.id)")
    private String templateId;

    @Schema(description = "提出单位")
    private String department;

    @Schema(description = "提出人姓名")
    private String proposer;

    @Schema(description = "提出人ID")
    private String proposerId;

    @Schema(description = "实施方案(纯文本)")
    private String plan;

    @Schema(description = "预估人天")
    private Integer estimatedDays;

    @Schema(description = "优先级 高/中/低")
    private String priority;

    @Schema(description = "需求价值评分 1-5")
    private Integer valueScore;

    @Schema(description = "技术难度评分 1-5")
    private Integer difficultyScore;

    @Schema(description = "资源投入评分 1-5")
    private Integer investScore;

    @Schema(description = "优先级得分 value*2-difficulty-invest")
    private Integer priorityScore;

    @Schema(description = "审批状态 0草稿 1待审批")
    private Integer submitStatus;

    @Schema(description = "提交审批时间")
    private Date submitTime;

    @Schema(description = "创建人ID")
    private String creatorId;

    @Schema(description = "创建人姓名")
    private String creatorName;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
