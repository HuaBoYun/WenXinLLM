package com.financial.sharing.consolidationReport.service;

import com.financial.sharing.consolidationReport.dto.ConsolidationScopeQueryParam;
import com.financial.sharing.consolidationReport.entity.TblConsolidationScope;

import java.util.List;

/**
 * 合并范围配置Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ConsolidationScopeService {

    /**
     * 查询合并范围配置列表
     * 
     * @param param 查询参数
     * @return 合并范围配置列表
     */
    List<TblConsolidationScope> getScopeList(ConsolidationScopeQueryParam param);

    /**
     * 根据ID查询合并范围配置
     * 
     * @param scopeId 范围配置ID
     * @return 合并范围配置
     */
    TblConsolidationScope getScopeById(String scopeId);

    /**
     * 新增合并范围配置
     * 
     * @param scope 合并范围配置
     */
    void saveScope(TblConsolidationScope scope);

    /**
     * 修改合并范围配置
     * 
     * @param scope 合并范围配置
     */
    void updateScope(TblConsolidationScope scope);

    /**
     * 删除合并范围配置
     * 
     * @param scopeId 范围配置ID
     */
    void deleteScope(String scopeId);

    /**
     * 批量保存合并范围配置
     * 
     * @param modelId 模型ID
     * @param scopeList 合并范围配置列表
     */
    void batchSaveScope(String modelId, List<TblConsolidationScope> scopeList);

    /**
     * 更新范围配置状态
     * 
     * @param scopeId 范围配置ID
     * @param isActive 是否启用
     */
    void updateScopeStatus(String scopeId, String isActive);
}

