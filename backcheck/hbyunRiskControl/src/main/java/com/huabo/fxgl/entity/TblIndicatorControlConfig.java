package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_INDICATOR_CONTROL_CONFIG")
@Schema(name = "TblIndicatorControlConfig", description = "\u6307\u6807\u63a7\u5236\u914d\u7f6e\u8868")
public class TblIndicatorControlConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("RIGHT_ID")
    private String rightId;

    @TableField("MODEL_ID")
    private String modelId;

    @TableField("CONTROL_LEVEL")
    private String controlLevel;

    @TableField("OPERATION_TYPE")
    private String operationType;

    @TableField("THRESHOLD_VALUE")
    private String thresholdValue;

    @TableField("SYSTEM_TYPE")
    private String systemType;

    @TableField("IS_ENABLED")
    private String isEnabled;

    @TableField("FIELD_MAPPING")
    private String fieldMapping;

    @TableField("EXTERNAL_PAGE_KEY")
    private String externalPageKey;

    @TableField("REMARK")
    private String remark;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String pageName;

    @TableField(exist = false)
    private String moduleName;

    @TableField(exist = false)
    private String displayName;

    @TableField(exist = false)
    private String modelName;

    @TableField(exist = false)
    private String modelCode;

    @TableField(exist = false)
    @Schema(name = "moduleType")
    private String moduleType;
}
