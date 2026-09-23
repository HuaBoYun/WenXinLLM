package com.financial.sharing.business.service;

import com.financial.sharing.business.entity.TblExpenseReport;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import java.util.List;
import java.util.Map;

/**
 * 报销单服务接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
public interface ExpenseReportService {

    /**
     * 分页查询报销单列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<TblExpenseReport>> getList(Map<String, Object> param);

    /**
     * 根据 ID 查询报销单详情
     *
     * @param reportId 报销单 ID
     * @return 报销单详情
     */
    MyJsonBean getById(String reportId);

    /**
     * 保存或更新报销单
     *
     * @param report 报销单对象
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblExpenseReport report);

    /**
     * 删除报销单
     *
     * @param reportId 报销单 ID
     * @return 操作结果
     */
    MyJsonBean delete(String reportId);

    /**
     * 批量删除报销单
     *
     * @param reportIds 报销单 ID 列表
     * @return 操作结果
     */
    MyJsonBean batchDelete(java.util.List<String> reportIds);

    /**
     * 提交报销单
     *
     * @param reportId 报销单 ID
     * @return 操作结果
     */
    MyJsonBean submit(String reportId);

    /**
     * 审批报销单
     *
     * @param reportId 报销单 ID
     * @param action 审批动作 (APPROVE-通过，REJECT-拒绝)
     * @param opinion 审批意见
     * @return 操作结果
     */
    MyJsonBean approve(String reportId, String action, String opinion);

    /**
     * 撤回报销单
     *
     * @param reportId 报销单 ID
     * @return 操作结果
     */
    MyJsonBean withdraw(String reportId);

    /**
     * 更新报销单状态
     *
     * @param reportId 报销单 ID
     * @param reportStatus 状态
     * @return 操作结果
     */
    MyJsonBean updateStatus(String reportId, String reportStatus);

    /**
     * 获取报销单统计信息
     *
     * @param param 查询参数
     * @return 统计信息
     */
    MyJsonBean getStatistics(Map<String, Object> param);

    /**
     * 根据报销单号查询
     *
     * @param reportCode 报销单号
     * @return 报销单详情
     */
    MyJsonBean getByReportCode(String reportCode);

    /**
     * 付款确认
     *
     * @param reportId 报销单 ID
     * @param paymentVoucherNo 付款凭证号
     * @return 操作结果
     */
    MyJsonBean confirmPayment(String reportId, String paymentVoucherNo);

    /**
     * 导出报销单
     */
    MyJsonBean export(Map<String, Object> param);

    /**
     * 获取报销单明细
     */
    MyJsonBean getDetails(String reportId);

    /**
     * 保存报销单明细
     */
    MyJsonBean saveDetails(String reportId, List<Map<String, Object>> details);

    /**
     * 报销单分摊
     */
    MyJsonBean allocate(String reportId, Map<String, Object> allocateData);

    /**
     * 报销单打印
     */
    MyJsonBean print(String reportId);
}
