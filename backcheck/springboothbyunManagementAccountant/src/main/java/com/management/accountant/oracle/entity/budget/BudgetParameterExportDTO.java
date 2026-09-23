package com.management.accountant.oracle.entity.budget;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ColumnWidth(18)
public class BudgetParameterExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("参数编码")
    @ColumnWidth(20)
    private String parameterCode;

    @ExcelProperty("参数名称")
    @ColumnWidth(25)
    private String parameterName;

    @ExcelProperty("参数类型")
    private String parameterType;

    @ExcelProperty("参数分类")
    private String parameterCategory;

    @ExcelProperty("参数范围")
    private String parameterScope;

    @ExcelProperty("默认值")
    @ColumnWidth(20)
    private String defaultValue;

    @ExcelProperty("当前值")
    @ColumnWidth(20)
    private String currentValue;

    @ExcelProperty("参数单位")
    private String parameterUnit;

    @ExcelProperty("验证规则")
    @ColumnWidth(30)
    private String validationRule;

    @ExcelProperty("是否必填")
    private String isRequired;

    @ExcelProperty("是否启用")
    private String isEnabled;

    @ExcelProperty("是否只读")
    private String isReadonly;

    @ExcelProperty("生效范围")
    @ColumnWidth(20)
    private String effectiveScope;

    @ExcelProperty("排序")
    private Integer sortOrder;

    @ExcelProperty("参数描述")
    @ColumnWidth(30)
    private String parameterDescription;

    @ExcelProperty("备注")
    @ColumnWidth(25)
    private String remark;

    // ========== 显式 Getter/Setter ==========
    public String getParameterCode() { return parameterCode; }
    public void setParameterCode(String parameterCode) { this.parameterCode = parameterCode; }
    public String getParameterName() { return parameterName; }
    public void setParameterName(String parameterName) { this.parameterName = parameterName; }
    public String getParameterType() { return parameterType; }
    public void setParameterType(String parameterType) { this.parameterType = parameterType; }
    public String getParameterCategory() { return parameterCategory; }
    public void setParameterCategory(String parameterCategory) { this.parameterCategory = parameterCategory; }
    public String getParameterScope() { return parameterScope; }
    public void setParameterScope(String parameterScope) { this.parameterScope = parameterScope; }
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    public String getCurrentValue() { return currentValue; }
    public void setCurrentValue(String currentValue) { this.currentValue = currentValue; }
    public String getParameterUnit() { return parameterUnit; }
    public void setParameterUnit(String parameterUnit) { this.parameterUnit = parameterUnit; }
    public String getValidationRule() { return validationRule; }
    public void setValidationRule(String validationRule) { this.validationRule = validationRule; }
    public String getIsRequired() { return isRequired; }
    public void setIsRequired(String isRequired) { this.isRequired = isRequired; }
    public String getIsEnabled() { return isEnabled; }
    public void setIsEnabled(String isEnabled) { this.isEnabled = isEnabled; }
    public String getIsReadonly() { return isReadonly; }
    public void setIsReadonly(String isReadonly) { this.isReadonly = isReadonly; }
    public String getEffectiveScope() { return effectiveScope; }
    public void setEffectiveScope(String effectiveScope) { this.effectiveScope = effectiveScope; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getParameterDescription() { return parameterDescription; }
    public void setParameterDescription(String parameterDescription) { this.parameterDescription = parameterDescription; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public static BudgetParameterExportDTO fromEntity(BudgetParameter p) {
        BudgetParameterExportDTO dto = new BudgetParameterExportDTO();
        dto.setParameterCode(p.getParameterCode());
        dto.setParameterName(p.getParameterName());
        dto.setParameterType(p.getParameterType());
        dto.setParameterCategory(p.getParameterCategory());
        dto.setParameterScope(p.getParameterScope());
        dto.setDefaultValue(p.getDefaultValue());
        dto.setCurrentValue(p.getCurrentValue());
        dto.setParameterUnit(p.getParameterUnit());
        dto.setValidationRule(p.getValidationRule());
        dto.setIsRequired(p.getIsRequired() != null && p.getIsRequired() ? "是" : "否");
        dto.setIsEnabled(p.getIsEnabled() != null && p.getIsEnabled() ? "启用" : "禁用");
        dto.setIsReadonly(p.getIsReadonly() != null && p.getIsReadonly() ? "是" : "否");
        dto.setEffectiveScope(p.getEffectiveScope());
        dto.setSortOrder(p.getSortOrder());
        dto.setParameterDescription(p.getParameterDescription());
        dto.setRemark(p.getRemark());
        return dto;
    }

    public BudgetParameter toEntity() {
        BudgetParameter p = new BudgetParameter();
        p.setParameterCode(this.parameterCode);
        p.setParameterName(this.parameterName);
        p.setParameterType(this.parameterType);
        p.setParameterCategory(this.parameterCategory);
        p.setParameterScope(this.parameterScope);
        p.setDefaultValue(this.defaultValue);
        p.setCurrentValue(this.currentValue);
        p.setParameterUnit(this.parameterUnit);
        p.setValidationRule(this.validationRule);
        p.setIsRequired("是".equals(this.isRequired));
        p.setIsEnabled(!"禁用".equals(this.isEnabled));
        p.setIsReadonly("是".equals(this.isReadonly));
        p.setEffectiveScope(this.effectiveScope);
        p.setSortOrder(this.sortOrder);
        p.setParameterDescription(this.parameterDescription);
        p.setRemark(this.remark);
        return p;
    }
}

