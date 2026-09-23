package com.huabo.fxgl.service;

import java.util.List;
import java.util.Map;

/**
 * 中文公式解析服务接口
 * 
 * @author AI Assistant
 * @since 2025-01-21
 */
public interface ChineseFormulaParsingService {

    /**
     * 解析中文公式为SQL表达式
     * 
     * @param chineseFormula 中文公式
     * @param sourceDataConfig 源数据配置
     * @param targetDataConfig 目标数据配置
     * @return 解析结果
     */
    Map<String, Object> parseChineseFormula(String chineseFormula, 
                                           String sourceDataConfig, 
                                           String targetDataConfig);

    /**
     * 验证中文公式语法
     * 
     * @param chineseFormula 中文公式
     * @return 验证结果
     */
    Map<String, Object> validateChineseFormula(String chineseFormula);

    /**
     * 获取支持的中文运算符列表
     * 
     * @return 运算符列表
     */
    List<Map<String, Object>> getSupportedOperators();

    /**
     * 获取常用公式模板
     * 
     * @param category 模板分类
     * @return 模板列表
     */
    List<Map<String, Object>> getFormulaTemplates(String category);

    /**
     * 智能提示功能
     * 
     * @param partialFormula 部分公式
     * @param availableFields 可用字段列表
     * @return 提示建议
     */
    List<Map<String, Object>> getFormulaSuggestions(String partialFormula, 
                                                   List<String> availableFields);

    /**
     * 格式化中文公式
     *
     * @param chineseFormula 原始公式
     * @return 格式化后的公式
     */
    String formatChineseFormula(String chineseFormula);

    /**
     * 校验并解析中文公式
     *
     * @param chineseFormula 中文公式
     * @param dataSourceConfig 数据源配置
     * @return 校验和解析结果
     */
    Map<String, Object> validateAndParseChineseFormula(String chineseFormula, Map<String, Object> dataSourceConfig);
}
