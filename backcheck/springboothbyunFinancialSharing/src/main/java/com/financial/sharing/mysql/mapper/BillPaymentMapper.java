package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.TblBillPayment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据兑付Mapper
 * @author system
 * @since 2025-01-05
 */
public interface BillPaymentMapper extends BaseMapper<TblBillPayment> {

    /**
     * 查询票据兑付记录
     * @param billId 票据ID
     * @return 兑付记录列表
     */
    List<TblBillPayment> selectByBillId(@Param("billId") String billId);
}

