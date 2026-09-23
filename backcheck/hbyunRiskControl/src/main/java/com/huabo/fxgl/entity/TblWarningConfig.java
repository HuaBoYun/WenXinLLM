package com.huabo.fxgl.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警配置表实体类
 * 
 * @author 华博云
 * @date 2025-09-30
 */
@Data
@Accessors(chain = true)
@TableName("TBL_WARNING_CONFIG")
@Schema(name="TblWarningConfig对象", description="预警配置表")
public class TblWarningConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "配置ID")
    @TableId(value = "CONFIG_ID", type = IdType.ASSIGN_ID)
    private String configId;

    @Schema(name = "配置类型(NOTIFICATION/SCHEDULE/THRESHOLD)")
    @TableField("CONFIG_TYPE")
    private String configType;

    @Schema(name = "配置名称")
    @TableField("CONFIG_NAME")
    private String configName;

    @Schema(name = "配置值(JSON格式)")
    @TableField("CONFIG_VALUE")
    private String configValue;

    @Schema(name = "描述")
    @TableField("DESCRIPTION")
    private String description;

    @Schema(name = "是否启用(Y/N)")
    @TableField("IS_ENABLED")
    private String isEnabled;

    @Schema(name = "排序顺序")
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    @Schema(name = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @Schema(name = "创建时间")
    @TableField("CREATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "更新人")
    @TableField("UPDATE_USER")
    private String updateUser;

    @Schema(name = "更新时间")
    @TableField("UPDATE_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 配置类型枚举
     */
    public static class ConfigType {
        public static final String NOTIFICATION = "NOTIFICATION";  // 通知配置
        public static final String SCHEDULE = "SCHEDULE";          // 调度配置
        public static final String THRESHOLD = "THRESHOLD";        // 阈值配置
    }

    /**
     * 启用状态枚举
     */
    public static class EnableStatus {
        public static final String ENABLED = "Y";   // 启用
        public static final String DISABLED = "N";  // 禁用
    }
}
