package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统参数配置实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_SYSTEM_PARAMETER")
public class SystemParameter {

    /**
     * 参数ID
     */
    @TableId(value = "PARAMETER_ID", type = IdType.ASSIGN_UUID)
    private String parameterId;

    /**
     * 参数键名
     */
    private String parameterKey;

    /**
     * 参数名称
     */
    private String parameterName;

    /**
     * 参数值
     */
    private String parameterValue;

    /**
     * 参数类型
     */
    private String parameterType;

    /**
     * 参数分组
     */
    private String parameterGroup;

    /**
     * 参数描述
     */
    private String parameterDescription;

    /**
     * 是否系统参数
     */
    private Boolean isSystem;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 值范围/选项
     */
    private String valueOptions;

    /**
     * 验证规则
     */
    private String validationRule;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // 参数类型常量
    public static final String TYPE_STRING = "STRING";
    public static final String TYPE_NUMBER = "NUMBER";
    public static final String TYPE_BOOLEAN = "BOOLEAN";
    public static final String TYPE_DATE = "DATE";
    public static final String TYPE_JSON = "JSON";
    public static final String TYPE_PASSWORD = "REDACTED";
    public static final String TYPE_EMAIL = "EMAIL";
    public static final String TYPE_URL = "URL";
    public static final String TYPE_FILE_PATH = "FILE_PATH";
    public static final String TYPE_SELECT = "SELECT";

    // 参数分组常量
    public static final String GROUP_SYSTEM = "SYSTEM";
    public static final String GROUP_DATABASE = "DATABASE";
    public static final String GROUP_SECURITY = "SECURITY";
    public static final String GROUP_NOTIFICATION = "NOTIFICATION";
    public static final String GROUP_MONITORING = "MONITORING";
    public static final String GROUP_REPORT = "REPORT";
    public static final String GROUP_INTEGRATION = "INTEGRATION";
    public static final String GROUP_PERFORMANCE = "PERFORMANCE";
    public static final String GROUP_BACKUP = "BACKUP";
    public static final String GROUP_CUSTOM = "CUSTOM";
}
