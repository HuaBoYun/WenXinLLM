package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 印章组合配置数据传输对象
 *
 * @author system
 * @date 2025-12-06
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "TcSealCombinationDTO", description = "印章组合配置数据传输对象")
public class TcSealCombinationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "印章组合ID")
    private String id;

    @ApiModelProperty(value = "组合编码", required = true)
    @NotBlank(message = "组合编码不能为空")
    @Size(min = 3, max = 50, message = "组合编码长度必须在3-50个字符之间")
    private String combinationCode;

    @ApiModelProperty(value = "组合名称", required = true)
    @NotBlank(message = "组合名称不能为空")
    @Size(min = 2, max = 100, message = "组合名称长度必须在2-100个字符之间")
    private String combinationName;

    @ApiModelProperty(value = "组合类型", required = true)
    @NotBlank(message = "组合类型不能为空")
    private String combinationType;

    @ApiModelProperty(value = "业务类型", required = true)
    @NotBlank(message = "业务类型不能为空")
    private String businessType;

    @ApiModelProperty(value = "业务场景")
    private String businessScenario;

    @ApiModelProperty(value = "权限级别", required = true)
    @NotBlank(message = "权限级别不能为空")
    private String authorityLevel;

    @ApiModelProperty(value = "最大金额限制")
    private BigDecimal maxAmountLimit;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态（1启用 0禁用）")
    private String status;

    @ApiModelProperty(value = "印章组合详情列表")
    private List<TcSealCombinationDetailDTO> details;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCombinationCode() { return combinationCode; }
    public void setCombinationCode(String combinationCode) { this.combinationCode = combinationCode; }
    public String getCombinationName() { return combinationName; }
    public void setCombinationName(String combinationName) { this.combinationName = combinationName; }
    public String getCombinationType() { return combinationType; }
    public void setCombinationType(String combinationType) { this.combinationType = combinationType; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getBusinessScenario() { return businessScenario; }
    public void setBusinessScenario(String businessScenario) { this.businessScenario = businessScenario; }
    public String getAuthorityLevel() { return authorityLevel; }
    public void setAuthorityLevel(String authorityLevel) { this.authorityLevel = authorityLevel; }
    public BigDecimal getMaxAmountLimit() { return maxAmountLimit; }
    public void setMaxAmountLimit(BigDecimal maxAmountLimit) { this.maxAmountLimit = maxAmountLimit; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<TcSealCombinationDetailDTO> getDetails() { return details; }
    public void setDetails(List<TcSealCombinationDetailDTO> details) { this.details = details; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
}
