package com.financial.sharing.service;

import com.financial.sharing.dto.param.PaymentOrderQueryParam;
import com.financial.sharing.dto.param.PaymentOrderSaveParam;
import com.financial.sharing.dto.param.WriteOffParam;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PaymentOrderVO;

import java.util.List;

/**
 * 付款单Service接口
 * @author system
 * @since 2025-01-05
 */
public interface PaymentOrderService {

    /**
     * 分页查询付款单
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<PaymentOrderVO> queryPage(PaymentOrderQueryParam param);

    /**
     * 查询付款单详情
     * @param paymentId 付款ID
     * @return 付款单详情
     */
    PaymentOrderVO getDetail(String paymentId);

    /**
     * 保存付款单(新增或修改)
     * @param param 保存参数
     * @return 付款ID
     */
    String save(PaymentOrderSaveParam param);

    /**
     * 删除付款单
     * @param paymentId 付款ID
     */
    void delete(String paymentId);

    /**
     * 确认付款
     * @param paymentId 付款ID
     */
    void confirmPayment(String paymentId);

    /**
     * 核销付款
     * @param param 核销参数
     */
    void writeOff(WriteOffParam param);

    /**
     * 查询供应商付款单
     * @param supplierId 供应商ID
     * @return 付款单列表
     */
    List<PaymentOrderVO> queryBySupplier(String supplierId);
}

