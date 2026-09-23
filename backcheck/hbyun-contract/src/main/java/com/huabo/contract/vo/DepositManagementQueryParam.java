package com.huabo.contract.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 保证金管理查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DepositManagementQueryParam extends BaseQueryParam {

    /**
     * 招投标项目ID
     */
    private Long biddingProjectId;

    /**
     * 保证金编号
     */
    private String depositNo;

    /**
     * 保证金类型(1:投标保证金,2:履约保证金,3:质量保证金,4:其他)
     */
    private Integer depositType;

    /**
     * 保证金金额最小值
     */
    private BigDecimal depositAmountMin;

    /**
     * 保证金金额最大值
     */
    private BigDecimal depositAmountMax;

    /**
     * 缴纳方式(1:现金,2:银行保函,3:保险保函,4:其他)
     */
    private Integer paymentMethod;

    /**
     * 保证金状态(1:已缴纳,2:已退还,3:已没收,4:已转履约,5:逾期)
     */
    private Integer depositStatus;

    /**
     * 缴纳时间开始
     */
    private Date paymentTimeStart;

    /**
     * 缴纳时间结束
     */
    private Date paymentTimeEnd;

    /**
     * 到期时间开始
     */
    private Date expiryTimeStart;

    /**
     * 到期时间结束
     */
    private Date expiryTimeEnd;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 保函编号
     */
    private String guaranteeNo;

    /**
     * 负责人ID
     */
    private Long managerId;

    /**
     * 负责人姓名
     */
    private String managerName;

    /**
     * 退还时间开始
     */
    private Date refundTimeStart;

    /**
     * 退还时间结束
     */
    private Date refundTimeEnd;

    /**
     * 创建时间开始
     */
    private Date createTimeStart;

    /**
     * 创建时间结束
     */
    private Date createTimeEnd;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 关键词搜索（保证金编号、银行名称、保函编号）
     */
    private String keyword;

    /**
     * 是否即将到期（7天内）
     */
    private Boolean expiringSoon;

    /**
     * 是否已逾期
     */
    private Boolean overdue;
}
