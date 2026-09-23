package com.financial.sharing.business.service;

import com.financial.sharing.business.entity.TblExpenseProvision;
import com.financial.sharing.util.MyJsonBean;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 费用预提服务接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
public interface ExpenseProvisionService {

    /**
     * 分页查询预提单列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean getList(Map<String, Object> param);

    /**
     * 根据ID查询预提单详情
     *
     * @param provisionId 预提单ID
     * @return 预提单详情
     */
    MyJsonBean getById(String provisionId);

    /**
     * 保存或更新预提单
     *
     * @param provision 预提单对象
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblExpenseProvision provision);

    /**
     * 删除预提单
     *
     * @param provisionId 预提单ID
     * @return 操作结果
     */
    MyJsonBean delete(String provisionId);

    /**
     * 批量删除预提单
     *
     * @param provisionIds 预提单ID列表
     * @return 操作结果
     */
    MyJsonBean batchDelete(java.util.List<String> provisionIds);

    /**
     * 提交预提单
     *
     * @param provisionId 预提单ID
     * @return 操作结果
     */
    MyJsonBean submit(String provisionId);

    /**
     * 审批预提单
     *
     * @param provisionId 预提单ID
     * @param action 审批动作 (APPROVE-通过，REJECT-拒绝)
     * @param opinion 审批意见
     * @return 操作结果
     */
    MyJsonBean approve(String provisionId, String action, String opinion);

    /**
     * 撤回预提单
     *
     * @param provisionId 预提单ID
     * @return 操作结果
     */
    MyJsonBean withdraw(String provisionId);

    /**
     * 冲销预提单
     *
     * @param provisionId 预提单ID
     * @param voucherNo 冲销凭证号
     * @return 操作结果
     */
    MyJsonBean reverse(String provisionId, String voucherNo);

    /**
     * 根据预提单号查询
     *
     * @param provisionCode 预提单号
     * @return 预提单详情
     */
    MyJsonBean getByProvisionCode(String provisionCode);

    /**
     * 导出预提单
     */
    MyJsonBean export(Map<String, Object> param);

    /**
     * 预提单统计
     */
    MyJsonBean getStatistics(Map<String, Object> param);
}
