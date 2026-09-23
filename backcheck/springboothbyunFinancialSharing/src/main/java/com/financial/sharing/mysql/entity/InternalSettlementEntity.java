package com.financial.sharing.mysql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 内部结算实体类 - MySQL版本
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("T_INTERNAL_SETTLEMENT")
@ApiModel(value = "InternalSettlementEntity", description = "内部结算实体")
public class InternalSettlementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算ID")
    @TableId(value = "SETTLEMENT_ID", type = IdType.ASSIGN_ID)
    private Long settlementId;

    @ApiModelProperty(value = "结算单号")
    @TableField("SETTLEMENT_NO")
    private String settlementNo;

    @ApiModelProperty(value = "结算方中心ID")
    @TableField("FROM_CENTER_ID")
    private Long fromCenterId;

    @ApiModelProperty(value = "被结算方中心ID")
    @TableField("TO_CENTER_ID")
    private Long toCenterId;

    @ApiModelProperty(value = "结算日期")
    @TableField("SETTLEMENT_DATE")
    private LocalDate settlementDate;

    @ApiModelProperty(value = "结算金额")
    @TableField("SETTLEMENT_AMOUNT")
    private BigDecimal settlementAmount;

    @ApiModelProperty(value = "结算类型(1成本分摊2利润分配3资金调拨)")
    @TableField("SETTLEMENT_TYPE")
    private Integer settlementType;

    @ApiModelProperty(value = "结算依据")
    @TableField("SETTLEMENT_BASIS")
    private String settlementBasis;

    @ApiModelProperty(value = "结算状态(1待结算2已结算)")
    @TableField("SETTLEMENT_STATUS")
    private Integer settlementStatus;

    @ApiModelProperty(value = "凭证ID")
    @TableField("VOUCHER_ID")
    private Long voucherId;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;

    @ApiModelProperty(value = "账簿ID")
    @TableField("BOOK_ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "版本号")
    @TableField("VERSION")
    private Integer version;

    @ApiModelProperty(value = "删除标识(0否1是)")
    @TableField("IS_DELETED")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATOR")
    private Long creator;

    @ApiModelProperty(value = "更新人")
    @TableField("UPDATER")
    private Long updater;

    // 扩展字段 - 不映射到数据库
    @ApiModelProperty(value = "结算方中心名称")
    @TableField(exist = false)
    private String fromCenterName;

    @ApiModelProperty(value = "被结算方中心名称")
    @TableField(exist = false)
    private String toCenterName;

    @ApiModelProperty(value = "结算类型名称")
    @TableField(exist = false)
    private String settlementTypeName;

    @ApiModelProperty(value = "结算状态名称")
    @TableField(exist = false)
    private String settlementStatusName;

    @ApiModelProperty(value = "凭证号")
    @TableField(exist = false)
    private String voucherNo;

    @ApiModelProperty(value = "创建人姓名")
    @TableField(exist = false)
    private String creatorName;

    @ApiModelProperty(value = "更新人姓名")
    @TableField(exist = false)
    private String updaterName;

    // 内部交易相关字段
    @ApiModelProperty(value = "交易ID")
    @TableField("TRANSACTION_ID")
    private Long transactionId;

    @ApiModelProperty(value = "交易单号")
    @TableField("TRANSACTION_NO")
    private String transactionNo;

    @ApiModelProperty(value = "交易类型")
    @TableField("TRANSACTION_TYPE")
    private Integer transactionType;

    @ApiModelProperty(value = "交易描述")
    @TableField("TRANSACTION_DESC")
    private String transactionDesc;

    @ApiModelProperty(value = "交易状态")
    @TableField("TRANSACTION_STATUS")
    private Integer transactionStatus;

    // 转移定价相关字段
    @ApiModelProperty(value = "定价策略ID")
    @TableField("PRICING_POLICY_ID")
    private Long pricingPolicyId;

    @ApiModelProperty(value = "定价方法")
    @TableField("PRICING_METHOD")
    private Integer pricingMethod;

    @ApiModelProperty(value = "基准价格")
    @TableField("BASE_PRICE")
    private BigDecimal basePrice;

    @ApiModelProperty(value = "利润率")
    @TableField("MARGIN_RATE")
    private BigDecimal marginRate;

    @ApiModelProperty(value = "调整系数")
    @TableField("ADJUSTMENT_FACTOR")
    private BigDecimal adjustmentFactor;

    // 利润中心相关字段
    @ApiModelProperty(value = "利润中心ID")
    @TableField("PROFIT_CENTER_ID")
    private Long profitCenterId;

    @ApiModelProperty(value = "收入金额")
    @TableField("REVENUE_AMOUNT")
    private BigDecimal revenueAmount;

    @ApiModelProperty(value = "成本金额")
    @TableField("COST_AMOUNT")
    private BigDecimal costAmount;

    @ApiModelProperty(value = "利润金额")
    @TableField("PROFIT_AMOUNT")
    private BigDecimal profitAmount;

    @ApiModelProperty(value = "利润率")
    @TableField("PROFIT_MARGIN")
    private BigDecimal profitMargin;

    // 资金管理相关字段
    @ApiModelProperty(value = "资金调配ID")
    @TableField("FUND_ALLOCATION_ID")
    private Long fundAllocationId;

    @ApiModelProperty(value = "调配类型")
    @TableField("ALLOCATION_TYPE")
    private Integer allocationType;

    @ApiModelProperty(value = "调配金额")
    @TableField("ALLOCATION_AMOUNT")
    private BigDecimal allocationAmount;

    @ApiModelProperty(value = "利率")
    @TableField("INTEREST_RATE")
    private BigDecimal interestRate;

    @ApiModelProperty(value = "利息金额")
    @TableField("INTEREST_AMOUNT")
    private BigDecimal interestAmount;

    @ApiModelProperty(value = "到期日期")
    @TableField("MATURITY_DATE")
    private LocalDate maturityDate;

    // 结算分析相关字段
    @ApiModelProperty(value = "结算效率")
    @TableField("SETTLEMENT_EFFICIENCY")
    private BigDecimal settlementEfficiency;

    @ApiModelProperty(value = "平均结算时间")
    @TableField("AVG_SETTLEMENT_TIME")
    private Integer avgSettlementTime;

    @ApiModelProperty(value = "成本节约")
    @TableField("COST_SAVINGS")
    private BigDecimal costSavings;

    @ApiModelProperty(value = "风险等级")
    @TableField("RISK_LEVEL")
    private Integer riskLevel;

    @ApiModelProperty(value = "优化建议")
    @TableField("OPTIMIZATION_SUGGESTION")
    private String optimizationSuggestion;

    // 审批流程相关字段
    @ApiModelProperty(value = "审批状态")
    @TableField("APPROVAL_STATUS")
    private Integer approvalStatus;

    @ApiModelProperty(value = "审批人")
    @TableField("APPROVER")
    private Long approver;

    @ApiModelProperty(value = "审批时间")
    @TableField("APPROVAL_TIME")
    private LocalDateTime approvalTime;

    @ApiModelProperty(value = "审批意见")
    @TableField("APPROVAL_COMMENT")
    private String approvalComment;

    // 业务流程相关字段
    @ApiModelProperty(value = "业务流程ID")
    @TableField("BUSINESS_PROCESS_ID")
    private Long businessProcessId;

    @ApiModelProperty(value = "流程状态")
    @TableField("PROCESS_STATUS")
    private Integer processStatus;

    @ApiModelProperty(value = "当前节点")
    @TableField("CURRENT_NODE")
    private String currentNode;

    @ApiModelProperty(value = "下一节点")
    @TableField("NEXT_NODE")
    private String nextNode;

    // 财务相关字段
    @ApiModelProperty(value = "会计期间")
    @TableField("ACCOUNTING_PERIOD")
    private String accountingPeriod;

    @ApiModelProperty(value = "科目编码")
    @TableField("SUBJECT_CODE")
    private String subjectCode;

    @ApiModelProperty(value = "科目名称")
    @TableField("SUBJECT_NAME")
    private String subjectName;

    @ApiModelProperty(value = "借方金额")
    @TableField("DEBIT_AMOUNT")
    private BigDecimal debitAmount;

    @ApiModelProperty(value = "贷方金额")
    @TableField("CREDIT_AMOUNT")
    private BigDecimal creditAmount;

    // 扩展业务字段
    @ApiModelProperty(value = "业务类型")
    @TableField("BUSINESS_TYPE")
    private String businessType;

    @ApiModelProperty(value = "业务编号")
    @TableField("BUSINESS_NO")
    private String businessNo;

    @ApiModelProperty(value = "业务描述")
    @TableField("BUSINESS_DESC")
    private String businessDesc;

    @ApiModelProperty(value = "关联单据ID")
    @TableField("RELATED_DOC_ID")
    private Long relatedDocId;

    @ApiModelProperty(value = "关联单据号")
    @TableField("RELATED_DOC_NO")
    private String relatedDocNo;

    @ApiModelProperty(value = "关联单据类型")
    @TableField("RELATED_DOC_TYPE")
    private String relatedDocType;

    // 系统字段
    @ApiModelProperty(value = "数据来源")
    @TableField("DATA_SOURCE")
    private String dataSource;

    @ApiModelProperty(value = "同步状态")
    @TableField("SYNC_STATUS")
    private Integer syncStatus;

    @ApiModelProperty(value = "同步时间")
    @TableField("SYNC_TIME")
    private LocalDateTime syncTime;

    @ApiModelProperty(value = "扩展字段1")
    @TableField("EXT_FIELD1")
    private String extField1;

    @ApiModelProperty(value = "扩展字段2")
    @TableField("EXT_FIELD2")
    private String extField2;

    @ApiModelProperty(value = "扩展字段3")
    @TableField("EXT_FIELD3")
    private String extField3;

    @ApiModelProperty(value = "扩展字段4")
    @TableField("EXT_FIELD4")
    private String extField4;

    @ApiModelProperty(value = "扩展字段5")
    @TableField("EXT_FIELD5")
    private String extField5;
}
