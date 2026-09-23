package com.global.treasurer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 印章组合详情视图对象
 *
 * @author system
 * @date 2025-12-06
 */
// @Data // 已移除,使用手动编写的getter/setter
@ApiModel(value = "TcSealCombinationDetailVO", description = "印章组合详情视图对象")
public class TcSealCombinationDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "详情ID")
    private String id;

    @ApiModelProperty(value = "印章组合ID")
    private String combinationId;

    @ApiModelProperty(value = "印章组合编码")
    private String combinationCode;

    @ApiModelProperty(value = "印章组合名称")
    private String combinationName;

    @ApiModelProperty(value = "印章ID")
    private String sealId;

    @ApiModelProperty(value = "印章编码")
    private String sealCode;

    @ApiModelProperty(value = "印章名称")
    private String sealName;

    @ApiModelProperty(value = "印章类型")
    private String sealType;

    @ApiModelProperty(value = "印章类型名称")
    private String sealTypeName;

    @ApiModelProperty(value = "序号")
    private Integer sequenceNo;

    @ApiModelProperty(value = "是否必需（1是 0否）")
    private String isRequired;

    @ApiModelProperty(value = "是否必需名称")
    private String isRequiredName;

    @ApiModelProperty(value = "状态（1启用 0禁用）")
    private String status;

    @ApiModelProperty(value = "状态名称")
    private String statusName;

    @ApiModelProperty(value = "备注")
    private String remark;

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

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCombinationId() { return combinationId; }
    public void setCombinationId(String combinationId) { this.combinationId = combinationId; }
    public String getCombinationCode() { return combinationCode; }
    public void setCombinationCode(String combinationCode) { this.combinationCode = combinationCode; }
    public String getCombinationName() { return combinationName; }
    public void setCombinationName(String combinationName) { this.combinationName = combinationName; }
    public String getSealId() { return sealId; }
    public void setSealId(String sealId) { this.sealId = sealId; }
    public String getSealCode() { return sealCode; }
    public void setSealCode(String sealCode) { this.sealCode = sealCode; }
    public String getSealName() { return sealName; }
    public void setSealName(String sealName) { this.sealName = sealName; }
    public String getSealType() { return sealType; }
    public void setSealType(String sealType) { this.sealType = sealType; }
    public String getSealTypeName() { return sealTypeName; }
    public void setSealTypeName(String sealTypeName) { this.sealTypeName = sealTypeName; }
    public Integer getSequenceNo() { return sequenceNo; }
    public void setSequenceNo(Integer sequenceNo) { this.sequenceNo = sequenceNo; }
    public String getIsRequired() { return isRequired; }
    public void setIsRequired(String isRequired) { this.isRequired = isRequired; }
    public String getIsRequiredName() { return isRequiredName; }
    public void setIsRequiredName(String isRequiredName) { this.isRequiredName = isRequiredName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
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

}
