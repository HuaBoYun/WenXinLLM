package com.financial.sharing.vo.result;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 损益结转结果
 *
 * @author system
 * @since 2024-12-08
 */
@Data
public class ProfitLossCarryForwardResult {

    /**
     * 结转ID
     */
    private String carryForwardId;

    /**
     * 结转期间
     */
    private String carryForwardPeriod;

    /**
     * 结转方式
     */
    private String carryForwardMethod;

    /**
     * 本年利润科目
     */
    private String currentYearProfitSubject;

    /**
     * 凭证类型
     */
    private String voucherType;

    /**
     * 结转状态：PREVIEW-预览，PROCESSING-处理中，COMPLETED-已完成，REVERSED-已反结转
     */
    private String carryForwardStatus;

    /**
     * 结转时间
     */
    private Date carryForwardTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 生成的凭证号
     */
    private String voucherNo;

    /**
     * 结转总金额
     */
    private BigDecimal totalAmount;

    /**
     * 凭证数量
     */
    private Integer voucherCount;

    /**
     * 总收入
     */
    private BigDecimal totalIncome;

    /**
     * 总费用
     */
    private BigDecimal totalExpense;

    /**
     * 净利润
     */
    private BigDecimal netProfit;
}