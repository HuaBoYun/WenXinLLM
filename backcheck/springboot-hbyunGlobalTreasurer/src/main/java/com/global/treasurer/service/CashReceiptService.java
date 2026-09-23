package com.global.treasurer.service;

import com.global.treasurer.entity.CashReceipt;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 收款管理服务接口
 *
 * @author AI Developer
 * @date 2025-01-15
 */
public interface CashReceiptService {

    /**
     * 分页查询收款单列表
     */
    Map<String, Object> getReceiptPage(Map<String, Object> param);

    /**
     * 根据ID查询收款单详情
     */
    CashReceipt getReceiptById(Long receiptId);

    /**
     * 创建收款单
     */
    int createReceipt(CashReceipt receipt);

    /**
     * 更新收款单
     */
    int updateReceipt(CashReceipt receipt);

    /**
     * 批量删除收款单
     */
    int batchDelete(List<Long> ids);

    /**
     * 批量确认收款
     */
    int batchConfirm(List<Long> ids, String confirmByName);

    /**
     * 核销收款单
     */
    int verifyReceipt(Long receiptId, String verifyUser);

    /**
     * 取消收款单
     */
    int cancelReceipt(Long receiptId, String updateUser);

    /**
     * 生成收款单号
     */
    String generateReceiptNo(Long orgId);

    /**
     * 导出收款单列表到Excel
     *
     * @param params 查询参数
     * @param response HTTP响应对象
     */
    void exportReceipts(Map<String, Object> params, HttpServletResponse response);

    /**
     * 导出单个收款单到Excel
     *
     * @param receiptId 收款单ID
     * @param response HTTP响应对象
     */
    void exportReceiptById(Long receiptId, HttpServletResponse response);
}
