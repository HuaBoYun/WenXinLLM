package com.financial.sharing.groupControl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 维度信息表
 * 
 * @author 华博云开发团队
 * @since 2026-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_DIMENSION_INFO")
@Schema(name = "维度信息实体类")
public class TblDimensionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维度ID
     */
    @TableId(value = "DIMENSION_ID", type = IdType.ASSIGN_ID)
    @Schema(name = "维度ID")
    private String dimensionId;

    /**
     * 维度编码
     */
    @TableField("DIMENSION_CODE")
    @Schema(name = "维度编码")
    private String dimensionCode;

    /**
     * 维度名称
     */
    @TableField("DIMENSION_NAME")
    @Schema(name = "维度名称")
    private String dimensionName;

    /**
     * 维度类型：PRESET(预置)/CUSTOM(自定义)/ARCHIVE(档案引入)
     */
    @TableField("DIMENSION_TYPE")
    @Schema(name = "维度类型")
    private String dimensionType;

    /**
     * 维度分类：SUBJECT(科目)/ENTITY(主体)/PERIOD(期间)/VERSION(版本)/CURRENCY(币种)/P1-P12
     */
    @TableField("DIMENSION_CATEGORY")
    @Schema(name = "维度分类")
    private String dimensionCategory;

    /**
     * 是否启用层级：Y/N
     */
    @TableField("IS_HIERARCHY")
    @Schema(name = "是否启用层级")
    private String isHierarchy;

    /**
     * 最大层级数
     */
    @TableField("MAX_LEVEL")
    @Schema(name = "最大层级数")
    private Integer maxLevel;

    /**
     * 是否默认体系：Y/N
     */
    @TableField("IS_DEFAULT")
    @Schema(name = "是否默认体系")
    private String isDefault;

    /**
     * 状态：ACTIVE(启用)/INACTIVE(停用)
     */
    @TableField("STATUS")
    @Schema(name = "状态")
    private String status;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    @Schema(name = "描述")
    private String description;

    /**
     * 来源系统：FINANCIAL_SHARING/BIP_PLATFORM
     */
    @TableField("SOURCE_SYSTEM")
    @Schema(name = "来源系统")
    private String sourceSystem;

    /**
     * 来源系统ID
     */
    @TableField("SOURCE_ID")
    @Schema(name = "来源系统ID")
    private String sourceId;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    @Schema(name = "租户ID")
    private String tenantId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @Schema(name = "创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    @Schema(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER")
    @Schema(name = "修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    @Schema(name = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}

