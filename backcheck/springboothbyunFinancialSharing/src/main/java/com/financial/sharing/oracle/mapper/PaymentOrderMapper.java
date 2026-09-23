package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.dto.param.PaymentOrderQueryParam;
import com.financial.sharing.oracle.entity.TblPaymentOrder;
import com.financial.sharing.vo.result.PaymentOrderVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 付款单Mapper
 * @author system
 * @since 2025-01-05
 */
public interface PaymentOrderMapper extends BaseMapper<TblPaymentOrder> {

    /**
     * 分页查询付款单
     * @param param 查询参数
     * @return 付款单列表
     */
    List<PaymentOrderVO> selectPageList(@Param("param") PaymentOrderQueryParam param);

    /**
     * 查询付款单详情
     * @param paymentId 付款ID
     * @return 付款单详情
     */
    PaymentOrderVO selectDetailById(@Param("paymentId") String paymentId);

    /**
     * 统计付款金额汇总
     * @param supplierId 供应商ID(可选)
     * @return 汇总信息
     */
    Map<String, BigDecimal> selectPaymentSummary(@Param("supplierId") String supplierId);

    /**
     * 查询供应商付款单
     * @param supplierId 供应商ID
     * @return 付款单列表
     */
    List<PaymentOrderVO> selectBySupplier(@Param("supplierId") String supplierId);

    /**
     * 更新核销金额
     * @param paymentId 付款ID
     * @param writeOffAmount 核销金额
     * @return 影响行数
     */
    int updateWriteOffAmount(@Param("paymentId") String paymentId, @Param("writeOffAmount") BigDecimal writeOffAmount);
}

