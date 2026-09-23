package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算模板Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetTemplateMapper extends BaseMapper<BudgetTemplate> {

    /**
     * 根据模板编码查询模板
     * 
     * @param templateCode 模板编码
     * @return 预算模板
     */
    BudgetTemplate selectByTemplateCode(@Param("templateCode") String templateCode);

    /**
     * 查询默认模板
     * 
     * @return 默认模板
     */
    BudgetTemplate selectDefaultTemplate();

    /**
     * 根据模板类型查询模板列表
     * 
     * @param templateType 模板类型
     * @return 模板列表
     */
    List<BudgetTemplate> selectByTemplateType(@Param("templateType") String templateType);

    /**
     * 批量启用/禁用模板
     * 
     * @param templateIds 模板ID列表
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int batchUpdateEnabled(@Param("templateIds") List<String> templateIds, @Param("isEnabled") Boolean isEnabled);

    /**
     * 批量删除模板
     *
     * @param templateIds 模板ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("templateIds") List<String> templateIds);

    /**
     * 清除默认模板标记
     *
     * @return 更新数量
     */
    int clearDefaultTemplate();

    /**
     * 设置默认模板
     *
     * @param templateId 模板ID
     * @return 更新数量
     */
    int setDefaultTemplate(@Param("templateId") String templateId);

    /**
     * 启用/禁用模板
     *
     * @param templateId 模板ID
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int toggleTemplate(@Param("templateId") String templateId, @Param("isEnabled") int isEnabled);

    /**
     * 批量启用/禁用模板
     *
     * @param templateIds 模板ID列表
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int batchToggleTemplates(@Param("templateIds") List<String> templateIds, @Param("isEnabled") int isEnabled);
}

