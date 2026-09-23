package com.financial.sharing.consolidationReport.service;

import com.financial.sharing.consolidationReport.dto.ConsolidationModelQueryParam;
import com.financial.sharing.consolidationReport.entity.TblConsolidationModel;

import java.util.List;

/**
 * 合并模型Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ConsolidationModelService {

    /**
     * 查询合并模型列表
     * 
     * @param param 查询参数
     * @return 合并模型列表
     */
    List<TblConsolidationModel> getModelList(ConsolidationModelQueryParam param);

    /**
     * 根据ID查询合并模型
     * 
     * @param modelId 模型ID
     * @return 合并模型
     */
    TblConsolidationModel getModelById(String modelId);

    /**
     * 新增合并模型
     * 
     * @param model 合并模型
     */
    void saveModel(TblConsolidationModel model);

    /**
     * 修改合并模型
     * 
     * @param model 合并模型
     */
    void updateModel(TblConsolidationModel model);

    /**
     * 删除合并模型
     * 
     * @param modelId 模型ID
     */
    void deleteModel(String modelId);

    /**
     * 更新模型状态
     * 
     * @param modelId 模型ID
     * @param status 状态
     */
    void updateModelStatus(String modelId, String status);
}

