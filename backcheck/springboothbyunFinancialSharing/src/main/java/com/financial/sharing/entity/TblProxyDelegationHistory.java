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
 * 委托历史记录表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_PROXY_DELEGATION_HISTORY")
@ApiModel(value = "TblProxyDelegationHistory", description = "委托历史记录表")
public class TblProxyDelegationHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "HISTORY_ID")
    @ApiModelProperty(value = "历史ID")
    private String historyId;

    @TableField("DELEGATION_ID")
    @ApiModelProperty(value = "委托ID")
    private String delegationId;

    @TableField("OPERATION_TYPE")
    @ApiModelProperty(value = "操作类型")
    private String operationType;

    @TableField("OPERATION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;

    @TableField("OPERATOR")
    @ApiModelProperty(value = "操作人ID")
    private String operator;

    @TableField("OPERATOR_NAME")
    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    @TableField("OPERATION_CONTENT")
    @ApiModelProperty(value = "操作内容")
    private String operationContent;

    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
}
