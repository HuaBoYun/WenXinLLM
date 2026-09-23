package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 票据池DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel("票据池DTO")
public class BillPoolDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("票据池ID")
    private Long poolId;

    @ApiModelProperty(value = "票据池名称", required = true)
    @NotNull(message = "票据池名称不能为空")
    @Size(max = 200, message = "票据池名称长度不能超过200个字符")
    private String poolName;

    @ApiModelProperty(value = "票据池编码", required = true)
    @NotNull(message = "票据池编码不能为空")
    @Size(max = 50, message = "票据池编码长度不能超过50个字符")
    private String poolCode;

    @ApiModelProperty(value = "票据池类型", required = true)
    @NotNull(message = "票据池类型不能为空")
    private String poolType;

    @ApiModelProperty("所有者ID")
    private Long ownerId;

    @ApiModelProperty("所有者名称")
    private String ownerName;

    @ApiModelProperty("票据池用途")
    private String poolPurpose;

    @ApiModelProperty("最大池值")
    private BigDecimal maxPoolValue;

    @ApiModelProperty("最小票据金额")
    private BigDecimal minBillValue;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("票据数量")
    private Integer billCount;

    @ApiModelProperty("质押率")
    private BigDecimal pledgeRate;

    @ApiModelProperty("融资金额")
    private BigDecimal financingAmount;

    @ApiModelProperty("池状态")
    private String poolStatus;

    @ApiModelProperty("允许的票据类型")
    private String allowedBillTypes;

    @ApiModelProperty("池规则JSON")
    private String poolRules;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getPoolId() { return poolId; }
    public void setPoolId(Long poolId) { this.poolId = poolId; }
    public String getPoolName() { return poolName; }
    public void setPoolName(String poolName) { this.poolName = poolName; }
    public String getPoolCode() { return poolCode; }
    public void setPoolCode(String poolCode) { this.poolCode = poolCode; }
    public String getPoolType() { return poolType; }
    public void setPoolType(String poolType) { this.poolType = poolType; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getPoolPurpose() { return poolPurpose; }
    public void setPoolPurpose(String poolPurpose) { this.poolPurpose = poolPurpose; }
    public BigDecimal getMaxPoolValue() { return maxPoolValue; }
    public void setMaxPoolValue(BigDecimal maxPoolValue) { this.maxPoolValue = maxPoolValue; }
    public BigDecimal getMinBillValue() { return minBillValue; }
    public void setMinBillValue(BigDecimal minBillValue) { this.minBillValue = minBillValue; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Integer getBillCount() { return billCount; }
    public void setBillCount(Integer billCount) { this.billCount = billCount; }
    public BigDecimal getPledgeRate() { return pledgeRate; }
    public void setPledgeRate(BigDecimal pledgeRate) { this.pledgeRate = pledgeRate; }
    public BigDecimal getFinancingAmount() { return financingAmount; }
    public void setFinancingAmount(BigDecimal financingAmount) { this.financingAmount = financingAmount; }
    public String getPoolStatus() { return poolStatus; }
    public void setPoolStatus(String poolStatus) { this.poolStatus = poolStatus; }
    public String getAllowedBillTypes() { return allowedBillTypes; }
    public void setAllowedBillTypes(String allowedBillTypes) { this.allowedBillTypes = allowedBillTypes; }
    public String getPoolRules() { return poolRules; }
    public void setPoolRules(String poolRules) { this.poolRules = poolRules; }

}
