package com.management.accountant.entity.as;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档版本管理实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tbl_as_document_version")
public class AsDocumentVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本ID（主键）
     */
    @TableId(value = "version_id", type = IdType.ASSIGN_ID)
    private String versionId;

    /**
     * 版本编号（唯一）
     */
    @TableField("version_code")
    private String versionCode;

    /**
     * 版本名称
     */
    @TableField("version_name")
    private String versionName;

    /**
     * 版本描述
     */
    @TableField("version_description")
    private String versionDescription;

    /**
     * 文档ID（关联文档）
     */
    @TableField("document_id")
    private String documentId;

    /**
     * 文档名称
     */
    @TableField("document_name")
    private String documentName;

    /**
     * 文档类型：PDF/DOC/DOCX/XLS/XLSX/PPT/PPTX/TXT/IMAGE/VIDEO/AUDIO/OTHER
     */
    @TableField("document_type")
    private String documentType;

    /**
     * 版本类型：MAJOR/MINOR/PATCH/DRAFT/RELEASE/HOTFIX/BRANCH/TAG
     */
    @TableField("version_type")
    private String versionType;

    /**
     * 版本状态：DRAFT/UNDER_REVIEW/APPROVED/PUBLISHED/ARCHIVED/DEPRECATED/DELETED
     */
    @TableField("version_status")
    private String versionStatus;

    /**
     * 版本号（语义化版本）
     */
    @TableField("version_number")
    private String versionNumber;

    /**
     * 主版本号
     */
    @TableField("major_version")
    private Integer majorVersion;

    /**
     * 次版本号
     */
    @TableField("minor_version")
    private Integer minorVersion;

    /**
     * 补丁版本号
     */
    @TableField("patch_version")
    private Integer patchVersion;

    /**
     * 构建版本号
     */
    @TableField("build_version")
    private String buildVersion;

    /**
     * 父版本ID
     */
    @TableField("parent_version_id")
    private String parentVersionId;

    /**
     * 基础版本ID（分支来源）
     */
    @TableField("base_version_id")
    private String baseVersionId;

    /**
     * 分支名称
     */
    @TableField("branch_name")
    private String branchName;

    /**
     * 标签名称
     */
    @TableField("tag_name")
    private String tagName;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件大小（字节）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件格式
     */
    @TableField("file_format")
    private String fileFormat;

    /**
     * 文件MD5值
     */
    @TableField("file_md5")
    private String fileMd5;

    /**
     * 文件SHA256值
     */
    @TableField("file_sha256")
    private String fileSha256;

    /**
     * 存储位置：LOCAL/CLOUD/HYBRID/DISTRIBUTED
     */
    @TableField("storage_location")
    private String storageLocation;

    /**
     * 存储路径
     */
    @TableField("storage_path")
    private String storagePath;

    /**
     * 备份路径
     */
    @TableField("backup_path")
    private String backupPath;

    /**
     * 变更类型：CREATE/UPDATE/DELETE/MERGE/BRANCH/TAG/ROLLBACK
     */
    @TableField("change_type")
    private String changeType;

    /**
     * 变更描述
     */
    @TableField("change_description")
    private String changeDescription;

    /**
     * 变更原因
     */
    @TableField("change_reason")
    private String changeReason;

    /**
     * 变更影响
     */
    @TableField("change_impact")
    private String changeImpact;

    /**
     * 变更日志
     */
    @TableField("change_log")
    private String changeLog;

    /**
     * 提交信息
     */
    @TableField("commit_message")
    private String commitMessage;

    /**
     * 提交哈希
     */
    @TableField("commit_hash")
    private String commitHash;

    /**
     * 是否当前版本
     */
    @TableField("is_current")
    private Boolean isCurrent;

    /**
     * 是否默认版本
     */
    @TableField("is_default")
    private Boolean isDefault;

    /**
     * 是否锁定版本
     */
    @TableField("is_locked")
    private Boolean isLocked;

    /**
     * 是否发布版本
     */
    @TableField("is_published")
    private Boolean isPublished;

    /**
     * 是否归档版本
     */
    @TableField("is_archived")
    private Boolean isArchived;

    /**
     * 访问级别：PUBLIC/INTERNAL/CONFIDENTIAL/SECRET
     */
    @TableField("access_level")
    private String accessLevel;

    /**
     * 权限配置
     */
    @TableField("permission_config")
    private String permissionConfig;

    /**
     * 审批状态：PENDING/APPROVED/REJECTED/CANCELLED
     */
    @TableField("approval_status")
    private String approvalStatus;

    /**
     * 审批流程ID
     */
    @TableField("approval_process_id")
    private String approvalProcessId;

    /**
     * 审批人ID
     */
    @TableField("approver_id")
    private String approverId;

    /**
     * 审批人姓名
     */
    @TableField("approver_name")
    private String approverName;

    /**
     * 审批时间
     */
    @TableField("approval_time")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @TableField("approval_comment")
    private String approvalComment;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /**
     * 发布人ID
     */
    @TableField("publisher_id")
    private String publisherId;

    /**
     * 发布人姓名
     */
    @TableField("publisher_name")
    private String publisherName;

    /**
     * 生效时间
     */
    @TableField("effective_time")
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    @TableField("expiry_time")
    private LocalDateTime expiryTime;

    /**
     * 保留期限（天）
     */
    @TableField("retention_days")
    private Integer retentionDays;

    /**
     * 归档时间
     */
    @TableField("archive_time")
    private LocalDateTime archiveTime;

    /**
     * 下载次数
     */
    @TableField("download_count")
    private Integer downloadCount;

    /**
     * 查看次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 编辑次数
     */
    @TableField("edit_count")
    private Integer editCount;

    /**
     * 最后访问时间
     */
    @TableField("last_access_time")
    private LocalDateTime lastAccessTime;

    /**
     * 最后修改时间
     */
    @TableField("last_modified_time")
    private LocalDateTime lastModifiedTime;

    /**
     * 版本大小（字节）
     */
    @TableField("version_size")
    private Long versionSize;

    /**
     * 压缩后大小（字节）
     */
    @TableField("compressed_size")
    private Long compressedSize;

    /**
     * 压缩比率
     */
    @TableField("compression_ratio")
    private Double compressionRatio;

    /**
     * 是否压缩
     */
    @TableField("is_compressed")
    private Boolean isCompressed;

    /**
     * 压缩算法：GZIP/ZIP/RAR/7Z/LZ4/ZSTD
     */
    @TableField("compression_algorithm")
    private String compressionAlgorithm;

    /**
     * 是否加密
     */
    @TableField("is_encrypted")
    private Boolean isEncrypted;

    /**
     * 加密算法：AES/DES/RSA/SM4
     */
    @TableField("encryption_algorithm")
    private String encryptionAlgorithm;

    /**
     * 加密密钥ID
     */
    @TableField("encryption_key_id")
    private String encryptionKeyId;

    /**
     * 校验和类型：MD5/SHA1/SHA256/CRC32
     */
    @TableField("checksum_type")
    private String checksumType;

    /**
     * 校验和值
     */
    @TableField("checksum_value")
    private String checksumValue;

    /**
     * 数字签名
     */
    @TableField("digital_signature")
    private String digitalSignature;

    /**
     * 签名算法
     */
    @TableField("signature_algorithm")
    private String signatureAlgorithm;

    /**
     * 签名证书
     */
    @TableField("signature_certificate")
    private String signatureCertificate;

    /**
     * 元数据信息
     */
    @TableField("metadata_info")
    private String metadataInfo;

    /**
     * 标签列表（JSON格式）
     */
    @TableField("tags")
    private String tags;

    /**
     * 关键词
     */
    @TableField("keywords")
    private String keywords;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private String categoryId;

    /**
     * 分类名称
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 部门ID
     */
    @TableField("department_id")
    private String departmentId;

    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;

    /**
     * 业务类型：CONTRACT/INVOICE/REPORT/CORRESPONDENCE/POLICY/PROCEDURE
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 业务状态：ACTIVE/INACTIVE/SUSPENDED/TERMINATED
     */
    @TableField("business_status")
    private String businessStatus;

    /**
     * 优先级：HIGH/MEDIUM/LOW/URGENT/NORMAL
     */
    @TableField("priority")
    private String priority;

    /**
     * 重要性：CRITICAL/HIGH/MEDIUM/LOW
     */
    @TableField("importance")
    private String importance;

    /**
     * 机密级别：PUBLIC/INTERNAL/CONFIDENTIAL/SECRET/TOP_SECRET
     */
    @TableField("confidentiality_level")
    private String confidentialityLevel;

    /**
     * 完整性级别：HIGH/MEDIUM/LOW
     */
    @TableField("integrity_level")
    private String integrityLevel;

    /**
     * 可用性级别：HIGH/MEDIUM/LOW
     */
    @TableField("availability_level")
    private String availabilityLevel;

    /**
     * 质量评分（0-100）
     */
    @TableField("quality_score")
    private Integer qualityScore;

    /**
     * 质量等级：EXCELLENT/GOOD/FAIR/POOR
     */
    @TableField("quality_grade")
    private String qualityGrade;

    /**
     * 合规状态：COMPLIANT/NON_COMPLIANT/UNDER_REVIEW/EXEMPT
     */
    @TableField("compliance_status")
    private String complianceStatus;

    /**
     * 合规检查时间
     */
    @TableField("compliance_check_time")
    private LocalDateTime complianceCheckTime;

    /**
     * 合规检查人
     */
    @TableField("compliance_checker")
    private String complianceChecker;

    /**
     * 风险等级：HIGH/MEDIUM/LOW/NONE
     */
    @TableField("risk_level")
    private String riskLevel;

    /**
     * 风险评估时间
     */
    @TableField("risk_assessment_time")
    private LocalDateTime riskAssessmentTime;

    /**
     * 风险评估人
     */
    @TableField("risk_assessor")
    private String riskAssessor;

    /**
     * 扩展字段1
     */
    @TableField("ext_field1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("ext_field2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("ext_field3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("ext_field4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("ext_field5")
    private String extField5;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

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
     * 创建人姓名
     */
    @TableField(value = "created_by_name", fill = FieldFill.INSERT)
    private String createdByName;

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
     * 更新人姓名
     */
    @TableField(value = "updated_by_name", fill = FieldFill.INSERT_UPDATE)
    private String updatedByName;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 是否删除（逻辑删除）
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    /**
     * 版本号（乐观锁）
     */
    @Version
    @TableField("version")
    private Integer version;

    // 非数据库字段

    /**
     * 子版本列表
     */
    @TableField(exist = false)
    private List<AsDocumentVersion> childVersions;

    /**
     * 父版本信息
     */
    @TableField(exist = false)
    private AsDocumentVersion parentVersion;

    /**
     * 版本差异信息
     */
    @TableField(exist = false)
    private String versionDiff;

    /**
     * 版本对比结果
     */
    @TableField(exist = false)
    private String comparisonResult;

    /**
     * 权限列表
     */
    @TableField(exist = false)
    private List<String> permissions;

    /**
     * 操作历史
     */
    @TableField(exist = false)
    private List<String> operationHistory;
}
