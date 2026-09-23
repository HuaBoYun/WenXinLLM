package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除
// import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 全球司库-账户限额实体类
 * 对应数据库表 TBL_GT_ACCOUNT_LIMIT
 *
 * @author AI Developer
 * @since 2026-01-16
 */
// @Data // 已移除,使用手动编写的getter/setter
// @EqualsAndHashCode // 已移除
@TableName("TBL_GT_ACCOUNT_LIMIT")
public class TblGtAccountLimit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 限额ID
     */
    @TableId(value = "LIMIT_ID", type = IdType.ASSIGN_ID)
    private Long limitId;

    /**
     * 关联账户ID
     */
    @TableField("ACCOUNT_ID")
    private Long accountId;

    /**
     * 账户号码
     */
    @TableField("ACCOUNT_NUMBER")
    private String accountNumber;

    /**
     * 限额类型: SINGLE-单笔限额, DAILY-日限额, MONTHLY-月限额, YEARLY-年限额
     */
    @TableField("LIMIT_TYPE")
    private String limitType;

    /**
     * 币种代码
     */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /**
     * 限额金额
     */
    @TableField("LIMIT_AMOUNT")
    private BigDecimal limitAmount;

    /**
     * 已使用金额
     */
    @TableField("USED_AMOUNT")
    private BigDecimal usedAmount;

    /**
     * 预警阈值(百分比)
     */
    @TableField("WARNING_THRESHOLD")
    private BigDecimal warningThreshold;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRY_DATE")
    private LocalDate expiryDate;

    /**
     * 限额状态: ACTIVE-生效, INACTIVE-未生效, EXPIRED-已失效
     */
    @TableField("LIMIT_STATUS")
    private String limitStatus;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 账户名称（扩展字段，JOIN TBL_GT_ACCOUNT_INFO 获取，不对应数据库列）
     */
    @TableField(exist = false)
    private String accountName;

    /**
     * 创建人ID
     */
    @TableField("CREATE_USER")
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATE_USER")
    private Long updateUser;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getLimitId() { return limitId; }
    public void setLimitId(Long limitId) { this.limitId = limitId; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getLimitType() { return limitType; }
    public void setLimitType(String limitType) { this.limitType = limitType; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public BigDecimal getLimitAmount() { return limitAmount; }
    public void setLimitAmount(BigDecimal limitAmount) { this.limitAmount = limitAmount; }
    public BigDecimal getUsedAmount() { return usedAmount; }
    public void setUsedAmount(BigDecimal usedAmount) { this.usedAmount = usedAmount; }
    public BigDecimal getWarningThreshold() { return warningThreshold; }
    public void setWarningThreshold(BigDecimal warningThreshold) { this.warningThreshold = warningThreshold; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getLimitStatus() { return limitStatus; }
    public void setLimitStatus(String limitStatus) { this.limitStatus = limitStatus; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public Long getCreateUser() { return createUser; }
    public void setCreateUser(Long createUser) { this.createUser = createUser; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Long getUpdateUser() { return updateUser; }
    public void setUpdateUser(Long updateUser) { this.updateUser = updateUser; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

}
