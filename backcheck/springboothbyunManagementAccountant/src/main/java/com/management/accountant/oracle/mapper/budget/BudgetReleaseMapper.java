package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetRelease;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算释放Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetReleaseMapper extends BaseMapper<BudgetRelease> {

    /**
     * 根据释放编码查询释放
     */
    BudgetRelease selectByReleaseCode(@Param("releaseCode") String releaseCode);

    /**
     * 根据预算ID查询释放列表
     */
    List<BudgetRelease> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据释放状态查询释放列表
     */
    List<BudgetRelease> selectByReleaseStatus(@Param("releaseStatus") String releaseStatus);

    /**
     * 查询待审批的释放列表
     */
    List<BudgetRelease> selectPendingReleases();

    /**
     * 批量删除释放
     */
    int batchDeleteReleases(@Param("releaseIds") List<String> releaseIds);
}

