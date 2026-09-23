package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收支管理查询参数
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="FinanceTransactionQueryParam", description="收支管理查询参数")
public class FinanceTransactionQueryParam extends BaseQueryParam {

    @Schema(name = "项目ID")
    private String projectId;

    @Schema(name = "交易编号")
    private String transactionNo;

    @Schema(name = "交易名称")
    private String transactionName;

    @Schema(name = "交易类型：1-收入，2-支出")
    private Integer transactionType;

    @Schema(name = "交易分类：1-合同收款，2-材料采购，3-人工费用，4-设备租赁，5-其他费用")
    private Integer transactionCategory;

    @Schema(name = "交易状态：1-待处理，2-处理中，3-已完成，4-异常")
    private Integer transactionStatus;

    @Schema(name = "最小交易金额")
    private BigDecimal minTransactionAmount;

    @Schema(name = "最大交易金额")
    private BigDecimal maxTransactionAmount;

    @Schema(name = "交易开始日期")
    private Date transactionStartDate;

    @Schema(name = "交易结束日期")
    private Date transactionEndDate;

    @Schema(name = "付款方/收款方")
    private String counterparty;

    @Schema(name = "银行账户")
    private String bankAccount;

    @Schema(name = "支付方式：1-银行转账，2-现金，3-支票，4-其他")
    private Integer paymentMethod;

    @Schema(name = "发票号码")
    private String invoiceNumber;

    @Schema(name = "审批状态：1-待审批，2-审批中，3-已审批，4-已拒绝")
    private Integer approvalStatus;

    @Schema(name = "审批人ID")
    private String approverId;

    @Schema(name = "财务确认状态：1-待确认，2-已确认，3-已拒绝")
    private Integer financeStatus;

    @Schema(name = "财务确认人ID")
    private String financeConfirmerId;

    @Schema(name = "关联合同ID")
    private String contractId;

    @Schema(name = "关联预算ID")
    private String budgetId;

    @Schema(name = "成本中心")
    private String costCenter;

    @Schema(name = "会计科目")
    private String accountingSubject;

    @Schema(name = "会计科目（映射用）")
    private String accountSubject;

    @Schema(name = "银行名称")
    private String bankName;

    @Schema(name = "对方账户")
    private String counterpartAccount;

    @Schema(name = "账户号码")
    private String accountNumber;

    @Schema(name = "审批人姓名")
    private String approverName;

    @Schema(name = "发票编号")
    private String invoiceNo;

    @Schema(name = "交易描述")
    private String description;

    @Schema(name = "管理员ID")
    private String managerId;

    @Schema(name = "管理员姓名")
    private String managerName;

    @Schema(name = "最小金额")
    private BigDecimal minAmount;

    @Schema(name = "最大金额")
    private BigDecimal maxAmount;

    @Schema(name = "审批开始日期")
    private Date approvalStartDate;

    @Schema(name = "审批结束日期")
    private Date approvalEndDate;

    @Schema(name = "创建开始时间")
    private Date createStartTime;

    @Schema(name = "创建结束时间")
    private Date createEndTime;

    @Schema(name = "创建人")
    private String createBy;

    @Schema(name = "附件路径")
    private String attachmentPath;

    @Schema(name = "备注")
    private String remarks;

    @Schema(name = "关键字搜索")
    private String keyword;

    @Schema(name = "是否大额交易")
    private Boolean isLargeAmount;

    @Schema(name = "是否需要审批")
    private Boolean needsApproval;

    @Schema(name = "是否需要财务确认")
    private Boolean needsFinanceConfirm;
}
