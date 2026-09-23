package com.financial.sharing.vo.param;

import lombok.Data;

/**
 * 收入批量结账查询参数
 * 
 * @author AI Agent
 * @since 2025-11-29
 */
@Data
public class RevenueBatchSettlementQueryParam {
    
    /**
     * 组织编码
     */
    private String orgCode;
    
    /**
     * 组织名称
     */
    private String orgName;
    
    /**
     * 会计期间方案
     */
    private String periodScheme;
    
    /**
     * 会计期间
     */
    private String accountingPeriod;
    
    /**
     * 结账状态(0未结账1已结账)
     */
    private Integer settlementStatus;
    
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

