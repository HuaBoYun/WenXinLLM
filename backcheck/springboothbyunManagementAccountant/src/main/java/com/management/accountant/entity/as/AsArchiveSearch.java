package com.management.accountant.entity.as;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 档案检索实体类
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_as_archive_search")
public class AsArchiveSearch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 检索ID
     */
    @TableId(value = "search_id", type = IdType.AUTO)
    private Long searchId;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 检索编号
     */
    @TableField("search_code")
    private String searchCode;

    /**
     * 检索名称
     */
    @TableField("search_name")
    private String searchName;

    /**
     * 检索描述
     */
    @TableField("search_description")
    private String searchDescription;

    /**
     * 检索类型：FULL_TEXT-全文检索, SEMANTIC-语义检索, IMAGE-图像检索, VOICE-语音检索, HYBRID-混合检索
     */
    @TableField("search_type")
    private String searchType;

    /**
     * 检索状态：ACTIVE-活跃, INACTIVE-非活跃, INDEXING-索引中, OPTIMIZING-优化中, ERROR-错误
     */
    @TableField("search_status")
    private String searchStatus;

    /**
     * 检索引擎：ELASTICSEARCH-ES引擎, SOLR-Solr引擎, LUCENE-Lucene引擎, CUSTOM-自定义引擎
     */
    @TableField("search_engine")
    private String searchEngine;

    /**
     * 索引名称
     */
    @TableField("index_name")
    private String indexName;

    /**
     * 索引配置
     */
    @TableField("index_config")
    private String indexConfig;

    /**
     * 检索配置
     */
    @TableField("search_config")
    private String searchConfig;

    /**
     * 检索查询语句
     */
    @TableField("search_query")
    private String searchQuery;

    /**
     * 检索关键词
     */
    @TableField("search_keywords")
    private String searchKeywords;

    /**
     * 检索过滤条件
     */
    @TableField("search_filters")
    private String searchFilters;

    /**
     * 检索排序规则
     */
    @TableField("search_sort")
    private String searchSort;

    /**
     * 检索结果数量
     */
    @TableField("result_count")
    private Integer resultCount;

    /**
     * 检索总耗时(毫秒)
     */
    @TableField("search_time")
    private Long searchTime;

    /**
     * 索引耗时(毫秒)
     */
    @TableField("index_time")
    private Long indexTime;

    /**
     * 检索准确率
     */
    @TableField("search_accuracy")
    private BigDecimal searchAccuracy;

    /**
     * 检索相关性得分
     */
    @TableField("relevance_score")
    private BigDecimal relevanceScore;

    /**
     * 检索置信度
     */
    @TableField("confidence_score")
    private BigDecimal confidenceScore;

    /**
     * 检索成功次数
     */
    @TableField("success_count")
    private Integer successCount;

    /**
     * 检索失败次数
     */
    @TableField("failure_count")
    private Integer failureCount;

    /**
     * 检索总次数
     */
    @TableField("total_count")
    private Integer totalCount;

    /**
     * 平均响应时间(毫秒)
     */
    @TableField("avg_response_time")
    private Long avgResponseTime;

    /**
     * 最大响应时间(毫秒)
     */
    @TableField("max_response_time")
    private Long maxResponseTime;

    /**
     * 最小响应时间(毫秒)
     */
    @TableField("min_response_time")
    private Long minResponseTime;

    /**
     * 索引文档数量
     */
    @TableField("indexed_docs")
    private Long indexedDocs;

    /**
     * 索引大小(字节)
     */
    @TableField("index_size")
    private Long indexSize;

    /**
     * 索引版本
     */
    @TableField("index_version")
    private String indexVersion;

    /**
     * 索引状态：BUILDING-构建中, READY-就绪, UPDATING-更新中, OPTIMIZING-优化中, ERROR-错误
     */
    @TableField("index_status")
    private String indexStatus;

    /**
     * 最后索引时间
     */
    @TableField("last_index_time")
    private LocalDateTime lastIndexTime;

    /**
     * 最后检索时间
     */
    @TableField("last_search_time")
    private LocalDateTime lastSearchTime;

    /**
     * OCR识别配置
     */
    @TableField("ocr_config")
    private String ocrConfig;

    /**
     * OCR识别准确率
     */
    @TableField("ocr_accuracy")
    private BigDecimal ocrAccuracy;

    /**
     * 语义检索配置
     */
    @TableField("semantic_config")
    private String semanticConfig;

    /**
     * 语义模型版本
     */
    @TableField("semantic_model_version")
    private String semanticModelVersion;

    /**
     * 相似度阈值
     */
    @TableField("similarity_threshold")
    private BigDecimal similarityThreshold;

    /**
     * 推荐算法：COLLABORATIVE-协同过滤, CONTENT_BASED-基于内容, HYBRID-混合推荐
     */
    @TableField("recommendation_algorithm")
    private String recommendationAlgorithm;

    /**
     * 推荐配置
     */
    @TableField("recommendation_config")
    private String recommendationConfig;

    /**
     * 推荐准确率
     */
    @TableField("recommendation_accuracy")
    private BigDecimal recommendationAccuracy;

    /**
     * 性能优化配置
     */
    @TableField("performance_config")
    private String performanceConfig;

    /**
     * 缓存配置
     */
    @TableField("cache_config")
    private String cacheConfig;

    /**
     * 缓存命中率
     */
    @TableField("cache_hit_rate")
    private BigDecimal cacheHitRate;

    /**
     * 检索历史保留天数
     */
    @TableField("history_retention_days")
    private Integer historyRetentionDays;

    /**
     * 统计分析配置
     */
    @TableField("analytics_config")
    private String analyticsConfig;

    /**
     * 热门关键词
     */
    @TableField("popular_keywords")
    private String popularKeywords;

    /**
     * 检索趋势数据
     */
    @TableField("search_trends")
    private String searchTrends;

    /**
     * 用户行为数据
     */
    @TableField("user_behavior")
    private String userBehavior;

    /**
     * API服务配置
     */
    @TableField("api_config")
    private String apiConfig;

    /**
     * API调用次数
     */
    @TableField("api_call_count")
    private Long apiCallCount;

    /**
     * API成功率
     */
    @TableField("api_success_rate")
    private BigDecimal apiSuccessRate;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 错误次数
     */
    @TableField("error_count")
    private Integer errorCount;

    /**
     * 最后错误时间
     */
    @TableField("last_error_time")
    private LocalDateTime lastErrorTime;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

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
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
