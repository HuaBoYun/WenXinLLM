package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.type.JdbcType;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收支管理实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("finance_transaction")
@Schema(name="FinanceTransaction对象", description="收支管理")
public class FinanceTransaction {

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(name = "项目ID")
    private String projectId;

    @Schema(name = "交易编号")
    private String transactionNo;

    @Schema(name = "交易名称")
    private String transactionName;

    @Schema(name = "交易类型：1-收入，2-支出")
    @TableField(value = "transaction_type", jdbcType = JdbcType.INTEGER)
    private Integer transactionType;

    @Schema(name = "交易分类：1-合同收款，2-材料采购，3-人工费用，4-设备租赁，5-其他费用")
    @TableField(value = "transaction_category", jdbcType = JdbcType.INTEGER)
    private Integer transactionCategory;



    @Schema(name = "交易日期")
    private Date transactionDate;

    @Schema(name = "交易状态：1-待处理，2-处理中，3-已完成，4-异常")
    private Integer transactionStatus;

    @Schema(name = "付款方/收款方")
    private String counterparty;

    @Schema(name = "银行账户")
    private String bankAccount;

    @Schema(name = "支付方式：1-银行转账，2-现金，3-支票，4-其他")
    private Integer paymentMethod;

    @Schema(name = "发票号码")
    private String invoiceNumber;

    @Schema(name = "发票金额")
    private BigDecimal invoiceAmount;

    @Schema(name = "税率")
    private BigDecimal taxRate;

    @Schema(name = "税额")
    private BigDecimal taxAmount;

    @Schema(name = "审批状态：1-待审批，2-审批中，3-已审批，4-已拒绝")
    private Integer approvalStatus;

    @Schema(name = "审批人ID")
    private String approverId;

    @Schema(name = "审批时间")
    private Date approvalTime;

    @Schema(name = "审批意见")
    private String approvalComments;

    @Schema(name = "财务确认状态：1-待确认，2-已确认，3-已拒绝")
    private Integer financeStatus;

    @Schema(name = "财务确认人ID")
    private String financeConfirmerId;

    @Schema(name = "财务确认时间")
    private Date financeConfirmTime;

    @Schema(name = "财务确认意见")
    private String financeComments;

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

    @Schema(name = "审批日期")
    private Date approvalDate;

    @Schema(name = "发票编号")
    private String invoiceNo;

    @Schema(name = "交易金额（映射用）")
    private BigDecimal amount;

    @Schema(name = "交易描述")
    private String description;

    @Schema(name = "管理员ID")
    private String managerId;

    @Schema(name = "管理员姓名")
    private String managerName;

    @Schema(name = "备注")
    private String remarks;

    @Schema(name = "附件路径")
    private String attachmentPath;

    @Schema(name = "创建人")
    private String createBy;

    @Schema(name = "创建时间")
    private Date createTime;

    @Schema(name = "更新人")
    private String updateBy;

    @Schema(name = "更新时间")
    private Date updateTime;

    /**
     * 判断是否为收入
     */
    public boolean isIncome() {
        return transactionType != null && transactionType.equals(1);
    }

    /**
     * 判断是否为支出
     */
    public boolean isExpense() {
        return transactionType != null && transactionType.equals(2);
    }

    /**
     * 判断是否需要审批
     */
    public boolean needsApproval() {
        return approvalStatus == null || approvalStatus == 1;
    }

    /**
     * 判断是否已审批
     */
    public boolean isApproved() {
        return approvalStatus != null && approvalStatus == 3;
    }

    /**
     * 判断是否已拒绝
     */
    public boolean isRejected() {
        return approvalStatus != null && approvalStatus == 4;
    }

    /**
     * 判断是否需要财务确认
     */
    public boolean needsFinanceConfirm() {
        return financeStatus == null || financeStatus == 1;
    }

    /**
     * 判断是否已财务确认
     */
    public boolean isFinanceConfirmed() {
        return financeStatus != null && financeStatus == 2;
    }

    /**
     * 判断是否为大额交易（超过10万）
     */
    public boolean isLargeAmount() {
        return amount != null && amount.compareTo(new BigDecimal("100000")) > 0;
    }

    /**
     * 计算含税金额
     */
    public BigDecimal getTaxInclusiveAmount() {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        if (taxAmount == null) {
            return amount;
        }
        return amount.add(taxAmount);
    }

    /**
     * 计算不含税金额
     */
    public BigDecimal getTaxExclusiveAmount() {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        if (taxAmount == null) {
            return amount;
        }
        return amount.subtract(taxAmount);
    }
}
