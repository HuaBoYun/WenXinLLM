package com.financial.sharing.budgetPlanning.service;

import com.financial.sharing.budgetPlanning.dto.BudgetDataQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetData;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 预算数据编制Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface BudgetDataService {

    /**
     * 查询预算数据列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetData> getDataList(BudgetDataQueryParam param);

    /**
     * 查询预算数据列表(不分页)
     * 
     * @param param 查询参数
     * @return 预算数据列表
     */
    List<TblBudgetData> getDataListNoPage(BudgetDataQueryParam param);

    /**
     * 根据ID查询预算数据
     * 
     * @param dataId 数据ID
     * @return 预算数据
     */
    TblBudgetData getDataById(String dataId);

    /**
     * 新增预算数据
     * 
     * @param data 预算数据
     */
    void addData(TblBudgetData data);

    /**
     * 修改预算数据
     * 
     * @param data 预算数据
     */
    void updateData(TblBudgetData data);

    /**
     * 删除预算数据
     * 
     * @param dataId 数据ID
     */
    void deleteData(String dataId);

    /**
     * 批量删除预算数据
     * 
     * @param dataIds 数据ID列表
     */
    void batchDeleteData(List<String> dataIds);

    /**
     * 提交预算数据
     * 
     * @param dataId 数据ID
     */
    void submitData(String dataId);

    /**
     * 撤回预算数据
     * 
     * @param dataId 数据ID
     */
    void withdrawData(String dataId);

    /**
     * 审批预算数据
     * 
     * @param dataId 数据ID
     */
    void approveData(String dataId);

    /**
     * 批量提交预算数据
     * 
     * @param dataIds 数据ID列表
     */
    void batchSubmitData(List<String> dataIds);
}

