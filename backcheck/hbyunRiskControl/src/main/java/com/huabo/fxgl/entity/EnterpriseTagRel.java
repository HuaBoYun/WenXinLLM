package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业标签关联实体类
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@ToString
@TableName("TBL_ENTERPRISE_TAG_REL")
@Schema(name="EnterpriseTagRel", description="企业标签关联")
public class EnterpriseTagRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="关联ID")
    @TableId(value = "REL_ID", type = IdType.ASSIGN_ID)
    private String relId;

    @Schema(name="企业ID")
    @TableField("ENTERPRISE_ID")
    private String enterpriseId;

    @Schema(name="标签ID")
    @TableField("TAG_ID")
    private String tagId;

    @Schema(name="标签值(如果标签有具体数值)")
    @TableField("TAG_VALUE")
    private String tagValue;

    @Schema(name="生效日期")
    @TableField("EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @Schema(name="失效日期")
    @TableField("EXPIRE_DATE")
    private LocalDate expireDate;

    @Schema(name="是否启用(Y/N)")
    @TableField("IS_ACTIVE")
    private String isActive;

    @Schema(name="创建时间")
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(name="创建人")
    @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
    private String createUser;

    // 关联查询字段
    @Schema(name="标签编码")
    @TableField(exist = false)
    private String tagCode;

    @Schema(name="标签名称")
    @TableField(exist = false)
    private String tagName;

    @Schema(name="标签类型")
    @TableField(exist = false)
    private String tagType;

    @Schema(name="标签分类")
    @TableField(exist = false)
    private String tagCategory;
}
