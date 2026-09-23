package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统配置实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_CONFIG")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(value = "CONFIG_ID", type = IdType.ASSIGN_UUID)
    private String configId;

    /**
     * 配置键
     */
    @TableField("CONFIG_KEY")
    private String configKey;

    /**
     * 配置值
     */
    @TableField("CONFIG_VALUE")
    private String configValue;

    /**
     * 配置名称
     */
    @TableField("CONFIG_NAME")
    private String configName;

    /**
     * 配置描述
     */
    @TableField("CONFIG_DESCRIPTION")
    private String configDescription;

    /**
     * 配置分组
     */
    @TableField("CONFIG_GROUP")
    private String configGroup;

    /**
     * 配置类型
     */
    @TableField("CONFIG_TYPE")
    private String configType;

    /**
     * 是否系统配置
     */
    @TableField("IS_SYSTEM")
    private String isSystem;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private String isEnabled;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    // 配置类型常量
    public static final String TYPE_STRING = "STRING";       // 字符串
    public static final String TYPE_NUMBER = "NUMBER";       // 数字
    public static final String TYPE_BOOLEAN = "BOOLEAN";     // 布尔值
    public static final String TYPE_JSON = "JSON";           // JSON对象
    public static final String TYPE_TEXT = "TEXT";           // 长文本
    public static final String TYPE_DATE = "DATE";           // 日期
    public static final String TYPE_DATETIME = "DATETIME";   // 日期时间

    // 配置分组常量
    public static final String GROUP_SYSTEM = "SYSTEM";           // 系统配置
    public static final String GROUP_BUSINESS = "BUSINESS";       // 业务配置
    public static final String GROUP_SECURITY = "SECURITY";       // 安全配置
    public static final String GROUP_NOTIFICATION = "NOTIFICATION"; // 通知配置
    public static final String GROUP_DATA_COLLECTION = "DATA_COLLECTION"; // 数据采集配置
    public static final String GROUP_REPORT = "REPORT";           // 报表配置
    public static final String GROUP_INTEGRATION = "INTEGRATION"; // 集成配置

    // 是否标识常量
    public static final String YES = "1";
    public static final String NO = "0";

    // 常用配置键常量
    public static final String KEY_DATA_COLLECTION_INTERVAL = "data.collection.interval";
    public static final String KEY_DATA_QUALITY_THRESHOLD = "data.quality.threshold";
    public static final String KEY_SYNC_BATCH_SIZE = "sync.batch.size";
    public static final String KEY_NOTIFICATION_EMAIL_ENABLED = "notification.email.enabled";
    public static final String KEY_NOTIFICATION_SMS_ENABLED = "notification.sms.enabled";
    public static final String KEY_REPORT_AUTO_GENERATION = "report.auto.generation";
    public static final String KEY_SYSTEM_MAINTENANCE_MODE = "system.maintenance.mode";
    public static final String KEY_FILE_UPLOAD_MAX_SIZE = "file.upload.max.size";
    public static final String KEY_SESSION_TIMEOUT = "session.timeout";
    public static final String KEY_PASSWORD_POLICY = "password.policy";

    // ==================== 手动getter方法 ====================

    public String getConfigId() {
        return this.configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }
}
