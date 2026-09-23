package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算改进建议实体类
 *
 * @author AI Agent
 * @date 2026-04-10
 */
@Data
@TableName("TBL_BUDGET_IMPROVEMENT_SUGGESTION")
public class BudgetImprovementSuggestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 建议ID (主键) */
    @TableId(value = "SUGGESTION_ID", type = IdType.ASSIGN_UUID)
    private String suggestionId;

    /** 建议编码 */
    @TableField("SUGGESTION_CODE")
    private String suggestionCode;

    /** 优先级 (HIGH/MEDIUM/LOW) */
    @TableField("PRIORITY")
    private String priority;

    /** 建议标题 */
    @TableField("TITLE")
    private String title;

    /** 建议描述 */
    @TableField("DESCRIPTION")
    private String description;

    /** 建议类别 (PROCESS/MONITOR/ASSESSMENT/COST/REVENUE) */
    @TableField("CATEGORY")
    private String category;

    /** 关联预算年度 */
    @TableField("BUDGET_YEAR")
    private Integer budgetYear;

    /** 关联组织ID */
    @TableField("ORGANIZATION_ID")
    private String organizationId;

    /** 状态 (PENDING/IN_PROGRESS/COMPLETED/CANCELLED) */
    @TableField("STATUS")
    private String status;

    /** 创建人 */
    @TableField("CREATE_BY")
    private String createBy;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private Date createTime;

    /** 更新人 */
    @TableField("UPDATE_BY")
    private String updateBy;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /** 删除标志 (0-正常 1-删除) */
    @TableField("DEL_FLAG")
    private Integer delFlag;
}
