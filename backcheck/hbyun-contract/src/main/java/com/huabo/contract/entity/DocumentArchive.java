package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 文档归档表实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("document_archive")
public class DocumentArchive implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private String projectId;

    /**
     * 文档编号
     */
    @TableField("document_no")
    private String documentNo;

    /**
     * 文档名称
     */
    @TableField("document_name")
    private String documentName;

    /**
     * 文档类型(1:合同文件,2:技术文件,3:管理文件,4:财务文件)
     */
    @TableField("document_type")
    private Integer documentType;

    /**
     * 文档分类
     */
    @TableField("document_category")
    private String documentCategory;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件大小(字节)
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件格式
     */
    @TableField("file_format")
    private String fileFormat;

    /**
     * 版本号
     */
    @TableField("version_no")
    private String versionNo;

    /**
     * 归档日期
     */
    @TableField("archive_date")
    private Date archiveDate;

    /**
     * 归档人ID
     */
    @TableField("archiver_id")
    private Long archiverId;

    /**
     * 存储位置
     */
    @TableField("storage_location")
    private String storageLocation;

    /**
     * 保存期限(年)
     */
    @TableField("retention_period")
    private Integer retentionPeriod;

    /**
     * 访问级别(1:公开,2:内部,3:机密)
     */
    @TableField("access_level")
    private Integer accessLevel;

    /**
     * 文档状态(1:有效,2:作废,3:归档)
     */
    @TableField("document_status")
    private Integer documentStatus;

    /**
     * 关键词
     */
    @TableField("keywords")
    private String keywords;

    /**
     * 文档摘要
     */
    @TableField("document_summary")
    private String documentSummary;

    /**
     * 相关文档
     */
    @TableField("related_documents")
    private String relatedDocuments;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 删除标记(0:未删除,1:已删除)
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 获取文档类型描述
     */
    public String getDocumentTypeDesc() {
        if (documentType == null) {
            return "未知";
        }
        switch (documentType) {
            case 1:
                return "合同文件";
            case 2:
                return "技术文件";
            case 3:
                return "管理文件";
            case 4:
                return "财务文件";
            default:
                return "其他";
        }
    }

    /**
     * 获取访问级别描述
     */
    public String getAccessLevelDesc() {
        if (accessLevel == null) {
            return "未知";
        }
        switch (accessLevel) {
            case 1:
                return "公开";
            case 2:
                return "内部";
            case 3:
                return "机密";
            default:
                return "未知";
        }
    }

    /**
     * 获取文档状态描述
     */
    public String getDocumentStatusDesc() {
        if (documentStatus == null) {
            return "未知";
        }
        switch (documentStatus) {
            case 1:
                return "有效";
            case 2:
                return "作废";
            case 3:
                return "归档";
            default:
                return "未知";
        }
    }

    /**
     * 获取文件大小描述
     */
    public String getFileSizeDesc() {
        if (fileSize == null || fileSize == 0) {
            return "0 B";
        }
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = fileSize.doubleValue();
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    /**
     * 判断是否为图片文件
     */
    public boolean isImageFile() {
        if (fileFormat == null) {
            return false;
        }
        String format = fileFormat.toLowerCase();
        return format.equals("jpg") || format.equals("jpeg") || format.equals("png") || 
               format.equals("gif") || format.equals("bmp") || format.equals("webp");
    }

    /**
     * 判断是否为文档文件
     */
    public boolean isDocumentFile() {
        if (fileFormat == null) {
            return false;
        }
        String format = fileFormat.toLowerCase();
        return format.equals("pdf") || format.equals("doc") || format.equals("docx") || 
               format.equals("xls") || format.equals("xlsx") || format.equals("ppt") || format.equals("pptx");
    }

    /**
     * 判断是否为压缩文件
     */
    public boolean isArchiveFile() {
        if (fileFormat == null) {
            return false;
        }
        String format = fileFormat.toLowerCase();
        return format.equals("zip") || format.equals("rar") || format.equals("7z") || format.equals("tar");
    }
}
