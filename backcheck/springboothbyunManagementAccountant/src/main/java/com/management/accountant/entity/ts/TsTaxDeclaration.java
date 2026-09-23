package com.management.accountant.entity.ts;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 税务申报实体类
 * 
 * @author AI Assistant
 * @since 2025-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tbl_ts_tax_declaration")
public class TsTaxDeclaration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申报ID
     */
    @TableId(value = "declaration_id", type = IdType.AUTO)
    private Long declarationId;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    /**
     * 申报编号
     */
    @TableField("declaration_code")
    private String declarationCode;

    /**
     * 申报名称
     */
    @TableField("declaration_name")
    private String declarationName;

    /**
     * 税种类型
     */
    @TableField("tax_type")
    private String taxType;

    /**
     * 申报类型
     */
    @TableField("declaration_type")
    private String declarationType;

    /**
     * 申报状态
     */
    @TableField("declaration_status")
    private String declarationStatus;

    /**
     * 申报期间
     */
    @TableField("declaration_period")
    private String declarationPeriod;

    /**
     * 申报年度
     */
    @TableField("declaration_year")
    private Integer declarationYear;

    /**
     * 申报月份
     */
    @TableField("declaration_month")
    private Integer declarationMonth;

    /**
     * 申报季度
     */
    @TableField("declaration_quarter")
    private Integer declarationQuarter;

    /**
     * 纳税人识别号
     */
    @TableField("taxpayer_id")
    private String taxpayerId;

    /**
     * 纳税人名称
     */
    @TableField("taxpayer_name")
    private String taxpayerName;

    /**
     * 纳税人类型
     */
    @TableField("taxpayer_type")
    private String taxpayerType;

    /**
     * 申报表类型
     */
    @TableField("form_type")
    private String formType;

    /**
     * 申报表版本
     */
    @TableField("form_version")
    private String formVersion;

    /**
     * 申报数据
     */
    @TableField("declaration_data")
    private String declarationData;

    /**
     * 计税依据
     */
    @TableField("tax_base")
    private BigDecimal taxBase;

    /**
     * 税率
     */
    @TableField("tax_rate")
    private BigDecimal taxRate;

    /**
     * 应纳税额
     */
    @TableField("tax_amount")
    private BigDecimal taxAmount;

    /**
     * 已缴税额
     */
    @TableField("paid_amount")
    private BigDecimal paidAmount;

    /**
     * 应补税额
     */
    @TableField("payable_amount")
    private BigDecimal payableAmount;

    /**
     * 应退税额
     */
    @TableField("refundable_amount")
    private BigDecimal refundableAmount;

    /**
     * 滞纳金
     */
    @TableField("penalty_amount")
    private BigDecimal penaltyAmount;

    /**
     * 罚款金额
     */
    @TableField("fine_amount")
    private BigDecimal fineAmount;

    /**
     * 申报截止日期
     */
    @TableField("deadline")
    private LocalDateTime deadline;

    /**
     * 申报提交时间
     */
    @TableField("submit_time")
    private LocalDateTime submitTime;

    /**
     * 申报受理时间
     */
    @TableField("accept_time")
    private LocalDateTime acceptTime;

    /**
     * 申报审核时间
     */
    @TableField("review_time")
    private LocalDateTime reviewTime;

    /**
     * 申报完成时间
     */
    @TableField("complete_time")
    private LocalDateTime completeTime;

    /**
     * 申报渠道
     */
    @TableField("declaration_channel")
    private String declarationChannel;

    /**
     * 申报方式
     */
    @TableField("declaration_method")
    private String declarationMethod;

    /**
     * 申报人员
     */
    @TableField("declarant")
    private String declarant;

    /**
     * 申报人员ID
     */
    @TableField("declarant_id")
    private Long declarantId;

    /**
     * 审核人员
     */
    @TableField("reviewer")
    private String reviewer;

    /**
     * 审核人员ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 审核意见
     */
    @TableField("review_comment")
    private String reviewComment;

    /**
     * 税务机关
     */
    @TableField("tax_authority")
    private String taxAuthority;

    /**
     * 税务机关代码
     */
    @TableField("tax_authority_code")
    private String taxAuthorityCode;

    /**
     * 受理回执号
     */
    @TableField("receipt_number")
    private String receiptNumber;

    /**
     * 缴款书号
     */
    @TableField("payment_voucher_number")
    private String paymentVoucherNumber;

    /**
     * 申报文件路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 申报文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 申报文件大小
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 申报文件类型
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 申报文件MD5
     */
    @TableField("file_md5")
    private String fileMd5;

    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 错误代码
     */
    @TableField("error_code")
    private String errorCode;

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
     * 优先级
     */
    @TableField("priority")
    private String priority;

    /**
     * 业务分类
     */
    @TableField("business_category")
    private String businessCategory;

    /**
     * 数据来源
     */
    @TableField("data_source")
    private String dataSource;

    /**
     * 数据版本
     */
    @TableField("data_version")
    private String dataVersion;

    /**
     * 同步状态
     */
    @TableField("sync_status")
    private String syncStatus;

    /**
     * 同步时间
     */
    @TableField("sync_time")
    private LocalDateTime syncTime;

    /**
     * 标签
     */
    @TableField("tags")
    private String tags;

    /**
     * 扩展字段1
     */
    @TableField("ext_field1")
    private String extField1;

    /**
     * 扩展字段2
     */
    @TableField("ext_field2")
    private String extField2;

    /**
     * 扩展字段3
     */
    @TableField("ext_field3")
    private String extField3;

    /**
     * 扩展字段4
     */
    @TableField("ext_field4")
    private String extField4;

    /**
     * 扩展字段5
     */
    @TableField("ext_field5")
    private String extField5;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Boolean isDeleted;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
