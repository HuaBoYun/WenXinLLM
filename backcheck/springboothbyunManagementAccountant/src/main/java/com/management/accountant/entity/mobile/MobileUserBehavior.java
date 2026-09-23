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
 * 移动用户行为实体类
 * 
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_mobile_user_behavior")
@ApiModel(value = "MobileUserBehavior对象", description = "移动用户行为")
public class MobileUserBehavior {

    @ApiModelProperty(value = "用户行为ID")
    @TableId(value = "user_behavior_id", type = IdType.ASSIGN_UUID)
    private String userBehaviorId;

    @ApiModelProperty(value = "用户行为编码")
    @TableField("user_behavior_code")
    private String userBehaviorCode;

    @ApiModelProperty(value = "用户行为名称")
    @TableField("user_behavior_name")
    private String userBehaviorName;

    @ApiModelProperty(value = "用户行为类型")
    @TableField("user_behavior_type")
    private String userBehaviorType;

    @ApiModelProperty(value = "用户行为分类")
    @TableField("user_behavior_category")
    private String userBehaviorCategory;

    @ApiModelProperty(value = "用户行为模块")
    @TableField("user_behavior_module")
    private String userBehaviorModule;

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

    @ApiModelProperty(value = "用户邮箱")
    @TableField("user_email")
    private String userEmail;

    @ApiModelProperty(value = "用户手机")
    @TableField("user_phone")
    private String userPhone;

    @ApiModelProperty(value = "设备ID")
    @TableField("device_id")
    private String deviceId;

    @ApiModelProperty(value = "设备编码")
    @TableField("device_code")
    private String deviceCode;

    @ApiModelProperty(value = "设备类型")
    @TableField("device_type")
    private String deviceType;

    @ApiModelProperty(value = "设备平台")
    @TableField("device_platform")
    private String devicePlatform;

    @ApiModelProperty(value = "会话ID")
    @TableField("session_id")
    private String sessionId;

    @ApiModelProperty(value = "会话开始时间")
    @TableField("session_start_time")
    private LocalDateTime sessionStartTime;

    @ApiModelProperty(value = "会话结束时间")
    @TableField("session_end_time")
    private LocalDateTime sessionEndTime;

    @ApiModelProperty(value = "会话持续时间")
    @TableField("session_duration")
    private Long sessionDuration;

    @ApiModelProperty(value = "行为事件")
    @TableField("behavior_event")
    private String behaviorEvent;

    @ApiModelProperty(value = "行为动作")
    @TableField("behavior_action")
    private String behaviorAction;

    @ApiModelProperty(value = "行为对象")
    @TableField("behavior_object")
    private String behaviorObject;

    @ApiModelProperty(value = "行为目标")
    @TableField("behavior_target")
    private String behaviorTarget;

    @ApiModelProperty(value = "行为结果")
    @TableField("behavior_result")
    private String behaviorResult;

    @ApiModelProperty(value = "行为状态")
    @TableField("behavior_status")
    private String behaviorStatus;

    @ApiModelProperty(value = "行为时间")
    @TableField("behavior_time")
    private LocalDateTime behaviorTime;

    @ApiModelProperty(value = "行为持续时间")
    @TableField("behavior_duration")
    private Long behaviorDuration;

    @ApiModelProperty(value = "页面路径")
    @TableField("page_path")
    private String pagePath;

    @ApiModelProperty(value = "页面标题")
    @TableField("page_title")
    private String pageTitle;

    @ApiModelProperty(value = "页面URL")
    @TableField("page_url")
    private String pageUrl;

    @ApiModelProperty(value = "来源页面")
    @TableField("source_page")
    private String sourcePage;

    @ApiModelProperty(value = "目标页面")
    @TableField("target_page")
    private String targetPage;

    @ApiModelProperty(value = "引用页面")
    @TableField("referrer_page")
    private String referrerPage;

    @ApiModelProperty(value = "页面停留时间")
    @TableField("page_stay_time")
    private Long pageStayTime;

    @ApiModelProperty(value = "点击位置X")
    @TableField("click_x")
    private Integer clickX;

    @ApiModelProperty(value = "点击位置Y")
    @TableField("click_y")
    private Integer clickY;

    @ApiModelProperty(value = "点击元素")
    @TableField("click_element")
    private String clickElement;

    @ApiModelProperty(value = "点击文本")
    @TableField("click_text")
    private String clickText;

    @ApiModelProperty(value = "滚动位置")
    @TableField("scroll_position")
    private Integer scrollPosition;

    @ApiModelProperty(value = "滚动深度")
    @TableField("scroll_depth")
    private BigDecimal scrollDepth;

    @ApiModelProperty(value = "输入内容")
    @TableField("input_content")
    private String inputContent;

    @ApiModelProperty(value = "搜索关键词")
    @TableField("search_keywords")
    private String searchKeywords;

    @ApiModelProperty(value = "搜索结果数")
    @TableField("search_results_count")
    private Integer searchResultsCount;

    @ApiModelProperty(value = "操作系统")
    @TableField("operating_system")
    private String operatingSystem;

    @ApiModelProperty(value = "操作系统版本")
    @TableField("os_version")
    private String osVersion;

    @ApiModelProperty(value = "浏览器")
    @TableField("browser")
    private String browser;

    @ApiModelProperty(value = "浏览器版本")
    @TableField("browser_version")
    private String browserVersion;

    @ApiModelProperty(value = "屏幕分辨率")
    @TableField("screen_resolution")
    private String screenResolution;

    @ApiModelProperty(value = "屏幕尺寸")
    @TableField("screen_size")
    private String screenSize;

    @ApiModelProperty(value = "网络类型")
    @TableField("network_type")
    private String networkType;

    @ApiModelProperty(value = "网络速度")
    @TableField("network_speed")
    private String networkSpeed;

    @ApiModelProperty(value = "IP地址")
    @TableField("ip_address")
    private String ipAddress;

    @ApiModelProperty(value = "地理位置")
    @TableField("geo_location")
    private String geoLocation;

    @ApiModelProperty(value = "城市")
    @TableField("city")
    private String city;

    @ApiModelProperty(value = "省份")
    @TableField("province")
    private String province;

    @ApiModelProperty(value = "国家")
    @TableField("country")
    private String country;

    @ApiModelProperty(value = "时区")
    @TableField("timezone")
    private String timezone;

    @ApiModelProperty(value = "语言")
    @TableField("language")
    private String language;

    @ApiModelProperty(value = "用户代理")
    @TableField("user_agent")
    private String userAgent;

    @ApiModelProperty(value = "错误信息")
    @TableField("error_message")
    private String errorMessage;

    @ApiModelProperty(value = "错误类型")
    @TableField("error_type")
    private String errorType;

    @ApiModelProperty(value = "错误代码")
    @TableField("error_code")
    private String errorCode;

    @ApiModelProperty(value = "错误堆栈")
    @TableField("error_stack")
    private String errorStack;

    @ApiModelProperty(value = "性能指标")
    @TableField("performance_metrics")
    private String performanceMetrics;

    @ApiModelProperty(value = "加载时间")
    @TableField("load_time")
    private Long loadTime;

    @ApiModelProperty(value = "响应时间")
    @TableField("response_time")
    private Long responseTime;

    @ApiModelProperty(value = "渲染时间")
    @TableField("render_time")
    private Long renderTime;

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

    @ApiModelProperty(value = "标签")
    @TableField("tags")
    private String tags;

    @ApiModelProperty(value = "分组")
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty(value = "批次号")
    @TableField("batch_number")
    private String batchNumber;

    @ApiModelProperty(value = "行为描述")
    @TableField("behavior_description")
    private String behaviorDescription;

    @ApiModelProperty(value = "行为备注")
    @TableField("behavior_remarks")
    private String behaviorRemarks;

    @ApiModelProperty(value = "优先级")
    @TableField("priority")
    private Integer priority;

    @ApiModelProperty(value = "重要性")
    @TableField("importance")
    private String importance;

    @ApiModelProperty(value = "紧急程度")
    @TableField("urgency")
    private String urgency;

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
