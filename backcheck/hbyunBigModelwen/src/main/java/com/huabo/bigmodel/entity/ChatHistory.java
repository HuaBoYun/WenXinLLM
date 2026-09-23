package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * AI对话历史记录实体
 */
@Data
@TableName("ai_chat_history")
@Schema(description = "AI对话历史记录")
public class ChatHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "对话标题")
    private String title;

    @Schema(description = "对话内容JSON")
    private String dialogue;

    @Schema(description = "是否包含文档")
    private Boolean hasDocument;

    @Schema(description = "历史记录类型：programming-编程，writing-写作/建模")
    private String type;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
