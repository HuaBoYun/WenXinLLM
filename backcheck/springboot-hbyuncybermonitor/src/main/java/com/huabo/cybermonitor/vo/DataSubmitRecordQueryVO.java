package com.huabo.cybermonitor.vo;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据报送记录查询VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataSubmitRecordQueryVO extends BaseVo {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 企业ID
     */
    private String enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 报送状态
     */
    private String submitStatus;

    /**
     * 审核状态
     */
    private String reviewStatus;

    /**
     * 质量检查结果
     */
    private String qualityCheckResult;

    /**
     * 数据质量评分-最小值
     */
    private Integer minQualityScore;

    /**
     * 数据质量评分-最大值
     */
    private Integer maxQualityScore;

    /**
     * 报送时间-起始
     */
    private String submitTimeBegin;

    /**
     * 报送时间-结束
     */
    private String submitTimeEnd;

    /**
     * 审核时间-起始
     */
    private String reviewTimeBegin;

    /**
     * 审核时间-结束
     */
    private String reviewTimeEnd;

    /**
     * 审核人
     */
    private String reviewer;

    /**
     * 文件大小-最小值
     */
    private Long minFileSize;

    /**
     * 文件大小-最大值
     */
    private Long maxFileSize;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向
     */
    private String orderDirection;
}
