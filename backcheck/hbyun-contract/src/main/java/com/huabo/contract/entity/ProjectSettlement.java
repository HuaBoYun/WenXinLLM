package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目结算实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("project_settlement")
@Schema(name="ProjectSettlement对象", description="项目结算管理")
public class ProjectSettlement {

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "项目ID")
    @TableField("project_id")
    private Long projectId;

    @Schema(name = "结算编号")
    @TableField("settlement_no")
    private String settlementNo;

    @Schema(name = "结算名称")
    @TableField("settlement_name")
    private String settlementName;

    @Schema(name = "结算类型(1:进度结算,2:阶段结算,3:最终结算)")
    @TableField("settlement_type")
    private Short settlementType;

    @Schema(name = "结算期间开始")
    @TableField("settlement_period_start")
    private Date settlementPeriodStart;

    @Schema(name = "结算期间结束")
    @TableField("settlement_period_end")
    private Date settlementPeriodEnd;

    @Schema(name = "合同金额")
    @TableField("contract_amount")
    private BigDecimal contractAmount;

    @Schema(name = "完成工程量金额")
    @TableField("completed_amount")
    private BigDecimal completedAmount;

    @Schema(name = "变更金额")
    @TableField("change_amount")
    private BigDecimal changeAmount;

    @Schema(name = "结算金额")
    @TableField("settlement_amount")
    private BigDecimal settlementAmount;

    @Schema(name = "前期结算金额")
    @TableField("previous_settlement_amount")
    private BigDecimal previousSettlementAmount;

    @Schema(name = "本期结算金额")
    @TableField("current_settlement_amount")
    private BigDecimal currentSettlementAmount;

    @Schema(name = "质保金比例(%)")
    @TableField("retention_rate")
    private BigDecimal retentionRate;

    @Schema(name = "质保金金额")
    @TableField("retention_amount")
    private BigDecimal retentionAmount;

    @Schema(name = "应付金额")
    @TableField("payable_amount")
    private BigDecimal payableAmount;

    @Schema(name = "结算依据")
    @TableField("settlement_basis")
    private String settlementBasis;

    @Schema(name = "结算说明")
    @TableField("settlement_description")
    private String settlementDescription;

    @Schema(name = "结算状态(1:待结算,2:结算中,3:已结算,4:已审核)")
    @TableField("settlement_status")
    private Short settlementStatus;

    @Schema(name = "结算人ID")
    @TableField(value = "settlor_id", insertStrategy = FieldStrategy.NOT_NULL)
    private Long settlorId;

    @Schema(name = "结算日期")
    @TableField("settlement_date")
    private Date settlementDate;

    @Schema(name = "审核人ID")
    @TableField("reviewer_id")
    private Long reviewerId;

    @Schema(name = "审核日期")
    @TableField("review_date")
    private Date reviewDate;

    @Schema(name = "审核意见")
    @TableField("review_comments")
    private String reviewComments;

    @Schema(name = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(name = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(name = "创建人")
    @TableField("create_by")
    private Long createBy;

    @Schema(name = "更新人")
    @TableField("update_by")
    private Long updateBy;

    // 扩展字段 - 用于前端显示
    @TableField(exist = false)
    @Schema(name = "项目名称")
    private String projectName;

    @TableField(exist = false)
    @Schema(name = "结算人姓名")
    private String settlorName;

    @TableField(exist = false)
    @Schema(name = "审核人姓名")
    private String reviewerName;

    // 计算属性方法
    public boolean isDailyInspection() {
        return settlementType != null && settlementType == 1;
    }

    public boolean isSpecialInspection() {
        return settlementType != null && settlementType == 2;
    }

    public boolean isComprehensiveInspection() {
        return settlementType != null && settlementType == 3;
    }

    public boolean isSettlementPending() {
        return settlementStatus != null && settlementStatus == 1;
    }

    public boolean isSettlementInProgress() {
        return settlementStatus != null && settlementStatus == 2;
    }

    public boolean isSettlementCompleted() {
        return settlementStatus != null && settlementStatus == 3;
    }

    public boolean isSettlementApproved() {
        return settlementStatus != null && settlementStatus == 4;
    }
}
