package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务梳理-模板（左侧树节点）
 */
@Data
@TableName("ai_business_template")
@Schema(description = "业务梳理模板")
public class BusinessTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "模板编号(如 01)")
    private String templateNo;

    @Schema(description = "模板标题")
    private String title;

    @Schema(description = "副标题/分组")
    private String subtitle;

    @Schema(description = "初始静态文件名(用于首次种子内容写入)")
    private String sourceFile;

    @Schema(description = "是否系统模板：1是 0否")
    private Integer isSystem;

    @Schema(description = "创建人ID")
    private String creatorId;

    @Schema(description = "创建人姓名")
    private String creatorName;

    @Schema(description = "排序号")
    private Integer sortNo;

    @Schema(description = "状态 1启用 0停用")
    private Integer status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    // ====== 以下为业务梳理改造新增字段（纯追加，对既有逻辑零侵入） ======

    @Schema(description = "节点类型:0=流程模版,1=需求")
    private Integer nodeType;

    @Schema(description = "需求来源:custom/builtin")
    private String flowSource;

    @Schema(description = "builtin引用的流程模版id")
    private String sourceFlowId;

    @Schema(description = "确认状态:0待确认,1已确认")
    private Integer confirmStatus;

    @Schema(description = "已确认指向的版本id")
    private String confirmedVersionId;

    @Schema(description = "需求描述")
    private String description;
}

