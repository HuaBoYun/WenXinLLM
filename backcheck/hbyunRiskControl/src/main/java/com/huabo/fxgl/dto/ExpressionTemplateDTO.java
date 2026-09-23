package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 表达式模板数据传输对象
 * 
 * @author AI Assistant
 * @date 2025-01-21
 * @version 1.0
 */
@Schema(description="表达式模板数据传输对象")
public class ExpressionTemplateDTO {
    
    @Schema(name = "模板ID", example = "TEMPLATE_001")
    private String templateId;
    
    @Schema(name = "模板编码", required = true, example = "FA_AMOUNT_CHECK")
    private String templateCode;

    @Schema(name = "模板名称", required = true, example = "金额异常检测")
    private String templateName;

    @Schema(name="模板分类")
    private String templateCategory;

    @Schema(name = "业务场景描述", example = "检测交易金额超过阈值的异常情况")
    private String businessScenario;

    @Schema(name = "中文公式模板", required = true, example = "金额 大于 {threshold_amount}")
    private String chineseFormula;

    @Schema(name = "SQL模板", required = true, example = "AMOUNT > {threshold_amount}")
    private String sqlTemplate;
    
    @Schema(name="参数配置JSON")
    private String parameterConfig;
    
    @Schema(name = "模板描述", example = "用于检测大额交易或异常金额")
    private String description;
    
    @Schema(name = "使用示例", example = "示例：金额 大于 1000000，用于识别大额交易")
    private String exampleUsage;
    
    @Schema(name = "是否系统预置模板", example = "false")
    private Boolean isSystemTemplate;
    
    @Schema(name = "是否启用", example = "true")
    private Boolean isEnabled;
    
    @Schema(name = "使用次数", example = "15")
    private Integer usageCount;
    
    @Schema(name = "创建人", example = "SYSTEM")
    private String createUser;
    
    @Schema(name = "创建时间", example = "2025-01-21T10:00:00")
    private String createTime;
    
    @Schema(name = "更新人", example = "ADMIN")
    private String updateUser;
    
    @Schema(name = "更新时间", example = "2025-01-21T15:30:00")
    private String updateTime;
    
    public ExpressionTemplateDTO() {
    }
    
    public String getTemplateId() {
        return templateId;
    }
    
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    
    public String getTemplateCode() {
        return templateCode;
    }
    
    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public String getTemplateCategory() {
        return templateCategory;
    }
    
    public void setTemplateCategory(String templateCategory) {
        this.templateCategory = templateCategory;
    }
    
    public String getBusinessScenario() {
        return businessScenario;
    }
    
    public void setBusinessScenario(String businessScenario) {
        this.businessScenario = businessScenario;
    }
    
    public String getChineseFormula() {
        return chineseFormula;
    }
    
    public void setChineseFormula(String chineseFormula) {
        this.chineseFormula = chineseFormula;
    }
    
    public String getSqlTemplate() {
        return sqlTemplate;
    }
    
    public void setSqlTemplate(String sqlTemplate) {
        this.sqlTemplate = sqlTemplate;
    }
    
    public String getParameterConfig() {
        return parameterConfig;
    }
    
    public void setParameterConfig(String parameterConfig) {
        this.parameterConfig = parameterConfig;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getExampleUsage() {
        return exampleUsage;
    }
    
    public void setExampleUsage(String exampleUsage) {
        this.exampleUsage = exampleUsage;
    }
    
    public Boolean getIsSystemTemplate() {
        return isSystemTemplate;
    }
    
    public void setIsSystemTemplate(Boolean isSystemTemplate) {
        this.isSystemTemplate = isSystemTemplate;
    }
    
    public Boolean getIsEnabled() {
        return isEnabled;
    }
    
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public Integer getUsageCount() {
        return usageCount;
    }
    
    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
    
    public String getCreateUser() {
        return createUser;
    }
    
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    public String getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "ExpressionTemplateDTO{" +
                "templateId='" + templateId + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", templateName='" + templateName + '\'' +
                ", templateCategory='" + templateCategory + '\'' +
                ", businessScenario='" + businessScenario + '\'' +
                ", chineseFormula='" + chineseFormula + '\'' +
                ", sqlTemplate='" + sqlTemplate + '\'' +
                ", parameterConfig='" + parameterConfig + '\'' +
                ", description='" + description + '\'' +
                ", exampleUsage='" + exampleUsage + '\'' +
                ", isSystemTemplate=" + isSystemTemplate +
                ", isEnabled=" + isEnabled +
                ", usageCount=" + usageCount +
                ", createUser='" + createUser + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
