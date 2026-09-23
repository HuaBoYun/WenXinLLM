package com.financial.sharing.vo.result;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 收入合同VO
 */
@Data
public class RevenueContractVO {
    
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
     * 客户名称
     */
    private String customerName;
    
    /**
     * 客户ID
     */
    private String customerId;
    
    /**
     * 合同类型
     */
    private String contractType;
    
    /**
     * 合同状态
     */
    private String contractStatus;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 合同金额
     */
    private BigDecimal contractAmount;
    
    /**
     * 已履约金额
     */
    private BigDecimal performedAmount;
    
    /**
     * 已收款金额
     */
    private BigDecimal receivedAmount;
    
    /**
     * 未收款金额
     */
    private BigDecimal unreceiveAmount;
    
    /**
     * 币种
     */
    private String currency;
    
    /**
     * 签订日期
     */
    private Date signDate;
    
    /**
     * 生效日期
     */
    private Date effectiveDate;
    
    /**
     * 到期日期
     */
    private Date expiryDate;
    
    /**
     * 负责人
     */
    private String responsiblePerson;
    
    /**
     * 履约进度
     */
    private BigDecimal performanceProgress;
    
    /**
     * 收款进度
     */
    private BigDecimal receivedProgress;
    
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

    /**
     * 合同状态名称
     */
    private String contractStatusName;

    /**
     * 确认方法
     */
    private String recognitionMethod;

    /**
     * 确认方法名称
     */
    private String recognitionMethodName;

    /**
     * 履约义务
     */
    private String performanceObligations;

    /**
     * 合同描述
     */
    private String contractDesc;
}
