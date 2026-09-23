package com.global.treasurer.financialProductDefinition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 金融分类实体类
 * 对应数据库表：TBL_FINANCIAL_CATEGORY
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_FINANCIAL_CATEGORY")
public class TblFinancialCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "CATEGORY_ID", type = IdType.INPUT)
    private Long categoryId;

    /**
     * 分类编码
     */
    @TableField("CATEGORY_CODE")
    private String categoryCode;

    /**
     * 分类名称
     */
    @TableField("CATEGORY_NAME")
    private String categoryName;

    /**
     * 父级ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 分类层级
     */
    @TableField("CATEGORY_LEVEL")
    private String categoryLevel;

    /**
     * 分类路径
     */
    @TableField("CATEGORY_PATH")
    private String categoryPath;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 是否启用(1-启用,0-禁用)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private Long orgId;

    /**
     * 子节点列表（非数据库字段，用于树形结构）
     */
    @TableField(exist = false)
    private List<TblFinancialCategory> children;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getCategoryLevel() { return categoryLevel; }
    public void setCategoryLevel(String categoryLevel) { this.categoryLevel = categoryLevel; }
    public String getCategoryPath() { return categoryPath; }
    public void setCategoryPath(String categoryPath) { this.categoryPath = categoryPath; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
    public List<TblFinancialCategory> getChildren() { return children; }
    public void setChildren(List<TblFinancialCategory> children) { this.children = children; }

}
