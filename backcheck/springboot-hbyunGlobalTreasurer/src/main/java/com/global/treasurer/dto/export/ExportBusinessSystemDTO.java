package com.global.treasurer.dto.export;

import com.global.treasurer.util.excel.annotation.ExcelField;
// import lombok.Data; // 已移除,使用手动编写的getter/setter

import java.io.Serializable;
import java.util.Date;

/**
 * 业务系统导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-28
 */
// @Data // 已移除,使用手动编写的getter/setter
public class ExportBusinessSystemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "系统名称", sort = 1, words = 20)
    private String systemName;

    @ExcelField(title = "系统编码", sort = 2, words = 15)
    private String systemCode;

    @ExcelField(title = "系统类型", sort = 3, words = 12)
    private String systemType;

    @ExcelField(title = "接口地址", sort = 4, words = 40)
    private String apiUrl;

    @ExcelField(title = "认证方式", sort = 5, words = 12)
    private String authType;

    @ExcelField(title = "系统描述", sort = 6, words = 30)
    private String description;

    @ExcelField(title = "创建时间", sort = 7, words = 20, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelField(title = "更新时间", sort = 8, words = 20, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ExcelField(title = "创建人", sort = 9, words = 15)
    private String createUser;

    @ExcelField(title = "更新人", sort = 10, words = 15)
    private String updateUser;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportBusinessSystemDTO fromEntity(com.global.treasurer.entity.TcBusinessSystem entity) {
        if (entity == null) {
            return null;
        }
        ExportBusinessSystemDTO dto = new ExportBusinessSystemDTO();
        dto.setSystemName(entity.getSystemName());
        dto.setSystemCode(entity.getSystemCode());
        dto.setSystemType(convertSystemType(entity.getSystemType()));
        dto.setApiUrl(entity.getApiUrl());
        dto.setAuthType(convertAuthType(entity.getAuthType()));
        dto.setDescription(entity.getSystemDesc() != null ? entity.getSystemDesc() : entity.getSystemDesc());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setCreateUser(entity.getCreateUser());
        dto.setUpdateUser(entity.getUpdateUser());
        return dto;
    }

    /**
     * 转换系统类型为中文
     */
    private static String convertSystemType(String type) {
        if (type == null) return "";
        switch (type) {
            case "ERP": return "ERP系统";
            case "CRM": return "CRM系统";
            case "OA": return "OA系统";
            case "HR": return "HR系统";
            case "FINANCE": return "财务系统";
            case "OTHER": return "其他系统";
            default: return type;
        }
    }

    /**
     * 转换认证方式为中文
     */
    private static String convertAuthType(String type) {
        if (type == null) return "";
        switch (type) {
            case "NONE": return "无认证";
            case "BASIC": return "Basic认证";
            case "TOKEN": return "Token认证";
            case "OAUTH2": return "OAuth2认证";
            case "CERTIFICATE": return "证书认证";
            default: return type;
        }
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}

