package com.global.treasurer.dto.imports;

import com.global.treasurer.util.excel.annotation.ExcelField;
// import lombok.Data; // 已移除

import java.io.Serializable;

/**
 * 业务系统导入DTO
 * 用于Excel批量导入业务系统数据
 *
 * @author 华博云开发团队
 * @since 2026-01-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class ImportBusinessSystemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 系统名称（必填）
     */
    @ExcelField(title = "系统名称", sort = 1, type = ExcelField.Type.IMPORT)
    private String systemName;

    /**
     * 系统编码（必填，唯一）
     */
    @ExcelField(title = "系统编码", sort = 2, type = ExcelField.Type.IMPORT)
    private String systemCode;

    /**
     * 系统类型（必填）
     * 可选值：ERP、CRM、OA、HR、FINANCE、OTHER
     */
    @ExcelField(title = "系统类型", sort = 3, type = ExcelField.Type.IMPORT)
    private String systemType;

    /**
     * 接口地址（必填）
     */
    @ExcelField(title = "接口地址", sort = 4, type = ExcelField.Type.IMPORT)
    private String apiUrl;

    /**
     * 认证方式（必填）
     * 可选值：NONE、BASIC、TOKEN、OAUTH2、CERTIFICATE
     */
    @ExcelField(title = "认证方式", sort = 5, type = ExcelField.Type.IMPORT)
    private String authType;

    /**
     * 系统描述（可选）
     */
    @ExcelField(title = "系统描述", sort = 6, type = ExcelField.Type.IMPORT)
    private String description;

    /**
     * 行号（用于错误提示）
     */
    private Integer rowNum;

    /**
     * 验证数据是否有效
     * @return 验证结果，null表示验证通过，否则返回错误信息
     */
    public String validate() {
        if (systemName == null || systemName.trim().isEmpty()) {
            return "系统名称不能为空";
        }
        if (systemCode == null || systemCode.trim().isEmpty()) {
            return "系统编码不能为空";
        }
        if (!systemCode.matches("^[A-Z0-9_]+$")) {
            return "系统编码只能包含大写字母、数字和下划线";
        }
        if (systemType == null || systemType.trim().isEmpty()) {
            return "系统类型不能为空";
        }
        if (!isValidSystemType(systemType)) {
            return "系统类型无效，可选值：ERP、CRM、OA、HR、FINANCE、OTHER";
        }
        if (apiUrl == null || apiUrl.trim().isEmpty()) {
            return "接口地址不能为空";
        }
        if (authType == null || authType.trim().isEmpty()) {
            return "认证方式不能为空";
        }
        if (!isValidAuthType(authType)) {
            return "认证方式无效，可选值：NONE、BASIC、TOKEN、OAUTH2、CERTIFICATE";
        }
        return null;
    }

    /**
     * 验证系统类型是否有效
     */
    private boolean isValidSystemType(String type) {
        return "ERP".equals(type) || "CRM".equals(type) || "OA".equals(type) 
            || "HR".equals(type) || "FINANCE".equals(type) || "OTHER".equals(type);
    }

    /**
     * 验证认证方式是否有效
     */
    private boolean isValidAuthType(String type) {
        return "NONE".equals(type) || "BASIC".equals(type) || "TOKEN".equals(type) 
            || "OAUTH2".equals(type) || "CERTIFICATE".equals(type);
    }


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }
    public String getSystemCode() { return systemCode; }
    public void setSystemCode(String systemCode) { this.systemCode = systemCode; }
    public String getSystemType() { return systemType; }
    public void setSystemType(String systemType) { this.systemType = systemType; }
    public String getApiUrl() { return apiUrl; }
    public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }
    public String getAuthType() { return authType; }
    public void setAuthType(String authType) { this.authType = authType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getRowNum() { return rowNum; }
    public void setRowNum(Integer rowNum) { this.rowNum = rowNum; }

}
