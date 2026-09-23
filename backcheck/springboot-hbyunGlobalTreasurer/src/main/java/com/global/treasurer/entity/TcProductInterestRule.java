package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品利息规则表
 * @author hbyun-admin
 * @date 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@TableName("TC_PRODUCT_INTEREST_RULE")
public class TcProductInterestRule implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    private String id;

    /**
     * 产品ID
     */
    @TableField("PRODUCT_ID")
    private String productId;

    /**
     * 利率类型
     */
    @TableField("INTEREST_RATE_TYPE")
    private String interestRateType;

    /**
     * 基准利率
     */
    @TableField("BASE_RATE")
    private BigDecimal baseRate;

    /**
     * 浮动点差
     */
    @TableField("FLOATING_SPREAD")
    private BigDecimal floatingSpread;

    /**
     * 计息方法
     */
    @TableField("CALCULATION_METHOD")
    private String calculationMethod;

    /**
     * 计息天数方法
     */
    @TableField("DAY_COUNT_METHOD")
    private String dayCountMethod;

    /**
     * 结息头尾规则
     */
    @TableField("INTEREST_HEAD_TAIL_RULE")
    private String interestHeadTailRule;

    /**
     * 预提头尾规则
     */
    @TableField("ACCRUAL_HEAD_TAIL_RULE")
    private String accrualHeadTailRule;

    /**
     * 利率周期
     */
    @TableField("RATE_PERIOD")
    private String ratePeriod;

    /**
     * 提前还本计息类型
     */
    @TableField("EARLY_REPAY_INTEREST_TYPE")
    private String earlyRepayInterestType;

    /**
     * 提前还本指定利率
     */
    @TableField("EARLY_REPAY_SPECIFIED_RATE")
    private BigDecimal earlyRepaySpecifiedRate;

    /**
     * 付息频率
     */
    @TableField("PAYMENT_FREQUENCY")
    private String paymentFrequency;

    /**
     * 复利频率
     */
    @TableField("COMPOUNDING_FREQUENCY")
    private String compoundingFrequency;

    /**
     * 状态(1:启用 0:停用)
     */
    @TableField("STATUS")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建用户
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 更新用户
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    private Long versionNo;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getInterestRateType() { return interestRateType; }
    public void setInterestRateType(String interestRateType) { this.interestRateType = interestRateType; }
    public BigDecimal getBaseRate() { return baseRate; }
    public void setBaseRate(BigDecimal baseRate) { this.baseRate = baseRate; }
    public BigDecimal getFloatingSpread() { return floatingSpread; }
    public void setFloatingSpread(BigDecimal floatingSpread) { this.floatingSpread = floatingSpread; }
    public String getCalculationMethod() { return calculationMethod; }
    public void setCalculationMethod(String calculationMethod) { this.calculationMethod = calculationMethod; }
    public String getDayCountMethod() { return dayCountMethod; }
    public void setDayCountMethod(String dayCountMethod) { this.dayCountMethod = dayCountMethod; }
    public String getInterestHeadTailRule() { return interestHeadTailRule; }
    public void setInterestHeadTailRule(String interestHeadTailRule) { this.interestHeadTailRule = interestHeadTailRule; }
    public String getAccrualHeadTailRule() { return accrualHeadTailRule; }
    public void setAccrualHeadTailRule(String accrualHeadTailRule) { this.accrualHeadTailRule = accrualHeadTailRule; }
    public String getRatePeriod() { return ratePeriod; }
    public void setRatePeriod(String ratePeriod) { this.ratePeriod = ratePeriod; }
    public String getEarlyRepayInterestType() { return earlyRepayInterestType; }
    public void setEarlyRepayInterestType(String earlyRepayInterestType) { this.earlyRepayInterestType = earlyRepayInterestType; }
    public BigDecimal getEarlyRepaySpecifiedRate() { return earlyRepaySpecifiedRate; }
    public void setEarlyRepaySpecifiedRate(BigDecimal earlyRepaySpecifiedRate) { this.earlyRepaySpecifiedRate = earlyRepaySpecifiedRate; }
    public String getPaymentFrequency() { return paymentFrequency; }
    public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }
    public String getCompoundingFrequency() { return compoundingFrequency; }
    public void setCompoundingFrequency(String compoundingFrequency) { this.compoundingFrequency = compoundingFrequency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public Long getVersionNo() { return versionNo; }
    public void setVersionNo(Long versionNo) { this.versionNo = versionNo; }
}
