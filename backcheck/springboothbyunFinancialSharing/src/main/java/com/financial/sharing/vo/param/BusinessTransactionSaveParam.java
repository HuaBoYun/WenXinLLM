package com.financial.sharing.vo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 业务事项保存参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
public class BusinessTransactionSaveParam {

    /**
     * 事项ID（更新时必填）
     */
    private Long transactionId;

    /**
     * 事项编号
     */
    @NotBlank(message = "事项编号不能为空")
    @Size(max = 50, message = "事项编号长度不能超过50个字符")
    private String transactionNo;

    /**
     * 事项类型
     */
    @NotBlank(message = "事项类型不能为空")
    @Size(max = 50, message = "事项类型长度不能超过50个字符")
    private String transactionType;

    /**
     * 事项日期
     */
    @NotNull(message = "事项日期不能为空")
    private LocalDate transactionDate;

    /**
     * 业务数据(JSON格式)
     */
    private String businessData;

    /**
     * 来源系统
     */
    @Size(max = 50, message = "来源系统长度不能超过50个字符")
    private String sourceSystem;

    /**
     * 事项状态(1待处理2已处理3已取消)
     */
    private Integer transactionStatus;

    /**
     * 账簿ID
     */
    @NotNull(message = "账簿ID不能为空")
    private Long bookId;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
     * 版本号
     */
    private Integer version;
}
