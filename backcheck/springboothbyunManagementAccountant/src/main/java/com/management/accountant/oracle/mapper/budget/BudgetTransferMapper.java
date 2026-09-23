package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetTransfer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算转移Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetTransferMapper extends BaseMapper<BudgetTransfer> {

    /**
     * 根据转移编码查询转移
     */
    BudgetTransfer selectByTransferCode(@Param("transferCode") String transferCode);

    /**
     * 根据源预算ID查询转移列表
     */
    List<BudgetTransfer> selectBySourceBudgetId(@Param("sourceBudgetId") String sourceBudgetId);

    /**
     * 根据目标预算ID查询转移列表
     */
    List<BudgetTransfer> selectByTargetBudgetId(@Param("targetBudgetId") String targetBudgetId);

    /**
     * 根据转移状态查询转移列表
     */
    List<BudgetTransfer> selectByTransferStatus(@Param("transferStatus") String transferStatus);

    /**
     * 查询待审批的转移列表
     */
    List<BudgetTransfer> selectPendingTransfers();

    /**
     * 批量删除转移
     */
    int batchDeleteTransfers(@Param("transferIds") List<String> transferIds);
}

