package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 企业标签定义实体类
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@ToString
@TableName("TBL_ENTERPRISE_TAG_DEF")
@Schema(name="EnterpriseTagDef", description="企业标签定义")
public class EnterpriseTagDef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="标签ID")
    @TableId(value = "TAG_ID", type = IdType.ASSIGN_ID)
    private String tagId;

    @Schema(name="标签编码")
    @TableField("TAG_CODE")
    private String tagCode;

    @Schema(name="标签名称")
    @TableField("TAG_NAME")
    private String tagName;

    @Schema(name="标签类型(ADVANTAGE-优势,ATTENTION-关注,RISK-风险,NORMAL-普通)")
    @TableField("TAG_TYPE")
    private String tagType;

    @Schema(name="标签分类(QUALIFICATION-资质,BUSINESS-业务,FINANCIAL-财务,RISK-风险)")
    @TableField("TAG_CATEGORY")
    private String tagCategory;

    @Schema(name="标签描述")
    @TableField("TAG_DESCRIPTION")
    private String tagDescription;

    @Schema(name="是否启用(Y/N)")
    @TableField("IS_ACTIVE")
    private String isActive;

    @Schema(name="排序序号")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @Schema(name="创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(name="创建人")
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    private String createUser;

    @Schema(name="更新时间")
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(name="更新人")
    @TableField(value = "UPDATE_USER", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;
}
