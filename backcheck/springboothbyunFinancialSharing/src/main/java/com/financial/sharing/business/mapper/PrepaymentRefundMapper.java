package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblPrepaymentRefund;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 预付款退款记录 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizPrepaymentRefundMapper")
public interface PrepaymentRefundMapper extends BaseMapper<TblPrepaymentRefund> {

    /**
     * 根据预付款单ID查询退款记录
     *
     * @param prepaymentId 预付款单ID
     * @return 退款记录列表
     */
    List<TblPrepaymentRefund> selectByPrepaymentId(@Param("prepaymentId") String prepaymentId);

    /**
     * 统计退款金额
     *
     * @param prepaymentId 预付款单ID
     * @return 退款金额
     */
    java.math.BigDecimal sumRefundAmount(@Param("prepaymentId") String prepaymentId);
}
