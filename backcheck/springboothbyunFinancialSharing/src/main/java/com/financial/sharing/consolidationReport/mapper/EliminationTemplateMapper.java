package com.financial.sharing.consolidationReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.consolidationReport.dto.EliminationTemplateQueryParam;
import com.financial.sharing.consolidationReport.entity.TblEliminationTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 抵消凭证模板Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface EliminationTemplateMapper extends BaseMapper<TblEliminationTemplate> {

    /**
     * 查询抵消凭证模板列表
     * 
     * @param param 查询参数
     * @return 抵消凭证模板列表
     */
    List<TblEliminationTemplate> selectTemplateList(@Param("param") EliminationTemplateQueryParam param);

    /**
     * 检查模板编码是否已存在
     * 
     * @param modelId 模型ID
     * @param templateCode 模板编码
     * @param excludeTemplateId 排除的模板ID(用于编辑时排除自己)
     * @return 数量
     */
    int checkTemplateCodeExists(@Param("modelId") String modelId,
                                @Param("templateCode") String templateCode,
                                @Param("excludeTemplateId") String excludeTemplateId);

    /**
     * 根据模型ID查询抵消凭证模板列表
     * 
     * @param modelId 模型ID
     * @return 抵消凭证模板列表
     */
    List<TblEliminationTemplate> selectByModelId(@Param("modelId") String modelId);

    /**
     * 根据模型ID删除抵消凭证模板
     * 
     * @param modelId 模型ID
     * @return 删除数量
     */
    int deleteByModelId(@Param("modelId") String modelId);
}

