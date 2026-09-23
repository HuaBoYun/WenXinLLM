package com.financial.sharing.budgetPlanning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 预算调整主表实体
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Data
@TableName("TBL_BUDGET_ADJUSTMENT")
public class TblBudgetAdjustment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调整单ID
     */
    @TableId(value = "ADJUSTMENT_ID", type = IdType.ASSIGN_ID)
    private String adjustmentId;

    /**
     * 调整单号
     */
    @TableField("ADJUSTMENT_NO")
    private String adjustmentNo;

    /**
     * 预算模型ID
     */
    @TableField("MODEL_ID")
    private String modelId;

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
     * 调整类型(FULL-整版调整/PARTIAL-零星调整)
     */
    @TableField("ADJUSTMENT_TYPE")
    private String adjustmentType;

    /**
     * 调整原因
     */
    @TableField("ADJUSTMENT_REASON")
    private String adjustmentReason;

    /**
     * 调整说明
     */
    @TableField("ADJUSTMENT_DESC")
    private String adjustmentDesc;

    /**
     * 状态(DRAFT-草稿/SUBMITTED-已提交/APPROVED-已审批/REJECTED-已驳回/EXECUTED-已执行)
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
     * 审批人
     */
    @TableField("APPROVE_USER")
    private String approveUser;

    /**
     * 审批时间
     */
    @TableField("APPROVE_TIME")
    private Date approveTime;

    /**
     * 审批意见
     */
    @TableField("APPROVE_OPINION")
    private String approveOpinion;

    /**
     * 执行人
     */
    @TableField("EXECUTE_USER")
    private String executeUser;

    /**
     * 执行时间
     */
    @TableField("EXECUTE_TIME")
    private Date executeTime;

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
     * 预算模型名称(非数据库字段)
     */
    @TableField(exist = false)
    private String modelName;

    /**
     * 提交人姓名(非数据库字段, 联表 TBL_STAFF.REALNAME)
     */
    @TableField(exist = false)
    private String submitUserName;

    /**
     * 创建人姓名(非数据库字段, 联表 TBL_STAFF.REALNAME)
     */
    @TableField(exist = false)
    private String createUserName;

    /**
     * 调整明细列表(非数据库字段)
     */
    @TableField(exist = false)
    private List<TblBudgetAdjustmentDetail> detailList;
}

