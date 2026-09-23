package com.global.treasurer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 印章组合配置视图对象
 *
 * @author system
 * @date 2025-12-06
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "TcSealCombinationVO", description = "印章组合配置视图对象")
public class TcSealCombinationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "印章组合ID")
    private String id;

    @ApiModelProperty(value = "组合编码")
    private String combinationCode;

    @ApiModelProperty(value = "组合名称")
    private String combinationName;

    @ApiModelProperty(value = "组合类型")
    private String combinationType;

    @ApiModelProperty(value = "组合类型名称")
    private String combinationTypeName;

    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @ApiModelProperty(value = "业务类型名称")
    private String businessTypeName;

    @ApiModelProperty(value = "业务场景")
    private String businessScenario;

    @ApiModelProperty(value = "业务场景名称")
    private String businessScenarioName;

    @ApiModelProperty(value = "权限级别")
    private String authorityLevel;

    @ApiModelProperty(value = "权限级别名称")
    private String authorityLevelName;

    @ApiModelProperty(value = "最大金额限制")
    private BigDecimal maxAmountLimit;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态（1启用 0禁用）")
    private String status;

    @ApiModelProperty(value = "状态名称")
    private String statusName;

    @ApiModelProperty(value = "印章数量")
    private Integer sealCount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新人姓名")
    private String updateUserName;

    @ApiModelProperty(value = "印章组合详情列表")
    private List<TcSealCombinationDetailVO> details;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCombinationCode() { return combinationCode; }
    public void setCombinationCode(String combinationCode) { this.combinationCode = combinationCode; }
    public String getCombinationName() { return combinationName; }
    public void setCombinationName(String combinationName) { this.combinationName = combinationName; }
    public String getCombinationType() { return combinationType; }
    public void setCombinationType(String combinationType) { this.combinationType = combinationType; }
    public String getCombinationTypeName() { return combinationTypeName; }
    public void setCombinationTypeName(String combinationTypeName) { this.combinationTypeName = combinationTypeName; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getBusinessTypeName() { return businessTypeName; }
    public void setBusinessTypeName(String businessTypeName) { this.businessTypeName = businessTypeName; }
    public String getBusinessScenario() { return businessScenario; }
    public void setBusinessScenario(String businessScenario) { this.businessScenario = businessScenario; }
    public String getBusinessScenarioName() { return businessScenarioName; }
    public void setBusinessScenarioName(String businessScenarioName) { this.businessScenarioName = businessScenarioName; }
    public String getAuthorityLevel() { return authorityLevel; }
    public void setAuthorityLevel(String authorityLevel) { this.authorityLevel = authorityLevel; }
    public String getAuthorityLevelName() { return authorityLevelName; }
    public void setAuthorityLevelName(String authorityLevelName) { this.authorityLevelName = authorityLevelName; }
    public BigDecimal getMaxAmountLimit() { return maxAmountLimit; }
    public void setMaxAmountLimit(BigDecimal maxAmountLimit) { this.maxAmountLimit = maxAmountLimit; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
    public Integer getSealCount() { return sealCount; }
    public void setSealCount(Integer sealCount) { this.sealCount = sealCount; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getCreateUserName() { return createUserName; }
    public void setCreateUserName(String createUserName) { this.createUserName = createUserName; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getUpdateUserName() { return updateUserName; }
    public void setUpdateUserName(String updateUserName) { this.updateUserName = updateUserName; }
    public List<TcSealCombinationDetailVO> getDetails() { return details; }
    public void setDetails(List<TcSealCombinationDetailVO> details) { this.details = details; }
}
