package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 票据标识实体类
 *
 * @author 华博云开发团队
 * @since 2026-02-02
 */
@TableName("TBL_BILL_IDENTIFICATION")
public class TblBillIdentification implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 标识ID */
    @TableId(value = "IDENTIFICATION_ID", type = IdType.INPUT)
    private Long identificationId;

    /** 标识编码 */
    @TableField("IDENTIFICATION_CODE")
    private String identificationCode;

    /** 标识名称 */
    @TableField("IDENTIFICATION_NAME")
    private String identificationName;

    /** 标识类型 */
    @TableField("IDENTIFICATION_TYPE")
    private String identificationType;

    /** 标识颜色 */
    @TableField("IDENTIFICATION_COLOR")
    private String identificationColor;

    /** 标识图标 */
    @TableField("IDENTIFICATION_ICON")
    private String identificationIcon;

    /** 父级标识ID */
    @TableField("PARENT_ID")
    private Long parentId;

    /** 排序序号 */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /** 标识描述 */
    @TableField("DESCRIPTION")
    private String description;

    /** 是否系统内置 */
    @TableField("IS_SYSTEM")
    private Integer isSystem;

    /** 是否启用 */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /** 所属公司ID */
    @TableField("COMPANY_ID")
    private Long companyId;

    /** 所属部门ID */
    @TableField("DEPT_ID")
    private Long deptId;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /** 创建人 */
    @TableField("CREATE_BY")
    private String createBy;

    /** 更新人 */
    @TableField("UPDATE_BY")
    private String updateBy;

    /** 删除标志 */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /** 备注 */
    @TableField("REMARK")
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
    public Integer getIsSystem() { return isSystem; }
    public void setIsSystem(Integer isSystem) { this.isSystem = isSystem; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Integer getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Integer deleteFlag) { this.deleteFlag = deleteFlag; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

