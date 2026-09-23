package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资产变动记录实体类
 * 用于资产变动监控、资产流向分析、资产运营效率分析
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ASSET_CHANGE_RECORD")
public class AssetChangeRecord {

    /**
     * 变动记录ID（主键）
     */
    @TableId(value = "CHANGE_ID", type = IdType.ASSIGN_UUID)
    private String changeId;

    /**
     * 资产ID
     */
    private String assetId;

    /**
     * 资产编码
     */
    private String assetCode;

    /**
     * 资产名称
     */
    private String assetName;

    /**
     * 所属企业ID
     */
    private String enterpriseId;

    /**
     * 所属企业名称
     */
    private String enterpriseName;

    /**
     * 变动类型
     */
    private String changeType;

    /**
     * 变动原因
     */
    private String changeReason;

    /**
     * 变动日期
     */
    private LocalDate changeDate;

    /**
     * 变动金额
     */
    private BigDecimal changeAmount;

    /**
     * 变动前价值
     */
    private BigDecimal beforeValue;

    /**
     * 变动后价值
     */
    private BigDecimal afterValue;

    /**
     * 变动前状态
     */
    private String beforeStatus;

    /**
     * 变动后状态
     */
    private String afterStatus;

    /**
     * 变动前位置
     */
    private String beforeLocation;

    /**
     * 变动后位置
     */
    private String afterLocation;

    /**
     * 变动前管理人
     */
    private String beforeManager;

    /**
     * 变动后管理人
     */
    private String afterManager;

    /**
     * 变动前部门
     */
    private String beforeDepartment;

    /**
     * 变动后部门
     */
    private String afterDepartment;

    /**
     * 转出方
     */
    private String transferor;

    /**
     * 转入方
     */
    private String transferee;

    /**
     * 转让价格
     */
    private BigDecimal transferPrice;

    /**
     * 转让方式
     */
    private String transferMethod;

    /**
     * 转让协议编号
     */
    private String transferAgreementNo;

    /**
     * 处置方式
     */
    private String disposalMethod;

    /**
     * 处置价格
     */
    private BigDecimal disposalPrice;

    /**
     * 处置收益
     */
    private BigDecimal disposalGain;

    /**
     * 购置供应商
     */
    private String supplier;

    /**
     * 购置合同编号
     */
    private String purchaseContractNo;

    /**
     * 审批状态
     */
    private String approvalStatus;

    /**
     * 审批日期
     */
    private LocalDate approvalDate;

    /**
     * 审批机构
     */
    private String approvalAuthority;

    /**
     * 审批文件编号
     */
    private String approvalDocumentNo;

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
     * 影响分析
     */
    private String impactAnalysis;

    /**
     * 合规性检查结果
     */
    private String complianceCheckResult;

    /**
     * 风险评估
     */
    private String riskAssessment;

    /**
     * 生效日期
     */
    private LocalDate effectiveDate;

    /**
     * 登记日期
     */
    private LocalDate registrationDate;

    /**
     * 登记机构
     */
    private String registrationAuthority;

    /**
     * 变动经办人
     */
    private String changeOperator;

    /**
     * 变动审核人
     */
    private String changeReviewer;

    /**
     * 变动批准人
     */
    private String changeApprover;

    /**
     * 相关文件
     */
    private String relatedDocuments;

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
    private String remarks;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    // ==================== 常量定义 ====================

    /**
     * 变动类型常量
     */
    public static final String CHANGE_TYPE_ACQUISITION = "ACQUISITION"; // 购置
    public static final String CHANGE_TYPE_DISPOSAL = "DISPOSAL"; // 处置
    public static final String CHANGE_TYPE_TRANSFER = "TRANSFER"; // 转移
    public static final String CHANGE_TYPE_REVALUATION = "REVALUATION"; // 重估
    public static final String CHANGE_TYPE_DEPRECIATION = "DEPRECIATION"; // 折旧
    public static final String CHANGE_TYPE_IMPAIRMENT = "IMPAIRMENT"; // 减值
    public static final String CHANGE_TYPE_UPGRADE = "UPGRADE"; // 升级改造
    public static final String CHANGE_TYPE_MAINTENANCE = "MAINTENANCE"; // 维修
    public static final String CHANGE_TYPE_RELOCATION = "RELOCATION"; // 搬迁
    public static final String CHANGE_TYPE_STATUS_CHANGE = "STATUS_CHANGE"; // 状态变更

    /**
     * 变动原因常量
     */
    public static final String CHANGE_REASON_BUSINESS_NEED = "BUSINESS_NEED"; // 业务需要
    public static final String CHANGE_REASON_TECHNOLOGY_UPGRADE = "TECHNOLOGY_UPGRADE"; // 技术升级
    public static final String CHANGE_REASON_COST_REDUCTION = "COST_REDUCTION"; // 成本控制
    public static final String CHANGE_REASON_EFFICIENCY_IMPROVEMENT = "EFFICIENCY_IMPROVEMENT"; // 效率提升
    public static final String CHANGE_REASON_POLICY_REQUIREMENT = "POLICY_REQUIREMENT"; // 政策要求
    public static final String CHANGE_REASON_MARKET_CHANGE = "MARKET_CHANGE"; // 市场变化
    public static final String CHANGE_REASON_ASSET_OPTIMIZATION = "ASSET_OPTIMIZATION"; // 资产优化
    public static final String CHANGE_REASON_RISK_CONTROL = "RISK_CONTROL"; // 风险控制

    /**
     * 转让方式常量
     */
    public static final String TRANSFER_METHOD_SALE = "SALE"; // 出售
    public static final String TRANSFER_METHOD_AUCTION = "AUCTION"; // 拍卖
    public static final String TRANSFER_METHOD_TENDER = "TENDER"; // 招标
    public static final String TRANSFER_METHOD_AGREEMENT = "AGREEMENT"; // 协议转让
    public static final String TRANSFER_METHOD_EXCHANGE = "EXCHANGE"; // 置换
    public static final String TRANSFER_METHOD_DONATION = "DONATION"; // 捐赠

    /**
     * 处置方式常量
     */
    public static final String DISPOSAL_METHOD_SALE = "SALE"; // 出售
    public static final String DISPOSAL_METHOD_SCRAP = "SCRAP"; // 报废
    public static final String DISPOSAL_METHOD_AUCTION = "AUCTION"; // 拍卖
    public static final String DISPOSAL_METHOD_DONATION = "DONATION"; // 捐赠
    public static final String DISPOSAL_METHOD_DESTRUCTION = "DESTRUCTION"; // 销毁
    public static final String DISPOSAL_METHOD_RECYCLING = "RECYCLING"; // 回收

    /**
     * 审批状态常量
     */
    public static final String APPROVAL_STATUS_PENDING = "PENDING"; // 待审批
    public static final String APPROVAL_STATUS_APPROVED = "APPROVED"; // 已审批
    public static final String APPROVAL_STATUS_REJECTED = "REJECTED"; // 已拒绝
    public static final String APPROVAL_STATUS_CANCELLED = "CANCELLED"; // 已取消
    public static final String APPROVAL_STATUS_EXPIRED = "EXPIRED"; // 已过期

    /**
     * 预警级别常量
     */
    public static final String WARNING_LEVEL_LOW = "LOW"; // 低级预警
    public static final String WARNING_LEVEL_MEDIUM = "MEDIUM"; // 中级预警
    public static final String WARNING_LEVEL_HIGH = "HIGH"; // 高级预警
    public static final String WARNING_LEVEL_CRITICAL = "CRITICAL"; // 严重预警

}
