package com.global.treasurer.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 票据标识VO
 *
 * @author 华博云开发团队
 * @since 2026-02-02
 */
public class BillIdentificationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 标识ID */
    private Long identificationId;

    /** 标识编码 */
    private String identificationCode;

    /** 标识名称 */
    private String identificationName;

    /** 标识类型 */
    private String identificationType;

    /** 标识类型名称 */
    private String identificationTypeName;

    /** 标识颜色 */
    private String identificationColor;

    /** 标识图标 */
    private String identificationIcon;

    /** 父级标识ID */
    private Long parentId;

    /** 父级标识名称 */
    private String parentName;

    /** 排序序号 */
    private Integer sortOrder;

    /** 标识描述 */
    private String description;

    /** 是否系统内置 */
    private Integer isSystem;

    /** 是否启用 */
    private Integer isEnabled;

    /** 所属公司ID */
    private Long companyId;

    /** 所属公司名称 */
    private String companyName;

    /** 所属部门ID */
    private Long deptId;

    /** 所属部门名称 */
    private String deptName;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 创建人 */
    private String createBy;

    /** 更新人 */
    private String updateBy;

    /** 备注 */
    private String remark;

    /** 子级标识列表（用于树形结构） */
    private List<BillIdentificationVO> children;

    // Getter and Setter methods
    public Long getIdentificationId() { return identificationId; }
    public void setIdentificationId(Long identificationId) { this.identificationId = identificationId; }
    public String getIdentificationCode() { return identificationCode; }
    public void setIdentificationCode(String identificationCode) { this.identificationCode = identificationCode; }
    public String getIdentificationName() { return identificationName; }
    public void setIdentificationName(String identificationName) { this.identificationName = identificationName; }
    public String getIdentificationType() { return identificationType; }
    public void setIdentificationType(String identificationType) { this.identificationType = identificationType; }
    public String getIdentificationTypeName() { return identificationTypeName; }
    public void setIdentificationTypeName(String identificationTypeName) { this.identificationTypeName = identificationTypeName; }
    public String getIdentificationColor() { return identificationColor; }
    public void setIdentificationColor(String identificationColor) { this.identificationColor = identificationColor; }
    public String getIdentificationIcon() { return identificationIcon; }
    public void setIdentificationIcon(String identificationIcon) { this.identificationIcon = identificationIcon; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getIsSystem() { return isSystem; }
    public void setIsSystem(Integer isSystem) { this.isSystem = isSystem; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public List<BillIdentificationVO> getChildren() { return children; }
    public void setChildren(List<BillIdentificationVO> children) { this.children = children; }
}

