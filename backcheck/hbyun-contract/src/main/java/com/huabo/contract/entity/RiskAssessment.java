package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险评估主表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("risk_assessment")
public class RiskAssessment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 评估编号
     */
    @TableField("assessment_no")
    private String assessmentNo;

    /**
     * 评估名称
     */
    @TableField("assessment_name")
    private String assessmentName;

    /**
     * 相对方ID
     */
    @TableField("counterpart_id")
    private Long counterpartId;

    /**
     * 评估类型(1:承接前,2:执行中,3:结项后)
     */
    @TableField("assessment_type")
    private Integer assessmentType;

    /**
     * 评估状态(1:待评估,2:评估中,3:已完成)
     */
    @TableField("assessment_status")
    private Integer assessmentStatus;

    /**
     * 总评分
     */
    @TableField("total_score")
    private BigDecimal totalScore;

    /**
     * 风险等级(1:低,2:中,3:高,4:极高)
     */
    @TableField("risk_level")
    private Integer riskLevel;

    /**
     * 评估人ID
     */
    @TableField("assessor_id")
    private Long assessorId;

    /**
     * 评估日期
     */
    @TableField("assessment_date")
    private Date assessmentDate;

    /**
     * 审批状态(1:待审批,2:已审批,3:已驳回)
     */
    @TableField("approval_status")
    private Integer approvalStatus;

    /**
     * 审批人ID
     */
    @TableField("approver_id")
    private Long approverId;

    /**
     * 审批日期
     */
    @TableField("approval_date")
    private Date approvalDate;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private Long updateBy;

    /**
     * 总体风险等级 - 数据库中暂不存在此字段，标记为不存在
     */
    @TableField(exist = false)
    private Integer overallRiskLevel;

    /**
     * 总体风险评分 - 数据库中暂不存在此字段，标记为不存在
     */
    @TableField(exist = false)
    private Double overallRiskScore;

    /**
     * 获取评估类型名称
     */
    public String getAssessmentTypeName() {
        if (assessmentType == null) {
            return "";
        }
        switch (assessmentType) {
            case 1:
                return "承接前评估";
            case 2:
                return "执行中评估";
            case 3:
                return "结项后评估";
            default:
                return "未知";
        }
    }

    /**
     * 获取评估状态名称
     */
    public String getAssessmentStatusName() {
        if (assessmentStatus == null) {
            return "";
        }
        switch (assessmentStatus) {
            case 1:
                return "待评估";
            case 2:
                return "评估中";
            case 3:
                return "已完成";
            default:
                return "未知";
        }
    }

    /**
     * 获取风险等级名称
     */
    public String getRiskLevelName() {
        if (riskLevel == null) {
            return "";
        }
        switch (riskLevel) {
            case 1:
                return "低风险";
            case 2:
                return "中风险";
            case 3:
                return "高风险";
            case 4:
                return "极高风险";
            default:
                return "未知";
        }
    }

    /**
     * 获取审批状态名称
     */
    public String getApprovalStatusName() {
        if (approvalStatus == null) {
            return "";
        }
        switch (approvalStatus) {
            case 1:
                return "待审批";
            case 2:
                return "已审批";
            case 3:
                return "已驳回";
            default:
                return "未知";
        }
    }

    /**
     * 获取风险等级颜色
     */
    public String getRiskLevelColor() {
        if (riskLevel == null) {
            return "#909399";
        }
        switch (riskLevel) {
            case 1:
                return "#67C23A"; // 绿色
            case 2:
                return "#E6A23C"; // 橙色
            case 3:
                return "#F56C6C"; // 红色
            case 4:
                return "#F56C6C"; // 深红色
            default:
                return "#909399";
        }
    }

    /**
     * 判断是否为高风险
     */
    public boolean isHighRisk() {
        return riskLevel != null && riskLevel >= 3;
    }

    /**
     * 判断是否需要审批
     */
    public boolean needApproval() {
        return isHighRisk() || (totalScore != null && totalScore.compareTo(new BigDecimal("70")) < 0);
    }
}
