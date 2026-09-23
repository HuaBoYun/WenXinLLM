package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 利率配置实体类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_GT_INTEREST_RATE")
public class TblGtInterestRate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 利率ID
     */
    @TableId(value = "RATE_ID", type = IdType.AUTO)
    private Long rateId;

    /**
     * 利率类型
     */
    private String rateType;

    /**
     * 币种代码
     */
    private String currencyCode;

    /**
     * 期限
     */
    private Integer term;

    /**
     * 期限单位
     */
    private String termUnit;

    /**
     * 利率值
     */
    private BigDecimal interestRate;

    /**
     * 利率日期
     */
    private LocalDate rateDate;

    /**
     * 利率来源
     */
    private String rateSource;

    /**
     * 是否启用
     */
    private Integer isActive;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getRateId() { return rateId; }
    public void setRateId(Long rateId) { this.rateId = rateId; }
    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public Integer getTerm() { return term; }
    public void setTerm(Integer term) { this.term = term; }
    public String getTermUnit() { return termUnit; }
    public void setTermUnit(String termUnit) { this.termUnit = termUnit; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public LocalDate getRateDate() { return rateDate; }
    public void setRateDate(LocalDate rateDate) { this.rateDate = rateDate; }
    public String getRateSource() { return rateSource; }
    public void setRateSource(String rateSource) { this.rateSource = rateSource; }
    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }
    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

}
