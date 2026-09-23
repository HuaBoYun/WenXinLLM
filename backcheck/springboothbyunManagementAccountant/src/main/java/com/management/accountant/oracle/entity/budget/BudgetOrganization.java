package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算组织体系实体
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@TableName("TBL_BUDGET_ORGANIZATION")
public class BudgetOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织ID
     */
    @TableId(value = "ORGANIZATION_ID", type = IdType.ASSIGN_UUID)
    private String organizationId;

    /**
     * 组织编码
     */
    @TableField("ORGANIZATION_CODE")
    private String organizationCode;

    /**
     * 组织名称
     */
    @TableField("ORGANIZATION_NAME")
    private String organizationName;

    /**
     * 组织类型 (single/multi/hierarchical/matrix/hybrid)
     */
    @TableField("ORGANIZATION_TYPE")
    private String organizationType;

    /**
     * 控制模式 (centralized/decentralized/hybrid)
     */
    @TableField("CONTROL_MODE")
    private String controlMode;

    /**
     * 上级组织ID
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 组织层级
     */
    @TableField("ORGANIZATION_LEVEL")
    private Integer organizationLevel;

    /**
     * 最大层级
     */
    @TableField("MAX_LEVELS")
    private Integer maxLevels;

    /**
     * 组织路径
     */
    @TableField("ORGANIZATION_PATH")
    private String organizationPath;

    /**
     * 排序序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 是否启用 (0-否 1-是)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 组织描述
     */
    @TableField("ORGANIZATION_DESCRIPTION")
    private String organizationDescription;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建人ID
     */
    @TableField("CREATOR_ID")
    private String creatorId;

    /**
     * 创建人姓名
     */
    @TableField("CREATOR_NAME")
    private String creatorName;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATER_ID")
    private String updaterId;

    /**
     * 更新人姓名
     */
    @TableField("UPDATER_NAME")
    private String updaterName;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 公司ID
     */
    @TableField("COMPANY_ID")
    private String companyId;

    /**
     * 公司名称
     */
    @TableField("COMPANY_NAME")
    private String companyName;

    /**
     * 状态
     */
    @TableField("STATUS")
    private String status;

    /**
     * 删除标志 (0-未删除 1-已删除)
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========

    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }

    public String getOrganizationCode() { return organizationCode; }
    public void setOrganizationCode(String organizationCode) { this.organizationCode = organizationCode; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getOrganizationType() { return organizationType; }
    public void setOrganizationType(String organizationType) { this.organizationType = organizationType; }

    public String getControlMode() { return controlMode; }
    public void setControlMode(String controlMode) { this.controlMode = controlMode; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public Integer getOrganizationLevel() { return organizationLevel; }
    public void setOrganizationLevel(Integer organizationLevel) { this.organizationLevel = organizationLevel; }

    public Integer getMaxLevels() { return maxLevels; }
    public void setMaxLevels(Integer maxLevels) { this.maxLevels = maxLevels; }

    public String getOrganizationPath() { return organizationPath; }
    public void setOrganizationPath(String organizationPath) { this.organizationPath = organizationPath; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }

    public String getOrganizationDescription() { return organizationDescription; }
    public void setOrganizationDescription(String organizationDescription) { this.organizationDescription = organizationDescription; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getUpdaterId() { return updaterId; }
    public void setUpdaterId(String updaterId) { this.updaterId = updaterId; }

    public String getUpdaterName() { return updaterName; }
    public void setUpdaterName(String updaterName) { this.updaterName = updaterName; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getDelFlag() { return delFlag; }
    public void setDelFlag(Integer delFlag) { this.delFlag = delFlag; }
}

