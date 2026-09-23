package com.global.treasurer.service;

import com.global.treasurer.entity.CashPayment;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 付款管理服务接口
 *
 * @author AI Developer
 * @date 2025-01-15
 */
public interface CashPaymentService {

    /**
     * 分页查询付款单列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Map<String, Object> getPaymentPage(Map<String, Object> param);

    /**
     * 根据ID查询付款单
     *
     * @param paymentId 付款单ID
     * @return 付款单信息
     */
    CashPayment getPaymentById(Long paymentId);

    /**
     * 创建付款单
     *
     * @param payment 付款单信息
     * @return 影响行数
     */
    int createPayment(CashPayment payment);

    /**
     * 更新付款单
     *
     * @param payment 付款单信息
     * @return 影响行数
     */
    int updatePayment(CashPayment payment);

    /**
     * 批量删除付款单
     *
     * @param ids 付款单ID列表
     * @return 影响行数
     */
    int batchDelete(List<Long> ids);

    /**
     * 批量提交审批
     *
     * @param ids 付款单ID列表
     * @param updateByName 更新人姓名
     * @return 影响行数
     */
    int batchSubmit(List<Long> ids, String updateByName);

    /**
     * 批量执行付款
     *
     * @param ids 付款单ID列表
     * @param executeByName 执行人姓名
     * @return 影响行数
     */
    int batchExecute(List<Long> ids, String executeByName);

    /**
     * 取消付款单
     *
     * @param paymentId 付款单ID
     * @param updateUser 更新人
     * @return 影响行数
     */
    int cancelPayment(Long paymentId, String updateUser);

    /**
     * 拒绝付款单
     *
     * @param paymentId 付款单ID
     * @param rejectUser 拒绝人
     * @param rejectReason 拒绝原因
     * @return 影响行数
     */
    int rejectPayment(Long paymentId, String rejectUser, String rejectReason);

    /**
     * 生成付款单号
     *
     * @param orgId 组织ID
     * @return 付款单号
     */
    String generatePaymentNo(Long orgId);

    /**
     * 导出付款单列表到Excel
     *
     * @param params 查询参数
     * @param response HTTP响应对象
     */
    void exportPayments(Map<String, Object> params, HttpServletResponse response);

    /**
     * 导出单个付款单到Excel
     *
     * @param paymentId 付款单ID
     * @param response HTTP响应对象
     */
    void exportPaymentById(Long paymentId, HttpServletResponse response);
}
