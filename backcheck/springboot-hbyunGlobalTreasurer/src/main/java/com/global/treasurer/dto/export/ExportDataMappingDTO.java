package com.global.treasurer.dto.export;

import com.global.treasurer.util.excel.annotation.ExcelField;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 数据映射配置导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-28
 */
// @Data // 已移除,使用手动编写的getter/setter
public class ExportDataMappingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "映射名称", sort = 1, words = 20)
    private String mappingName;

    @ExcelField(title = "源字段", sort = 2, words = 20)
    private String sourceField;

    @ExcelField(title = "目标字段", sort = 3, words = 20)
    private String targetField;

    @ExcelField(title = "字段类型", sort = 4, words = 12)
    private String fieldType;

    @ExcelField(title = "转换规则", sort = 5, words = 30)
    private String conversionRule;

    @ExcelField(title = "默认值", sort = 6, words = 15)
    private String defaultValue;

    @ExcelField(title = "是否必填", sort = 7, words = 10)
    private String isRequired;

    @ExcelField(title = "状态", sort = 8, words = 10)
    private String status;

    @ExcelField(title = "备注", sort = 9, words = 30)
    private String remark;

    @ExcelField(title = "创建时间", sort = 10, words = 20, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelField(title = "更新时间", sort = 11, words = 20, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportDataMappingDTO fromEntity(com.global.treasurer.entity.TcDataMapping entity) {
        if (entity == null) {
            return null;
        }
        ExportDataMappingDTO dto = new ExportDataMappingDTO();
        dto.setMappingName(entity.getMappingName());
        dto.setSourceField(entity.getSourceField());
        dto.setTargetField(entity.getTargetField());
        dto.setFieldType(entity.getFieldType());
        dto.setConversionRule(entity.getConversionRule());
        dto.setDefaultValue(entity.getDefaultValue());
        dto.setIsRequired("1".equals(entity.getIsRequired()) ? "是" : "否");
        dto.setStatus("1".equals(entity.getStatus()) ? "启用" : "停用");
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getMappingName() { return mappingName; }
    public void setMappingName(String mappingName) { this.mappingName = mappingName; }
    public String getSourceField() { return sourceField; }
    public void setSourceField(String sourceField) { this.sourceField = sourceField; }
    public String getTargetField() { return targetField; }
    public void setTargetField(String targetField) { this.targetField = targetField; }
    public String getFieldType() { return fieldType; }
    public void setFieldType(String fieldType) { this.fieldType = fieldType; }
    public String getConversionRule() { return conversionRule; }
    public void setConversionRule(String conversionRule) { this.conversionRule = conversionRule; }
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    public String getIsRequired() { return isRequired; }
    public void setIsRequired(String isRequired) { this.isRequired = isRequired; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

}
