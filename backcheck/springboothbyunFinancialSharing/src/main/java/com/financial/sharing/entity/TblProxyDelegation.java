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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 代理委托主表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROXY_DELEGATION")
@ApiModel(value = "TblProxyDelegation", description = "代理委托主表")
public class TblProxyDelegation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "DELEGATION_ID")
    @ApiModelProperty(value = "委托ID")
    private String delegationId;

    @TableField("DELEGATION_CODE")
    @ApiModelProperty(value = "委托编码")
    private String delegationCode;

    @TableField("DELEGATION_TYPE")
    @ApiModelProperty(value = "委托类型")
    private String delegationType;

    @TableField("DELEGATOR_ID")
    @ApiModelProperty(value = "委托人ID")
    private String delegatorId;

    @TableField("DELEGATOR_NAME")
    @ApiModelProperty(value = "委托人姓名")
    private String delegatorName;

    @TableField("DELEGATOR_DEPT")
    @ApiModelProperty(value = "委托人部门")
    private String delegatorDept;

    @TableField("PROXY_ID")
    @ApiModelProperty(value = "代理人ID")
    private String proxyId;

    @TableField("PROXY_NAME")
    @ApiModelProperty(value = "代理人姓名")
    private String proxyName;

    @TableField("PROXY_DEPT")
    @ApiModelProperty(value = "代理人部门")
    private String proxyDept;

    @TableField("DELEGATION_SCOPE")
    @ApiModelProperty(value = "委托范围")
    private String delegationScope;

    @TableField("PERMISSION_SCOPE")
    @ApiModelProperty(value = "权限范围")
    private String permissionScope;

    @TableField("AMOUNT_LIMIT")
    @ApiModelProperty(value = "金额限制")
    private BigDecimal amountLimit;

    @TableField("START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @TableField("END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @TableField("IS_ENABLED")
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField("CREATE_USER")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @TableField("UPDATE_USER")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @TableField("DESCRIPTION")
    @ApiModelProperty(value = "委托说明")
    private String description;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
