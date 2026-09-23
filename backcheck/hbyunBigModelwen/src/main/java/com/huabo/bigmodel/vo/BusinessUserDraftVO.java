package com.huabo.bigmodel.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
 
import java.io.Serializable;
import java.util.Date;

/**
 * 业务梳理-用户草稿（不含 content，用于列表）
 */
@Data
@Schema(description = "业务梳理用户草稿概要")
public class BusinessUserDraftVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "草稿ID")
    private String id;

    @Schema(description = "所属模板ID")
    private String templateId;

    @Schema(description = "模板标题（冗余展示）")
    private String templateTitle;

    @Schema(description = "草稿标题")
    private String title;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "基于版本号")
    private Integer baseVersionNo;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}

