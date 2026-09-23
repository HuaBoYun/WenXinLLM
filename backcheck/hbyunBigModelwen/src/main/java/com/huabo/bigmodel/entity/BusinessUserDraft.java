package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务梳理-用户草稿箱
 */
@Data
@TableName("ai_business_user_draft")
@Schema(description = "业务梳理用户草稿")
public class BusinessUserDraft implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "所属模板ID")
    private String templateId;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "草稿标题")
    private String title;

    @Schema(description = "草稿HTML内容")
    private String content;

    @Schema(description = "基于哪个版本ID")
    private String baseVersionId;

    @Schema(description = "基于哪个版本号")
    private Integer baseVersionNo;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}

