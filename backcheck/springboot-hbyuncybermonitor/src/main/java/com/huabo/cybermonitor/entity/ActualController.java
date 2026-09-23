package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 实际控制人实体类
 *
 * @author system
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ACTUAL_CONTROLLER")
public class ActualController {

    /**
     * 实际控制人ID
     */
    @TableId(value = "CONTROLLER_ID", type = IdType.ASSIGN_UUID)
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
     * 直接持股比例（%）
     */
    private BigDecimal directShareholdingRatio;

    /**
     * 间接持股比例（%）
     */
    private BigDecimal indirectShareholdingRatio;

    /**
     * 综合持股比例（%）
     */
    private BigDecimal totalShareholdingRatio;

    /**
     * 表决权比例（%）
     */
    private BigDecimal votingRightRatio;

    /**
     * 控制层级
     */
    private Integer controlLevel;

    /**
     * 控制路径
     */
    private String controlPath;

    /**
     * 控制关系描述
     */
    private String controlRelationDescription;

    /**
     * 是否最终控制人
     */
    private Boolean isUltimateController;

    /**
     * 是否一致行动人
     */
    private Boolean isConcertedAction;

    /**
     * 一致行动人协议
     */
    private String concertedActionAgreement;

    /**
     * 控制开始日期
     */
    private LocalDateTime controlStartDate;

    /**
     * 控制结束日期
     */
    private LocalDateTime controlEndDate;

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
     * 控制变动风险
     */
    private String controlChangeRisk;

    /**
     * 关联交易风险
     */
    private String relatedTransactionRisk;

    /**
     * 利益输送风险
     */
    private String benefitTransferRisk;

    /**
     * 监管关注度
     */
    private String regulatoryAttention;

    /**
     * 是否需要特别监管
     */
    private Boolean needSpecialSupervision;

    /**
     * 特别监管原因
     */
    private String specialSupervisionReason;

    /**
     * 识别方法
     */
    private String identificationMethod;

    /**
     * 识别依据
     */
    private String identificationBasis;

    /**
     * 识别时间
     */
    private LocalDateTime identificationTime;

    /**
     * 确认状态
     */
    private String confirmationStatus;

    /**
     * 确认人
     */
    private String confirmedBy;

    /**
     * 确认时间
     */
    private LocalDateTime confirmationTime;

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

    // 控制人类型常量
    public static final String CONTROLLER_TYPE_ENTERPRISE = "ENTERPRISE"; // 企业
    public static final String CONTROLLER_TYPE_INDIVIDUAL = "INDIVIDUAL"; // 个人
    public static final String CONTROLLER_TYPE_GOVERNMENT = "GOVERNMENT"; // 政府
    public static final String CONTROLLER_TYPE_INSTITUTION = "INSTITUTION"; // 机构
    public static final String CONTROLLER_TYPE_FUND = "FUND"; // 基金
    public static final String CONTROLLER_TYPE_TRUST = "TRUST"; // 信托
    public static final String CONTROLLER_TYPE_OTHER = "OTHER"; // 其他

    // 控制人性质常量
    public static final String CONTROLLER_NATURE_STATE_OWNED = "STATE_OWNED"; // 国有
    public static final String CONTROLLER_NATURE_COLLECTIVE = "COLLECTIVE"; // 集体
    public static final String CONTROLLER_NATURE_PRIVATE = "PRIVATE"; // 民营
    public static final String CONTROLLER_NATURE_FOREIGN = "FOREIGN"; // 外资
    public static final String CONTROLLER_NATURE_MIXED = "MIXED"; // 混合

    // 控制方式常量
    public static final String CONTROL_METHOD_SHAREHOLDING = "SHAREHOLDING"; // 股权控制
    public static final String CONTROL_METHOD_VOTING_RIGHT = "VOTING_RIGHT"; // 表决权控制
    public static final String CONTROL_METHOD_AGREEMENT = "AGREEMENT"; // 协议控制
    public static final String CONTROL_METHOD_MANAGEMENT = "MANAGEMENT"; // 管理控制
    public static final String CONTROL_METHOD_FINANCIAL = "FINANCIAL"; // 财务控制
    public static final String CONTROL_METHOD_OPERATIONAL = "OPERATIONAL"; // 经营控制
    public static final String CONTROL_METHOD_MIXED = "MIXED"; // 混合控制
    public static final String CONTROL_METHOD_BOARD_CONTROL = "BOARD_CONTROL"; // 董事会控制
    public static final String CONTROL_METHOD_TRUST = "TRUST"; // 信托控制
    public static final String CONTROL_METHOD_OTHER = "OTHER"; // 其他控制

    // 控制状态常量
    public static final String CONTROL_STATUS_ACTIVE = "ACTIVE"; // 有效控制
    public static final String CONTROL_STATUS_INACTIVE = "INACTIVE"; // 失效控制
    public static final String CONTROL_STATUS_DISPUTED = "DISPUTED"; // 争议控制
    public static final String CONTROL_STATUS_TRANSITIONAL = "TRANSITIONAL"; // 过渡控制
    public static final String CONTROL_STATUS_SUSPENDED = "SUSPENDED"; // 暂停控制
    public static final String CONTROL_STATUS_TERMINATED = "TERMINATED"; // 已终止

    // 控制稳定性常量
    public static final String CONTROL_STABILITY_STABLE = "STABLE"; // 稳定
    public static final String CONTROL_STABILITY_UNSTABLE = "UNSTABLE"; // 不稳定
    public static final String CONTROL_STABILITY_VOLATILE = "VOLATILE"; // 易变
    public static final String CONTROL_STABILITY_UNCERTAIN = "UNCERTAIN"; // 不确定

    // 控制风险等级常量
    public static final String CONTROL_RISK_LEVEL_LOW = "LOW"; // 低风险
    public static final String CONTROL_RISK_LEVEL_MEDIUM = "MEDIUM"; // 中风险
    public static final String CONTROL_RISK_LEVEL_HIGH = "HIGH"; // 高风险
    public static final String CONTROL_RISK_LEVEL_CRITICAL = "CRITICAL"; // 严重风险

    // 监管关注度常量
    public static final String REGULATORY_ATTENTION_LOW = "LOW"; // 低关注
    public static final String REGULATORY_ATTENTION_MEDIUM = "MEDIUM"; // 中关注
    public static final String REGULATORY_ATTENTION_HIGH = "HIGH"; // 高关注
    public static final String REGULATORY_ATTENTION_CRITICAL = "CRITICAL"; // 重点关注

    // 识别方法常量
    public static final String IDENTIFICATION_METHOD_AUTOMATIC = "AUTOMATIC"; // 自动识别
    public static final String IDENTIFICATION_METHOD_MANUAL = "MANUAL"; // 人工识别
    public static final String IDENTIFICATION_METHOD_ALGORITHM = "ALGORITHM"; // 算法识别
    public static final String IDENTIFICATION_METHOD_MIXED = "MIXED"; // 混合识别
    public static final String IDENTIFICATION_METHOD_SHAREHOLDING = "SHAREHOLDING"; // 持股比例识别
    public static final String IDENTIFICATION_METHOD_VOTING_RIGHT = "VOTING_RIGHT"; // 表决权识别
    public static final String IDENTIFICATION_METHOD_BOARD_CONTROL = "BOARD_CONTROL"; // 董事会控制识别
    public static final String IDENTIFICATION_METHOD_AGREEMENT = "AGREEMENT"; // 协议控制识别

    // 确认状态常量
    public static final String CONFIRMATION_STATUS_PENDING = "PENDING"; // 待确认
    public static final String CONFIRMATION_STATUS_CONFIRMED = "CONFIRMED"; // 已确认
    public static final String CONFIRMATION_STATUS_REJECTED = "REJECTED"; // 已拒绝
    public static final String CONFIRMATION_STATUS_DISPUTED = "DISPUTED"; // 有争议
}
