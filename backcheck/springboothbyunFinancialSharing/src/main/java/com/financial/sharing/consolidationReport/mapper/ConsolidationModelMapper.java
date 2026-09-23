package com.financial.sharing.consolidationReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.consolidationReport.dto.ConsolidationModelQueryParam;
import com.financial.sharing.consolidationReport.entity.TblConsolidationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合并模型Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ConsolidationModelMapper extends BaseMapper<TblConsolidationModel> {

    /**
     * 查询合并模型列表
     * 
     * @param param 查询参数
     * @return 合并模型列表
     */
    List<TblConsolidationModel> selectModelList(@Param("param") ConsolidationModelQueryParam param);

    /**
     * 检查模型编码是否存在
     * 
     * @param modelCode 模型编码
     * @param tenantId 租户ID
     * @param excludeModelId 排除的模型ID(用于编辑时排除自己)
     * @return 数量
     */
    int checkModelCodeExists(@Param("modelCode") String modelCode, 
                            @Param("tenantId") String tenantId,
                            @Param("excludeModelId") String excludeModelId);
}

