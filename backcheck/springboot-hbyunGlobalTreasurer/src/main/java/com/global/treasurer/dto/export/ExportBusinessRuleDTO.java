package com.global.treasurer.dto.export;

import com.global.treasurer.util.excel.annotation.ExcelField;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 业务规则导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-28
 */
// @Data // 已移除,使用手动编写的getter/setter
public class ExportBusinessRuleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "规则编码", sort = 1, words = 15)
    private String ruleCode;

    @ExcelField(title = "规则名称", sort = 2, words = 20)
    private String ruleName;

    @ExcelField(title = "规则类型", sort = 3, words = 12)
    private String ruleType;

    @ExcelField(title = "适用模块", sort = 4, words = 15)
    private String moduleCode;

    @ExcelField(title = "优先级", sort = 5, words = 10)
    private String priority;

    @ExcelField(title = "规则状态", sort = 6, words = 10)
    private String ruleStatus;

    @ExcelField(title = "生效时间", sort = 7, words = 20)
    private String effectiveTime;

    @ExcelField(title = "失效时间", sort = 8, words = 20)
    private String expireTime;

    @ExcelField(title = "规则描述", sort = 9, words = 30)
    private String description;

    @ExcelField(title = "状态", sort = 10, words = 10)
    private String status;

    @ExcelField(title = "创建时间", sort = 11, words = 20, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportBusinessRuleDTO fromEntity(com.global.treasurer.entity.TblGtBusinessRule entity) {
        if (entity == null) {
            return null;
        }
        ExportBusinessRuleDTO dto = new ExportBusinessRuleDTO();
        dto.setRuleCode(entity.getRuleCode());
        dto.setRuleName(entity.getRuleName());
        dto.setRuleType(convertRuleType(entity.getRuleType()));
        dto.setModuleCode(entity.getModuleCode());
        dto.setPriority(convertPriority(entity.getPriority()));
        dto.setRuleStatus(entity.getRuleStatus());
        dto.setEffectiveTime(entity.getEffectiveTime() != null ? entity.getEffectiveTime().toString() : "");
        dto.setExpireTime(entity.getExpireTime() != null ? entity.getExpireTime().toString() : "");
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus() != null && entity.getStatus() == 1 ? "启用" : "停用");
        dto.setCreateTime(entity.getCreateTime());
        return dto;
    }

    private static String convertRuleType(String type) {
        if (type == null) return "";
        switch (type) {
            case "VALIDATION": return "校验规则";
            case "CALCULATION": return "计算规则";
            case "WORKFLOW": return "流程规则";
            case "ALERT": return "预警规则";
            default: return type;
        }
    }

    private static String convertPriority(String priority) {
        if (priority == null) return "";
        switch (priority) {
            case "HIGH": return "高";
            case "MEDIUM": return "中";
            case "LOW": return "低";
            default: return priority;
        }
    }


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getRuleStatus() { return ruleStatus; }
    public void setRuleStatus(String ruleStatus) { this.ruleStatus = ruleStatus; }
    public String getEffectiveTime() { return effectiveTime; }
    public void setEffectiveTime(String effectiveTime) { this.effectiveTime = effectiveTime; }
    public String getExpireTime() { return expireTime; }
    public void setExpireTime(String expireTime) { this.expireTime = expireTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

}
