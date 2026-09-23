package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArReceiptSaveParam;
import com.financial.sharing.vo.param.ArReceiptQueryParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收款单服务接口
 * @author system
 * @since 2026-01-04
 */
public interface ArReceiptService {

    /**
     * 分页查询收款单列表
     */
    MyJsonBean<PageResult> getReceiptList(ArReceiptQueryParam param);

    /**
     * 根据ID查询收款单详情
     */
    MyJsonBean getReceiptById(String receiptId);

    /**
     * 根据收款单号查询收款单
     */
    MyJsonBean getReceiptByNo(String receiptNo, Long tenantId);

    /**
     * 保存或更新收款单
     */
    MyJsonBean saveOrUpdateReceipt(ArReceiptSaveParam param);

    /**
     * 删除收款单
     */
    MyJsonBean deleteReceipt(String receiptId);

    /**
     * 批量删除收款单
     */
    MyJsonBean batchDeleteReceipt(List<String> receiptIds);

    /**
     * 确认收款
     */
    MyJsonBean confirmReceipt(String receiptId, BigDecimal confirmAmount, String confirmBy);

    /**
     * 取消收款单
     */
    MyJsonBean cancelReceipt(String receiptId, String cancelReason, String cancelBy);

    /**
     * 查询客户的收款单
     */
    MyJsonBean getReceiptsByCustomer(String customerId, Long tenantId);

    /**
     * 查询待核销的收款单
     */
    MyJsonBean getPendingWriteOffReceipts(String customerId, Long tenantId);

    /**
     * 查询收款单统计
     */
    MyJsonBean getReceiptStatistics(ArReceiptQueryParam param);

    /**
     * 导出收款单
     */
    MyJsonBean exportReceipts(ArReceiptQueryParam param);

    /**
     * 生成收款单号
     */
    String generateReceiptNo(Long tenantId);

    /**
     * 批量核销收款单
     * @param receiptIds 收款单ID列表
     * @param operatorId 操作人ID
     * @param remark 核销备注
     * @return 核销结果
     */
    MyJsonBean batchWriteOff(List<String> receiptIds, String operatorId, String remark);
}

