package com.management.accountant.entity.mobile;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 移动设备管理实体类
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_mobile_device_management")
public class MobileDeviceManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备管理ID，主键
     */
    @TableId(value = "device_id", type = IdType.ASSIGN_ID)
    private String deviceId;

    /**
     * 设备名称
     */
    @TableField("device_name")
    private String deviceName;

    /**
     * 设备编码
     */
    @TableField("device_code")
    private String deviceCode;

    /**
     * 设备唯一标识符
     */
    @TableField("device_uuid")
    private String deviceUuid;

    /**
     * 设备IMEI号
     */
    @TableField("device_imei")
    private String deviceImei;

    /**
     * 设备序列号
     */
    @TableField("device_serial_number")
    private String deviceSerialNumber;

    /**
     * 设备类型：PHONE-手机/TABLET-平板/WATCH-手表/TV-电视/CAR-车载/IOT-物联网设备
     */
    @TableField("device_type")
    private String deviceType;

    /**
     * 设备平台：ANDROID-安卓/IOS-苹果/WINDOWS-Windows/LINUX-Linux/OTHER-其他
     */
    @TableField("device_platform")
    private String devicePlatform;

    /**
     * 设备品牌
     */
    @TableField("device_brand")
    private String deviceBrand;

    /**
     * 设备型号
     */
    @TableField("device_model")
    private String deviceModel;

    /**
     * 设备制造商
     */
    @TableField("device_manufacturer")
    private String deviceManufacturer;

    /**
     * 操作系统版本
     */
    @TableField("os_version")
    private String osVersion;

    /**
     * 应用版本
     */
    @TableField("app_version")
    private String appVersion;

    /**
     * 设备屏幕尺寸
     */
    @TableField("screen_size")
    private String screenSize;

    /**
     * 设备屏幕分辨率
     */
    @TableField("screen_resolution")
    private String screenResolution;

    /**
     * 设备屏幕密度
     */
    @TableField("screen_density")
    private Double screenDensity;

    /**
     * 设备内存大小（GB）
     */
    @TableField("memory_size")
    private Double memorySize;

    /**
     * 设备存储大小（GB）
     */
    @TableField("storage_size")
    private Double storageSize;

    /**
     * 设备可用存储（GB）
     */
    @TableField("available_storage")
    private Double availableStorage;

    /**
     * 设备CPU信息
     */
    @TableField("cpu_info")
    private String cpuInfo;

    /**
     * 设备GPU信息
     */
    @TableField("gpu_info")
    private String gpuInfo;

    /**
     * 设备网络类型：WIFI-WiFi/4G-4G网络/5G-5G网络/ETHERNET-以太网/BLUETOOTH-蓝牙
     */
    @TableField("network_type")
    private String networkType;

    /**
     * 设备IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 设备MAC地址
     */
    @TableField("mac_address")
    private String macAddress;

    /**
     * 设备位置信息，JSON格式存储
     */
    @TableField("location_info")
    private String locationInfo;

    /**
     * 设备传感器信息，JSON格式存储
     */
    @TableField("sensor_info")
    private String sensorInfo;

    /**
     * 设备电池信息，JSON格式存储
     */
    @TableField("battery_info")
    private String batteryInfo;

    /**
     * 设备配置信息，JSON格式存储
     */
    @TableField("device_config")
    private String deviceConfig;

    /**
     * 设备权限信息，JSON格式存储
     */
    @TableField("device_permissions")
    private String devicePermissions;

    /**
     * 设备安全信息，JSON格式存储
     */
    @TableField("security_info")
    private String securityInfo;

    /**
     * 设备管理策略，JSON格式存储
     */
    @TableField("management_policy")
    private String managementPolicy;

    /**
     * 设备监控配置，JSON格式存储
     */
    @TableField("monitoring_config")
    private String monitoringConfig;

    /**
     * 设备备份配置，JSON格式存储
     */
    @TableField("backup_config")
    private String backupConfig;

    /**
     * 设备更新配置，JSON格式存储
     */
    @TableField("update_config")
    private String updateConfig;

    /**
     * 设备用户ID
     */
    @TableField("device_user_id")
    private String deviceUserId;

    /**
     * 设备用户名
     */
    @TableField("device_user_name")
    private String deviceUserName;

    /**
     * 设备用户邮箱
     */
    @TableField("device_user_email")
    private String deviceUserEmail;

    /**
     * 设备用户电话
     */
    @TableField("device_user_phone")
    private String deviceUserPhone;

    /**
     * 设备组织ID
     */
    @TableField("organization_id")
    private String organizationId;

    /**
     * 设备部门ID
     */
    @TableField("department_id")
    private String departmentId;

    /**
     * 设备状态：ONLINE-在线/OFFLINE-离线/LOCKED-锁定/LOST-丢失/RETIRED-退役
     */
    @TableField("device_status")
    private String deviceStatus;

    /**
     * 设备健康状态：HEALTHY-健康/WARNING-警告/CRITICAL-严重/UNKNOWN-未知
     */
    @TableField("health_status")
    private String healthStatus;

    /**
     * 设备合规状态：COMPLIANT-合规/NON_COMPLIANT-不合规/UNKNOWN-未知
     */
    @TableField("compliance_status")
    private String complianceStatus;

    /**
     * 设备安全状态：SECURE-安全/AT_RISK-有风险/COMPROMISED-已泄露/UNKNOWN-未知
     */
    @TableField("security_status")
    private String securityStatus;

    /**
     * 是否已注册：0-否/1-是
     */
    @TableField("is_registered")
    private Boolean isRegistered;

    /**
     * 是否已激活：0-否/1-是
     */
    @TableField("is_activated")
    private Boolean isActivated;

    /**
     * 是否已认证：0-否/1-是
     */
    @TableField("is_authenticated")
    private Boolean isAuthenticated;

    /**
     * 是否已授权：0-否/1-是
     */
    @TableField("is_authorized")
    private Boolean isAuthorized;

    /**
     * 是否已加密：0-否/1-是
     */
    @TableField("is_encrypted")
    private Boolean isEncrypted;

    /**
     * 是否已越狱/Root：0-否/1-是
     */
    @TableField("is_jailbroken")
    private Boolean isJailbroken;

    /**
     * 是否支持MDM：0-否/1-是
     */
    @TableField("is_mdm_supported")
    private Boolean isMdmSupported;

    /**
     * 是否启用MDM：0-否/1-是
     */
    @TableField("is_mdm_enabled")
    private Boolean isMdmEnabled;

    /**
     * 注册时间
     */
    @TableField("registration_time")
    private LocalDateTime registrationTime;

    /**
     * 激活时间
     */
    @TableField("activation_time")
    private LocalDateTime activationTime;

    /**
     * 最后在线时间
     */
    @TableField("last_online_time")
    private LocalDateTime lastOnlineTime;

    /**
     * 最后同步时间
     */
    @TableField("last_sync_time")
    private LocalDateTime lastSyncTime;

    /**
     * 最后备份时间
     */
    @TableField("last_backup_time")
    private LocalDateTime lastBackupTime;

    /**
     * 最后更新时间
     */
    @TableField("last_update_time")
    private LocalDateTime lastUpdateTime;

    /**
     * 设备使用次数
     */
    @TableField("usage_count")
    private Long usageCount;

    /**
     * 设备使用时长（分钟）
     */
    @TableField("usage_duration")
    private Long usageDuration;

    /**
     * 设备错误次数
     */
    @TableField("error_count")
    private Long errorCount;

    /**
     * 设备崩溃次数
     */
    @TableField("crash_count")
    private Long crashCount;

    /**
     * 设备重启次数
     */
    @TableField("restart_count")
    private Long restartCount;

    /**
     * 设备平均响应时间（毫秒）
     */
    @TableField("avg_response_time")
    private Long avgResponseTime;

    /**
     * 设备平均内存使用率
     */
    @TableField("avg_memory_usage")
    private Double avgMemoryUsage;

    /**
     * 设备平均CPU使用率
     */
    @TableField("avg_cpu_usage")
    private Double avgCpuUsage;

    /**
     * 设备平均网络使用（KB）
     */
    @TableField("avg_network_usage")
    private Long avgNetworkUsage;

    /**
     * 设备平均电池使用（mAh）
     */
    @TableField("avg_battery_usage")
    private Double avgBatteryUsage;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;

    /**
     * 创建人ID
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志：0-未删除/1-已删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @Version
    @TableField("version")
    private Integer version;

    // 扩展字段

    /**
     * 设备管理员
     */
    @TableField("device_admin")
    private String deviceAdmin;

    /**
     * 设备负责人
     */
    @TableField("device_owner")
    private String deviceOwner;

    /**
     * 设备联系人
     */
    @TableField("device_contact")
    private String deviceContact;

    /**
     * 设备采购日期
     */
    @TableField("purchase_date")
    private LocalDateTime purchaseDate;

    /**
     * 设备保修期（月）
     */
    @TableField("warranty_period")
    private Integer warrantyPeriod;

    /**
     * 设备保修到期日期
     */
    @TableField("warranty_expiry_date")
    private LocalDateTime warrantyExpiryDate;

    /**
     * 设备成本
     */
    @TableField("device_cost")
    private Double deviceCost;

    /**
     * 设备折旧
     */
    @TableField("device_depreciation")
    private Double deviceDepreciation;

    /**
     * 设备残值
     */
    @TableField("device_residual_value")
    private Double deviceResidualValue;

    /**
     * 设备维护记录，JSON格式存储
     */
    @TableField("maintenance_records")
    private String maintenanceRecords;

    /**
     * 设备故障记录，JSON格式存储
     */
    @TableField("fault_records")
    private String faultRecords;

    /**
     * 设备升级记录，JSON格式存储
     */
    @TableField("upgrade_records")
    private String upgradeRecords;

    /**
     * 扩展属性，JSON格式存储
     */
    @TableField("extended_attributes")
    private String extendedAttributes;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
}
