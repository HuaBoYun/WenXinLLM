package com.financial.sharing.vo.param;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同履约参数
 */
@Getter
@Setter
public class ContractPerformanceParam {
    
    /**
     * 合同ID
     */
    private String contractId;
    
    /**
     * 履约记录ID
     */
    private String performanceId;
    
    /**
     * 履约日期
     */
    private Date performanceDate;
    
    /**
     * 履约金额
     */
    private BigDecimal performanceAmount;
    
    /**
     * 履约内容
     */
    private String performanceContent;
    
    /**
     * 履约状态
     */
    private String performanceStatus;
    
    /**
     * 收款金额
     */
    private BigDecimal receivedAmount;
    
    /**
     * 收款日期
     */
    private Date receivedDate;
    
    /**
     * 发票号码
     */
    private String invoiceNo;
    
    /**
     * 发票金额
     */
    private BigDecimal invoiceAmount;
    
    /**
     * 发票日期
     */
    private Date invoiceDate;
    
    /**
     * 备注
     */
    private String remark;
}
