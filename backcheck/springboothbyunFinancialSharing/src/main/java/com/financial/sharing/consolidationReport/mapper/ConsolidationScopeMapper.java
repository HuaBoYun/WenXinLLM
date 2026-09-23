package com.financial.sharing.consolidationReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.consolidationReport.dto.ConsolidationScopeQueryParam;
import com.financial.sharing.consolidationReport.entity.TblConsolidationScope;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合并范围配置Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ConsolidationScopeMapper extends BaseMapper<TblConsolidationScope> {

    /**
     * 查询合并范围配置列表
     * 
     * @param param 查询参数
     * @return 合并范围配置列表
     */
    List<TblConsolidationScope> selectScopeList(@Param("param") ConsolidationScopeQueryParam param);

    /**
     * 检查组织是否已在合并范围内
     * 
     * @param modelId 模型ID
     * @param orgId 组织ID
     * @param excludeScopeId 排除的范围配置ID(用于编辑时排除自己)
     * @return 数量
     */
    int checkOrgExists(@Param("modelId") String modelId,
                      @Param("orgId") String orgId,
                      @Param("excludeScopeId") String excludeScopeId);

    /**
     * 批量插入合并范围配置
     * 
     * @param list 合并范围配置列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<TblConsolidationScope> list);

    /**
     * 根据模型ID删除合并范围配置
     * 
     * @param modelId 模型ID
     * @return 删除数量
     */
    int deleteByModelId(@Param("modelId") String modelId);
}

