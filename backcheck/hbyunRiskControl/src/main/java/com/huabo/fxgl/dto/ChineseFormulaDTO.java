package com.huabo.fxgl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 中文公式数据传输对象
 * 
 * @author AI Assistant
 * @date 2025-01-21
 * @version 1.0
 */
@Schema(description="中文公式数据传输对象")
public class ChineseFormulaDTO {
    
    @Schema(name = "中文公式", required = true, example = "金额 大于 10000 并且 状态 等于 有效")
    private String chineseFormula;
    
    @Schema(name="源数据配置")
    private String sourceDataConfig;
    
    @Schema(name="目标数据配置")
    private String targetDataConfig;
    
    @Schema(name = "可用字段列表")
    private List<String> availableFields;
    
    @Schema(name = "部分公式（用于智能建议）", example = "金额 大于")
    private String partialFormula;
    
    @Schema(name="验证类型")
    private String validationType;
    
    @Schema(name = "是否格式化", example = "true")
    private Boolean formatOutput;
    
    public ChineseFormulaDTO() {
    }
    
    public ChineseFormulaDTO(String chineseFormula) {
        this.chineseFormula = chineseFormula;
    }
    
    public String getChineseFormula() {
        return chineseFormula;
    }
    
    public void setChineseFormula(String chineseFormula) {
        this.chineseFormula = chineseFormula;
    }
    
    public String getSourceDataConfig() {
        return sourceDataConfig;
    }
    
    public void setSourceDataConfig(String sourceDataConfig) {
        this.sourceDataConfig = sourceDataConfig;
    }
    
    public String getTargetDataConfig() {
        return targetDataConfig;
    }
    
    public void setTargetDataConfig(String targetDataConfig) {
        this.targetDataConfig = targetDataConfig;
    }
    
    public List<String> getAvailableFields() {
        return availableFields;
    }
    
    public void setAvailableFields(List<String> availableFields) {
        this.availableFields = availableFields;
    }
    
    public String getPartialFormula() {
        return partialFormula;
    }
    
    public void setPartialFormula(String partialFormula) {
        this.partialFormula = partialFormula;
    }
    
    public String getValidationType() {
        return validationType;
    }
    
    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }
    
    public Boolean getFormatOutput() {
        return formatOutput;
    }
    
    public void setFormatOutput(Boolean formatOutput) {
        this.formatOutput = formatOutput;
    }
    
    @Override
    public String toString() {
        return "ChineseFormulaDTO{" +
                "chineseFormula='" + chineseFormula + '\'' +
                ", sourceDataConfig='" + sourceDataConfig + '\'' +
                ", targetDataConfig='" + targetDataConfig + '\'' +
                ", availableFields=" + availableFields +
                ", partialFormula='" + partialFormula + '\'' +
                ", validationType='" + validationType + '\'' +
                ", formatOutput=" + formatOutput +
                '}';
    }
}
