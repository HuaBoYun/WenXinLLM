package com.financial.sharing.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 会计期间状态变更日志实体类
 *
 * @author system
 * @since 2025-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ACCOUNTING_PERIOD_LOG")
@ApiModel(value = "TblAccountingPeriodLog对象", description = "会计期间状态变更日志")
public class TblAccountingPeriodLog extends Model<TblAccountingPeriodLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志ID")
    @TableId(value = "LOG_ID", type = IdType.ASSIGN_ID)
    private Long logId;

    @ApiModelProperty(value = "期间ID")
    @TableField("PERIOD_ID")
    private Long periodId;

    @ApiModelProperty(value = "原状态")
    @TableField("OLD_STATUS")
    private String oldStatus;

    @ApiModelProperty(value = "新状态")
    @TableField("NEW_STATUS")
    private String newStatus;

    @ApiModelProperty(value = "操作类型")
    @TableField("OPERATION_TYPE")
    private String operationType;

    @ApiModelProperty(value = "操作原因")
    @TableField("OPERATION_REASON")
    private String operationReason;

    @ApiModelProperty(value = "操作人ID")
    @TableField("OPERATOR_ID")
    private String operatorId;

    @ApiModelProperty(value = "操作人姓名")
    @TableField("OPERATOR_NAME")
    private String operatorName;

    @ApiModelProperty(value = "操作时间")
    @TableField("OPERATION_TIME")
    private Date operationTime;

    @ApiModelProperty(value = "账簿ID")
    @TableField("BOOK_ID")
    private Long bookId;

    @ApiModelProperty(value = "租户ID")
    @TableField("TENANT_ID")
    private Long tenantId;

    @ApiModelProperty(value = "备注")
    @TableField("REMARK")
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.logId;
    }

    /**
     * 操作类型枚举
     */
    public static class OperationType {
        public static final String CREATE = "CREATE";
        public static final String OPEN = "OPEN";
        public static final String CLOSE = "CLOSE";
        public static final String LOCK = "LOCK";
        public static final String UPDATE = "UPDATE";
        public static final String SET_CURRENT = "SET_CURRENT";
    }
}