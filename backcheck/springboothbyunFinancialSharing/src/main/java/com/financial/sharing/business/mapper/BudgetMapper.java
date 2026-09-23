package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblBudget;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 预算 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizBudgetMapper")
public interface BudgetMapper extends BaseMapper<TblBudget> {

    /**
     * 分页查询预算
     *
     * @param page 分页参数
     * @param budgetName 预算名称
     * @param budgetType 预算类型
     * @param budgetStatus 预算状态
     * @param departmentId 部门ID
     * @param budgetYear 预算年度
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页结果
     */
    IPage<TblBudget> selectBudgetPage(Page<TblBudget> page,
                                              @Param("budgetName") String budgetName,
                                              @Param("budgetType") String budgetType,
                                              @Param("budgetStatus") String budgetStatus,
                                              @Param("departmentId") String departmentId,
                                              @Param("budgetYear") String budgetYear,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    /**
     * 根据预算编号查询
     *
     * @param budgetCode 预算编号
     * @return 预算
     */
    TblBudget selectByBudgetCode(@Param("budgetCode") String budgetCode);

    /**
     * 批量删除预算
     *
     * @param ids 预算ID列表
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<String> ids);

    /**
     * 检查预算余额
     *
     * @param budgetId 预算ID
     * @param amount 金额
     * @return 是否充足
     */
    boolean checkBudgetBalance(@Param("budgetId") String budgetId, @Param("amount") java.math.BigDecimal amount);
}
