package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblExpenseReportDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 报销单费用明细 Mapper
 */
@Component
public interface ExpenseReportDetailMapper extends BaseMapper<TblExpenseReportDetail> {

    /**
     * 根据报销单ID查询明细列表
     */
    @Select("SELECT DETAIL_ID, REPORT_ID, EXPENSE_ITEM_ID, EXPENSE_ITEM_NAME, EXPENSE_AMOUNT, EXPENSE_DATE, RECEIPT_COUNT, DESCRIPTION, CREATE_TIME, UPDATE_TIME FROM TBL_EXPENSE_REPORT_DETAIL WHERE REPORT_ID = #{reportId} ORDER BY CREATE_TIME ASC")
    List<TblExpenseReportDetail> selectByReportId(@Param("reportId") String reportId);

    /**
     * 根据报销单ID删除明细
     */
    @Delete("DELETE FROM TBL_EXPENSE_REPORT_DETAIL WHERE REPORT_ID = #{reportId}")
    int deleteByReportId(@Param("reportId") String reportId);
}
