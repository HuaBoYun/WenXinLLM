package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 标书模板表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bid_template")
public class BidTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模板编号
     */
    @TableField("template_no")
    private String templateNo;

    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;

    /**
     * 模板类型(1:技术标,2:商务标,3:资格标,4:综合标)
     */
    @TableField("template_type")
    private Integer templateType;

    /**
     * 适用行业
     */
    @TableField("applicable_industry")
    private String applicableIndustry;

    /**
     * 适用项目类型
     */
    @TableField("applicable_project_type")
    private String applicableProjectType;

    /**
     * 模板描述
     */
    @TableField("template_description")
    private String templateDescription;

    /**
     * 模板内容
     */
    @TableField("template_content")
    private String templateContent;

    /**
     * 文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件大小（字节）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 版本号
     */
    @TableField("version")
    private String version;

    /**
     * 是否启用(0:否,1:是)
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 是否默认模板(0:否,1:是)
     */
    @TableField("is_default")
    private Integer isDefault;

    /**
     * 使用次数
     */
    @TableField("usage_count")
    private Integer usageCount;

    /**
     * 最后使用时间
     */
    @TableField("last_used_time")
    private Date lastUsedTime;

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
     * 下载次数
     */
    @TableField("download_count")
    private Integer downloadCount;

    /**
     * 评分
     */
    @TableField("rating_score")
    private Double ratingScore;

    /**
     * 评分次数
     */
    @TableField("rating_count")
    private Integer ratingCount;

    /**
     * 关键词
     */
    @TableField("keywords")
    private String keywords;

    /**
     * 最后使用日期
     */
    @TableField("last_used_date")
    private Date lastUsedDate;

    /**
     * 模板状态
     */
    @TableField("template_status")
    private Integer templateStatus;

    /**
     * 获取模板类型名称
     */
    public String getTemplateTypeName() {
        if (templateType == null) {
            return "";
        }
        switch (templateType) {
            case 1:
                return "技术标";
            case 2:
                return "商务标";
            case 3:
                return "资格标";
            case 4:
                return "综合标";
            default:
                return "未知";
        }
    }

    /**
     * 获取启用状态名称
     */
    public String getIsEnabledName() {
        if (isEnabled == null) {
            return "否";
        }
        return isEnabled == 1 ? "是" : "否";
    }

    /**
     * 获取默认模板状态名称
     */
    public String getIsDefaultName() {
        if (isDefault == null) {
            return "否";
        }
        return isDefault == 1 ? "是" : "否";
    }

    /**
     * 判断是否启用
     */
    public boolean isEnabled() {
        return isEnabled != null && isEnabled == 1;
    }

    /**
     * 判断是否为默认模板
     */
    public boolean isDefault() {
        return isDefault != null && isDefault == 1;
    }

    /**
     * 获取文件大小显示文本
     */
    public String getFileSizeText() {
        if (fileSize == null) {
            return "未知";
        }
        if (fileSize < 1024) {
            return fileSize + "B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.2fKB", fileSize / 1024.0);
        } else if (fileSize < 1024 * 1024 * 1024) {
            return String.format("%.2fMB", fileSize / (1024.0 * 1024.0));
        } else {
            return String.format("%.2fGB", fileSize / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 获取模板类型颜色
     */
    public String getTemplateTypeColor() {
        if (templateType == null) {
            return "#909399";
        }
        switch (templateType) {
            case 1:
                return "#409EFF"; // 蓝色
            case 2:
                return "#67C23A"; // 绿色
            case 3:
                return "#E6A23C"; // 橙色
            case 4:
                return "#F56C6C"; // 红色
            default:
                return "#909399";
        }
    }

    /**
     * 判断是否为常用模板（使用次数>=10）
     */
    public boolean isPopularTemplate() {
        return usageCount != null && usageCount >= 10;
    }

    /**
     * 判断是否为新模板（创建时间<=7天）
     */
    public boolean isNewTemplate() {
        if (createTime == null) {
            return false;
        }
        long diff = new Date().getTime() - createTime.getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        return days <= 7;
    }

    /**
     * 判断是否长期未使用（最后使用时间>=30天前）
     */
    public boolean isLongTimeUnused() {
        if (lastUsedTime == null) {
            return createTime != null && isLongTimeUnusedSinceCreate();
        }
        long diff = new Date().getTime() - lastUsedTime.getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        return days >= 30;
    }

    /**
     * 判断创建后是否长期未使用
     */
    private boolean isLongTimeUnusedSinceCreate() {
        long diff = new Date().getTime() - createTime.getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        return days >= 30;
    }

    /**
     * 增加使用次数
     */
    public void incrementUsageCount() {
        if (usageCount == null) {
            usageCount = 0;
        }
        usageCount++;
        lastUsedTime = new Date();
    }

    /**
     * 获取文件扩展名
     */
    public String getFileExtension() {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 判断是否为Word文档
     */
    public boolean isWordDocument() {
        String extension = getFileExtension();
        return "doc".equals(extension) || "docx".equals(extension);
    }

    /**
     * 判断是否为PDF文档
     */
    public boolean isPdfDocument() {
        return "pdf".equals(getFileExtension());
    }

    /**
     * 判断是否为Excel文档
     */
    public boolean isExcelDocument() {
        String extension = getFileExtension();
        return "xls".equals(extension) || "xlsx".equals(extension);
    }
}
