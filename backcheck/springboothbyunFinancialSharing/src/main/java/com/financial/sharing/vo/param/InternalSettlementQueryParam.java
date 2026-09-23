package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 内部结算查询参数类
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "InternalSettlementQueryParam", description = "内部结算查询参数")
public class InternalSettlementQueryParam extends PageableParam {

    // ==================== 基础查询字段 ====================
    
    @ApiModelProperty(value = "结算ID")
    private Long settlementId;

    @ApiModelProperty(value = "结算单号")
    private String settlementNo;

    @ApiModelProperty(value = "结算单号模糊查询")
    private String settlementNoLike;

    @ApiModelProperty(value = "结算方中心ID")
    private Long fromCenterId;

    @ApiModelProperty(value = "被结算方中心ID")
    private Long toCenterId;

    @ApiModelProperty(value = "结算日期开始")
    private LocalDate settlementDateStart;

    @ApiModelProperty(value = "结算日期结束")
    private LocalDate settlementDateEnd;

    @ApiModelProperty(value = "结算金额最小值")
    private BigDecimal settlementAmountMin;

    @ApiModelProperty(value = "结算金额最大值")
    private BigDecimal settlementAmountMax;

    @ApiModelProperty(value = "结算类型")
    private Integer settlementType;

    @ApiModelProperty(value = "结算类型列表")
    private List<Integer> settlementTypeList;

    @ApiModelProperty(value = "结算依据")
    private String settlementBasis;

    @ApiModelProperty(value = "结算依据模糊查询")
    private String settlementBasisLike;

    @ApiModelProperty(value = "结算状态")
    private Integer settlementStatus;

    @ApiModelProperty(value = "结算状态列表")
    private List<Integer> settlementStatusList;

    @ApiModelProperty(value = "凭证ID")
    private Long voucherId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "备注模糊查询")
    private String remarkLike;

    @ApiModelProperty(value = "账簿ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "创建时间开始")
    private LocalDateTime createTimeStart;

    @ApiModelProperty(value = "创建时间结束")
    private LocalDateTime createTimeEnd;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "更新人")
    private Long updater;

    // ==================== 内部交易查询字段 ====================

    @ApiModelProperty(value = "交易ID")
    private Long transactionId;

    @ApiModelProperty(value = "交易单号")
    private String transactionNo;

    @ApiModelProperty(value = "交易单号模糊查询")
    private String transactionNoLike;

    @ApiModelProperty(value = "交易类型")
    private Integer transactionType;

    @ApiModelProperty(value = "交易类型列表")
    private List<Integer> transactionTypeList;

    @ApiModelProperty(value = "交易描述")
    private String transactionDesc;

    @ApiModelProperty(value = "交易描述模糊查询")
    private String transactionDescLike;

    @ApiModelProperty(value = "交易状态")
    private Integer transactionStatus;

    @ApiModelProperty(value = "交易状态列表")
    private List<Integer> transactionStatusList;

    @ApiModelProperty(value = "交易日期开始")
    private LocalDate transactionDateStart;

    @ApiModelProperty(value = "交易日期结束")
    private LocalDate transactionDateEnd;

    // ==================== 转移定价查询字段 ====================

    @ApiModelProperty(value = "定价策略ID")
    private Long pricingPolicyId;

    @ApiModelProperty(value = "策略编码")
    private String policyCode;

    @ApiModelProperty(value = "策略编码模糊查询")
    private String policyCodeLike;

    @ApiModelProperty(value = "策略名称")
    private String policyName;

    @ApiModelProperty(value = "策略名称模糊查询")
    private String policyNameLike;

    @ApiModelProperty(value = "定价方法")
    private Integer pricingMethod;

    @ApiModelProperty(value = "定价方法列表")
    private List<Integer> pricingMethodList;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品名称模糊查询")
    private String productNameLike;

    @ApiModelProperty(value = "基准价格最小值")
    private BigDecimal basePriceMin;

    @ApiModelProperty(value = "基准价格最大值")
    private BigDecimal basePriceMax;

    @ApiModelProperty(value = "利润率最小值")
    private BigDecimal marginRateMin;

    @ApiModelProperty(value = "利润率最大值")
    private BigDecimal marginRateMax;

    @ApiModelProperty(value = "生效日期开始")
    private LocalDate effectiveDateStart;

    @ApiModelProperty(value = "生效日期结束")
    private LocalDate effectiveDateEnd;

    @ApiModelProperty(value = "失效日期开始")
    private LocalDate expiryDateStart;

    @ApiModelProperty(value = "失效日期结束")
    private LocalDate expiryDateEnd;

    @ApiModelProperty(value = "策略状态")
    private Integer policyStatus;

    @ApiModelProperty(value = "策略状态列表")
    private List<Integer> policyStatusList;

    // ==================== 利润中心查询字段 ====================

    @ApiModelProperty(value = "利润中心ID")
    private Long profitCenterId;

    @ApiModelProperty(value = "中心编码")
    private String centerCode;

    @ApiModelProperty(value = "中心编码模糊查询")
    private String centerCodeLike;

    @ApiModelProperty(value = "中心名称")
    private String centerName;

    @ApiModelProperty(value = "中心名称模糊查询")
    private String centerNameLike;

    @ApiModelProperty(value = "中心类型")
    private Integer centerType;

    @ApiModelProperty(value = "中心类型列表")
    private List<Integer> centerTypeList;

    @ApiModelProperty(value = "负责人")
    private String managerName;

    @ApiModelProperty(value = "负责人模糊查询")
    private String managerNameLike;

    @ApiModelProperty(value = "上级中心ID")
    private Long parentCenterId;

    @ApiModelProperty(value = "中心状态")
    private Integer centerStatus;

    @ApiModelProperty(value = "中心状态列表")
    private List<Integer> centerStatusList;

    @ApiModelProperty(value = "收入金额最小值")
    private BigDecimal revenueAmountMin;

    @ApiModelProperty(value = "收入金额最大值")
    private BigDecimal revenueAmountMax;

    @ApiModelProperty(value = "成本金额最小值")
    private BigDecimal costAmountMin;

    @ApiModelProperty(value = "成本金额最大值")
    private BigDecimal costAmountMax;

    @ApiModelProperty(value = "利润金额最小值")
    private BigDecimal profitAmountMin;

    @ApiModelProperty(value = "利润金额最大值")
    private BigDecimal profitAmountMax;

    @ApiModelProperty(value = "利润率最小值")
    private BigDecimal profitMarginMin;

    @ApiModelProperty(value = "利润率最大值")
    private BigDecimal profitMarginMax;

    // ==================== 资金管理查询字段 ====================

    @ApiModelProperty(value = "资金调配ID")
    private Long fundAllocationId;

    @ApiModelProperty(value = "调配单号")
    private String allocationNo;

    @ApiModelProperty(value = "调配单号模糊查询")
    private String allocationNoLike;

    @ApiModelProperty(value = "调配类型")
    private Integer allocationType;

    @ApiModelProperty(value = "调配类型列表")
    private List<Integer> allocationTypeList;

    @ApiModelProperty(value = "调配金额最小值")
    private BigDecimal allocationAmountMin;

    @ApiModelProperty(value = "调配金额最大值")
    private BigDecimal allocationAmountMax;

    @ApiModelProperty(value = "利率最小值")
    private BigDecimal interestRateMin;

    @ApiModelProperty(value = "利率最大值")
    private BigDecimal interestRateMax;

    @ApiModelProperty(value = "调配日期开始")
    private LocalDate allocationDateStart;

    @ApiModelProperty(value = "调配日期结束")
    private LocalDate allocationDateEnd;

    @ApiModelProperty(value = "到期日期开始")
    private LocalDate maturityDateStart;

    @ApiModelProperty(value = "到期日期结束")
    private LocalDate maturityDateEnd;

    @ApiModelProperty(value = "调配状态")
    private Integer allocationStatus;

    @ApiModelProperty(value = "调配状态列表")
    private List<Integer> allocationStatusList;

    // ==================== 结算分析查询字段 ====================

    @ApiModelProperty(value = "分析类型")
    private Integer analysisType;

    @ApiModelProperty(value = "分析对象")
    private List<Long> analysisObject;

    @ApiModelProperty(value = "分析期间开始")
    private String analysisStartPeriod;

    @ApiModelProperty(value = "分析期间结束")
    private String analysisEndPeriod;

    @ApiModelProperty(value = "结算效率最小值")
    private BigDecimal settlementEfficiencyMin;

    @ApiModelProperty(value = "结算效率最大值")
    private BigDecimal settlementEfficiencyMax;

    @ApiModelProperty(value = "平均结算时间最小值")
    private Integer avgSettlementTimeMin;

    @ApiModelProperty(value = "平均结算时间最大值")
    private Integer avgSettlementTimeMax;

    @ApiModelProperty(value = "成本节约最小值")
    private BigDecimal costSavingsMin;

    @ApiModelProperty(value = "成本节约最大值")
    private BigDecimal costSavingsMax;

    @ApiModelProperty(value = "风险等级")
    private Integer riskLevel;

    @ApiModelProperty(value = "风险等级列表")
    private List<Integer> riskLevelList;

    // ==================== 审批流程查询字段 ====================

    @ApiModelProperty(value = "审批状态")
    private Integer approvalStatus;

    @ApiModelProperty(value = "审批状态列表")
    private List<Integer> approvalStatusList;

    @ApiModelProperty(value = "审批人")
    private Long approver;

    @ApiModelProperty(value = "审批时间开始")
    private LocalDateTime approvalTimeStart;

    @ApiModelProperty(value = "审批时间结束")
    private LocalDateTime approvalTimeEnd;

    @ApiModelProperty(value = "审批意见")
    private String approvalComment;

    @ApiModelProperty(value = "审批意见模糊查询")
    private String approvalCommentLike;

    // ==================== 业务流程查询字段 ====================

    @ApiModelProperty(value = "业务流程ID")
    private Long businessProcessId;

    @ApiModelProperty(value = "流程状态")
    private Integer processStatus;

    @ApiModelProperty(value = "流程状态列表")
    private List<Integer> processStatusList;

    @ApiModelProperty(value = "当前节点")
    private String currentNode;

    @ApiModelProperty(value = "下一节点")
    private String nextNode;

    // ==================== 财务查询字段 ====================

    @ApiModelProperty(value = "会计期间")
    private String accountingPeriod;

    @ApiModelProperty(value = "会计期间列表")
    private List<String> accountingPeriodList;

    @ApiModelProperty(value = "科目编码")
    private String subjectCode;

    @ApiModelProperty(value = "科目编码模糊查询")
    private String subjectCodeLike;

    @ApiModelProperty(value = "科目名称")
    private String subjectName;

    @ApiModelProperty(value = "科目名称模糊查询")
    private String subjectNameLike;

    @ApiModelProperty(value = "借方金额最小值")
    private BigDecimal debitAmountMin;

    @ApiModelProperty(value = "借方金额最大值")
    private BigDecimal debitAmountMax;

    @ApiModelProperty(value = "贷方金额最小值")
    private BigDecimal creditAmountMin;

    @ApiModelProperty(value = "贷方金额最大值")
    private BigDecimal creditAmountMax;

    // ==================== 扩展业务查询字段 ====================

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "业务类型列表")
    private List<String> businessTypeList;

    @ApiModelProperty(value = "业务编号")
    private String businessNo;

    @ApiModelProperty(value = "业务编号模糊查询")
    private String businessNoLike;

    @ApiModelProperty(value = "业务描述")
    private String businessDesc;

    @ApiModelProperty(value = "业务描述模糊查询")
    private String businessDescLike;

    @ApiModelProperty(value = "关联单据ID")
    private Long relatedDocId;

    @ApiModelProperty(value = "关联单据号")
    private String relatedDocNo;

    @ApiModelProperty(value = "关联单据号模糊查询")
    private String relatedDocNoLike;

    @ApiModelProperty(value = "关联单据类型")
    private String relatedDocType;

    @ApiModelProperty(value = "关联单据类型列表")
    private List<String> relatedDocTypeList;

    // ==================== 系统查询字段 ====================

    @ApiModelProperty(value = "数据来源")
    private String dataSource;

    @ApiModelProperty(value = "数据来源列表")
    private List<String> dataSourceList;

    @ApiModelProperty(value = "同步状态")
    private Integer syncStatus;

    @ApiModelProperty(value = "同步状态列表")
    private List<Integer> syncStatusList;

    @ApiModelProperty(value = "同步时间开始")
    private LocalDateTime syncTimeStart;

    @ApiModelProperty(value = "同步时间结束")
    private LocalDateTime syncTimeEnd;

    // ==================== 扩展字段 ====================

    @ApiModelProperty(value = "扩展字段1")
    private String extField1;

    @ApiModelProperty(value = "扩展字段2")
    private String extField2;

    @ApiModelProperty(value = "扩展字段3")
    private String extField3;

    @ApiModelProperty(value = "扩展字段4")
    private String extField4;

    @ApiModelProperty(value = "扩展字段5")
    private String extField5;

    // ==================== 查询控制字段 ====================

    @ApiModelProperty(value = "是否包含已删除数据")
    private Boolean includeDeleted;

    @ApiModelProperty(value = "排序字段")
    private String orderBy;

    @ApiModelProperty(value = "排序方向(ASC/DESC)")
    private String orderDirection;

    @ApiModelProperty(value = "分组字段")
    private String groupBy;

    @ApiModelProperty(value = "是否需要统计信息")
    private Boolean needStats;

    @ApiModelProperty(value = "是否需要详细信息")
    private Boolean needDetail;

    @ApiModelProperty(value = "查询模式(NORMAL/EXPORT/ANALYSIS)")
    private String queryMode;
}
