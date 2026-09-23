package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算数据编制实体类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
@TableName("TBL_BUDGET_DATA")
public class TblBudgetData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据ID
     */
    @TableId("DATA_ID")
    private String dataId;

    /**
     * 模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

    /**
     * 表单ID
     */
    @TableField("FORM_ID")
    private String formId;

    /**
     * 预算期间
     */
    @TableField("PERIOD")
    private String period;

    /**
     * 预算版本
     */
    @TableField("VERSION")
    private String version;

    /**
     * 科目编码
     */
    @TableField("SUBJECT_CODE")
    private String subjectCode;

    /**
     * 主体编码
     */
    @TableField("ORGANIZATION_CODE")
    private String organizationCode;

    /**
     * 数据值(JSON格式)
     */
    @TableField("DATA_VALUES")
    private String dataValues;

    /**
     * 状态(DRAFT-草稿/SUBMITTED-已提交/APPROVED-已审批)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 提交人
     */
    @TableField("SUBMIT_USER")
    private String submitUser;

    /**
     * 提交时间
     */
    @TableField("SUBMIT_TIME")
    private Date submitTime;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

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
     * 模型名称(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String modelName;

    /**
     * 表单名称(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String formName;

    /**
     * 科目名称(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String subjectName;

    /**
     * 主体名称(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String organizationName;

    /**
     * 提交人姓名(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String submitUserName;

    /**
     * 创建人姓名(非数据库字段,用于关联查询)
     */
    @TableField(exist = false)
    private String createUserName;
}

