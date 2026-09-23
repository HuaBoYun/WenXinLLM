package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * AI提示词配置实体
 * <p>
 * 保存用户自定义的系统提示词，按 userId + promptType 唯一。
 * promptType：consult-AI咨询，writing-AI建模（预留扩展）。
 */
@Data
@TableName("ai_prompt_config")
@Schema(description = "AI提示词配置")
public class PromptConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "提示词类型：consult-AI咨询，writing-AI建模")
    private String promptType;

    @Schema(description = "提示词内容")
    private String promptContent;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
