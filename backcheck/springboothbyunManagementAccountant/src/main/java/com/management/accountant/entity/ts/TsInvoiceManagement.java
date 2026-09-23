package com.management.accountant.entity.ts;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 发票管理实体类
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ts_invoice_management")
public class TsInvoiceManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发票ID
     */
    @TableId(value = "invoice_id", type = IdType.AUTO)
    private Long invoiceId;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 发票代码
     */
    @TableField("invoice_code")
    private String invoiceCode;

    /**
     * 发票号码
     */
    @TableField("invoice_number")
    private String invoiceNumber;

    /**
     * 发票类型 (SPECIAL_VAT:增值税专用发票, ORDINARY_VAT:增值税普通发票, ELECTRONIC:电子发票, RECEIPT:收据)
     */
    @TableField("invoice_type")
    private String invoiceType;

    /**
     * 发票状态 (DRAFT:草稿, RECOGNIZED:已识别, VERIFIED:已验真, ARCHIVED:已归档, INVALID:作废)
     */
    @TableField("invoice_status")
    private String invoiceStatus;

    /**
     * 开票日期
     */
    @TableField("invoice_date")
    private LocalDateTime invoiceDate;

    /**
     * 销售方名称
     */
    @TableField("seller_name")
    private String sellerName;

    /**
     * 销售方纳税人识别号
     */
    @TableField("seller_tax_number")
    private String sellerTaxNumber;

    /**
     * 销售方地址电话
     */
    @TableField("seller_address_phone")
    private String sellerAddressPhone;

    /**
     * 销售方开户行及账号
     */
    @TableField("seller_bank_account")
    private String sellerBankAccount;

    /**
     * 购买方名称
     */
    @TableField("buyer_name")
    private String buyerName;

    /**
     * 购买方纳税人识别号
     */
    @TableField("buyer_tax_number")
    private String buyerTaxNumber;

    /**
     * 购买方地址电话
     */
    @TableField("buyer_address_phone")
    private String buyerAddressPhone;

    /**
     * 购买方开户行及账号
     */
    @TableField("buyer_bank_account")
    private String buyerBankAccount;

    /**
     * 货物或应税劳务名称
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 规格型号
     */
    @TableField("specification")
    private String specification;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 数量
     */
    @TableField("quantity")
    private BigDecimal quantity;

    /**
     * 单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 税率
     */
    @TableField("tax_rate")
    private BigDecimal taxRate;

    /**
     * 税额
     */
    @TableField("tax_amount")
    private BigDecimal taxAmount;

    /**
     * 价税合计
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 价税合计(大写)
     */
    @TableField("total_amount_chinese")
    private String totalAmountChinese;

    /**
     * 收款人
     */
    @TableField("payee")
    private String payee;

    /**
     * 复核
     */
    @TableField("reviewer")
    private String reviewer;

    /**
     * 开票人
     */
    @TableField("drawer")
    private String drawer;

    /**
     * 机器编号
     */
    @TableField("machine_number")
    private String machineNumber;

    /**
     * 密码区
     */
    @TableField("password_area")
    private String passwordArea;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 发票文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 发票文件名
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件大小(字节)
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

    /**
     * OCR识别状态 (PENDING:待识别, PROCESSING:识别中, SUCCESS:识别成功, FAILED:识别失败)
     */
    @TableField("ocr_status")
    private String ocrStatus;

    /**
     * OCR识别结果
     */
    @TableField("ocr_result")
    private String ocrResult;

    /**
     * OCR识别置信度
     */
    @TableField("ocr_confidence")
    private BigDecimal ocrConfidence;

    /**
     * OCR识别时间
     */
    @TableField("ocr_time")
    private LocalDateTime ocrTime;

    /**
     * 验真状态 (PENDING:待验真, PROCESSING:验真中, SUCCESS:验真成功, FAILED:验真失败)
     */
    @TableField("verification_status")
    private String verificationStatus;

    /**
     * 验真结果
     */
    @TableField("verification_result")
    private String verificationResult;

    /**
     * 验真时间
     */
    @TableField("verification_time")
    private LocalDateTime verificationTime;

    /**
     * 验真来源 (TAX_BUREAU:税务局, THIRD_PARTY:第三方)
     */
    @TableField("verification_source")
    private String verificationSource;

    /**
     * 风险等级 (LOW:低风险, MEDIUM:中风险, HIGH:高风险, CRITICAL:严重风险)
     */
    @TableField("risk_level")
    private String riskLevel;

    /**
     * 风险原因
     */
    @TableField("risk_reason")
    private String riskReason;

    /**
     * 风险评估时间
     */
    @TableField("risk_assessment_time")
    private LocalDateTime riskAssessmentTime;

    /**
     * 归档状态 (PENDING:待归档, ARCHIVED:已归档, FAILED:归档失败)
     */
    @TableField("archive_status")
    private String archiveStatus;

    /**
     * 归档路径
     */
    @TableField("archive_path")
    private String archivePath;

    /**
     * 归档时间
     */
    @TableField("archive_time")
    private LocalDateTime archiveTime;

    /**
     * 业务分类 (PURCHASE:采购, SALES:销售, EXPENSE:费用, ASSET:资产)
     */
    @TableField("business_category")
    private String businessCategory;

    /**
     * 会计科目
     */
    @TableField("accounting_subject")
    private String accountingSubject;

    /**
     * 成本中心
     */
    @TableField("cost_center")
    private String costCenter;

    /**
     * 项目编号
     */
    @TableField("project_code")
    private String projectCode;

    /**
     * 合同编号
     */
    @TableField("contract_number")
    private String contractNumber;

    /**
     * 审批状态 (PENDING:待审批, APPROVED:已审批, REJECTED:已拒绝)
     */
    @TableField("approval_status")
    private String approvalStatus;

    /**
     * 审批人ID
     */
    @TableField("approver_id")
    private Long approverId;

    /**
     * 审批人姓名
     */
    @TableField("approver_name")
    private String approverName;

    /**
     * 审批时间
     */
    @TableField("approval_time")
    private LocalDateTime approvalTime;

    /**
     * 审批意见
     */
    @TableField("approval_comment")
    private String approvalComment;

    /**
     * 处理状态 (PENDING:待处理, PROCESSING:处理中, COMPLETED:已完成, FAILED:处理失败)
     */
    @TableField("processing_status")
    private String processingStatus;

    /**
     * 处理结果
     */
    @TableField("processing_result")
    private String processingResult;

    /**
     * 处理时间
     */
    @TableField("processing_time")
    private LocalDateTime processingTime;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    @TableField("max_retry_count")
    private Integer maxRetryCount;

    /**
     * 下次重试时间
     */
    @TableField("next_retry_time")
    private LocalDateTime nextRetryTime;

    /**
     * 优先级 (LOW:低, NORMAL:普通, HIGH:高, URGENT:紧急)
     */
    @TableField("priority")
    private String priority;

    /**
     * 标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 扩展属性(JSON格式)
     */
    @TableField("extra_properties")
    private String extraProperties;

    /**
     * 创建人ID
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @TableField(value = "created_name", fill = FieldFill.INSERT)
    private String createdName;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人ID
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    /**
     * 更新人姓名
     */
    @TableField(value = "updated_name", fill = FieldFill.INSERT_UPDATE)
    private String updatedName;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 逻辑删除标志
     */
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    /**
     * 版本号
     */
    @TableField("version")
    @Version
    private Integer version;
}
