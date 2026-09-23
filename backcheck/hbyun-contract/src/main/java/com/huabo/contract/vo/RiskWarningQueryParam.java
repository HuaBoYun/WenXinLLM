package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险预警查询参数
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RiskWarningQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 评估名称
     */
    private String assessmentName;

    /**
     * 风险等级
     * 1-低风险，2-中风险，3-高风险，4-极高风险
     */
    private Integer riskLevel;

    /**
     * 最小风险等级
     */
    private Integer minRiskLevel;

    /**
     * 最大风险等级
     */
    private Integer maxRiskLevel;

    /**
     * 评估类型
     * 1-承接前，2-执行中，3-结项后
     */
    private Integer assessmentType;

    /**
     * 预警状态
     * 1-待处理，2-处理中，3-已处理，4-已忽略
     */
    private Integer warningStatus;

    /**
     * 相对方ID
     */
    private Long counterpartId;

    /**
     * 相对方名称
     */
    private String counterpartName;

    /**
     * 评估开始日期
     */
    private Date startDate;

    /**
     * 评估结束日期
     */
    private Date endDate;

    /**
     * 创建开始时间
     */
    private Date createStartTime;

    /**
     * 创建结束时间
     */
    private Date createEndTime;

    /**
     * 评估人ID
     */
    private Long assessorId;

    /**
     * 评估人姓名
     */
    private String assessorName;

    /**
     * 风险分类
     * 1-相对方资格资信，2-政策风险，3-资金来源，4-技术质量，5-法律风险，6-收款风险，7-税务风险，8-施工条件及环境
     */
    private Integer riskCategory;

    /**
     * 最小风险评分
     */
    private Double minRiskScore;

    /**
     * 最大风险评分
     */
    private Double maxRiskScore;

    /**
     * 是否只查询高风险
     */
    private Boolean highRiskOnly;

    /**
     * 是否只查询未处理的预警
     */
    private Boolean unhandledOnly;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方向
     * ASC-升序，DESC-降序
     */
    private String orderDirection;

    /**
     * 是否包含风险详情
     */
    private Boolean includeDetails;

    /**
     * 预警类型
     */
    private String warningType;

    /**
     * 行业类型
     */
    private String industryType;

    /**
     * 项目规模
     */
    private String projectScale;

    /**
     * 地区代码
     */
    private String regionCode;

    /**
     * 是否紧急预警
     */
    private Boolean urgent;

    /**
     * 预警级别
     */
    private Integer warningLevel;

    /**
     * 责任人ID
     */
    private Long responsiblePersonId;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 标签
     */
    private String tags;

    /**
     * 自定义字段1
     */
    private String customField1;

    /**
     * 自定义字段2
     */
    private String customField2;

    /**
     * 自定义字段3
     */
    private String customField3;
}
