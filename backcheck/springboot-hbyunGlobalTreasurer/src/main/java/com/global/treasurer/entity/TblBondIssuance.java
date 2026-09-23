package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 债券发行实体类
 * 对应数据库表: TBL_BOND_ISSUANCE
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Data
@TableName("TBL_BOND_ISSUANCE")
public class TblBondIssuance implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 债券ID */
    @TableId(value = "BOND_ID", type = IdType.ASSIGN_ID)
    private Long bondId;

    /** 债券编号 */
    @TableField("BOND_NO")
    private String bondNo;

    /** 债券名称 */
    @TableField("BOND_NAME")
    private String bondName;

    /** 公司ID */
    @TableField("COMPANY_ID")
    private Long companyId;

    /** 公司名称 */
    @TableField("COMPANY_NAME")
    private String companyName;

    /** 债券类型 */
    @TableField("BOND_TYPE")
    private String bondType;

    /** 发行金额 */
    @TableField("ISSUE_AMOUNT")
    private BigDecimal issueAmount;

    /** 存续金额 */
    @TableField("OUTSTANDING_AMOUNT")
    private BigDecimal outstandingAmount;

    /** 币种 */
    @TableField("CURRENCY_CODE")
    private String currencyCode;

    /** 票面利率 */
    @TableField("COUPON_RATE")
    private BigDecimal couponRate;

    /** 债券代码 */
    @TableField("BOND_CODE")
    private String bondCode;

    /** 面值 */
    @TableField("FACE_VALUE")
    private BigDecimal faceValue;

    /** 债券期限 */
    @TableField("BOND_TERM")
    private Integer bondTerm;

    /** 期限单位 */
    @TableField("TERM_UNIT")
    private String termUnit;

    /** 付息频率 */
    @TableField("PAYMENT_FREQUENCY")
    private String paymentFrequency;

    /** 信用评级 */
    @TableField("CREDIT_RATING")
    private String creditRating;

    /** 承销商 */
    @TableField("UNDERWRITER")
    private String underwriter;

    /** 受托管理人 */
    @TableField("TRUSTEE")
    private String trustee;

    /** 评级机构 */
    @TableField("RATING_AGENCY")
    private String ratingAgency;

    /** 上市交易所 */
    @TableField("LISTING_EXCHANGE")
    private String listingExchange;

    /** 评级日期 */
    @TableField("RATING_DATE")
    private Date ratingDate;

    /** 评级展望 */
    @TableField("RATING_OUTLOOK")
    private String ratingOutlook;

    /** 评级说明 */
    @TableField("RATING_NOTES")
    private String ratingNotes;

    /** 上市日期 */
    @TableField("LISTING_DATE")
    private Date listingDate;

    /** 证券代码 */
    @TableField("STOCK_CODE")
    private String stockCode;

    /** 上市说明 */
    @TableField("LISTING_NOTES")
    private String listingNotes;

    /** 兑付类型 */
    @TableField("REDEEM_TYPE")
    private String redeemType;

    /** 兑付金额 */
    @TableField("REDEEM_AMOUNT")
    private BigDecimal redeemAmount;

    /** 兑付日期 */
    @TableField("REDEEM_DATE")
    private Date redeemDate;

    /** 兑付说明 */
    @TableField("REDEEM_NOTES")
    private String redeemNotes;

    /** 发行日期 */
    @TableField("ISSUE_DATE")
    private Date issueDate;

    /** 到期日期 */
    @TableField("MATURITY_DATE")
    private Date maturityDate;

    /** 债券状态 */
    @TableField("BOND_STATUS")
    private String bondStatus;

    /** 删除标志 */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /** 创建人 */
    @TableField("CREATED_BY")
    private String createdBy;

    /** 创建时间 */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField("UPDATED_BY")
    private String updatedBy;

    /** 更新时间 */
    @TableField("UPDATED_TIME")
    private Date updatedTime;

    // ============ 兼容性方法（保持与前端和其他代码的兼容） ============

    /** 获取发行ID（兼容旧代码） */
    public Long getIssuanceId() {
        return bondId;
    }

    /** 设置发行ID（兼容旧代码） */
    public void setIssuanceId(Long issuanceId) {
        this.bondId = issuanceId;
    }

    /** 获取发行编号（兼容旧代码） */
    public String getIssuanceNo() {
        return bondNo;
    }

    /** 设置发行编号（兼容旧代码） */
    public void setIssuanceNo(String issuanceNo) {
        this.bondNo = issuanceNo;
    }

    /** 获取发行状态（兼容旧代码） */
    public String getIssuanceStatus() {
        return bondStatus;
    }

    /** 设置发行状态（兼容旧代码） */
    public void setIssuanceStatus(String issuanceStatus) {
        this.bondStatus = issuanceStatus;
    }

    /** 获取发行金额（兼容旧代码） */
    public BigDecimal getIssuanceAmount() {
        return issueAmount;
    }

    /** 设置发行金额（兼容旧代码） */
    public void setIssuanceAmount(BigDecimal issuanceAmount) {
        this.issueAmount = issuanceAmount;
    }

    /** 获取发行日期（兼容旧代码） */
    public Date getIssuanceDate() {
        return issueDate;
    }

    /** 设置发行日期（兼容旧代码） */
    public void setIssuanceDate(Date issuanceDate) {
        this.issueDate = issuanceDate;
    }

    /** 获取实际发行金额（兼容旧代码，映射到存续金额） */
    public BigDecimal getActualIssuanceAmount() {
        return outstandingAmount;
    }

    /** 设置实际发行金额（兼容旧代码，映射到存续金额） */
    public void setActualIssuanceAmount(BigDecimal actualIssuanceAmount) {
        this.outstandingAmount = actualIssuanceAmount;
    }

    // ============ 额外的 setter 方法（确保编译通过） ============

    public void setBondNo(String bondNo) {
        this.bondNo = bondNo;
    }

    public void setBondStatus(String bondStatus) {
        this.bondStatus = bondStatus;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getBondStatus() {
        return bondStatus;
    }

    public BigDecimal getIssueAmount() {
        return issueAmount;
    }

    public void setBondId(Long bondId) {
        this.bondId = bondId;
    }

    public void setBondName(String bondName) {
        this.bondName = bondName;
    }

    public void setBondType(String bondType) {
        this.bondType = bondType;
    }

    public void setIssueAmount(BigDecimal issueAmount) {
        this.issueAmount = issueAmount;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setCouponRate(BigDecimal couponRate) {
        this.couponRate = couponRate;
    }

    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }

    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }

    public void setBondTerm(Integer bondTerm) {
        this.bondTerm = bondTerm;
    }

    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public void setUnderwriter(String underwriter) {
        this.underwriter = underwriter;
    }

    public void setTrustee(String trustee) {
        this.trustee = trustee;
    }

    public void setRatingAgency(String ratingAgency) {
        this.ratingAgency = ratingAgency;
    }

    public void setListingExchange(String listingExchange) {
        this.listingExchange = listingExchange;
    }

    public void setRatingDate(Date ratingDate) {
        this.ratingDate = ratingDate;
    }

    public void setRatingOutlook(String ratingOutlook) {
        this.ratingOutlook = ratingOutlook;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public void setRatingNotes(String ratingNotes) {
        this.ratingNotes = ratingNotes;
    }

    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public void setListingNotes(String listingNotes) {
        this.listingNotes = listingNotes;
    }

    public void setRedeemType(String redeemType) {
        this.redeemType = redeemType;
    }

    public void setRedeemAmount(BigDecimal redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public void setRedeemDate(Date redeemDate) {
        this.redeemDate = redeemDate;
    }

    public void setRedeemNotes(String redeemNotes) {
        this.redeemNotes = redeemNotes;
    }

    // ============ 额外的 getter 方法（确保编译通过） ============

    public Long getBondId() {
        return bondId;
    }

    public String getBondName() {
        return bondName;
    }

    public String getBondType() {
        return bondType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getCouponRate() {
        return couponRate;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTermUnit() {
        return termUnit;
    }
}

