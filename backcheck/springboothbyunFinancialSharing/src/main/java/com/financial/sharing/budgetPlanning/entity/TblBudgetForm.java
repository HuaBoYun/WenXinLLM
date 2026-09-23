package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 预算表单配置实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_BUDGET_FORM")
public class TblBudgetForm {

    /**
     * 表单ID
     */
    @TableId("FORM_ID")
    private String formId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 表单编码
     */
    @TableField("FORM_CODE")
    private String formCode;

    /**
     * 表单名称
     */
    @TableField("FORM_NAME")
    private String formName;

    /**
     * 表单类型：BUDGET(编制表单)/ADJUST(调整表单)/QUERY(查询表单)
     */
    @TableField("FORM_TYPE")
    private String formType;

    /**
     * 表单配置(JSON格式)
     * 包含:字段配置、布局配置、验证规则等
     */
    @TableField("FORM_CONFIG")
    private String formConfig;

    /**
     * 状态：DRAFT(草稿)/ACTIVE(启用)/INACTIVE(停用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 排序号
     */
    @TableField("SORT_ORDER")
    private Integer sortOrder;

    /**
     * 租户ID
     */
    @TableField("ORG_ID")
    private String orgId;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 模型名称(非数据库字段)
     */
    @TableField(exist = false)
    private String modelName;
}

