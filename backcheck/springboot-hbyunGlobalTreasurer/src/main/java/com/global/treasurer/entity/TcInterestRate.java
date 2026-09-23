package com.global.treasurer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 财资公共模块 - 利率数据表
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
@ApiModel(value = "TcInterestRate", description = "利率数据管理")
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除
@TableName("TC_INTEREST_RATE")
public class TcInterestRate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.INPUT)
    @TableField("ID")
    @ApiModelProperty(value = "主键ID")
    private String id;

    /**
     * 利率日期
     */
    @TableField("RATE_DATE")
    @ApiModelProperty(value = "利率日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rateDate;

    /**
     * 利率类型
     */
    @TableField("RATE_TYPE")
    @ApiModelProperty(value = "利率类型", required = true)
    private String rateType;

    /**
     * 利率编码
     */
    @TableField("RATE_CODE")
    @ApiModelProperty(value = "利率编码", required = true)
    private String rateCode;

    /**
     * 利率名称
     */
    @TableField("RATE_NAME")
    @ApiModelProperty(value = "利率名称", required = true)
    private String rateName;

    /**
     * 货币代码
     */
    @TableField("CURRENCY_CODE")
    @ApiModelProperty(value = "货币代码", required = true)
    private String currencyCode;

    /**
     * 期限类型
     */
    @TableField("TERM_TYPE")
    @ApiModelProperty(value = "期限类型")
    private String termType;

    /**
     * 期限值
     */
    @TableField("TERM_VALUE")
    @ApiModelProperty(value = "期限值")
    private Integer termValue;

    /**
     * 期限单位
     */
    @TableField("TERM_UNIT")
    @ApiModelProperty(value = "期限单位")
    private String termUnit;

    /**
     * 利率
     */
    @TableField("INTEREST_RATE")
    @ApiModelProperty(value = "利率", required = true)
    private BigDecimal interestRate;

    /**
     * 数据来源
     */
    @TableField("DATA_SOURCE")
    @ApiModelProperty(value = "数据来源")
    private String dataSource;

    /**
     * 来源系统
     */
    @TableField("SOURCE_SYSTEM")
    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;

    /**
     * 发布时间
     */
    @TableField("PUBLISH_TIME")
    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /**
     * 生效时间
     */
    @TableField("EFFECTIVE_TIME")
    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveTime;

    /**
     * 失效时间
     */
    @TableField("EXPIRE_TIME")
    @ApiModelProperty(value = "失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 状态：1-启用，0-停用
     */
    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    /**
     * 备注
     */
    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateUser;

    /**
     * 版本号
     */
    @TableField("VERSION_NO")
    @ApiModelProperty(value = "版本号", hidden = true)
    private Integer versionNo;

    /**
     * 根据ID创建实例
     */
    public static TcInterestRate ofId(String id) {
        TcInterestRate interestRate = new TcInterestRate();
        interestRate.setId(id);
        return interestRate;
    }

    /**
     * 根据利率编码创建实例
     */
    public static TcInterestRate ofRateCode(String rateCode) {
        TcInterestRate interestRate = new TcInterestRate();
        interestRate.setRateCode(rateCode);
        return interestRate;
    }

    /**
     * 根据利率日期和编码创建实例
     */
    public static TcInterestRate ofDateAndCode(Date rateDate, String rateCode) {
        TcInterestRate interestRate = new TcInterestRate();
        interestRate.setRateDate(rateDate);
        interestRate.setRateCode(rateCode);
        return interestRate;
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Date getRateDate() { return rateDate; }
    public void setRateDate(Date rateDate) { this.rateDate = rateDate; }
    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }
    public String getRateCode() { return rateCode; }
    public void setRateCode(String rateCode) { this.rateCode = rateCode; }
    public String getRateName() { return rateName; }
    public void setRateName(String rateName) { this.rateName = rateName; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getTermType() { return termType; }
    public void setTermType(String termType) { this.termType = termType; }
    public Integer getTermValue() { return termValue; }
    public void setTermValue(Integer termValue) { this.termValue = termValue; }
    public String getTermUnit() { return termUnit; }
    public void setTermUnit(String termUnit) { this.termUnit = termUnit; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public String getSourceSystem() { return sourceSystem; }
    public void setSourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; }
    public Date getPublishTime() { return publishTime; }
    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }
    public Date getEffectiveTime() { return effectiveTime; }
    public void setEffectiveTime(Date effectiveTime) { this.effectiveTime = effectiveTime; }
    public Date getExpireTime() { return expireTime; }
    public void setExpireTime(Date expireTime) { this.expireTime = expireTime; }
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
    public Integer getVersionNo() { return versionNo; }
    public void setVersionNo(Integer versionNo) { this.versionNo = versionNo; }
}
