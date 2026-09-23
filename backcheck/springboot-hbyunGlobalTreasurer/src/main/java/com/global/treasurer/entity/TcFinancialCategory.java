package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 金融业务分类表
 * @author hbyun-admin
 * @date 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@TableName("TC_FINANCIAL_CATEGORY")
public class TcFinancialCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private String id;

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
     * 父级分类ID
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 分类层级
     */
    @TableField("CATEGORY_LEVEL")
    private Integer categoryLevel;

    /**
     * 分类路径
     */
    @TableField("CATEGORY_PATH")
    private String categoryPath;

    /**
     * 分类描述
     */
    @TableField("CATEGORY_DESC")
    private String categoryDesc;

    /**
     * 适用范围
     */
    @TableField("APPLICABLE_SCOPE")
    private String applicableScope;

    /**
     * 业务规则
     */
    @TableField("BUSINESS_RULES")
    private String businessRules;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 状态(1:启用 0:停用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建用户
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新用户
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private Long versionNo;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    public Integer getCategoryLevel() { return categoryLevel; }
    public void setCategoryLevel(Integer categoryLevel) { this.categoryLevel = categoryLevel; }
    public String getCategoryPath() { return categoryPath; }
    public void setCategoryPath(String categoryPath) { this.categoryPath = categoryPath; }
    public String getCategoryDesc() { return categoryDesc; }
    public void setCategoryDesc(String categoryDesc) { this.categoryDesc = categoryDesc; }
    public String getApplicableScope() { return applicableScope; }
    public void setApplicableScope(String applicableScope) { this.applicableScope = applicableScope; }
    public String getBusinessRules() { return businessRules; }
    public void setBusinessRules(String businessRules) { this.businessRules = businessRules; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Long getVersionNo() { return versionNo; }
    public void setVersionNo(Long versionNo) { this.versionNo = versionNo; }

}
