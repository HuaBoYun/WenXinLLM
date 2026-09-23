package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblSqlTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * SQL模板Mapper接口
 * 
 * @author 华博云
 * @since 2025-01-21
 */
@Mapper
public interface TblSqlTemplateMapper extends BaseMapper<TblSqlTemplate> {

    /**
     * 插入SQL模板
     *
     * @param templateId 模板ID
     * @param templateCode 模板编码
     * @param templateName 模板名称
     * @param templateType 模板类型
     * @param businessScenario 业务场景
     * @param sqlContent SQL模板内容
     * @param parameterConfig 参数配置
     * @param thresholdConfig 阈值配置
     * @param description 描述
     * @param complexityLevel 复杂度级别
     * @param isSystem 是否系统预置
     * @param createUser 创建人
     * @return 插入结果
     */
    int insertSqlTemplate(
            @Param("templateId") String templateId,
            @Param("templateCode") String templateCode,
            @Param("templateName") String templateName,
            @Param("templateType") String templateType,
            @Param("businessScenario") String businessScenario,
            @Param("sqlContent") String sqlContent,
            @Param("parameterConfig") String parameterConfig,
            @Param("thresholdConfig") String thresholdConfig,
            @Param("description") String description,
            @Param("complexityLevel") String complexityLevel,
            @Param("isSystem") String isSystem,
            @Param("createUser") String createUser
    );
}
