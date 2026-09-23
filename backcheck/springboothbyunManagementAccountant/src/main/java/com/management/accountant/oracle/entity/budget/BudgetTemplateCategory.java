package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算模板分类实体类
 *
 * @author AI Agent
 * @date 2026-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_TEMPLATE_CATEGORY")
public class BudgetTemplateCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID (主键)
     */
    @TableId(value = "CATEGORY_ID", type = IdType.ASSIGN_UUID)
    private String categoryId;

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
     * 父分类ID
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
     * 排序序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 分类描述
     */
    @TableField("CATEGORY_DESCRIPTION")
    private String categoryDescription;

    /**
     * 是否启用
     */
    @TableField("IS_ENABLED")
    private Boolean isEnabled;

    /**
     * 模板数量
     */
    @TableField("TEMPLATE_COUNT")
    private Integer templateCount;

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
     * 删除标志 (0未删除 1已删除)
     */
    @TableField("DEL_FLAG")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    // 显式Getter和Setter方法，防止Lombok处理失败 ==========
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
