package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实际控制人查询参数VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ActualControllerQueryVO extends BaseVo {

    /**
     * 实际控制人ID
     */
    private String controllerId;

    /**
     * 被控制企业ID
     */
    private String controlledEnterpriseId;

    /**
     * 被控制企业名称
     */
    private String controlledEnterpriseName;

    /**
     * 控制人企业ID
     */
    private String controllerEnterpriseId;

    /**
     * 控制人企业名称
     */
    private String controllerEnterpriseName;

    /**
     * 控制人类型
     */
    private String controllerType;

    /**
     * 控制人性质
     */
    private String controllerNature;

    /**
     * 控制方式
     */
    private String controlMethod;

    /**
     * 最小综合持股比例（%）
     */
    private BigDecimal minTotalShareholdingRatio;

    /**
     * 最大综合持股比例（%）
     */
    private BigDecimal maxTotalShareholdingRatio;

    /**
     * 最小表决权比例（%）
     */
    private BigDecimal minVotingRightRatio;

    /**
     * 最大表决权比例（%）
     */
    private BigDecimal maxVotingRightRatio;

    /**
     * 最小控制层级
     */
    private Integer minControlLevel;

    /**
     * 最大控制层级
     */
    private Integer maxControlLevel;

    /**
     * 是否最终控制人
     */
    private Boolean isUltimateController;

    /**
     * 是否一致行动人
     */
    private Boolean isConcertedAction;

    /**
     * 控制开始日期
     */
    private String controlStartDate;

    /**
     * 控制结束日期
     */
    private String controlEndDate;

    /**
     * 控制状态
     */
    private String controlStatus;

    /**
     * 控制稳定性
     */
    private String controlStability;

    /**
     * 控制风险等级
     */
    private String controlRiskLevel;

    /**
     * 监管关注度
     */
    private String regulatoryAttention;

    /**
     * 是否需要特别监管
     */
    private Boolean needSpecialSupervision;

    /**
     * 识别方法
     */
    private String identificationMethod;

    /**
     * 确认状态
     */
    private String confirmationStatus;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向
     */
    private String sortDirection;
}
