package com.financial.sharing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 委托权限表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROXY_DELEGATION_PERMISSION")
@ApiModel(value = "TblProxyDelegationPermission", description = "委托权限表")
public class TblProxyDelegationPermission implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PERMISSION_ID")
    @ApiModelProperty(value = "权限ID")
    private String permissionId;

    @TableField("DELEGATION_ID")
    @ApiModelProperty(value = "委托ID")
    private String delegationId;

    @TableField("PERMISSION_TYPE")
    @ApiModelProperty(value = "权限类型")
    private String permissionType;

    @TableField("PERMISSION_LEVEL")
    @ApiModelProperty(value = "权限级别")
    private String permissionLevel;

    @TableField("PERMISSION_VALUE")
    @ApiModelProperty(value = "权限值")
    private String permissionValue;

    @TableField("AMOUNT_LIMIT")
    @ApiModelProperty(value = "金额限制")
    private java.math.BigDecimal amountLimit;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
