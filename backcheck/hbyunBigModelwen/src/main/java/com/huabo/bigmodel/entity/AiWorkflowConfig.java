package com.huabo.bigmodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * AI办公工作流配置实体（触发关键字 + 步骤链，业务人员在画布上编排）
 */
@Data
@TableName("ai_workflow_config")
@Schema(description = "AI办公工作流配置")
public class AiWorkflowConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID(雪花/前端生成的稳定ID)")
    private String id;

    @Schema(description = "归属用户ID(staffid)")
    private String userId;

    @Schema(description = "创建人登录账号")
    private String userName;

    @Schema(description = "工作流名称")
    private String name;

    @Schema(description = "状态 1启用 0停用")
    private Integer enabled;

    @Schema(description = "触发关键字JSON数组")
    private String keywords;

    @Schema(description = "步骤JSON数组(按顺序串行执行, 含画布坐标x/y)")
    private String steps;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
