package com.global.treasurer.dto;

// import lombok.Data; // 已移除,使用手动编写的getter/setter

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 担保物DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
// @Data // 已移除,使用手动编写的getter/setter
public class CollateralDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 担保物ID */
    private Long collateralId;

    /** 担保物编号 */
    private String collateralNo;

    /** 担保物类型 */
    @NotBlank(message = "担保物类型不能为空")
    private String collateralType;

    /** 担保物名称 */
    @NotBlank(message = "担保物名称不能为空")
    private String collateralName;

    /** 所有权人 */
    @NotBlank(message = "所有权人不能为空")
    private String ownerCompany;

    /** 评估价值 */
    @NotNull(message = "评估价值不能为空")
    private BigDecimal assessedValue;

    /** 币种 */
    @NotBlank(message = "币种不能为空")
    private String currencyCode;

    /** 所在地点 */
    private String location;

    /** 面积 */
    private BigDecimal area;

    /** 取得日期 */
    private Date purchaseDate;

    /** 使用年限 */
    private Integer usefulLife;

    /** 描述 */
    private String description;

    /** 权属证明 */
    private String certificates;

    /** 公司ID */
    @NotNull(message = "公司ID不能为空")
    private Long companyId;

    /** 公司名称 */
    private String companyName;

    /** 备注 */
    private String remark;

    // 完整的getter和setter方法

    public Long getCollateralId() {
        return collateralId;
    }

    public void setCollateralId(Long collateralId) {
        this.collateralId = collateralId;
    }

    public String getCollateralNo() {
        return collateralNo;
    }

    public void setCollateralNo(String collateralNo) {
        this.collateralNo = collateralNo;
    }

    public String getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
    }

    public String getCollateralName() {
        return collateralName;
    }

    public void setCollateralName(String collateralName) {
        this.collateralName = collateralName;
    }

    public String getOwnerCompany() {
        return ownerCompany;
    }

    public void setOwnerCompany(String ownerCompany) {
        this.ownerCompany = ownerCompany;
    }

    public BigDecimal getAssessedValue() {
        return assessedValue;
    }

    public void setAssessedValue(BigDecimal assessedValue) {
        this.assessedValue = assessedValue;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getUsefulLife() {
        return usefulLife;
    }

    public void setUsefulLife(Integer usefulLife) {
        this.usefulLife = usefulLife;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

