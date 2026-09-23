package com.management.accountant.oracle.entity.budget;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预算限额操作历史实体
 * 对应表: TBL_BUDGET_LIMIT_HISTORY
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("TBL_BUDGET_LIMIT_HISTORY")
public class BudgetLimitHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "HISTORY_ID", type = IdType.ASSIGN_UUID)
    private String historyId;

    @TableField("LIMIT_ID")
    private String limitId;

    /** 操作类型: CREATE/UPDATE/DELETE/FREEZE/ADJUST/ENABLE/DISABLE */
    @TableField("OPERATION_TYPE")
    private String operationType;

    @TableField("OPERATION_DESC")
    private String operationDesc;

    @TableField("BEFORE_VALUE")
    private String beforeValue;

    @TableField("AFTER_VALUE")
    private String afterValue;

    @TableField("OPERATOR")
    private String operator;

    @TableField("OPERATE_TIME")
    private Date operateTime;

    @TableField("IP_ADDRESS")
    private String ipAddress;

    @TableField("CREATE_TIME")
    private Date createTime;
}
