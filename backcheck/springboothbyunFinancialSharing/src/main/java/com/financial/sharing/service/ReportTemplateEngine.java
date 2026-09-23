package com.financial.sharing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 报告模板引擎接口
 */
public interface ReportTemplateEngine {

    /**
     * 解析模板
     */
    ReportTemplate parseTemplate(String templateContent);

    /**
     * 从文件解析模板
     */
    ReportTemplate parseTemplateFromFile(String templatePath);

    /**
     * 填充数据
     */
    String fillData(ReportTemplate template, Map<String, Object> data);

    /**
     * 生成报告
     */
    byte[] generateReport(ReportTemplate template, Map<String, Object> data,
                         ReportFormat format);

    /**
     * 验证模板语法
     */
    ValidationResult validateTemplate(String templateContent);

    /**
     * 获取模板变量
     */
    TemplateVariables extractVariables(ReportTemplate template);

    /**
     * 预览报告
     */
    byte[] previewReport(ReportTemplate template, Map<String, Object> sampleData,
                        ReportFormat format, int maxRows);

    // 内部类定义
    class ReportTemplate {
        private String templateId;
        private String templateName;
        private String templateContent;
        private String templateType; // FREEMARKER, VELOCITY, THYMELEAF
        private Map<String, Object> metadata;
        private long createTime;
        private long updateTime;

        // getters and setters
        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        public String getTemplateName() { return templateName; }
        public void setTemplateName(String templateName) { this.templateName = templateName; }
        public String getTemplateContent() { return templateContent; }
        public void setTemplateContent(String templateContent) { this.templateContent = templateContent; }
        public String getTemplateType() { return templateType; }
        public void setTemplateType(String templateType) { this.templateType = templateType; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        public long getUpdateTime() { return updateTime; }
        public void setUpdateTime(long updateTime) { this.updateTime = updateTime; }
    }

    class TemplateVariables {
        private Map<String, VariableInfo> variables;
        private Map<String, Object> functions;

        // getters and setters
        public Map<String, VariableInfo> getVariables() { return variables; }
        public void setVariables(Map<String, VariableInfo> variables) { this.variables = variables; }
        public Map<String, Object> getFunctions() { return functions; }
        public void setFunctions(Map<String, Object> functions) { this.functions = functions; }
    }

    class VariableInfo {
        private String name;
        private String type;
        private String description;
        private boolean required;
        private Object defaultValue;

        // getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public Object getDefaultValue() { return defaultValue; }
        public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }
    }

    class ValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;

        public ValidationResult() {
            this.errors = new ArrayList<>();
            this.warnings = new ArrayList<>();
        }

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }

        public void addError(String error) {
            this.errors.add(error);
            this.valid = false;
        }

        public void addWarning(String warning) {
            this.warnings.add(warning);
        }
    }

    enum ReportFormat {
        PDF("pdf", "application/pdf"),
        HTML("html", "text/html"),
        EXCEL("excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        WORD("word", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        JSON("json", "application/json");

        private String extension;
        private String contentType;

        ReportFormat(String extension, String contentType) {
            this.extension = extension;
            this.contentType = contentType;
        }

        public String getExtension() { return extension; }
        public String getContentType() { return contentType; }
    }
}