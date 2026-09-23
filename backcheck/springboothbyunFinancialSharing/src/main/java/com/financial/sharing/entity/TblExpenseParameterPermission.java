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
 * 参数权限表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_EXPENSE_PARAMETER_PERMISSION")
@ApiModel(value = "TblExpenseParameterPermission", description = "参数权限表")
public class TblExpenseParameterPermission implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "PERMISSION_ID")
    @ApiModelProperty(value = "权限ID")
    private String permissionId;

    @TableField("PARAMETER_ID")
    @ApiModelProperty(value = "参数ID")
    private String parameterId;

    @TableField("PERMISSION_TYPE")
    @ApiModelProperty(value = "权限类型")
    private String permissionType;

    @TableField("PERMISSION_TARGET")
    @ApiModelProperty(value = "权限目标")
    private String permissionTarget;

    @TableField("PERMISSION_VALUE")
    @ApiModelProperty(value = "权限值")
    private Integer permissionValue;

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
