package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFundPlanTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资金计划模板Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Mapper
public interface FundPlanTemplateMapper extends BaseMapper<TblFundPlanTemplate> {

    /**
     * 分页查询资金计划模板列表
     *
     * @param params 查询参数
     * @return 资金计划模板列表
     */
    List<TblFundPlanTemplate> selectTemplatePage(Map<String, Object> params);

    /**
     * 统计模板数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countTemplateList(Map<String, Object> params);

    /**
     * 查询模板汇总信息
     *
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectTemplateSummary(Map<String, Object> params);

    /**
     * 根据模板类型查询默认模板
     *
     * @param templateType 模板类型
     * @param orgId 组织ID
     * @return 模板
     */
    TblFundPlanTemplate selectDefaultTemplate(@Param("templateType") String templateType, @Param("orgId") Long orgId);

    int updateTemplateStatus(@Param("templateId") Long templateId, @Param("status") String status);

    int updateTemplateUsage(@Param("templateId") Long templateId);

    TblFundPlanTemplate selectTemplateById(@Param("templateId") Long templateId);
}
