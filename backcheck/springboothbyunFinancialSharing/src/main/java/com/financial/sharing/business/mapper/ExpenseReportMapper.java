package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblExpenseReport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 报销单 Mapper
 */
@Component("bizExpenseReportMapper")
public interface ExpenseReportMapper extends BaseMapper<TblExpenseReport> {

    /**
     * 根据报销单号查询
     */
    TblExpenseReport selectByReportCode(@Param("reportCode") String reportCode);

    /**
     * 根据申请人 ID 查询
     */
    List<TblExpenseReport> selectByApplicantId(@Param("applicantId") String applicantId);

    /**
     * 根据状态查询
     */
    List<TblExpenseReport> selectByReportStatus(@Param("reportStatus") String reportStatus);

    /**
     * 根据部门 ID 查询
     */
    List<TblExpenseReport> selectByDeptId(@Param("deptId") String deptId);

    /**
     * 根据报销类型查询
     */
    List<TblExpenseReport> selectByReportType(@Param("reportType") String reportType);

    /**
     * 查询待审批的报销单
     */
    List<TblExpenseReport> selectPendingReports();

    /**
     * 根据日期范围查询
     */
    List<TblExpenseReport> selectByDateRange(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

    /**
     * 统计报销金额
     */
    java.math.BigDecimal sumTotalAmount(
        @Param("applicantId") String applicantId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

    /**
     * 查询未付款的报销单
     */
    List<TblExpenseReport> selectUnpaidReports();
}
