package com.financial.sharing.vo.result;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合同履约VO
 */
@Getter
@Setter
public class ContractPerformanceVO {
    
    /**
     * 履约记录ID
     */
    private String performanceId;
    
    /**
     * 合同ID
     */
    private String contractId;
    
    /**
     * 合同编号
     */
    private String contractNo;
    
    /**
     * 合同名称
     */
    private String contractName;
    
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
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 收款金额
     */
    private BigDecimal receivedAmount;
    
    /**
     * 收款日期
     */
    private Date receivedDate;
    
    /**
     * 未收款金额
     */
    private BigDecimal unreceiveAmount;
    
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
     * 创建人
     */
    private String createBy;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 备注
     */
    private String remark;
}
