package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应收待办事项实体类
 * @author system
 * @since 2026-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_AR_TODO")
@ApiModel(value = "ArTodoEntity对象", description = "应收待办事项表")
public class ArTodoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "待办ID")
    @TableId(value = "TODO_ID", type = IdType.ASSIGN_UUID)
    private String todoId;

    @ApiModelProperty(value = "待办类型")
    @TableField("TODO_TYPE")
    private String todoType;

    @ApiModelProperty(value = "待办内容")
    @TableField("TODO_CONTENT")
    private String todoContent;

    @ApiModelProperty(value = "业务类型")
    @TableField("BUSINESS_TYPE")
    private String businessType;

    @ApiModelProperty(value = "业务ID")
    @TableField("BUSINESS_ID")
    private String businessId;

    @ApiModelProperty(value = "优先级(1高 2中 3低)")
    @TableField("PRIORITY")
    private Integer priority;

    @ApiModelProperty(value = "待办状态(0待处理 1已处理)")
    @TableField("TODO_STATUS")
    private Integer todoStatus;

    @ApiModelProperty(value = "处理人ID")
    @TableField("HANDLER_ID")
    private String handlerId;

    @ApiModelProperty(value = "处理人姓名")
    @TableField("HANDLER_NAME")
    private String handlerName;

    @ApiModelProperty(value = "待办标题")
    @TableField("TODO_TITLE")
    private String todoTitle;

    @ApiModelProperty(value = "处理时间")
    @TableField("HANDLE_TIME")
    private LocalDateTime handleTime;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "组织ID")
    @TableField("ORG_ID")
    private String orgId;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "CREATE_BY", fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    @TableField(value = "UPDATE_BY", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "删除标识(0否 1是)")
    @TableField("IS_DELETED")
    @TableLogic
    private Integer isDeleted;
}

