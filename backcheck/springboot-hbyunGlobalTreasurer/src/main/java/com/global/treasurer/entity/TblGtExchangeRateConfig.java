package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 汇率配置实体类
 * 对应表: TBL_GT_EXCHANGE_RATE_CONFIG
 *
 * @author 华博云开发团队
 * @since 2026-01-28
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_GT_EXCHANGE_RATE_CONFIG")
public class TblGtExchangeRateConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 配置编码
     */
    @TableField("CONFIG_CODE")
    private String configCode;

    /**
     * 配置名称
     */
    @TableField("CONFIG_NAME")
    private String configName;

    /**
     * 基准币种
     */
    @TableField("BASE_CURRENCY")
    private String baseCurrency;

    /**
     * 目标币种
     */
    @TableField("TARGET_CURRENCY")
    private String targetCurrency;

    /**
     * 汇率
     */
    @TableField("EXCHANGE_RATE")
    private BigDecimal exchangeRate;

    /**
     * 汇率类型(SPOT-现汇, CASH-现钞, MIDDLE-中间价)
     */
    @TableField("RATE_TYPE")
    private String rateType;

    /**
     * 数据来源(MANUAL-手工录入, AUTO-自动获取, API-API接口)
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 生效日期
     */
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 失效日期
     */
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    /**
     * 更新频率(DAILY-每日, HOURLY-每小时, REALTIME-实时)
     */
    @TableField("UPDATE_FREQUENCY")
    private String updateFrequency;

    /**
     * 是否自动更新(0-否, 1-是)
     */
    @TableField("AUTO_UPDATE")
    private Integer autoUpdate;

    /**
     * 状态(0-禁用, 1-启用)
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 组织ID
     */
    @TableField("ORG_ID")
    private Long orgId;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConfigCode() { return configCode; }
    public void setConfigCode(String configCode) { this.configCode = configCode; }
    public String getConfigName() { return configName; }
    public void setConfigName(String configName) { this.configName = configName; }
    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }
    public String getTargetCurrency() { return targetCurrency; }
    public void setTargetCurrency(String targetCurrency) { this.targetCurrency = targetCurrency; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }
    public String getDataSource() { return dataSource; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public Date getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; }
    public Date getExpireDate() { return expireDate; }
    public void setExpireDate(Date expireDate) { this.expireDate = expireDate; }
    public String getUpdateFrequency() { return updateFrequency; }
    public void setUpdateFrequency(String updateFrequency) { this.updateFrequency = updateFrequency; }
    public Integer getAutoUpdate() { return autoUpdate; }
    public void setAutoUpdate(Integer autoUpdate) { this.autoUpdate = autoUpdate; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getOrgId() { return orgId; }
    public void setOrgId(Long orgId) { this.orgId = orgId; }
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
