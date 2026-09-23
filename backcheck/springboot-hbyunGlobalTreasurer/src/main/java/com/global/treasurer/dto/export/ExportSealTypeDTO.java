package com.global.treasurer.dto.export;

import com.global.treasurer.util.excel.annotation.ExcelField;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 印鉴类型导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-03-26
 */
public class ExportSealTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "类型编码", sort = 1, words = 12)
    private String typeCode;

    @ExcelField(title = "类型名称", sort = 2, words = 15)
    private String name;

    @ExcelField(title = "印鉴级别", sort = 3, words = 10)
    private String sealLevel;

    @ExcelField(title = "适用范围", sort = 4, words = 15)
    private String scope;

    @ExcelField(title = "描述", sort = 5, words = 25)
    private String description;

    @ExcelField(title = "状态", sort = 6, words = 8)
    private String status;

    @ExcelField(title = "创建时间", sort = 7, words = 20)
    private String createTime;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportSealTypeDTO fromEntity(com.global.treasurer.entity.TblSealType entity) {
        if (entity == null) {
            return null;
        }
        ExportSealTypeDTO dto = new ExportSealTypeDTO();
        dto.setTypeCode(entity.getTypeCode());
        dto.setName(entity.getName());
        // 印鉴级别转换
        String level = entity.getSealLevel();
        if ("1".equals(level)) {
            dto.setSealLevel("一级印鉴");
        } else if ("2".equals(level)) {
            dto.setSealLevel("二级印鉴");
        } else if ("3".equals(level)) {
            dto.setSealLevel("三级印鉴");
        } else {
            dto.setSealLevel(level);
        }
        dto.setScope(entity.getScope());
        dto.setDescription(entity.getRemark());
        // 状态转换
        Integer isActive = entity.getIsActive();
        dto.setStatus(isActive != null && isActive == 1 ? "启用" : "禁用");
        // 时间格式化
        Date ct = entity.getCreateTime();
        dto.setCreateTime(ct != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ct) : "");
        return dto;
    }

    public String getTypeCode() { return typeCode; }
    public void setTypeCode(String typeCode) { this.typeCode = typeCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSealLevel() { return sealLevel; }
    public void setSealLevel(String sealLevel) { this.sealLevel = sealLevel; }
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}

