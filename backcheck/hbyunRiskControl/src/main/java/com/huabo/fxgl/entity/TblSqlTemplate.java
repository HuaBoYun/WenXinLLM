package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * SQL模板表
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_SQL_TEMPLATE")
@Schema(name="TblSqlTemplate对象", description="SQL模板表")
public class TblSqlTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "模板ID")
    @TableId(value = "TEMPLATE_ID", type = IdType.ASSIGN_ID)
    private String templateId;

    @Schema(name = "模板编码")
    @TableField("TEMPLATE_CODE")
    private String templateCode;

    @Schema(name = "模板名称")
    @TableField("TEMPLATE_NAME")
    private String templateName;

    @Schema(name = "模板类型(PROCUREMENT/FINANCE/AUDIT)")
    @TableField("TEMPLATE_TYPE")
    private String templateType;

    @Schema(name = "业务场景")
    @TableField("BUSINESS_SCENARIO")
    private String businessScenario;

    @Schema(name = "SQL模板内容")
    @TableField("SQL_CONTENT")
    private String sqlContent;

    @Schema(name = "参数配置JSON")
    @TableField("PARAMETER_CONFIG")
    private String parameterConfig;

    @Schema(name = "默认阈值配置JSON")
    @TableField("THRESHOLD_CONFIG")
    private String thresholdConfig;

    @Schema(name = "预警配置JSON")
    @TableField("WARNING_CONFIG")
    private String warningConfig;

    @Schema(name = "模板描述")
    @TableField("DESCRIPTION")
    private String description;

    @Schema(name = "复杂度级别(LOW/MEDIUM/HIGH)")
    @TableField("COMPLEXITY_LEVEL")
    private String complexityLevel;

    @Schema(name = "是否系统预置(Y/N)")
    @TableField("IS_SYSTEM")
    private String isSystem;

    @Schema(name = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
