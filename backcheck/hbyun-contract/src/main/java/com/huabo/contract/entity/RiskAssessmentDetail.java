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
 * 风险评估明细表实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("risk_assessment_detail")
public class RiskAssessmentDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评估主表ID
     */
    @TableField("assessment_id")
    private Long assessmentId;

    /**
     * 风险类别(1:资格资信,2:政策,3:资金,4:技术,5:法律,6:收款,7:税务,8:施工环境)
     */
    @TableField("risk_category")
    private Integer riskCategory;

    /**
     * 风险项目
     */
    @TableField("risk_item")
    private String riskItem;

    /**
     * 风险描述
     */
    @TableField("risk_description")
    private String riskDescription;

    /**
     * 风险概率(0-1)
     */
    @TableField("risk_probability")
    private BigDecimal riskProbability;

    /**
     * 风险影响(1:轻微,2:一般,3:严重,4:灾难)
     */
    @TableField("risk_impact")
    private Integer riskImpact;

    /**
     * 风险得分
     */
    @TableField("risk_score")
    private BigDecimal riskScore;

    /**
     * 缓解措施
     */
    @TableField("mitigation_measures")
    private String mitigationMeasures;

    /**
     * 责任人
     */
    @TableField("responsible_person")
    private String responsiblePerson;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 获取风险类别名称
     */
    public String getRiskCategoryName() {
        if (riskCategory == null) {
            return "";
        }
        switch (riskCategory) {
            case 1:
                return "资格资信风险";
            case 2:
                return "政策风险";
            case 3:
                return "资金风险";
            case 4:
                return "技术风险";
            case 5:
                return "法律风险";
            case 6:
                return "收款风险";
            case 7:
                return "税务风险";
            case 8:
                return "施工环境风险";
            default:
                return "未知";
        }
    }

    /**
     * 获取风险影响名称
     */
    public String getRiskImpactName() {
        if (riskImpact == null) {
            return "";
        }
        switch (riskImpact) {
            case 1:
                return "轻微";
            case 2:
                return "一般";
            case 3:
                return "严重";
            case 4:
                return "灾难";
            default:
                return "未知";
        }
    }

    /**
     * 获取风险影响颜色
     */
    public String getRiskImpactColor() {
        if (riskImpact == null) {
            return "#909399";
        }
        switch (riskImpact) {
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
     * 计算风险得分
     * 风险得分 = 风险概率 * 风险影响 * 25
     */
    public BigDecimal calculateRiskScore() {
        if (riskProbability == null || riskImpact == null) {
            return BigDecimal.ZERO;
        }
        return riskProbability.multiply(new BigDecimal(riskImpact)).multiply(new BigDecimal("25"));
    }

    /**
     * 判断是否为高风险项目
     */
    public boolean isHighRiskItem() {
        if (riskScore == null) {
            return false;
        }
        return riskScore.compareTo(new BigDecimal("75")) >= 0;
    }

    /**
     * 获取风险等级
     */
    public Integer getRiskLevel() {
        if (riskScore == null) {
            return 1;
        }
        if (riskScore.compareTo(new BigDecimal("25")) < 0) {
            return 1; // 低风险
        } else if (riskScore.compareTo(new BigDecimal("50")) < 0) {
            return 2; // 中风险
        } else if (riskScore.compareTo(new BigDecimal("75")) < 0) {
            return 3; // 高风险
        } else {
            return 4; // 极高风险
        }
    }

    /**
     * 获取风险等级名称
     */
    public String getRiskLevelName() {
        Integer level = getRiskLevel();
        switch (level) {
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
}
