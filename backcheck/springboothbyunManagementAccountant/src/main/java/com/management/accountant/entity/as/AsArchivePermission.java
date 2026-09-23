package com.management.accountant.entity.as;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 档案权限管理实体类
 * 
 * @author AI Assistant
 * @since 2024-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_as_archive_permission")
@ApiModel(value = "AsArchivePermission对象", description = "档案权限管理表")
public class AsArchivePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 基本信息字段 ====================

    @ApiModelProperty(value = "权限ID，主键")
    @TableId(value = "permission_id", type = IdType.ASSIGN_ID)
    private Long permissionId;

    @ApiModelProperty(value = "权限编码，唯一标识")
    @TableField("permission_code")
    private String permissionCode;

    @ApiModelProperty(value = "权限名称")
    @TableField("permission_name")
    private String permissionName;

    @ApiModelProperty(value = "权限描述")
    @TableField("permission_description")
    private String permissionDescription;

    @ApiModelProperty(value = "权限类型：RESOURCE-资源权限/OPERATION-操作权限/DATA-数据权限/FUNCTION-功能权限")
    @TableField("permission_type")
    private String permissionType;

    @ApiModelProperty(value = "权限级别：SYSTEM-系统级/MODULE-模块级/RESOURCE-资源级/RECORD-记录级")
    @TableField("permission_level")
    private String permissionLevel;

    // ==================== 资源信息字段 ====================

    @ApiModelProperty(value = "资源类型：ARCHIVE-档案/DOCUMENT-文档/FOLDER-文件夹/CATEGORY-分类/SYSTEM-系统")
    @TableField("resource_type")
    private String resourceType;

    @ApiModelProperty(value = "资源ID")
    @TableField("resource_id")
    private String resourceId;

    @ApiModelProperty(value = "资源名称")
    @TableField("resource_name")
    private String resourceName;

    @ApiModelProperty(value = "资源路径")
    @TableField("resource_path")
    private String resourcePath;

    @ApiModelProperty(value = "资源范围，JSON格式存储")
    @TableField("resource_scope")
    private String resourceScope;

    // ==================== 主体信息字段 ====================

    @ApiModelProperty(value = "主体类型：USER-用户/ROLE-角色/GROUP-用户组/DEPARTMENT-部门/ORGANIZATION-组织")
    @TableField("subject_type")
    private String subjectType;

    @ApiModelProperty(value = "主体ID")
    @TableField("subject_id")
    private String subjectId;

    @ApiModelProperty(value = "主体名称")
    @TableField("subject_name")
    private String subjectName;

    @ApiModelProperty(value = "主体描述")
    @TableField("subject_description")
    private String subjectDescription;

    // ==================== 权限配置字段 ====================

    @ApiModelProperty(value = "操作权限：READ-读取/WRITE-写入/DELETE-删除/EXECUTE-执行/ADMIN-管理")
    @TableField("operation_permissions")
    private String operationPermissions;

    @ApiModelProperty(value = "数据权限：ALL-全部数据/DEPT-部门数据/SELF-个人数据/CUSTOM-自定义数据")
    @TableField("data_permissions")
    private String dataPermissions;

    @ApiModelProperty(value = "字段权限，JSON格式存储")
    @TableField("field_permissions")
    private String fieldPermissions;

    @ApiModelProperty(value = "条件权限，JSON格式存储")
    @TableField("condition_permissions")
    private String conditionPermissions;

    @ApiModelProperty(value = "时间权限，JSON格式存储")
    @TableField("time_permissions")
    private String timePermissions;

    // ==================== 权限控制字段 ====================

    @ApiModelProperty(value = "访问级别：PUBLIC-公开/INTERNAL-内部/CONFIDENTIAL-机密/SECRET-秘密/TOP_SECRET-绝密")
    @TableField("access_level")
    private String accessLevel;

    @ApiModelProperty(value = "安全等级：LOW-低/MEDIUM-中/HIGH-高/CRITICAL-关键")
    @TableField("security_level")
    private String securityLevel;

    @ApiModelProperty(value = "权限优先级：1-最高/2-高/3-中/4-低/5-最低")
    @TableField("permission_priority")
    private Integer permissionPriority;

    @ApiModelProperty(value = "权限权重：0-100")
    @TableField("permission_weight")
    private Integer permissionWeight;

    // ==================== 继承和委托字段 ====================

    @ApiModelProperty(value = "是否可继承：0-否/1-是")
    @TableField("is_inheritable")
    private Integer isInheritable;

    @ApiModelProperty(value = "是否可委托：0-否/1-是")
    @TableField("is_delegatable")
    private Integer isDelegatable;

    @ApiModelProperty(value = "父权限ID")
    @TableField("parent_permission_id")
    private Long parentPermissionId;

    @ApiModelProperty(value = "继承路径")
    @TableField("inheritance_path")
    private String inheritancePath;

    @ApiModelProperty(value = "委托人ID")
    @TableField("delegator_id")
    private String delegatorId;

    @ApiModelProperty(value = "被委托人ID")
    @TableField("delegate_id")
    private String delegateId;

    // ==================== 生效控制字段 ====================

    @ApiModelProperty(value = "生效时间")
    @TableField("effective_time")
    private LocalDateTime effectiveTime;

    @ApiModelProperty(value = "失效时间")
    @TableField("expiry_time")
    private LocalDateTime expiryTime;

    @ApiModelProperty(value = "是否永久有效：0-否/1-是")
    @TableField("is_permanent")
    private Integer isPermanent;

    @ApiModelProperty(value = "权限状态：ACTIVE-激活/INACTIVE-停用/SUSPENDED-暂停/EXPIRED-过期/REVOKED-撤销")
    @TableField("permission_status")
    private String permissionStatus;

    // ==================== 审批流程字段 ====================

    @ApiModelProperty(value = "是否需要审批：0-否/1-是")
    @TableField("requires_approval")
    private Integer requiresApproval;

    @ApiModelProperty(value = "审批状态：PENDING-待审批/APPROVED-已审批/REJECTED-已拒绝/CANCELLED-已取消")
    @TableField("approval_status")
    private String approvalStatus;

    @ApiModelProperty(value = "审批流程ID")
    @TableField("approval_process_id")
    private String approvalProcessId;

    @ApiModelProperty(value = "审批人ID")
    @TableField("approver_id")
    private String approverId;

    @ApiModelProperty(value = "审批时间")
    @TableField("approval_time")
    private LocalDateTime approvalTime;

    @ApiModelProperty(value = "审批意见")
    @TableField("approval_comment")
    private String approvalComment;

    // ==================== 使用统计字段 ====================

    @ApiModelProperty(value = "使用次数")
    @TableField("usage_count")
    private Long usageCount;

    @ApiModelProperty(value = "最后使用时间")
    @TableField("last_used_time")
    private LocalDateTime lastUsedTime;

    @ApiModelProperty(value = "最后访问IP")
    @TableField("last_access_ip")
    private String lastAccessIp;

    @ApiModelProperty(value = "访问设备信息")
    @TableField("access_device_info")
    private String accessDeviceInfo;

    // ==================== 风险控制字段 ====================

    @ApiModelProperty(value = "风险等级：LOW-低风险/MEDIUM-中风险/HIGH-高风险/CRITICAL-严重风险")
    @TableField("risk_level")
    private String riskLevel;

    @ApiModelProperty(value = "风险评分：0-100")
    @TableField("risk_score")
    private Integer riskScore;

    @ApiModelProperty(value = "风险描述")
    @TableField("risk_description")
    private String riskDescription;

    @ApiModelProperty(value = "合规状态：COMPLIANT-合规/NON_COMPLIANT-不合规/UNDER_REVIEW-审查中")
    @TableField("compliance_status")
    private String complianceStatus;

    // ==================== 监控告警字段 ====================

    @ApiModelProperty(value = "是否启用监控：0-否/1-是")
    @TableField("is_monitored")
    private Integer isMonitored;

    @ApiModelProperty(value = "告警阈值，JSON格式存储")
    @TableField("alert_thresholds")
    private String alertThresholds;

    @ApiModelProperty(value = "告警配置，JSON格式存储")
    @TableField("alert_config")
    private String alertConfig;

    @ApiModelProperty(value = "最后告警时间")
    @TableField("last_alert_time")
    private LocalDateTime lastAlertTime;

    // ==================== 扩展字段 ====================

    @ApiModelProperty(value = "权限标签，JSON格式存储")
    @TableField("permission_tags")
    private String permissionTags;

    @ApiModelProperty(value = "权限属性，JSON格式存储")
    @TableField("permission_attributes")
    private String permissionAttributes;

    @ApiModelProperty(value = "扩展配置，JSON格式存储")
    @TableField("extended_config")
    private String extendedConfig;

    @ApiModelProperty(value = "业务数据，JSON格式存储")
    @TableField("business_data")
    private String businessData;

    @ApiModelProperty(value = "备注信息")
    @TableField("remarks")
    private String remarks;

    // ==================== 审计字段 ====================

    @ApiModelProperty(value = "租户ID")
    @TableField("tenant_id")
    private String tenantId;

    @ApiModelProperty(value = "创建人ID")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "更新人ID")
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "版本号")
    @Version
    @TableField("version")
    private Integer version;

    @ApiModelProperty(value = "是否删除：0-否/1-是")
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    // ==================== 非数据库字段 ====================

    @ApiModelProperty(value = "子权限列表")
    @TableField(exist = false)
    private List<AsArchivePermission> childPermissions;

    @ApiModelProperty(value = "父权限信息")
    @TableField(exist = false)
    private AsArchivePermission parentPermission;

    @ApiModelProperty(value = "权限详情")
    @TableField(exist = false)
    private Map<String, Object> permissionDetails;

    @ApiModelProperty(value = "使用统计")
    @TableField(exist = false)
    private Map<String, Object> usageStatistics;

    @ApiModelProperty(value = "风险评估结果")
    @TableField(exist = false)
    private Map<String, Object> riskAssessment;

    @ApiModelProperty(value = "操作历史")
    @TableField(exist = false)
    private List<Map<String, Object>> operationHistory;
}
