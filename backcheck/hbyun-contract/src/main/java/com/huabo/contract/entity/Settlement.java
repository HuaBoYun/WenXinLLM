package com.huabo.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目结算管理实体类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("settlement")
@Schema(name="Settlement对象", description="项目结算管理")
public class Settlement {

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "项目ID")
    private Long projectId;

    @Schema(name = "结算编号")
    private String settlementNo;

    @Schema(name = "结算名称")
    private String settlementName;

    @Schema(name = "结算类型：1-进度结算，2-竣工结算，3-最终结算，4-变更结算")
    private Integer settlementType;

    @Schema(name = "结算阶段：1-施工阶段，2-竣工阶段，3-保修阶段")
    private Integer settlementStage;

    @Schema(name = "结算期间开始")
    private Date settlementPeriodStart;

    @Schema(name = "结算期间结束")
    private Date settlementPeriodEnd;

    @Schema(name = "申请日期")
    private Date applicationDate;

    @Schema(name = "申请人ID")
    private Long applicantId;

    @Schema(name = "申请人姓名")
    private String applicantName;

    @Schema(name = "申请单位")
    private String applicantUnit;

    @Schema(name = "合同金额")
    private BigDecimal contractAmount;

    @Schema(name = "已完成工程量")
    private BigDecimal completedWorkload;

    @Schema(name = "完成工程量百分比")
    private BigDecimal completionPercentage;

    @Schema(name = "申请结算金额")
    private BigDecimal applicationAmount;

    @Schema(name = "审核结算金额")
    private BigDecimal auditAmount;

    @Schema(name = "批准结算金额")
    private BigDecimal approvedAmount;

    @Schema(name = "变更金额")
    private BigDecimal changeAmount;

    @Schema(name = "索赔金额")
    private BigDecimal claimAmount;

    @Schema(name = "扣款金额")
    private BigDecimal deductionAmount;

    @Schema(name = "奖励金额")
    private BigDecimal bonusAmount;

    @Schema(name = "税费")
    private BigDecimal taxAmount;

    @Schema(name = "实际结算金额")
    private BigDecimal actualSettlementAmount;

    @Schema(name = "累计已结算金额")
    private BigDecimal cumulativeSettledAmount;

    @Schema(name = "本次结算金额")
    private BigDecimal currentSettlementAmount;

    @Schema(name = "质保金")
    private BigDecimal retentionMoney;

    @Schema(name = "质保金比例")
    private BigDecimal retentionRate;

    @Schema(name = "应付金额")
    private BigDecimal payableAmount;

    @Schema(name = "已付金额")
    private BigDecimal paidAmount;

    @Schema(name = "未付金额")
    private BigDecimal unpaidAmount;

    @Schema(name = "结算状态：1-申请中，2-审核中，3-已审核，4-已批准，5-已支付，6-已拒绝")
    private Integer settlementStatus;

    @Schema(name = "审核人ID")
    private Long auditBy;

    @Schema(name = "审核人姓名")
    private String auditByName;

    @Schema(name = "审核时间")
    private Date auditTime;

    @Schema(name = "审核意见")
    private String auditComments;

    @Schema(name = "批准人ID")
    private Long approveBy;

    @Schema(name = "批准人姓名")
    private String approveByName;

    @Schema(name = "批准时间")
    private Date approveTime;

    @Schema(name = "批准意见")
    private String approveComments;

    @Schema(name = "支付时间")
    private Date paymentTime;

    @Schema(name = "支付方式")
    private String paymentMethod;

    @Schema(name = "支付凭证")
    private String paymentVoucher;

    @Schema(name = "结算依据")
    private String settlementBasis;

    @Schema(name = "结算说明")
    private String settlementDescription;

    @Schema(name = "备注")
    private String remarks;

    @Schema(name = "附件路径")
    private String attachmentPath;

    @Schema(name = "创建人")
    private Long createBy;

    @Schema(name = "创建时间")
    private Date createTime;

    @Schema(name = "更新人")
    private Long updateBy;

    @Schema(name = "更新时间")
    private Date updateTime;

    /**
     * 判断是否为进度结算
     */
    public boolean isProgressSettlement() {
        return settlementType != null && settlementType == 1;
    }

    /**
     * 判断是否为竣工结算
     */
    public boolean isCompletionSettlement() {
        return settlementType != null && settlementType == 2;
    }

    /**
     * 判断是否为最终结算
     */
    public boolean isFinalSettlement() {
        return settlementType != null && settlementType == 3;
    }

    /**
     * 判断结算是否已批准
     */
    public boolean isApproved() {
        return settlementStatus != null && settlementStatus >= 4;
    }

    /**
     * 判断结算是否已支付
     */
    public boolean isPaid() {
        return settlementStatus != null && settlementStatus == 5;
    }

    /**
     * 判断结算是否被拒绝
     */
    public boolean isRejected() {
        return settlementStatus != null && settlementStatus == 6;
    }

    /**
     * 判断是否为大额结算
     */
    public boolean isLargeAmount() {
        return actualSettlementAmount != null && actualSettlementAmount.compareTo(new BigDecimal("1000000")) >= 0;
    }

    /**
     * 计算结算差异金额
     */
    public BigDecimal getSettlementVariance() {
        if (applicationAmount == null || approvedAmount == null) {
            return BigDecimal.ZERO;
        }
        return applicationAmount.subtract(approvedAmount);
    }

    /**
     * 计算结算差异率
     */
    public BigDecimal getSettlementVarianceRate() {
        if (applicationAmount == null || applicationAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal variance = getSettlementVariance();
        return variance.divide(applicationAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
    }

    /**
     * 计算支付进度
     */
    public BigDecimal getPaymentProgress() {
        if (payableAmount == null || payableAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (paidAmount == null) {
            return BigDecimal.ZERO;
        }
        return paidAmount.divide(payableAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
    }

    /**
     * 判断是否完全支付
     */
    public boolean isFullyPaid() {
        if (payableAmount == null || paidAmount == null) {
            return false;
        }
        return paidAmount.compareTo(payableAmount) >= 0;
    }

    /**
     * 判断是否有质保金
     */
    public boolean hasRetentionMoney() {
        return retentionMoney != null && retentionMoney.compareTo(BigDecimal.ZERO) > 0;
    }
}
