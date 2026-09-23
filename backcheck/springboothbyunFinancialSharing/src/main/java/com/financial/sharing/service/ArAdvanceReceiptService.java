package com.financial.sharing.service;

import com.financial.sharing.dto.param.ArAdvanceReceiptQueryParam;
import com.financial.sharing.oracle.entity.ArAdvanceReceiptEntity;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

/**
 * 预收款服务接口
 * @author system
 * @since 2026-01-04
 */
public interface ArAdvanceReceiptService {

    /**
     * 分页查询预收款列表
     */
    MyJsonBean<PageResult> getAdvanceReceiptList(ArAdvanceReceiptQueryParam param);

    /**
     * 根据ID查询预收款详情
     */
    MyJsonBean getAdvanceReceiptById(String advanceId);

    /**
     * 保存或更新预收款
     */
    MyJsonBean saveOrUpdateAdvanceReceipt(ArAdvanceReceiptEntity entity);

    /**
     * 删除预收款
     */
    MyJsonBean deleteAdvanceReceipt(String advanceId);

    /**
     * 查询客户可用预收款
     */
    MyJsonBean getAvailableAdvanceReceipts(String customerId, Long tenantId);

    /**
     * 预收款冲销
     */
    MyJsonBean offsetAdvanceReceipt(String advanceId, java.math.BigDecimal offsetAmount, String operatorId);

    /**
     * 查询预收款统计
     */
    MyJsonBean getAdvanceReceiptStatistics(ArAdvanceReceiptQueryParam param);
}

