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
 * 移动推送通知实体类
 * 
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_mobile_push_notification")
@ApiModel(value = "MobilePushNotification对象", description = "移动推送通知")
public class MobilePushNotification {

    @ApiModelProperty(value = "推送通知ID")
    @TableId(value = "push_notification_id", type = IdType.ASSIGN_UUID)
    private String pushNotificationId;

    @ApiModelProperty(value = "推送通知编码")
    @TableField("push_notification_code")
    private String pushNotificationCode;

    @ApiModelProperty(value = "推送通知名称")
    @TableField("push_notification_name")
    private String pushNotificationName;

    @ApiModelProperty(value = "推送通知类型")
    @TableField("push_notification_type")
    private String pushNotificationType;

    @ApiModelProperty(value = "推送通知分类")
    @TableField("push_notification_category")
    private String pushNotificationCategory;

    @ApiModelProperty(value = "推送通知模块")
    @TableField("push_notification_module")
    private String pushNotificationModule;

    @ApiModelProperty(value = "应用配置ID")
    @TableField("app_config_id")
    private String appConfigId;

    @ApiModelProperty(value = "应用编码")
    @TableField("app_code")
    private String appCode;

    @ApiModelProperty(value = "应用名称")
    @TableField("app_name")
    private String appName;

    @ApiModelProperty(value = "目标用户ID")
    @TableField("target_user_id")
    private String targetUserId;

    @ApiModelProperty(value = "目标用户名")
    @TableField("target_user_name")
    private String targetUserName;

    @ApiModelProperty(value = "目标设备ID")
    @TableField("target_device_id")
    private String targetDeviceId;

    @ApiModelProperty(value = "目标设备编码")
    @TableField("target_device_code")
    private String targetDeviceCode;

    @ApiModelProperty(value = "推送标题")
    @TableField("push_title")
    private String pushTitle;

    @ApiModelProperty(value = "推送内容")
    @TableField("push_content")
    private String pushContent;

    @ApiModelProperty(value = "推送摘要")
    @TableField("push_summary")
    private String pushSummary;

    @ApiModelProperty(value = "推送图标")
    @TableField("push_icon")
    private String pushIcon;

    @ApiModelProperty(value = "推送图片")
    @TableField("push_image")
    private String pushImage;

    @ApiModelProperty(value = "推送声音")
    @TableField("push_sound")
    private String pushSound;

    @ApiModelProperty(value = "推送震动")
    @TableField("push_vibration")
    private String pushVibration;

    @ApiModelProperty(value = "推送优先级")
    @TableField("push_priority")
    private String pushPriority;

    @ApiModelProperty(value = "推送渠道")
    @TableField("push_channel")
    private String pushChannel;

    @ApiModelProperty(value = "推送平台")
    @TableField("push_platform")
    private String pushPlatform;

    @ApiModelProperty(value = "推送方式")
    @TableField("push_method")
    private String pushMethod;

    @ApiModelProperty(value = "推送策略")
    @TableField("push_strategy")
    private String pushStrategy;

    @ApiModelProperty(value = "推送时间")
    @TableField("push_time")
    private LocalDateTime pushTime;

    @ApiModelProperty(value = "计划推送时间")
    @TableField("scheduled_push_time")
    private LocalDateTime scheduledPushTime;

    @ApiModelProperty(value = "实际推送时间")
    @TableField("actual_push_time")
    private LocalDateTime actualPushTime;

    @ApiModelProperty(value = "过期时间")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "推送状态")
    @TableField("push_status")
    private String pushStatus;

    @ApiModelProperty(value = "发送状态")
    @TableField("send_status")
    private String sendStatus;

    @ApiModelProperty(value = "接收状态")
    @TableField("receive_status")
    private String receiveStatus;

    @ApiModelProperty(value = "阅读状态")
    @TableField("read_status")
    private String readStatus;

    @ApiModelProperty(value = "点击状态")
    @TableField("click_status")
    private String clickStatus;

    @ApiModelProperty(value = "是否已发送")
    @TableField("is_sent")
    private Boolean isSent;

    @ApiModelProperty(value = "是否已接收")
    @TableField("is_received")
    private Boolean isReceived;

    @ApiModelProperty(value = "是否已阅读")
    @TableField("is_read")
    private Boolean isRead;

    @ApiModelProperty(value = "是否已点击")
    @TableField("is_clicked")
    private Boolean isClicked;

    @ApiModelProperty(value = "是否已过期")
    @TableField("is_expired")
    private Boolean isExpired;

    @ApiModelProperty(value = "发送时间")
    @TableField("sent_time")
    private LocalDateTime sentTime;

    @ApiModelProperty(value = "接收时间")
    @TableField("received_time")
    private LocalDateTime receivedTime;

    @ApiModelProperty(value = "阅读时间")
    @TableField("read_time")
    private LocalDateTime readTime;

    @ApiModelProperty(value = "点击时间")
    @TableField("clicked_time")
    private LocalDateTime clickedTime;

    @ApiModelProperty(value = "发送次数")
    @TableField("send_count")
    private Integer sendCount;

    @ApiModelProperty(value = "重试次数")
    @TableField("retry_count")
    private Integer retryCount;

    @ApiModelProperty(value = "最大重试次数")
    @TableField("max_retry_count")
    private Integer maxRetryCount;

    @ApiModelProperty(value = "错误次数")
    @TableField("error_count")
    private Integer errorCount;

    @ApiModelProperty(value = "最后错误信息")
    @TableField("last_error_message")
    private String lastErrorMessage;

    @ApiModelProperty(value = "最后错误时间")
    @TableField("last_error_time")
    private LocalDateTime lastErrorTime;

    @ApiModelProperty(value = "推送令牌")
    @TableField("push_token")
    private String pushToken;

    @ApiModelProperty(value = "推送密钥")
    @TableField("push_key")
    private String pushKey;

    @ApiModelProperty(value = "推送证书")
    @TableField("push_certificate")
    private String pushCertificate;

    @ApiModelProperty(value = "推送配置")
    @TableField("push_config")
    private String pushConfig;

    @ApiModelProperty(value = "推送参数")
    @TableField("push_params")
    private String pushParams;

    @ApiModelProperty(value = "推送头部")
    @TableField("push_headers")
    private String pushHeaders;

    @ApiModelProperty(value = "推送载荷")
    @TableField("push_payload")
    private String pushPayload;

    @ApiModelProperty(value = "推送响应")
    @TableField("push_response")
    private String pushResponse;

    @ApiModelProperty(value = "推送结果")
    @TableField("push_result")
    private String pushResult;

    @ApiModelProperty(value = "推送日志")
    @TableField("push_log")
    private String pushLog;

    @ApiModelProperty(value = "业务类型")
    @TableField("business_type")
    private String businessType;

    @ApiModelProperty(value = "业务模块")
    @TableField("business_module")
    private String businessModule;

    @ApiModelProperty(value = "业务场景")
    @TableField("business_scenario")
    private String businessScenario;

    @ApiModelProperty(value = "业务数据")
    @TableField("business_data")
    private String businessData;

    @ApiModelProperty(value = "业务ID")
    @TableField("business_id")
    private String businessId;

    @ApiModelProperty(value = "业务编码")
    @TableField("business_code")
    private String businessCode;

    @ApiModelProperty(value = "业务名称")
    @TableField("business_name")
    private String businessName;

    @ApiModelProperty(value = "跳转链接")
    @TableField("jump_url")
    private String jumpUrl;

    @ApiModelProperty(value = "跳转类型")
    @TableField("jump_type")
    private String jumpType;

    @ApiModelProperty(value = "跳转参数")
    @TableField("jump_params")
    private String jumpParams;

    @ApiModelProperty(value = "动作类型")
    @TableField("action_type")
    private String actionType;

    @ApiModelProperty(value = "动作参数")
    @TableField("action_params")
    private String actionParams;

    @ApiModelProperty(value = "标签")
    @TableField("tags")
    private String tags;

    @ApiModelProperty(value = "分组")
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty(value = "批次号")
    @TableField("batch_number")
    private String batchNumber;

    @ApiModelProperty(value = "推送描述")
    @TableField("push_description")
    private String pushDescription;

    @ApiModelProperty(value = "推送备注")
    @TableField("push_remarks")
    private String pushRemarks;

    @ApiModelProperty(value = "优先级")
    @TableField("priority")
    private Integer priority;

    @ApiModelProperty(value = "重要性")
    @TableField("importance")
    private String importance;

    @ApiModelProperty(value = "紧急程度")
    @TableField("urgency")
    private String urgency;

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
