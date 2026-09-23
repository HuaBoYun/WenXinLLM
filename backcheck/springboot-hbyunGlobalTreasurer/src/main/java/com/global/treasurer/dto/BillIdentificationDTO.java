package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 票据标识DTO
 *
 * @author 华博云开发团队
 * @since 2026-02-02
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillIdentificationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 标识ID */
    private Long identificationId;

    /** 标识编码 */
    @Size(max = 50, message = "标识编码长度不能超过50个字符")
    private String identificationCode;

    /** 标识名称 */
    @NotBlank(message = "标识名称不能为空")
    @Size(max = 100, message = "标识名称长度不能超过100个字符")
    private String identificationName;

    /** 标识类型 */
    @NotBlank(message = "标识类型不能为空")
    private String identificationType;

    /** 标识颜色 */
    @Size(max = 20, message = "标识颜色长度不能超过20个字符")
    private String identificationColor;

    /** 标识图标 */
    @Size(max = 100, message = "标识图标长度不能超过100个字符")
    private String identificationIcon;

    /** 父级标识ID */
    private Long parentId;

    /** 排序序号 */
    private Integer sortOrder;

    /** 标识描述 */
    @Size(max = 500, message = "标识描述长度不能超过500个字符")
    private String description;

    /** 是否启用 */
    private Integer isEnabled;

    /** 所属公司ID */
    private Long companyId;

    /** 所属部门ID */
    private Long deptId;

    /** 备注 */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    // Getter and Setter methods
    public Long getIdentificationId() { return identificationId; }
    public void setIdentificationId(Long identificationId) { this.identificationId = identificationId; }
    public String getIdentificationCode() { return identificationCode; }
    public void setIdentificationCode(String identificationCode) { this.identificationCode = identificationCode; }
    public String getIdentificationName() { return identificationName; }
    public void setIdentificationName(String identificationName) { this.identificationName = identificationName; }
    public String getIdentificationType() { return identificationType; }
    public void setIdentificationType(String identificationType) { this.identificationType = identificationType; }
    public String getIdentificationColor() { return identificationColor; }
    public void setIdentificationColor(String identificationColor) { this.identificationColor = identificationColor; }
    public String getIdentificationIcon() { return identificationIcon; }
    public void setIdentificationIcon(String identificationIcon) { this.identificationIcon = identificationIcon; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

