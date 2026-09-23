package com.financial.sharing.business.service;

import com.financial.sharing.business.entity.TblBudget;
import com.financial.sharing.util.MyJsonBean;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 预算服务接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
public interface BudgetService {

    /**
     * 分页查询预算列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean getList(Map<String, Object> param);

    /**
     * 根据ID查询预算详情
     *
     * @param budgetId 预算ID
     * @return 预算详情
     */
    MyJsonBean getById(String budgetId);

    /**
     * 保存或更新预算
     *
     * @param budget 预算对象
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblBudget budget);

    /**
     * 删除预算
     *
     * @param budgetId 预算ID
     * @return 操作结果
     */
    MyJsonBean delete(String budgetId);

    /**
     * 批量删除预算
     *
     * @param budgetIds 预算ID列表
     * @return 操作结果
     */
    MyJsonBean batchDelete(java.util.List<String> budgetIds);

    /**
     * 提交预算
     *
     * @param budgetId 预算ID
     * @return 操作结果
     */
    MyJsonBean submit(String budgetId);

    /**
     * 审批预算
     *
     * @param budgetId 预算ID
     * @param action 审批动作 (APPROVE-通过，REJECT-拒绝)
     * @param opinion 审批意见
     * @return 操作结果
     */
    MyJsonBean approve(String budgetId, String action, String opinion);

    /**
     * 预算控制
     *
     * @param budgetId 预算ID
     * @param amount 金额
     * @return 操作结果
     */
    MyJsonBean checkBudget(String budgetId, BigDecimal amount);

    /**
     * 根据预算编号查询
     *
     * @param budgetCode 预算编号
     * @return 预算详情
     */
    MyJsonBean getByBudgetCode(String budgetCode);

    /**
     * 预算控制检查
     */
    Map<String, Object> checkBudgetControl(String businessType, String businessId,
                                           String departmentId, String projectId,
                                           BigDecimal amount, String currency);

    /**
     * 预算占用
     */
    MyJsonBean occupy(String businessType, String businessId, String departmentId,
                      String projectId, BigDecimal amount, String currency);

    /**
     * 预算释放
     */
    MyJsonBean release(String businessType, String businessId);

    /**
     * 预算统计
     */
    MyJsonBean getStatistics(Map<String, Object> param);

    /**
     * 导出预算
     */
    MyJsonBean export(Map<String, Object> param);
}
