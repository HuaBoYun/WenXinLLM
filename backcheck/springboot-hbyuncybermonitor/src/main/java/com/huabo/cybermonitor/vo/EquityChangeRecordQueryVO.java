package com.huabo.cybermonitor.vo;

import java.math.BigDecimal;

import com.huabo.cybermonitor.util.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 股权变动记录查询参数VO
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EquityChangeRecordQueryVO extends BaseVo {

    /**
     * 变动记录ID
     */
    private String changeId;

    /**
     * 股权结构ID
     */
    private String equityId;

    /**
     * 被投资企业ID
     */
    private String investeeEnterpriseId;

    /**
     * 被投资企业名称
     */
    private String investeeEnterpriseName;

    /**
     * 投资方企业ID
     */
    private String investorEnterpriseId;

    /**
     * 投资方企业名称
     */
    private String investorEnterpriseName;

    /**
     * 变动类型
     */
    private String changeType;

    /**
     * 变动原因
     */
    private String changeReason;

    /**
     * 最小变动金额（万元）
     */
    private BigDecimal minChangeAmount;

    /**
     * 最大变动金额（万元）
     */
    private BigDecimal maxChangeAmount;

    /**
     * 转让方企业ID
     */
    private String transferorEnterpriseId;

    /**
     * 转让方企业名称
     */
    private String transferorEnterpriseName;

    /**
     * 受让方企业ID
     */
    private String transfereeEnterpriseId;

    /**
     * 受让方企业名称
     */
    private String transfereeEnterpriseName;

    /**
     * 转让方式
     */
    private String transferMethod;

    /**
     * 变动开始日期
     */
    private String changeStartDate;

    /**
     * 变动结束日期
     */
    private String changeEndDate;

    /**
     * 生效开始日期
     */
    private String effectiveStartDate;

    /**
     * 生效结束日期
     */
    private String effectiveEndDate;

    /**
     * 审批状态
     */
    private String approvalStatus;

    /**
     * 审批机关
     */
    private String approvalAuthority;

    /**
     * 是否重大变动
     */
    private Boolean isMajorChange;

    /**
     * 是否需要预警
     */
    private Boolean needWarning;

    /**
     * 预警级别
     */
    private String warningLevel;

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
