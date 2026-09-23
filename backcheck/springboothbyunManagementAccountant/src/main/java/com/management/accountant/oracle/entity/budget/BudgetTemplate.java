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
 * 预算模板实体类
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_BUDGET_TEMPLATE")
public class BudgetTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID (主键)
     */
    @TableId(value = "TEMPLATE_ID", type = IdType.ASSIGN_UUID)
    private String templateId;

    /**
     * 模板编码（唯一）
     */
    @TableField("TEMPLATE_CODE")
    private String templateCode;

    /**
     * 模板名称
     */
    @TableField("TEMPLATE_NAME")
    private String templateName;

    /**
     * 模板类型 (STANDARD/CUSTOM/INDUSTRY)
     */
    @TableField("TEMPLATE_TYPE")
    private String templateType;

    /**
     * 模板描述
     */
    @TableField("TEMPLATE_DESCRIPTION")
    private String templateDescription;

    /**
     * 是否默认模板 (0否 1是)
     */
    @TableField("IS_DEFAULT")
    private Integer isDefault;

    /**
     * 是否启用 (0否 1是)
     */
    @TableField("IS_ENABLED")
    private Integer isEnabled;

    /**
     * 是否系统模板
     */
    @TableField("IS_SYSTEM")
    private Integer isSystem;

    /**
     * 排序序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 模板配置（JSON，含结构/字段/计算规则）
     */
    @TableField("TEMPLATE_CONFIG")
    private String templateConfig;

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
     * 创建人（操作人）
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人（操作人）
     */
    @TableField("UPDATE_BY")
    private String updateBy;

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

    /**
     * 模板状态
     */
    @TableField("TEMPLATE_STATUS")
    private String templateStatus;

    /**
     * 分类ID
     */
    @TableField("CATEGORY_ID")
    private String categoryId;

    /**
     * 适用年度
     */
    @TableField("APPLICABLE_YEAR")
    private Integer applicableYear;

    /**
     * 字段数量
     */
    @TableField("FIELD_COUNT")
    private Integer fieldCount;

    /**
     * 使用次数
     */
    @TableField("USAGE_COUNT")
    private Integer usageCount;

    /**
     * 是否公开 (0否 1是)
     */
    @TableField("IS_PUBLIC")
    private Integer isPublic;

    /**
     * 是否允许复制 (0否 1是)
     */
    @TableField("ALLOW_COPY")
    private Integer allowCopy;

    // ========== 显式Getter和Setter方法，防止Lombok处理失败 ==========
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}

