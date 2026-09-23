package com.global.treasurer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * 票据标识查询DTO
 *
 * @author 华博云开发团队
 * @since 2026-02-02
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillIdentificationQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 标识编码 */
    private String identificationCode;

    /** 标识名称 */
    private String identificationName;

    /** 标识类型 */
    private String identificationType;

    /** 父级标识ID */
    private Long parentId;

    /** 是否启用 */
    private Integer isEnabled;

    /** 是否系统内置 */
    private Integer isSystem;

    /** 所属公司ID */
    private Long companyId;

    /** 所属部门ID */
    private Long deptId;

    /** 当前页码 */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 20;

    // Getter and Setter methods
    public String getIdentificationCode() { return identificationCode; }
    public void setIdentificationCode(String identificationCode) { this.identificationCode = identificationCode; }
    public String getIdentificationName() { return identificationName; }
    public void setIdentificationName(String identificationName) { this.identificationName = identificationName; }
    public String getIdentificationType() { return identificationType; }
    public void setIdentificationType(String identificationType) { this.identificationType = identificationType; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Integer getIsSystem() { return isSystem; }
    public void setIsSystem(Integer isSystem) { this.isSystem = isSystem; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}

