package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 股权变动记录实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("EQUITY_CHANGE_RECORD")
public class EquityChangeRecord {

    /**
     * 变动记录ID
     */
    @TableId(value = "CHANGE_ID", type = IdType.ASSIGN_UUID)
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
     * 变动前持股比例（%）
     */
    private BigDecimal beforeShareholdingRatio;

    /**
     * 变动后持股比例（%）
     */
    private BigDecimal afterShareholdingRatio;

    /**
     * 变动前持股数量（万股）
     */
    private BigDecimal beforeShareholdingQuantity;

    /**
     * 变动后持股数量（万股）
     */
    private BigDecimal afterShareholdingQuantity;

    /**
     * 变动前投资金额（万元）
     */
    private BigDecimal beforeInvestmentAmount;

    /**
     * 变动后投资金额（万元）
     */
    private BigDecimal afterInvestmentAmount;

    /**
     * 变动金额（万元）
     */
    private BigDecimal changeAmount;

    /**
     * 变动前是否控股
     */
    private Boolean beforeIsControlling;

    /**
     * 变动后是否控股
     */
    private Boolean afterIsControlling;

    /**
     * 变动前是否实际控制人
     */
    private Boolean beforeIsActualController;

    /**
     * 变动后是否实际控制人
     */
    private Boolean afterIsActualController;

    /**
     * 变动前表决权比例（%）
     */
    private BigDecimal beforeVotingRightRatio;

    /**
     * 变动后表决权比例（%）
     */
    private BigDecimal afterVotingRightRatio;

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
     * 转让价格（万元）
     */
    private BigDecimal transferPrice;

    /**
     * 转让方式
     */
    private String transferMethod;

    /**
     * 变动日期
     */
    private LocalDateTime changeDate;

    /**
     * 生效日期
     */
    private LocalDateTime effectiveDate;

    /**
     * 审批状态
     */
    private String approvalStatus;

    /**
     * 审批机关
     */
    private String approvalAuthority;

    /**
     * 审批文号
     */
    private String approvalNumber;

    /**
     * 审批日期
     */
    private LocalDateTime approvalDate;

    /**
     * 变动影响分析
     */
    private String impactAnalysis;

    /**
     * 合规性检查结果
     */
    private String complianceCheckResult;

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
     * 预警原因
     */
    private String warningReason;

    /**
     * 数据来源
     */
    private String dataSource;

    /**
     * 数据更新时间
     */
    private LocalDateTime dataUpdateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // 变动类型常量
    public static final String CHANGE_TYPE_INCREASE = "INCREASE"; // 增资
    public static final String CHANGE_TYPE_DECREASE = "DECREASE"; // 减资
    public static final String CHANGE_TYPE_TRANSFER = "TRANSFER"; // 转让
    public static final String CHANGE_TYPE_PLEDGE = "PLEDGE"; // 质押
    public static final String CHANGE_TYPE_UNPLEDGE = "UNPLEDGE"; // 解押
    public static final String CHANGE_TYPE_FREEZE = "FREEZE"; // 冻结
    public static final String CHANGE_TYPE_UNFREEZE = "UNFREEZE"; // 解冻
    public static final String CHANGE_TYPE_MERGER = "MERGER"; // 合并
    public static final String CHANGE_TYPE_SPLIT = "SPLIT"; // 分立
    public static final String CHANGE_TYPE_LIQUIDATION = "LIQUIDATION"; // 清算

    // 变动原因常量
    public static final String CHANGE_REASON_BUSINESS_EXPANSION = "BUSINESS_EXPANSION"; // 业务扩张
    public static final String CHANGE_REASON_CAPITAL_INCREASE = "CAPITAL_INCREASE"; // 增资扩股
    public static final String CHANGE_REASON_DEBT_REPAYMENT = "DEBT_REPAYMENT"; // 偿还债务
    public static final String CHANGE_REASON_STRATEGIC_ADJUSTMENT = "STRATEGIC_ADJUSTMENT"; // 战略调整
    public static final String CHANGE_REASON_ASSET_RESTRUCTURING = "ASSET_RESTRUCTURING"; // 资产重组
    public static final String CHANGE_REASON_MARKET_TRANSACTION = "MARKET_TRANSACTION"; // 市场交易
    public static final String CHANGE_REASON_POLICY_REQUIREMENT = "POLICY_REQUIREMENT"; // 政策要求
    public static final String CHANGE_REASON_OTHER = "OTHER"; // 其他

    // 转让方式常量
    public static final String TRANSFER_METHOD_AGREEMENT = "AGREEMENT"; // 协议转让
    public static final String TRANSFER_METHOD_AUCTION = "AUCTION"; // 拍卖
    public static final String TRANSFER_METHOD_TENDER = "TENDER"; // 招标
    public static final String TRANSFER_METHOD_EXCHANGE = "EXCHANGE"; // 产权交易所
    public static final String TRANSFER_METHOD_INTERNAL = "INTERNAL"; // 内部转让
    public static final String TRANSFER_METHOD_JUDICIAL = "JUDICIAL"; // 司法拍卖

    // 审批状态常量
    public static final String APPROVAL_STATUS_PENDING = "PENDING"; // 待审批
    public static final String APPROVAL_STATUS_APPROVED = "APPROVED"; // 已审批
    public static final String APPROVAL_STATUS_REJECTED = "REJECTED"; // 已拒绝
    public static final String APPROVAL_STATUS_CANCELLED = "CANCELLED"; // 已取消
    public static final String APPROVAL_STATUS_EXPIRED = "EXPIRED"; // 已过期

    // 预警级别常量
    public static final String WARNING_LEVEL_LOW = "LOW"; // 低风险
    public static final String WARNING_LEVEL_MEDIUM = "MEDIUM"; // 中风险
    public static final String WARNING_LEVEL_HIGH = "HIGH"; // 高风险
    public static final String WARNING_LEVEL_CRITICAL = "CRITICAL"; // 严重风险
}
