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
 * 印鉴组合配置实体类
 * 对应表: TBL_SEAL_COMBINATION_CONFIG
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
// @Data // 已移除,使用手动编写的getter/setter
@TableName("TBL_SEAL_COMBINATION_CONFIG")
public class TcSealCombination implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "COMBINATION_ID", type = IdType.AUTO)
    private Long id;

    /**
     * 组合编码
     */
    @TableField("COMBINATION_CODE")
    private String combinationCode;

    /**
     * 组合名称
     */
    @TableField("COMBINATION_NAME")
    private String combinationName;

    /**
     * 组合类型
     */
    @TableField("COMBINATION_TYPE")
    private String combinationType;

    /**
     * 业务类型
     */
    @TableField("BUSINESS_TYPE")
    private String businessType;

    /**
     * 权限级别
     */
    @TableField("AUTHORITY_LEVEL")
    private String authorityLevel;

    /**
     * 金额限制
     */
    @TableField("MAX_AMOUNT_LIMIT")
    private BigDecimal maxAmountLimit;

    /**
     * 印章列表(JSON格式)
     */
    @TableField("REQUIRED_SEALS")
    private String sealList;

    /**
     * 组合描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 状态(1启用,0禁用)
     */
    @TableField("IS_ENABLED")
    private Integer status;

    /**
     * 使用次数
     */
    @TableField("USAGE_COUNT")
    private Integer usageCount;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    // 以下方法由Lombok生成,手动添加以解决编译问题
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCombinationCode() { return combinationCode; }
    public void setCombinationCode(String combinationCode) { this.combinationCode = combinationCode; }
    public String getCombinationName() { return combinationName; }
    public void setCombinationName(String combinationName) { this.combinationName = combinationName; }
    public String getCombinationType() { return combinationType; }
    public void setCombinationType(String combinationType) { this.combinationType = combinationType; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getAuthorityLevel() { return authorityLevel; }
    public void setAuthorityLevel(String authorityLevel) { this.authorityLevel = authorityLevel; }
    public BigDecimal getMaxAmountLimit() { return maxAmountLimit; }
    public void setMaxAmountLimit(BigDecimal maxAmountLimit) { this.maxAmountLimit = maxAmountLimit; }
    public String getSealList() { return sealList; }
    public void setSealList(String sealList) { this.sealList = sealList; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }
    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Long getUpdateBy() { return updateBy; }
    public void setUpdateBy(Long updateBy) { this.updateBy = updateBy; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
