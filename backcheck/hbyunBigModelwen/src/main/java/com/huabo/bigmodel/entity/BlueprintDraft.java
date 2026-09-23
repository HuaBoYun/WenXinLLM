package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务蓝图-草稿
 */
@Data
@TableName("ai_blueprint_draft")
@Schema(description = "业务蓝图草稿")
public class BlueprintDraft implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "草稿标题(默认项目名)")
    private String title;

    @Schema(description = "表单整体JSON(requirements+bugs+项目信息)")
    private String formData;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
