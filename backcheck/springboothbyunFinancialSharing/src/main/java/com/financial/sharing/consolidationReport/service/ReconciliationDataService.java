package com.financial.sharing.consolidationReport.service;

import com.financial.sharing.consolidationReport.dto.ReconciliationDataQueryParam;
import com.financial.sharing.consolidationReport.entity.TblReconciliationData;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 对账数据Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReconciliationDataService {

    /**
     * 查询对账数据列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblReconciliationData> getReconciliationList(ReconciliationDataQueryParam param);

    /**
     * 根据ID查询对账数据
     * 
     * @param reconciliationId 对账数据ID
     * @return 对账数据
     */
    TblReconciliationData getReconciliationById(String reconciliationId);

    /**
     * 新增对账数据
     * 
     * @param data 对账数据
     */
    void saveReconciliation(TblReconciliationData data);

    /**
     * 修改对账数据
     * 
     * @param data 对账数据
     */
    void updateReconciliation(TblReconciliationData data);

    /**
     * 删除对账数据
     * 
     * @param reconciliationId 对账数据ID
     */
    void deleteReconciliation(String reconciliationId);

    /**
     * 批量删除对账数据
     * 
     * @param modelId 模型ID
     * @param period 期间
     */
    void deleteByModelIdAndPeriod(String modelId, String period);

    /**
     * 批量导入对账数据
     * 
     * @param dataList 对账数据列表
     * @param modelId 模型ID
     * @param period 期间
     * @return 导入结果
     */
    Map<String, Object> batchImport(List<TblReconciliationData> dataList, String modelId, String period);

    /**
     * 根据模型ID和期间统计对账数据
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 统计结果
     */
    Map<String, Object> getStatistics(String modelId, String period);
}

