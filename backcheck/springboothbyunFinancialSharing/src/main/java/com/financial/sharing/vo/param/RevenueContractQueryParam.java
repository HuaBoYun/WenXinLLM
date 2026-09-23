package com.financial.sharing.vo.param;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收入合同查询参数
 */
@Data
public class RevenueContractQueryParam {
    
    /**
     * 合同编号
     */
    private String contractNo;
    
    /**
     * 合同名称
     */
    private String contractName;
    
    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同状态
     */
    private String contractStatus;
    
    /**
     * 合同类型
     */
    private String contractType;
    
    /**
     * 签订日期开始
     */
    private Date signDateStart;
    
    /**
     * 签订日期结束
     */
    private Date signDateEnd;
    
    /**
     * 合同金额最小值
     */
    private BigDecimal contractAmountMin;
    
    /**
     * 合同金额最大值
     */
    private BigDecimal contractAmountMax;
    
    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 账簿ID
     */
    private Long bookId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 10;
}
