package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblBill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 账单 Mapper
 */
@Component("bizBillMapper")
public interface BillMapper extends BaseMapper<TblBill> {

    /**
     * 根据账单号码查询
     */
    TblBill selectByBillNumber(@Param("billNumber") String billNumber);

    /**
     * 根据状态查询
     */
    List<TblBill> selectByStatus(@Param("status") String status);

    /**
     * 根据供应商名称查询
     */
    List<TblBill> selectBySupplierName(@Param("supplierName") String supplierName);

    /**
     * 根据账单类型查询
     */
    List<TblBill> selectByBillType(@Param("billType") String billType);

    /**
     * 查询待稽核账单
     */
    List<TblBill> selectPendingAudit();

    /**
     * 根据日期范围查询
     */
    List<TblBill> selectByDateRange(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

    /**
     * 统计账单金额
     */
    java.math.BigDecimal sumBillAmount(
        @Param("status") String status,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

    /**
     * 根据目标类型和目标ID查询
     */
    List<TblBill> selectByTarget(
        @Param("targetType") String targetType,
        @Param("targetId") String targetId
    );
}
