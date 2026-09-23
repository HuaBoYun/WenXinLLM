package com.global.treasurer.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 印章组合详情实体类
 */
@ApiModel(value = "TcSealCombinationDetail", description = "印章组合详情")
// @Data // 已移除,使用手动编写的getter/setter
// @AllArgsConstructor // 已移除
// @NoArgsConstructor // 已移除

@TableName("TC_SEAL_COMBINATION_DETAIL")
public class TcSealCombinationDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @TableField("ID")
    @ApiModelProperty(value = "主键ID")
    private String id;

    @TableField("COMBINATION_ID")
    @ApiModelProperty(value = "组合ID")
    private String combinationId;

    @TableField("SEAL_ID")
    @ApiModelProperty(value = "印章ID")
    private String sealId;

    @TableField("SEQUENCE_NO")
    @ApiModelProperty(value = "序号")
    private Integer sequenceNo;

    @TableField("IS_REQUIRED")
    @ApiModelProperty(value = "是否必需：1-是，0-否")
    private String isRequired;

    @TableField("UPDATE_TIME")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("STATUS")
    @ApiModelProperty(value = "状态：1-启用，0-停用")
    private String status;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField("CREATE_TIME")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

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
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }

}
