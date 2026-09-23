package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 合同智能审查实体
 * @author AI Agent
 * @date 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_CONTRACT_SMART_REVIEW")
public class GzctContractSmartReview {

    @TableId(value = "REVIEW_ID", type = IdType.ASSIGN_UUID)
    private String reviewId;

    /** 合同名称 */
    @TableField("CONTRACT_NAME")
    private String contractName;

    /** 合同类型：采购合同/销售合同/工程合同/租赁合同/服务合同 */
    @TableField("CONTRACT_TYPE")
    private String contractType;

    /** 审查状态：pending/reviewing/done/risk */
    @TableField("REVIEW_STATUS")
    private String reviewStatus;

    /** 风险等级：high/medium/low */
    @TableField("RISK_LEVEL")
    private String riskLevel;

    /** 审查人 */
    @TableField("REVIEWER")
    private String reviewer;

    /** 审查时间 */
    @TableField("REVIEW_TIME")
    private LocalDateTime reviewTime;

    /** 风险条款（JSON格式） */
    @TableField("RISK_CLAUSES")
    private String riskClauses;

    /** 缺失条款（JSON格式） */
    @TableField("MISSING_CLAUSES")
    private String missingClauses;

    /** 偏离条款（JSON格式） */
    @TableField("DEVIATION_CLAUSES")
    private String deviationClauses;

    /** 备注 */
    @TableField("REMARK")
    private String remark;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
