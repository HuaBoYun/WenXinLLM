package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 标书模板查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BidTemplateQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer pageNumber;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 模板编号
     */
    private String templateNo;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型(1:技术标,2:商务标,3:资格标,4:综合标)
     */
    private Integer templateType;

    /**
     * 适用行业
     */
    private String applicableIndustry;

    /**
     * 适用项目类型
     */
    private String applicableProjectType;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 版本号
     */
    private String version;

    /**
     * 是否启用(0:否,1:是)
     */
    private Integer isEnabled;

    /**
     * 是否默认模板(0:否,1:是)
     */
    private Integer isDefault;

    /**
     * 最小使用次数
     */
    private Integer minUsageCount;

    /**
     * 最大使用次数
     */
    private Integer maxUsageCount;

    /**
     * 最后使用开始时间
     */
    private Date lastUsedStartTime;

    /**
     * 最后使用结束时间
     */
    private Date lastUsedEndTime;

    /**
     * 创建开始时间
     */
    private Date createStartTime;

    /**
     * 创建结束时间
     */
    private Date createEndTime;

    /**
     * 更新开始时间
     */
    private Date updateStartTime;

    /**
     * 更新结束时间
     */
    private Date updateEndTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 更新人姓名
     */
    private String updateByName;

    /**
     * 是否只查询启用的模板
     */
    private Boolean onlyEnabled;

    /**
     * 是否只查询默认模板
     */
    private Boolean onlyDefault;

    /**
     * 是否只查询常用模板
     */
    private Boolean onlyPopular;

    /**
     * 是否只查询新模板
     */
    private Boolean onlyNew;

    /**
     * 是否只查询长期未使用的模板
     */
    private Boolean onlyLongTimeUnused;

    /**
     * 是否只查询Word文档
     */
    private Boolean onlyWordDocument;

    /**
     * 是否只查询PDF文档
     */
    private Boolean onlyPdfDocument;

    /**
     * 是否只查询Excel文档
     */
    private Boolean onlyExcelDocument;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向（ASC/DESC）
     */
    private String orderDirection;

    /**
     * 关键词搜索（模板编号、模板名称、文件名称）
     */
    private String keyword;

    /**
     * 最小文件大小（字节）
     */
    private Long minFileSize;

    /**
     * 最大文件大小（字节）
     */
    private Long maxFileSize;
}
