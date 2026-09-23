package com.financial.sharing.budgetControl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量预算转移请求
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Data
@ApiModel(value = "BatchTransferRequest", description = "批量预算转移请求")
public class BatchTransferRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "转移请求列表")
    private List<BudgetTransferRequest> transferRequests;

    @ApiModelProperty(value = "操作用户")
    private String operateUser;

    @ApiModelProperty(value = "组织ID")
    private String orgId;
}

