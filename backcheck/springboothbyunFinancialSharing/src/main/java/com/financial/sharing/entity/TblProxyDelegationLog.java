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
 * 代理操作日志表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROXY_DELEGATION_LOG")
@ApiModel(value = "TblProxyDelegationLog", description = "代理操作日志表")
public class TblProxyDelegationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "LOG_ID")
    @ApiModelProperty(value = "日志ID")
    private String logId;

    @TableField("DELEGATION_ID")
    @ApiModelProperty(value = "委托ID")
    private String delegationId;

    @TableField("OPERATOR")
    @ApiModelProperty(value = "操作人ID")
    private String operator;

    @TableField("OPERATOR_NAME")
    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    @TableField("OPERATION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;

    @TableField("OPERATION_CONTENT")
    @ApiModelProperty(value = "操作内容")
    private String operationContent;

    @TableField("BUSINESS_TYPE")
    @ApiModelProperty(value = "业务类型")
    private String businessType;

    @TableField("BUSINESS_ID")
    @ApiModelProperty(value = "业务ID")
    private String businessId;

    @TableField("IP_ADDRESS")
    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
