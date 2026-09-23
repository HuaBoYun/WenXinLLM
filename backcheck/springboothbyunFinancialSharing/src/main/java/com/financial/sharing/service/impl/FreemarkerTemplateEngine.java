package com.financial.sharing.service.impl;

import com.financial.sharing.service.ReportTemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Freemarker模板引擎实现
 */
@Slf4j
@Service("freemarkerTemplateEngine")
public class FreemarkerTemplateEngine implements ReportTemplateEngine {

    private final Configuration configuration;

    public FreemarkerTemplateEngine() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_31);
        this.configuration.setDefaultEncoding("UTF-8");
        this.configuration.setNumberFormat("0.##");
    }

    @Override
    public ReportTemplate parseTemplate(String templateContent) {
        ReportTemplate template = new ReportTemplate();
        template.setTemplateId("FTL_" + System.currentTimeMillis());
        template.setTemplateName("Freemarker Template");
        template.setTemplateContent(templateContent);
        template.setTemplateType("FREEMARKER");
        template.setCreateTime(System.currentTimeMillis());
        template.setUpdateTime(System.currentTimeMillis());

        // 解析模板以验证语法
        try {
            StringReader reader = new StringReader(templateContent);
            new Template("temp", reader, configuration);
            log.debug("模板解析成功");
        } catch (IOException e) {
            log.error("模板解析IO异常", e);
            throw new RuntimeException("模板解析失败: " + e.getMessage());
        }

        return template;
    }

    @Override
    public ReportTemplate parseTemplateFromFile(String templatePath) {
        try {
            Template ftlTemplate = configuration.getTemplate(templatePath);

            ReportTemplate template = new ReportTemplate();
            template.setTemplateId("FTL_FILE_" + System.currentTimeMillis());
            template.setTemplateName(new File(templatePath).getName());
            template.setTemplateContent(getTemplateContent(ftlTemplate));
            template.setTemplateType("FREEMARKER");
            template.setCreateTime(System.currentTimeMillis());
            template.setUpdateTime(System.currentTimeMillis());

            return template;
        } catch (IOException e) {
            log.error("从文件解析模板失败: {}", templatePath, e);
            throw new RuntimeException("模板文件解析失败: " + e.getMessage());
        }
    }

    @Override
    public String fillData(ReportTemplate template, Map<String, Object> data) {
        try {
            StringReader reader = new StringReader(template.getTemplateContent());
            Template ftlTemplate = new Template("template", reader, configuration);

            StringWriter writer = new StringWriter();
            ftlTemplate.process(data, writer);

            return writer.toString();
        } catch (IOException | TemplateException e) {
            log.error("填充模板数据失败", e);
            throw new RuntimeException("填充模板数据失败: " + e.getMessage());
        }
    }

    @Override
    public byte[] generateReport(ReportTemplate template, Map<String, Object> data,
                               ReportFormat format) {
        String filledContent = fillData(template, data);

        switch (format) {
            case HTML:
                return filledContent.getBytes();
            case PDF:
                return convertToPdf(filledContent);
            case WORD:
                return convertToWord(filledContent);
            case EXCEL:
                return convertToExcel(filledContent, data);
            case JSON:
                return convertToJson(filledContent);
            default:
                throw new IllegalArgumentException("不支持的报告格式: " + format);
        }
    }

    @Override
    public ValidationResult validateTemplate(String templateContent) {
        ValidationResult result = new ValidationResult();
        result.setValid(true);

        try {
            StringReader reader = new StringReader(templateContent);
            Template template = new Template("validation", reader, configuration);

            // 检查模板语法
            StringWriter writer = new StringWriter();
            template.process(Collections.emptyMap(), writer);

            // 检查常用指令
            checkTemplateSyntax(templateContent, result);

        } catch (TemplateException e) {
            result.setValid(false);
            result.addError("模板语法错误: " + e.getMessage());
        } catch (IOException e) {
            result.setValid(false);
            result.addError("模板IO错误: " + e.getMessage());
        }

        return result;
    }

    @Override
    public TemplateVariables extractVariables(ReportTemplate template) {
        TemplateVariables variables = new TemplateVariables();
        Map<String, VariableInfo> varMap = new HashMap<>();
        Map<String, Object> functionMap = new HashMap<>();

        String content = template.getTemplateContent();

        // 提取变量引用 ${variable}
        extractVariableReferences(content, varMap);

        // 提取指令 <#assign>, <#list>等
        extractDirectives(content, varMap, functionMap);

        variables.setVariables(varMap);
        variables.setFunctions(functionMap);

        return variables;
    }

    @Override
    public byte[] previewReport(ReportTemplate template, Map<String, Object> sampleData,
                               ReportFormat format, int maxRows) {
        // 限制预览数据量
        Map<String, Object> limitedData = limitDataSize(sampleData, maxRows);

        return generateReport(template, limitedData, format);
    }

    // 私有辅助方法
    private String getTemplateContent(Template template) throws IOException {
        StringWriter writer = new StringWriter();
        template.dump(writer);
        return writer.toString();
    }

    private byte[] convertToPdf(String htmlContent) {
        // 这里需要使用HTML转PDF的库
        // 暂时返回模拟数据
        return ("PDF Content: " + htmlContent).getBytes();
    }

    private byte[] convertToWord(String htmlContent) {
        // 这里需要使用HTML转Word的库
        // 暂时返回模拟数据
        return ("Word Content: " + htmlContent).getBytes();
    }

    private byte[] convertToExcel(String content, Map<String, Object> data) {
        // 这里需要使用Excel生成库
        // 暂时返回模拟数据
        return ("Excel Content: " + content).getBytes();
    }

    private byte[] convertToJson(String content) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("content", content);
        jsonMap.put("timestamp", System.currentTimeMillis());

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsBytes(jsonMap);
        } catch (Exception e) {
            throw new RuntimeException("JSON转换失败", e);
        }
    }

    private void checkTemplateSyntax(String content, ValidationResult result) {
        // 检查未闭合的标签
        int openTags = countOccurrences(content, "<#");
        int closeTags = countOccurrences(content, "</#");

        if (openTags != closeTags) {
            result.addWarning("存在未配对的指令标签");
        }

        // 检查未闭合的变量
        int openVars = countOccurrences(content, "${");
        int closeVars = countOccurrences(content, "}");

        if (openVars != closeVars) {
            result.addWarning("存在未配对的变量引用");
        }
    }

    private int countOccurrences(String text, String pattern) {
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }

        return count;
    }

    private void extractVariableReferences(String content, Map<String, VariableInfo> varMap) {
        // 简单的变量提取实现
        // 实际项目中可以使用正则表达式或解析器
        String[] lines = content.split("\n");

        for (String line : lines) {
            int start = 0;
            while ((start = line.indexOf("${", start)) != -1) {
                int end = line.indexOf("}", start);
                if (end != -1) {
                    String varName = line.substring(start + 2, end);
                    if (!varMap.containsKey(varName)) {
                        VariableInfo info = new VariableInfo();
                        info.setName(varName);
                        info.setType("Object");
                        info.setRequired(false);
                        varMap.put(varName, info);
                    }
                    start = end + 1;
                } else {
                    break;
                }
            }
        }
    }

    private void extractDirectives(String content, Map<String, VariableInfo> varMap,
                                 Map<String, Object> functionMap) {
        // 提取指令，如 <#list>, <#if>等
        // 简化实现，实际项目中需要完整的解析器
        String[] directives = {"list", "if", "foreach", "assign"};

        for (String directive : directives) {
            String pattern = "<#" + directive;
            if (content.contains(pattern)) {
                functionMap.put(directive, "Freemarker指令: " + directive);
            }
        }
    }

    private Map<String, Object> limitDataSize(Map<String, Object> data, int maxRows) {
        Map<String, Object> limitedData = new HashMap<>();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof List) {
                List<?> list = (List<?>) value;
                if (list.size() > maxRows) {
                    limitedData.put(entry.getKey(), list.subList(0, maxRows));
                } else {
                    limitedData.put(entry.getKey(), value);
                }
            } else {
                limitedData.put(entry.getKey(), value);
            }
        }

        return limitedData;
    }
}