package com.financial.sharing.vo.param;

import com.financial.sharing.util.PageableParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 业务事项查询参数
 * 
 * @author system
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessTransactionQueryParam extends PageableParam {

    /**
     * 事项编号
     */
    private String transactionNo;

    /**
     * 事项类型
     */
    private String transactionType;

    /**
     * 事项日期开始
     */
    private LocalDate transactionDateStart;

    /**
     * 事项日期结束
     */
    private LocalDate transactionDateEnd;

    /**
     * 来源系统
     */
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
}
