package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
// import lombok.AllArgsConstructor; // 已移除,使用手动编写的getter/setter
// import lombok.Data; // 已移除,使用手动编写的getter/setter
// import lombok.NoArgsConstructor; // 已移除,使用手动编写的getter/setter
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品本金规则表
 * @author hbyun-admin
 * @date 2024-01-01
 */
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除,使用手动编写的getter/setter
// @NoArgsConstructor // 已移除,使用手动编写的getter/setter
@TableName("TC_PRODUCT_PRINCIPAL_RULE")
public class TcProductPrincipalRule implements Serializable {
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
     * 本金发生次数
     */
    @TableField("PRINCIPAL_FREQUENCY")
    private String principalFrequency;

    /**
     * 本金类型
     */
    @TableField("PRINCIPAL_TYPE")
    private String principalType;

    /**
     * 币种代码
     */
    @TableField("CURRENCY_CODES")
    private String currencyCodes;

    /**
     * 单据模式
     */
    @TableField("DOCUMENT_MODE")
    private String documentMode;

    /**
     * 还本赎回方式
     */
    @TableField("REPAYMENT_METHOD")
    private String repaymentMethod;

    /**
     * 最小金额
     */
    @TableField("MIN_AMOUNT")
    private BigDecimal minAmount;

    /**
     * 最大金额
     */
    @TableField("MAX_AMOUNT")
    private BigDecimal maxAmount;

    /**
     * 递增金额
     */
    @TableField("STEP_AMOUNT")
    private BigDecimal stepAmount;

    /**
     * 计算公式
     */
    @TableField("CALCULATION_FORMULA")
    private String calculationFormula;

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

    // 完整的getter和setter方法

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrincipalFrequency() {
        return principalFrequency;
    }

    public void setPrincipalFrequency(String principalFrequency) {
        this.principalFrequency = principalFrequency;
    }

    public String getPrincipalType() {
        return principalType;
    }

    public void setPrincipalType(String principalType) {
        this.principalType = principalType;
    }

    public String getCurrencyCodes() {
        return currencyCodes;
    }

    public void setCurrencyCodes(String currencyCodes) {
        this.currencyCodes = currencyCodes;
    }

    public String getDocumentMode() {
        return documentMode;
    }

    public void setDocumentMode(String documentMode) {
        this.documentMode = documentMode;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getStepAmount() {
        return stepAmount;
    }

    public void setStepAmount(BigDecimal stepAmount) {
        this.stepAmount = stepAmount;
    }

    public String getCalculationFormula() {
        return calculationFormula;
    }

    public void setCalculationFormula(String calculationFormula) {
        this.calculationFormula = calculationFormula;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }
}
