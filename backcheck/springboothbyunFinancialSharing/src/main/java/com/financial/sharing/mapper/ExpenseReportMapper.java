package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.entity.TblExpenseReport;
import com.financial.sharing.util.PageableParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 报销单 Mapper接口 - Oracle/达梦版本
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Component("expenseReportMapper")
public interface ExpenseReportMapper extends BaseMapper<TblExpenseReport> {

    /**
     * 分页查询报销单
     *
     * @param page 分页参数
     * @param reportCode 报销单号
     * @param applicant 申请人
     * @param reportStatus 状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页结果
     */
    IPage<TblExpenseReport> selectExpenseReportPage(Page<TblExpenseReport> page,
                                                 @Param("reportCode") String reportCode,
                                                 @Param("applicant") String applicant,
                                                 @Param("reportStatus") String reportStatus,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    /**
     * 根据报销单号查询
     *
     * @param reportCode 报销单号
     * @return 报销单
     */
    TblExpenseReport selectByReportCode(@Param("reportCode") String reportCode);

    /**
     * 统计报销单数量按状态
     *
     * @return 统计结果
     */
    List<TblExpenseReport> countByStatus();

    /**
     * 统计报销单金额按类型
     *
     * @return 统计结果
     */
    List<TblExpenseReport> sumAmountByType();

    /**
     * 批量删除报销单
     *
     * @param ids 报销单ID列表
     * @param updater 更新人
     * @return 删除数量
     */
    int batchDelete(@Param("ids") List<String> ids, @Param("updater") String updater);

    /**
     * 更新报销单状态
     *
     * @param ids 报销单ID列表
     * @param reportStatus 状态
     * @param updater 更新人
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ids") List<String> ids,
                       @Param("reportStatus") String reportStatus,
                       @Param("updater") String updater);
}
