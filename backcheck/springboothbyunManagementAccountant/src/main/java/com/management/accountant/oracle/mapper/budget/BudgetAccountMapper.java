package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算科目Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetAccountMapper extends BaseMapper<BudgetAccount> {

    /**
     * 根据科目编码查询科目
     */
    BudgetAccount selectByAccountCode(@Param("accountCode") String accountCode);

    /**
     * 根据科目类型查询科目列表
     */
    List<BudgetAccount> selectByAccountType(@Param("accountType") String accountType);

    /**
     * 根据上级科目ID查询子科目列表
     */
    List<BudgetAccount> selectByParentId(@Param("parentId") String parentId);

    /**
     * 查询科目树结构
     */
    List<BudgetAccount> selectAccountTree(@Param("parentId") String parentId);

    /**
     * 查询末级科目列表
     */
    List<BudgetAccount> selectLeafAccounts();

    /**
     * 查询启用的科目列表
     */
    List<BudgetAccount> selectEnabledAccounts();

    /**
     * 批量删除科目
     */
    int batchDeleteAccounts(@Param("accountIds") List<String> accountIds);
}

