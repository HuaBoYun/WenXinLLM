package com.global.treasurer.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行承兑汇票DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Data
public class BankAcceptanceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 承兑汇票ID */
    private Long acceptanceId;

    /** 承兑汇票编号 */
    private String acceptanceNumber;

    /** 承兑金额 */
    @NotNull(message = "承兑金额不能为空")
    private BigDecimal acceptanceAmount;

    /** 币种 */
    private String currency;

    /** 出票日期 */
    private Date issueDate;

    /** 到期日期 */
    @NotNull(message = "到期日期不能为空")
    private Date maturityDate;

    /** 出票人 */
    @NotBlank(message = "出票人不能为空")
    private String drawer;

    /** 收款人 */
    @NotBlank(message = "收款人不能为空")
    private String payee;

    /** 承兑银行 */
    @NotBlank(message = "承兑银行不能为空")
    private String acceptingBank;

    /** 承兑银行账号 */
    private String acceptingBankAccount;

    /** 保证金金额 */
    private BigDecimal marginAmount;

    /** 保证金比例 */
    private BigDecimal marginRate;

    /** 公司ID */
    private String companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;

    // 显式添加getter方法以确保编译通过
    public Long getAcceptanceId() {
        return acceptanceId;
    }
}

