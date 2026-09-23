package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetAccountMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预算科目映射Mapper
 *
 * @author AI Agent
 * @date 2026-03-31
 */
@Mapper
public interface BudgetAccountMappingMapper extends BaseMapper<BudgetAccountMapping> {

    /**
     * 根据科目ID查询映射列表
     *
     * @param accountId 科目ID
     * @return 映射列表
     */
    @Select("SELECT * FROM TBL_BUDGET_ACCOUNT_MAPPING WHERE ACCOUNT_ID = #{accountId} AND DEL_FLAG = 0 ORDER BY CREATE_TIME")
    List<BudgetAccountMapping> selectByAccountId(@Param("accountId") String accountId);
}
