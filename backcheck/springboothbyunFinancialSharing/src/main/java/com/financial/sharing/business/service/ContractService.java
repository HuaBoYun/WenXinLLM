package com.financial.sharing.business.service;

import com.financial.sharing.business.entity.TblContract;
import com.financial.sharing.business.entity.TblContractPaymentPlan;
import com.financial.sharing.util.MyJsonBean;

import java.util.List;
import java.util.Map;

/**
 * 合同服务接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
public interface ContractService {

    /**
     * 分页查询合同列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean getList(Map<String, Object> param);

    /**
     * 根据ID查询合同详情
     *
     * @param contractId 合同ID
     * @return 合同详情
     */
    MyJsonBean getById(String contractId);

    /**
     * 保存或更新合同
     *
     * @param contract 合同对象
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblContract contract);

    /**
     * 删除合同
     *
     * @param contractId 合同ID
     * @return 操作结果
     */
    MyJsonBean delete(String contractId);

    /**
     * 批量删除合同
     *
     * @param contractIds 合同ID列表
     * @return 操作结果
     */
    MyJsonBean batchDelete(java.util.List<String> contractIds);

    /**
     * 提交合同
     *
     * @param contractId 合同ID
     * @return 操作结果
     */
    MyJsonBean submit(String contractId);

    /**
     * 审批合同
     *
     * @param contractId 合同ID
     * @param action 审批动作 (APPROVE-通过，REJECT-拒绝)
     * @param opinion 审批意见
     * @return 操作结果
     */
    MyJsonBean approve(String contractId, String action, String opinion);

    /**
     * 获取收付款计划
     *
     * @param contractId 合同ID
     * @return 收付款计划列表
     */
    MyJsonBean getPaymentPlans(String contractId);

    /**
     * 保存收付款计划
     *
     * @param contractId 合同ID
     * @param plans 收付款计划列表
     * @return 操作结果
     */
    MyJsonBean savePaymentPlans(String contractId, List<TblContractPaymentPlan> plans);

    /**
     * 根据合同编号查询
     *
     * @param contractCode 合同编号
     * @return 合同详情
     */
    MyJsonBean getByContractCode(String contractCode);

    /**
     * 导出合同
     */
    MyJsonBean export(Map<String, Object> param);

    /**
     * 合同统计
     */
    MyJsonBean getStatistics(Map<String, Object> param);

    /**
     * 合同履约
     */
    MyJsonBean fulfill(String contractId, Map<String, Object> fulfillData);

    /**
     * 付款申请
     */
    MyJsonBean paymentRequest(String contractId, Map<String, Object> paymentData);

    /**
     * 合同变更
     */
    MyJsonBean modify(String contractId, Map<String, Object> modifyData);

    /**
     * 合同终止
     */
    MyJsonBean terminate(String contractId, Map<String, Object> terminateData);

    /**
     * 合同执行分析
     */
    MyJsonBean getExecutionAnalysis(String contractId);
}
