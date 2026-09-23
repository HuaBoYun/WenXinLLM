package com.huabo.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 合同类型统计VO
 * 用于映射getContractType查询结果
 * 
 * @author AI Agent
 * @since 2025-01-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="ContractTypeVO", description="合同类型统计视图对象")
public class ContractTypeVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 合同类型名称
     */
    @Schema(name = "合同类型名称")
    private String name;
    
    /**
     * 合同数量
     */
    @Schema(name = "合同数量")
    private Integer value;
}
