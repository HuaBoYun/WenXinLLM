package com.financial.sharing.budgetControl.service;

import com.financial.sharing.budgetControl.dto.BatchReleaseRequest;
import com.financial.sharing.budgetControl.dto.BatchTransferRequest;
import com.financial.sharing.budgetControl.dto.BudgetControlRequest;
import com.financial.sharing.budgetControl.dto.BudgetControlResponse;
import com.financial.sharing.budgetControl.dto.BudgetOccupancyQueryParam;
import com.financial.sharing.budgetControl.dto.BudgetTransferRequest;
import com.financial.sharing.budgetControl.dto.ReleaseRecordQueryParam;
import com.financial.sharing.budgetControl.dto.TransferRecordQueryParam;
import com.financial.sharing.util.MyJsonBean;

/**
 * 预算控制Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface BudgetControlService {

    /**
     * 预算控制检查（OpenAPI接口）
     *
     * @param request 控制请求
     * @return 控制结果
     */
    BudgetControlResponse checkBudget(BudgetControlRequest request);

    /**
     * 预算占用
     *
     * @param request 占用请求
     * @return 操作结果
     */
    MyJsonBean occupyBudget(BudgetControlRequest request);

    /**
     * 预算释放
     *
     * @param request 释放请求
     * @return 操作结果
     */
    MyJsonBean releaseBudget(BudgetControlRequest request);

    /**
     * 预算转移
     *
     * @param fromRequest 源预算请求
     * @param toRequest 目标预算请求
     * @return 操作结果
     */
    MyJsonBean transferBudget(BudgetControlRequest fromRequest, BudgetControlRequest toRequest);

    /**
     * 查询预算占用情况
     *
     * @param bizOrgId 业务组织ID
     * @param subjectCode 科目编码
     * @param period 期间
     * @param orgId 组织ID
     * @return 占用情况
     */
    MyJsonBean queryOccupancy(String bizOrgId, String subjectCode, String period, String orgId);

    /**
     * 分页查询预算占用情况
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean queryOccupancyPage(BudgetOccupancyQueryParam param);

    /**
     * 查询预算占用统计
     *
     * @param param 查询参数
     * @return 统计数据
     */
    MyJsonBean queryOccupancyStatistics(BudgetOccupancyQueryParam param);

    /**
     * 查询预算占用趋势
     *
     * @param param 查询参数
     * @return 趋势数据
     */
    MyJsonBean queryOccupancyTrend(BudgetOccupancyQueryParam param);

    /**
     * 批量释放预算
     *
     * @param request 批量释放请求
     * @return 操作结果
     */
    MyJsonBean batchReleaseBudget(BatchReleaseRequest request);

    /**
     * 查询释放记录
     *
     * @param param 查询参数
     * @return 释放记录列表
     */
    MyJsonBean queryReleaseRecords(ReleaseRecordQueryParam param);

    /**
     * 获取释放记录统计
     *
     * @param param 查询参数
     * @return 统计数据
     */
    MyJsonBean getReleaseRecordStatistics(ReleaseRecordQueryParam param);

    /**
     * 执行预算转移（增强版）
     *
     * @param request 转移请求
     * @return 操作结果
     */
    MyJsonBean transferBudgetEnhanced(BudgetTransferRequest request);

    /**
     * 批量转移预算
     *
     * @param request 批量转移请求
     * @return 操作结果
     */
    MyJsonBean batchTransferBudget(BatchTransferRequest request);

    /**
     * 查询转移记录
     *
     * @param param 查询参数
     * @return 转移记录列表
     */
    MyJsonBean queryTransferRecords(TransferRecordQueryParam param);

    /**
     * 获取转移记录统计
     *
     * @param param 查询参数
     * @return 统计数据
     */
    MyJsonBean getTransferRecordStatistics(TransferRecordQueryParam param);
}

