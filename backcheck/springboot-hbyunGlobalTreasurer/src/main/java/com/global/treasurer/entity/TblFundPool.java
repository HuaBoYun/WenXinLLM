package com.global.treasurer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资金池实体类（对应数据库表 TBL_FUND_POOL）
 * @author Claude
 * @date 2026-01-20
 */
// @Data
@TableName("TBL_FUND_POOL")
@ApiModel(value = "TblFundPool", description = "资金池实体")
public class TblFundPool implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "POOL_ID", type = IdType.AUTO)
    @ApiModelProperty("资金池ID（自增）")
    private Long poolId;

    @TableField("POOL_NAME")
    @ApiModelProperty("资金池名称")
    private String poolName;

    @TableField("POOL_CODE")
    @ApiModelProperty("资金池编码")
    private String poolCode;

    @TableField("POOL_TYPE")
    @ApiModelProperty("资金池类型(PHYSICAL-实体池/VIRTUAL-虚拟池/NOTIONAL-名义池)")
    private String poolType;

    @TableField("CURRENCY")
    @ApiModelProperty("币种")
    private String currency;

    @TableField("TOTAL_LIMIT")
    @ApiModelProperty("总额度")
    private BigDecimal totalLimit;

    @TableField("USED_AMOUNT")
    @ApiModelProperty("已用金额")
    private BigDecimal usedAmount;

    @TableField("AVAILABLE_AMOUNT")
    @ApiModelProperty("可用金额")
    private BigDecimal availableAmount;

    @TableField("STATUS")
    @ApiModelProperty("状态(ACTIVE-启用/INACTIVE-停用)")
    private String status;

    @TableField("IS_ENABLED")
    @ApiModelProperty("是否启用(1-启用/0-禁用)")
    private Integer isEnabled;

    @TableField("COMPANY_NAME")
    @ApiModelProperty("所属公司")
    private String companyName;

    @TableField("MANAGER_ID")
    @ApiModelProperty("负责人ID")
    private String managerId;

    @TableField("DESCRIPTION")
    @ApiModelProperty("描述")
    private String description;

    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty("创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty("更新人")
    private String updateUser;

    @TableField("REMARK")
    @ApiModelProperty("备注")
    private String remark;

    // 以下方法由Lombok生成,手动添加以解决编译问题
    public Long getPoolId() { return poolId; }
    public void setPoolId(Long poolId) { this.poolId = poolId; }
    public String getPoolName() { return poolName; }
    public void setPoolName(String poolName) { this.poolName = poolName; }
    public String getPoolCode() { return poolCode; }
    public void setPoolCode(String poolCode) { this.poolCode = poolCode; }
    public String getPoolType() { return poolType; }
    public void setPoolType(String poolType) { this.poolType = poolType; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public BigDecimal getTotalLimit() { return totalLimit; }
    public void setTotalLimit(BigDecimal totalLimit) { this.totalLimit = totalLimit; }
    public BigDecimal getUsedAmount() { return usedAmount; }
    public void setUsedAmount(BigDecimal usedAmount) { this.usedAmount = usedAmount; }
    public BigDecimal getAvailableAmount() { return availableAmount; }
    public void setAvailableAmount(BigDecimal availableAmount) { this.availableAmount = availableAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
