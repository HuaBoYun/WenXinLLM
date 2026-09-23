package com.global.treasurer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 印章组合详情数据传输对象
 *
 * @author system
 * @date 2025-12-06
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "TcSealCombinationDetailDTO", description = "印章组合详情数据传输对象")
public class TcSealCombinationDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "详情ID")
    private String id;

    @ApiModelProperty(value = "印章组合ID")
    private String combinationId;

    @ApiModelProperty(value = "印章ID", required = true)
    @NotBlank(message = "印章ID不能为空")
    private String sealId;

    @ApiModelProperty(value = "序号", required = true)
    @NotNull(message = "序号不能为空")
    private Integer sequenceNo;

    @ApiModelProperty(value = "是否必需（1是 0否）")
    private String isRequired;

    @ApiModelProperty(value = "状态（1启用 0禁用）")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "印章编码")
    private String sealCode;

    @ApiModelProperty(value = "印章名称")
    private String sealName;

    @ApiModelProperty(value = "印章类型")
    private String sealType;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCombinationId() { return combinationId; }
    public void setCombinationId(String combinationId) { this.combinationId = combinationId; }
    public String getSealId() { return sealId; }
    public void setSealId(String sealId) { this.sealId = sealId; }
    public Integer getSequenceNo() { return sequenceNo; }
    public void setSequenceNo(Integer sequenceNo) { this.sequenceNo = sequenceNo; }
    public String getIsRequired() { return isRequired; }
    public void setIsRequired(String isRequired) { this.isRequired = isRequired; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getSealCode() { return sealCode; }
    public void setSealCode(String sealCode) { this.sealCode = sealCode; }
    public String getSealName() { return sealName; }
    public void setSealName(String sealName) { this.sealName = sealName; }
    public String getSealType() { return sealType; }
    public void setSealType(String sealType) { this.sealType = sealType; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
}
