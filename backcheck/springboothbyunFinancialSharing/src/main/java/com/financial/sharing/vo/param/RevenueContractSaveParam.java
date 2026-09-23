package com.financial.sharing.vo.param;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 收入合同保存参数
 */
@Getter
@Setter
public class RevenueContractSaveParam {
    
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
     * 合同金额
     */
    private BigDecimal contractAmount;
    
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
     * 合同条款
     */
    private List<ContractClause> contractClauses;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 合同条款内部类
     */
    @Data
    public static class ContractClause {
        /**
         * 条款名称
         */
        private String clauseName;
        
        /**
         * 条款内容
         */
        private String clauseContent;
        
        /**
         * 条款金额
         */
        private BigDecimal clauseAmount;
    }
}
