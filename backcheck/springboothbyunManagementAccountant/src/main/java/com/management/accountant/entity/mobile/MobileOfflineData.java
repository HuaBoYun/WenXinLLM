package com.management.accountant.entity.mobile;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 移动离线数据实体类
 * 
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_mobile_offline_data")
@ApiModel(value = "MobileOfflineData对象", description = "移动离线数据")
public class MobileOfflineData {

    @ApiModelProperty(value = "离线数据ID")
    @TableId(value = "offline_data_id", type = IdType.ASSIGN_UUID)
    private String offlineDataId;

    @ApiModelProperty(value = "离线数据编码")
    @TableField("offline_data_code")
    private String offlineDataCode;

    @ApiModelProperty(value = "离线数据名称")
    @TableField("offline_data_name")
    private String offlineDataName;

    @ApiModelProperty(value = "离线数据类型")
    @TableField("offline_data_type")
    private String offlineDataType;

    @ApiModelProperty(value = "离线数据分类")
    @TableField("offline_data_category")
    private String offlineDataCategory;

    @ApiModelProperty(value = "离线数据模块")
    @TableField("offline_data_module")
    private String offlineDataModule;

    @ApiModelProperty(value = "应用配置ID")
    @TableField("app_config_id")
    private String appConfigId;

    @ApiModelProperty(value = "应用编码")
    @TableField("app_code")
    private String appCode;

    @ApiModelProperty(value = "应用名称")
    @TableField("app_name")
    private String appName;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "用户名")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "设备ID")
    @TableField("device_id")
    private String deviceId;

    @ApiModelProperty(value = "设备编码")
    @TableField("device_code")
    private String deviceCode;

    @ApiModelProperty(value = "数据内容")
    @TableField("data_content")
    private String dataContent;

    @ApiModelProperty(value = "数据格式")
    @TableField("data_format")
    private String dataFormat;

    @ApiModelProperty(value = "数据大小")
    @TableField("data_size")
    private Long dataSize;

    @ApiModelProperty(value = "数据版本")
    @TableField("data_version")
    private String dataVersion;

    @ApiModelProperty(value = "数据哈希值")
    @TableField("data_hash")
    private String dataHash;

    @ApiModelProperty(value = "数据校验码")
    @TableField("data_checksum")
    private String dataChecksum;

    @ApiModelProperty(value = "数据压缩类型")
    @TableField("compression_type")
    private String compressionType;

    @ApiModelProperty(value = "压缩前大小")
    @TableField("original_size")
    private Long originalSize;

    @ApiModelProperty(value = "压缩后大小")
    @TableField("compressed_size")
    private Long compressedSize;

    @ApiModelProperty(value = "压缩比率")
    @TableField("compression_ratio")
    private BigDecimal compressionRatio;

    @ApiModelProperty(value = "加密类型")
    @TableField("encryption_type")
    private String encryptionType;

    @ApiModelProperty(value = "加密密钥")
    @TableField("encryption_key")
    private String encryptionKey;

    @ApiModelProperty(value = "是否加密")
    @TableField("is_encrypted")
    private Boolean isEncrypted;

    @ApiModelProperty(value = "数据状态")
    @TableField("data_status")
    private String dataStatus;

    @ApiModelProperty(value = "同步状态")
    @TableField("sync_status")
    private String syncStatus;

    @ApiModelProperty(value = "下载状态")
    @TableField("download_status")
    private String downloadStatus;

    @ApiModelProperty(value = "上传状态")
    @TableField("upload_status")
    private String uploadStatus;

    @ApiModelProperty(value = "是否可用")
    @TableField("is_available")
    private Boolean isAvailable;

    @ApiModelProperty(value = "是否过期")
    @TableField("is_expired")
    private Boolean isExpired;

    @ApiModelProperty(value = "过期时间")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "最后访问时间")
    @TableField("last_access_time")
    private LocalDateTime lastAccessTime;

    @ApiModelProperty(value = "访问次数")
    @TableField("access_count")
    private Integer accessCount;

    @ApiModelProperty(value = "下载次数")
    @TableField("download_count")
    private Integer downloadCount;

    @ApiModelProperty(value = "上传次数")
    @TableField("upload_count")
    private Integer uploadCount;

    @ApiModelProperty(value = "同步次数")
    @TableField("sync_count")
    private Integer syncCount;

    @ApiModelProperty(value = "错误次数")
    @TableField("error_count")
    private Integer errorCount;

    @ApiModelProperty(value = "最后错误信息")
    @TableField("last_error_message")
    private String lastErrorMessage;

    @ApiModelProperty(value = "最后错误时间")
    @TableField("last_error_time")
    private LocalDateTime lastErrorTime;

    @ApiModelProperty(value = "数据来源")
    @TableField("data_source")
    private String dataSource;

    @ApiModelProperty(value = "数据目标")
    @TableField("data_target")
    private String dataTarget;

    @ApiModelProperty(value = "数据路径")
    @TableField("data_path")
    private String dataPath;

    @ApiModelProperty(value = "存储路径")
    @TableField("storage_path")
    private String storagePath;

    @ApiModelProperty(value = "备份路径")
    @TableField("backup_path")
    private String backupPath;

    @ApiModelProperty(value = "缓存路径")
    @TableField("cache_path")
    private String cachePath;

    @ApiModelProperty(value = "临时路径")
    @TableField("temp_path")
    private String tempPath;

    @ApiModelProperty(value = "数据标签")
    @TableField("data_tags")
    private String dataTags;

    @ApiModelProperty(value = "数据描述")
    @TableField("data_description")
    private String dataDescription;

    @ApiModelProperty(value = "数据备注")
    @TableField("data_remarks")
    private String dataRemarks;

    @ApiModelProperty(value = "优先级")
    @TableField("priority")
    private Integer priority;

    @ApiModelProperty(value = "重要性")
    @TableField("importance")
    private String importance;

    @ApiModelProperty(value = "紧急程度")
    @TableField("urgency")
    private String urgency;

    @ApiModelProperty(value = "业务类型")
    @TableField("business_type")
    private String businessType;

    @ApiModelProperty(value = "业务模块")
    @TableField("business_module")
    private String businessModule;

    @ApiModelProperty(value = "业务场景")
    @TableField("business_scenario")
    private String businessScenario;

    @ApiModelProperty(value = "数据依赖")
    @TableField("data_dependencies")
    private String dataDependencies;

    @ApiModelProperty(value = "数据关联")
    @TableField("data_relations")
    private String dataRelations;

    @ApiModelProperty(value = "数据约束")
    @TableField("data_constraints")
    private String dataConstraints;

    @ApiModelProperty(value = "数据规则")
    @TableField("data_rules")
    private String dataRules;

    @ApiModelProperty(value = "数据验证")
    @TableField("data_validation")
    private String dataValidation;

    @ApiModelProperty(value = "数据转换")
    @TableField("data_transformation")
    private String dataTransformation;

    @ApiModelProperty(value = "数据映射")
    @TableField("data_mapping")
    private String dataMapping;

    @ApiModelProperty(value = "数据配置")
    @TableField("data_config")
    private String dataConfig;

    @ApiModelProperty(value = "性能指标")
    @TableField("performance_metrics")
    private String performanceMetrics;

    @ApiModelProperty(value = "质量指标")
    @TableField("quality_metrics")
    private String qualityMetrics;

    @ApiModelProperty(value = "监控指标")
    @TableField("monitoring_metrics")
    private String monitoringMetrics;

    @ApiModelProperty(value = "扩展属性")
    @TableField("extended_attributes")
    private String extendedAttributes;

    @ApiModelProperty(value = "自定义字段1")
    @TableField("custom_field1")
    private String customField1;

    @ApiModelProperty(value = "自定义字段2")
    @TableField("custom_field2")
    private String customField2;

    @ApiModelProperty(value = "自定义字段3")
    @TableField("custom_field3")
    private String customField3;

    @ApiModelProperty(value = "自定义字段4")
    @TableField("custom_field4")
    private String customField4;

    @ApiModelProperty(value = "自定义字段5")
    @TableField("custom_field5")
    private String customField5;

    @ApiModelProperty(value = "排序号")
    @TableField("sort_order")
    private Integer sortOrder;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "备注")
    @TableField("remarks")
    private String remarks;

    @ApiModelProperty(value = "租户ID")
    @TableField("tenant_id")
    private String tenantId;

    @ApiModelProperty(value = "组织ID")
    @TableField("organization_id")
    private String organizationId;

    @ApiModelProperty(value = "部门ID")
    @TableField("department_id")
    private String departmentId;

    @ApiModelProperty(value = "创建人ID")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    @ApiModelProperty(value = "创建人姓名")
    @TableField(value = "created_name", fill = FieldFill.INSERT)
    private String createdName;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    @ApiModelProperty(value = "更新人姓名")
    @TableField(value = "updated_name", fill = FieldFill.INSERT_UPDATE)
    private String updatedName;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "版本号")
    @TableField("version")
    @Version
    private Integer version;
}
