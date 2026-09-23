package com.global.treasurer.dto.export;

import com.global.treasurer.entity.TblRegulatoryAuthority;
import com.global.treasurer.util.excel.annotation.ExcelField;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 监管机构导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-03-25
 */
public class ExportRegulatoryAuthorityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "机构编码", sort = 1, words = 15)
    private String authorityCode;

    @ExcelField(title = "机构名称", sort = 2, words = 25)
    private String authorityName;

    @ExcelField(title = "英文名称", sort = 3, words = 25)
    private String authorityNameEng;

    @ExcelField(title = "机构类型", sort = 4, words = 15)
    private String authorityType;

    @ExcelField(title = "国家代码", sort = 5, words = 10)
    private String countryCode;

    @ExcelField(title = "管辖范围", sort = 6, words = 30)
    private String jurisdiction;

    @ExcelField(title = "联系人", sort = 7, words = 12)
    private String contactPerson;

    @ExcelField(title = "联系电话", sort = 8, words = 15)
    private String contactPhone;

    @ExcelField(title = "联系邮箱", sort = 9, words = 25)
    private String contactEmail;

    @ExcelField(title = "地址", sort = 10, words = 30)
    private String address;

    @ExcelField(title = "网站", sort = 11, words = 25)
    private String website;

    @ExcelField(title = "状态", sort = 12, words = 8)
    private String isActive;

    @ExcelField(title = "创建时间", sort = 13, words = 18)
    private String createdTime;

    @ExcelField(title = "备注", sort = 14, words = 30)
    private String remark;

    public static ExportRegulatoryAuthorityDTO fromEntity(TblRegulatoryAuthority entity) {
        if (entity == null) return null;
        ExportRegulatoryAuthorityDTO dto = new ExportRegulatoryAuthorityDTO();
        dto.authorityCode = entity.getAuthorityCode();
        dto.authorityName = entity.getAuthorityName();
        dto.authorityNameEng = entity.getAuthorityNameEng();
        // 机构类型转中文
        String type = entity.getAuthorityType();
        if ("CENTRAL_BANK".equals(type)) dto.authorityType = "央行";
        else if ("SECURITIES_COMMISSION".equals(type)) dto.authorityType = "证监会";
        else if ("BANKING_REGULATOR".equals(type)) dto.authorityType = "银保监会";
        else if ("FOREX_REGULATOR".equals(type)) dto.authorityType = "外汇局";
        else if ("TAX_AUTHORITY".equals(type)) dto.authorityType = "税务局";
        else dto.authorityType = type;
        dto.countryCode = entity.getCountryCode();
        dto.jurisdiction = entity.getJurisdiction();
        dto.contactPerson = entity.getContactPerson();
        dto.contactPhone = entity.getContactPhone();
        dto.contactEmail = entity.getContactEmail();
        dto.address = entity.getAddress();
        dto.website = entity.getWebsite();
        dto.isActive = Integer.valueOf(1).equals(entity.getIsActive()) ? "启用" : "停用";
        dto.createdTime = entity.getCreatedTime() != null
                ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getCreatedTime()) : "";
        dto.remark = entity.getRemark();
        return dto;
    }

    public String getAuthorityCode() { return authorityCode; }
    public String getAuthorityName() { return authorityName; }
    public String getAuthorityNameEng() { return authorityNameEng; }
    public String getAuthorityType() { return authorityType; }
    public String getCountryCode() { return countryCode; }
    public String getJurisdiction() { return jurisdiction; }
    public String getContactPerson() { return contactPerson; }
    public String getContactPhone() { return contactPhone; }
    public String getContactEmail() { return contactEmail; }
    public String getAddress() { return address; }
    public String getWebsite() { return website; }
    public String getIsActive() { return isActive; }
    public String getCreatedTime() { return createdTime; }
    public String getRemark() { return remark; }
}

